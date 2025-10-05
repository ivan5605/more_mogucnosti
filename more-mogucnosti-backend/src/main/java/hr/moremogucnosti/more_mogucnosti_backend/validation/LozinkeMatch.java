package hr.moremogucnosti.more_mogucnosti_backend.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) //anotacija ide na klasu/record na direkt na neko polje
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LozinkeMatchValidator.class)
public @interface LozinkeMatch {
    String message() default "Lozinke se ne podudaraju.";

    String lozinka();
    String lozinkaPotvrda();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
