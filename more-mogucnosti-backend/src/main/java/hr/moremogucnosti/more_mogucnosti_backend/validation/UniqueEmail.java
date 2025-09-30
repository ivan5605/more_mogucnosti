package hr.moremogucnosti.more_mogucnosti_backend.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD}) //anotacija ide na polje
@Retention(RetentionPolicy.RUNTIME) //dostupna u runtimeu da bi je validatori mogli čitati
@Constraint(validatedBy = UniqueEmailValidator.class) //ta je klasa zadužena za provjeru
public @interface UniqueEmail {
    String message() default "Ova email adresa se već koristi!"; //defaultna poruka

    Class<?>[] groups() default {}; //omogucava da isti model validiram u različitim situacijama
    //dok pozivam validaciju, napisem koju grupu validiram, samo validacije koje imaju tu klasu
    //groups = Create.class, npr na lozinku groups = Create.class pa se preskace za update class

    Class<? extends Payload>[] payload() default {};
    //ako krsenje nosi neku dodatnu oznaku/metadata
}
