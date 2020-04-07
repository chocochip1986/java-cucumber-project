package cds_automation.validator;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class IsUpperCaseValidator implements ConstraintValidator<IsUpperCase, Object> {

  private String text;

  @Override
  public void initialize(IsUpperCase constraintAnnotation) {}

  @Override
  public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {

    // PLEASE NOTE. THIS IS USABLE ONLY IF BLANK SPACE IS ACCEPTABLE AND STRING IS ALPHANUMERIC.

    String text = (String) o;
    char charlower;
    char[] textarr = new char[99];
    if (text.equals("")) {
      return true;
    }
    for (int i = 0; i <= text.length() - 1; i++) {
      // Parse into char array
      textarr[i] = text.charAt(i);
      charlower = Character.toLowerCase(textarr[i]);
      // Account for numeric characters. If numeric, skip over
      if (!Character.isDigit(textarr[i])) {
        if (textarr[i] == charlower) {
          log.error("Column entry must be uppercase.");
          return false;
        }
      } else {
        return true;
      }
    }
    return true;
  }
}
