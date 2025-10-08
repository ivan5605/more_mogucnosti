package hr.moremogucnosti.more_mogucnosti_backend.service;

import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikUpdateDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikViewDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Korisnik;
import hr.moremogucnosti.more_mogucnosti_backend.exception.DuplicateException;
import hr.moremogucnosti.more_mogucnosti_backend.exception.LozinkaException;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.KorisnikMapper;
import hr.moremogucnosti.more_mogucnosti_backend.repository.KorisnikRepository;
import hr.moremogucnosti.more_mogucnosti_backend.security.AppUserPrincipal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KorisnikServiceTest {

    @Mock
    KorisnikRepository korisnikRepository;

    @Mock
    KorisnikMapper korisnikMapper;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    AppUserPrincipal principal;

    @InjectMocks
    KorisnikService korisnikService;

    @Test
    @DisplayName("vraća viewDTO")
    void findById_found() {
        //arrange
        Long idKorisnik = 1L;
        Korisnik korisnik = setKorisnik(idKorisnik, "Ivo", "Ivić", "ivo@gmail.com");

        when(korisnikRepository.findById(idKorisnik)).thenReturn(Optional.of(korisnik));
        when(korisnikMapper.toViewDto(korisnik)).thenReturn(new KorisnikViewDto(korisnik.getId(), korisnik.getIme(), korisnik.getPrezime(), korisnik.getEmail()));

        //act
        KorisnikViewDto rezultat = korisnikService.findById(idKorisnik);

        //assert
        assertEquals("Ivo", rezultat.ime());
        assertEquals("Ivić", rezultat.prezime());
        assertEquals("ivo@gmail.com", rezultat.email());

        //provera jel su se Mockovi pozvali onak kak oočekivam
        verify(korisnikRepository).findById(idKorisnik);
        verify(korisnikMapper).toViewDto(korisnik);
        verifyNoMoreInteractions(korisnikRepository, korisnikMapper); //nema neocekivanih poziva na tim mockovima
    }

    @Test
    void findById_notFound() {
        Long idKorisnik = 100L;
        when(korisnikRepository.findById(idKorisnik)).thenReturn(Optional.empty());

        var ex = assertThrows(ResourceNotFoundException.class, () -> korisnikService.findById(idKorisnik));

        verify(korisnikRepository).findById(idKorisnik);
        verifyNoInteractions(korisnikMapper);
        verifyNoMoreInteractions(korisnikRepository);
        assertEquals("Korisnik sa ID-jem 100 ne postoji", ex.getMessage());
    }

    @Test
    void korisnikDeleteProfil_ok() {
        //arrange
        Long idKorisnik = 1L;
        String email = "ivo@gmail.com";
        String lozinka = "Lozinka123";

        when(principal.getId()).thenReturn(idKorisnik);
        when(principal.getUsername()).thenReturn(email);

        var k = new Korisnik();
        k.setId(idKorisnik);
        k.setEmail(email);

        when(korisnikRepository.findByIdWUloga(idKorisnik)).thenReturn(Optional.of(k));

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(email, lozinka, List.of()));

        //act
        korisnikService.korisnikDeleteProfil(principal, lozinka);

        //assert
        verify(authenticationManager).authenticate(argThat(auth ->
                auth instanceof UsernamePasswordAuthenticationToken
                    && email.equals(auth.getName())
                    && lozinka.equals(auth.getCredentials())
        ));

        verify(korisnikRepository).findByIdWUloga(idKorisnik);
        verify(korisnikRepository).deleteById(idKorisnik);
        verifyNoMoreInteractions(korisnikRepository, korisnikMapper);
    }

    @Test
    void korisnikDeleteProfil_netocnaLozinka() {
        when(principal.getId()).thenReturn(1L);
        when(principal.getUsername()).thenReturn("ivo@gmail.com");

        when(korisnikRepository.findByIdWUloga(1L)).thenReturn(Optional.of(new Korisnik()));

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(new BadCredentialsException("Netočna lozinka!"));

        assertThrows(LozinkaException.class,
                () -> korisnikService.korisnikDeleteProfil(principal, "lozinka"));
    }

    @Test
    void korisnikDeleteProfil_notFound() {
        when(principal.getId()).thenReturn(1L);
        when(korisnikRepository.findByIdWUloga(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> korisnikService.korisnikDeleteProfil(principal, "lozinka"));

        verify(korisnikRepository).findByIdWUloga(1L);
        verifyNoInteractions(authenticationManager);
        verify(korisnikRepository, never()).deleteById(anyLong());
    }

    @Test
    void adminDeleteKorisnik() {
        Long id = 1L;
        Korisnik k = setKorisnik(id, "Ivo", "Ivić", "ivo@gmail.com");

        when(korisnikRepository.findById(id)).thenReturn(Optional.of(new Korisnik()));

        korisnikService.adminDeleteKorisnik(id);

        verify(korisnikRepository).findById(1L);
        verify(korisnikRepository).deleteById(1L);
        verifyNoMoreInteractions(korisnikRepository);
    }

    @Test
    void adminDeleteKorisnik_notFound() {
        when(korisnikRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> korisnikService.adminDeleteKorisnik(1L));

        verify(korisnikRepository).findById(1L);
        verify(korisnikRepository, never()).deleteById(anyLong());
        verifyNoMoreInteractions(korisnikRepository);
    }

    @Test
    void korisnikUpdateProfil() {
        //arrange
        Korisnik k = setKorisnik(1L, "Ivo", "Ivić", "ivo@gmail.com");
        KorisnikUpdateDto updateDto = new KorisnikUpdateDto("Novo", "Novo", "novo@gmail.com");

        when(principal.getId()).thenReturn(1L);

        when(korisnikRepository.findById(1L)).thenReturn(Optional.of(k));
        when(korisnikRepository.existsByEmailAndIdNot("novo@gmail.com", 1L)).thenReturn(false);

        when(korisnikMapper.toViewDto(any(Korisnik.class))).thenReturn(new KorisnikViewDto(1L, "Novo", "Novo", "novo@gmail.com"));

        //act
        KorisnikViewDto rezultat = korisnikService.korisnikUpdateProfil(principal, updateDto);

        //assert
        verify(korisnikRepository).findById(1L);
        verify(korisnikRepository).existsByEmailAndIdNot("novo@gmail.com", 1L);

        var captor = ArgumentCaptor.forClass(Korisnik.class);
        verify(korisnikMapper).toViewDto(captor.capture()); //uhvatim argument koji se prosledjen u toViewDto(), da vidim jel bil azuriran
        Korisnik poslanMapperu = captor.getValue();
        assertEquals("Novo", poslanMapperu.getIme());
        assertEquals("Novo", poslanMapperu.getPrezime());
        assertEquals("novo@gmail.com", poslanMapperu.getEmail());

        assertEquals("Novo", rezultat.ime());
        assertEquals("Novo", rezultat.prezime());
        assertEquals("novo@gmail.com", rezultat.email());

        verifyNoMoreInteractions(korisnikRepository, korisnikMapper);
    }

    @Test
    void korisnikUpdateProfil_notFound() {
        when(principal.getId()).thenReturn(1L);
        when(korisnikRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> korisnikService.korisnikUpdateProfil(principal, new KorisnikUpdateDto("ime", "prezime", "email")));

        verify(korisnikRepository).findById(1L);
        verifyNoInteractions(korisnikMapper);
        verifyNoMoreInteractions(korisnikRepository);
    }

    @Test
    void korisnikUpdateProfil_duplicate() {
        Korisnik k = setKorisnik(1L, "ime", "prezime", "email");

        when(principal.getId()).thenReturn(1L);
        when(korisnikRepository.findById(1L)).thenReturn(Optional.of(k));
        when(korisnikRepository.existsByEmailAndIdNot("email2", 1L)).thenReturn(true);

        assertThrows(DuplicateException.class,
                () -> korisnikService.korisnikUpdateProfil(principal, new KorisnikUpdateDto("ime", "prezime", "email2")));

        verify(korisnikRepository).findById(1L);
        verify(korisnikRepository).existsByEmailAndIdNot("email2", 1L);
        verifyNoMoreInteractions(korisnikRepository);
        verifyNoInteractions(korisnikMapper);
    }

    private Korisnik setKorisnik (Long id, String ime, String prezime, String email) {
        Korisnik k = new Korisnik();
        k.setId(id);
        k.setIme(ime);
        k.setPrezime(prezime);
        k.setEmail(email);
        return k;
    }


}