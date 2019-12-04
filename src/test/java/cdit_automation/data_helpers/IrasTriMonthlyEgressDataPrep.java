package cdit_automation.data_helpers;

import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.AssessableIncomeStatus;
import cdit_automation.enums.Gender;
import cdit_automation.enums.NationalityEnum;
import cdit_automation.enums.PersonIdTypeEnum;
import cdit_automation.models.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class IrasTriMonthlyEgressDataPrep extends BatchFileDataPrep {

  private enum ColumnHeaders {
    NAME("Name"),
    NATURAL_ID("NaturalId"),
    TYPE("Type"),
    DATE_OF_DEATH("DOD"),
    NATIONALITY("Nationality"),
    CITIZENSHIP_RENUNCIATION_DATE("CitizenshipRenunciationDate"),
    ASSESSABLE_INCOME_STATUS("AIStatus"),
    ASSESSABLE_INCOME_YEAR("AIYear");

    public final String label;

    ColumnHeaders(String label) {
      this.label = label;
    }

    public String getValue() {
      return this.label;
    }
  }

  private String getValueOfColumn(Map<String, String> m, ColumnHeaders headers) {
    return m.get(headers.getValue());
  }

  private boolean isMatches(String source, String pattern) {
    return source.matches(pattern);
  }

  public Optional<Income> getIncomeOpt(Map<String, String> m, Person person) {
    String extractedStatus = getValueOfColumn(m, ColumnHeaders.ASSESSABLE_INCOME_STATUS);
    if (!isMatches(extractedStatus, "^[^-]+$")) {
      return Optional.empty();
    }

    Income result =
        Income.builder()
            .assessableIncomeStatus(
                AssessableIncomeStatus.valueOf(
                    getValueOfColumn(m, ColumnHeaders.ASSESSABLE_INCOME_STATUS)))
            .person(person)
            .assessableIncome(BigDecimal.valueOf(1000000))
            .build();

    String extractedYear = getValueOfColumn(m, ColumnHeaders.ASSESSABLE_INCOME_YEAR);
    if (isMatches(extractedYear, "^[^-]+$")) {
      result.setYear(Short.valueOf(extractedYear));
    }

    return Optional.of(result);
  }

  public Optional<Nationality> getNationalityOpt(Map<String, String> m, Person person) {
    Nationality result =
        Nationality.builder()
            .person(person)
            .nationality(NationalityEnum.valueOf(getValueOfColumn(m, ColumnHeaders.NATIONALITY)))
            .citizenshipAttainmentDate(Timestamp.valueOf(LocalDateTime.now().minusYears(25)))
            .build();

    String extractedRenunciationDate =
        getValueOfColumn(m, ColumnHeaders.CITIZENSHIP_RENUNCIATION_DATE);
    if (isMatches(extractedRenunciationDate, "^[0-9]{8}$")) {
      result.setCitizenshipRenunciationDate(
          Timestamp.valueOf(
              LocalDate.parse(
                      getValueOfColumn(m, ColumnHeaders.CITIZENSHIP_RENUNCIATION_DATE),
                      DateTimeFormatter.ofPattern("yyyyMMdd"))
                  .atTime(LocalTime.MAX)));
    }
    return Optional.of(result);
  }

  public Optional<PersonDetail> getPersonDetailOpt(Map<String, String> m, Person person) {
    PersonDetail result =
        PersonDetail.builder()
            .person(person)
            .gender(Gender.FEMALE)
            .dateOfBirth(LocalDate.now().minusYears(25))
            .build();

    String extractedDeathDate = getValueOfColumn(m, ColumnHeaders.DATE_OF_DEATH);
    if (isMatches(extractedDeathDate, "^[^-]+$")) {
      result.setDateOfDeath(
          LocalDate.parse(extractedDeathDate, DateTimeFormatter.ofPattern("yyyyMMdd")));
    }
    return Optional.of(result);
  }

  public Optional<PersonId> getPersonIdOpt(Map<String, String> m, Person person) {
    String autoGenerateNaturalId =
        getValueOfColumn(m, ColumnHeaders.TYPE).equalsIgnoreCase(PersonIdTypeEnum.NRIC.toString())
            ? Phaker.validNric()
            : Phaker.validFin();
    String extractedNaturalId = getValueOfColumn(m, ColumnHeaders.NATURAL_ID);
    return Optional.of(
        PersonId.builder()
            .person(person)
            .personIdType(PersonIdTypeEnum.valueOf(getValueOfColumn(m, ColumnHeaders.TYPE)))
            .naturalId(
                isMatches(extractedNaturalId, "^[STFG]\\d{7}[A-Z]$")
                    ? extractedNaturalId
                    : autoGenerateNaturalId)
            .build());
  }

  public Optional<PersonName> getPersonNameOpt(Map<String, String> m, Person person) {
    String extractedName = getValueOfColumn(m, ColumnHeaders.NAME);
    String generatedName =
        extractedName.equalsIgnoreCase("AUTO") ? Phaker.validName() : extractedName;
    return Optional.of(PersonName.builder().person(person).name(generatedName).build());
  }

  public Optional<Person> getPersonOpt() {
    return Optional.of(Person.create());
  }
}
