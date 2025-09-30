package hr.moremogucnosti.more_mogucnosti_backend.validation;

import hr.moremogucnosti.more_mogucnosti_backend.repository.KorisnikRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> //validira String polja označena sa UniqueEmail
{

    private final KorisnikRepository korisnikRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if (email == null || email.isEmpty()){
            return true;
        }
        return !korisnikRepository.existsByEmail(email); //provjera jel u bazi ima vec taj email
    }
}
