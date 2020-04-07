package cds_automation.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidIDValidator.class})
public @interface ValidID {
  String message() default "Must be valid ID. Either FIN or NRIC entered was incorrect in format.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
