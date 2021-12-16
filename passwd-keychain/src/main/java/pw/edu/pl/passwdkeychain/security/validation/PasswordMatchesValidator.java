package pw.edu.pl.passwdkeychain.security.validation;

import pw.edu.pl.passwdkeychain.dto.PasswordDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        //
    }

    @Override
    public boolean isValid(final Object obj, ConstraintValidatorContext context) {
        final PasswordDTO password = (PasswordDTO) obj;
        boolean result = password.getSavedPassword().equals(password.getSavedPasswordAgain());
        if (result) {
            return true;
        } else {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Passwords don't match ").addConstraintViolation();
            return false;
        }
    }

}