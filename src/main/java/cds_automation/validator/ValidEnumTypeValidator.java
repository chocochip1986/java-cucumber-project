package cds_automation.validator;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ValidEnumTypeValidator implements ConstraintValidator<ValidEnumType, Object> {

  List<String> valueList = null;

  @Override
  public void initialize(ValidEnumType constraintAnnotation) {
    valueList = new ArrayList<>();
    Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClazz();

    @SuppressWarnings("rawtypes")
    Enum[] enumValArr = enumClass.getEnumConstants();

    for (@SuppressWarnings("rawtypes") Enum enumVal : enumValArr) {
      valueList.add(enumVal.toString().toUpperCase());
    }
  }

  @Override
  public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {

    if (o != null) {
      String enumVal = (o.toString()).toUpperCase();

      if (valueList.contains(enumVal.toUpperCase())) {
        return true;
      }
    }
    return false;
  }
}
