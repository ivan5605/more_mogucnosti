package hr.moremogucnosti.more_mogucnosti_backend.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LozinkeMatchValidator implements ConstraintValidator<LozinkeMatch, Object> {

    private String lozinka;
    private String lozinkaPotvrda;
    private String message;

    @Override
    public void initialize(LozinkeMatch constraintAnnotation) { //ucitam parametre iz anotacije
        this.lozinka = constraintAnnotation.lozinka();
        this.lozinkaPotvrda = constraintAnnotation.lozinkaPotvrda();
        this.message = constraintAnnotation.message();
    }

    //samo podudaranje ne jel null i slicno
    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        if (o == null) return true; //da prepustim @NotNull

        var wrapper = new org.springframework.beans.BeanWrapperImpl(o); //citanje vrijednosti polja preko wrappera
        Object loz = wrapper.getPropertyValue(lozinka);
        Object potvrda = wrapper.getPropertyValue(lozinkaPotvrda);

        if (isBlank(loz) || isBlank(potvrda)) return true; //ako je onda prepustim @NotBlank

        boolean podudaraju = loz.equals(potvrda);
        if (!podudaraju) { //kreiram ConstraintViolation, vezem poruku za tocno to polje
            constraintValidatorContext.disableDefaultConstraintViolation(); //da ne dobim poruku na objektu (to je default)
            constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(lozinkaPotvrda)
                    .addConstraintViolation();
        }
        return podudaraju; //vracam true i ne stvaram ConstraintViolation
    }

    private boolean isBlank(Object o) {
        return o == null || (o instanceof String s && s.isBlank());
    }
}
