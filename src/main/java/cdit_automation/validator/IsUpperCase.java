package cdit_automation.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {IsUpperCaseValidator.class})
public @interface IsUpperCase {

  String message() default "Text must be uppercase.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
