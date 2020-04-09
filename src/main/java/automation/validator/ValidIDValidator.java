package automation.validator;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class ValidIDValidator implements ConstraintValidator<ValidID, Object> {

  private static final String NRIC_PATTERN = "[ST]\\d{7}[A-Z]";
  private static final String FIN_PATTERN = "[FG]\\d{7}[A-Z]";

  @Override
  public void initialize(ValidID constraintAnnotation) {
    // Do nothing because its never used
  }

  @Override
  public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {

    // 1) Check format 2) Check Checksum of respective ID where present
    String id = (String) o;
    if (id.matches(NRIC_PATTERN) && !id.matches(FIN_PATTERN)) {
      return IdValidator.isNricValid(id);
    } else {
      return IdValidator.isFinValid(id);
    }
  }
}
