import { apiPublic, apiAuth } from "./Config";

const base = "/rezervacija";


export const createRezervacija = (rezervacija) => apiAuth.post(`${base}/create`, rezervacija);

export const getZauzetiDatumi = (idSoba) => apiPublic.get(`${base}/datumi/${idSoba}`);

export const getZauzetiDatumiOsim = (idSoba, idRez) => apiPublic.get(`${base}/datumi/${idSoba}`,
  { params: { idRez } });

export const getRezervacijeKorisnika = () => apiAuth.get(`${base}/korisnik`);

export const getAktivneRezervacije = (idKorisnik) => apiAuth.get(`${base}/admin/korisnikAkt/${idKorisnik}`);

export const getStareRezervacije = (idKorisnik) => apiAuth.get(`${base}/admin/korisnikSt/${idKorisnik}`);

export const updateRezervacija = (idRezervacija, rezervacija) => apiAuth.put(`${base}/update/${idRezervacija}`, rezervacija);

export const deleteRezervacija = (idRezervacija) => apiAuth.delete(`${base}/delete/${idRezervacija}`);

export const adminDeleteRezervacija = (idRezervacija) => apiAuth.delete(`${base}/admin/delete/${idRezervacija}`);

export const adminUpdateRezervacija = (idRezervacija, rezervacijaDto) => apiAuth.put(`${base}/admin/update/${idRezervacija}`, rezervacijaDto);

export const getRezervacijaHotela = (idHotel) => apiAuth.get(`${base}/admin/hotel/${idHotel}`);
