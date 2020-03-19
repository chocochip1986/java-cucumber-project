package cdit_automation.data_helpers;

import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.AssessableIncomeStatus;
import cdit_automation.enums.NationalityEnum;
import cdit_automation.enums.PersonIdTypeEnum;
import cdit_automation.models.*;
import cdit_automation.models.embeddables.BiTemporalData;
import cdit_automation.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public class IrasTriMonthlyEgressDataPrep extends BatchFileDataPrep {

  @Autowired BatchRepo batchRepo;

  private enum ColumnHeaders {
    NAME("Name"),
    NATURAL_ID("NaturalId"),
    TYPE("Type"),
    DATE_OF_DEATH("DOD"),
    NATIONALITY("Nationality"),
    CITIZENSHIP_RENUNCIATION_DATE("CitizenshipRenunciationDate"),
    CITIZENSHIP_ATTAINMENT_DATE("CitizenshipAttainmentDate"),
    ASSESSABLE_INCOME_STATUS("AIStatus"),
    ASSESSABLE_INCOME_YEAR("AIYear"),
    INDEX("Index");

    public final String label;

    ColumnHeaders(String label) {
      this.label = label;
    }

    public String getValue() {
      return this.label;
    }
  }

  private Batch batch;

  public void createBatch() {
    this.batch = new Batch();
    batch.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
    batchRepo.save(batch);
  }

  private Batch getBatch() {
    return this.batch;
  }

  private boolean isValid(String source) {
    return source.matches("^[^-]+$");
  }

  public Income getIncome(Map<String, String> m, List<Person> persons) {
    return Income.create(
        getBatch(),
        persons.get(parseStringSize(m.get(ColumnHeaders.INDEX.getValue())) - 1),
        Short.valueOf(m.get(ColumnHeaders.ASSESSABLE_INCOME_YEAR.getValue())),
        BigDecimal.valueOf(10000),
        AssessableIncomeStatus.valueOf(m.get(ColumnHeaders.ASSESSABLE_INCOME_STATUS.getValue())),
        new BiTemporalData()
            .generateNewBiTemporalData(
                dateUtils.beginningOfDayToTimestamp(dateUtils.monthsBeforeToday(1))));
  }

  public Nationality getNationality(Map<String, String> m, Person person) {
    Nationality result =
        Nationality.create(
            getBatch(),
            person,
            NationalityEnum.valueOf(m.get(ColumnHeaders.NATIONALITY.getValue())),
            new BiTemporalData()
                .generateNewBiTemporalData(
                    dateUtils.beginningOfDayToTimestamp(dateUtils.yearsBeforeToday(10))),
            null);

    String citizenshipAttainmentDateStr =
        m.get(ColumnHeaders.CITIZENSHIP_ATTAINMENT_DATE.getValue());
    boolean hasAttainmentDate = isValid(citizenshipAttainmentDateStr);
    if (hasAttainmentDate) {
      LocalDate attainmentDate =
          LocalDate.parse(citizenshipAttainmentDateStr, dateUtils.DATETIME_FORMATTER_YYYYMMDD);
      result.setCitizenshipAttainmentDate(dateUtils.beginningOfDayToTimestamp(attainmentDate));
      result.setBiTemporalData(
          new BiTemporalData()
              .generateNewBiTemporalData(dateUtils.beginningOfDayToTimestamp(attainmentDate)));
    }

    String citizenshipRenunciationDateStr =
        m.get(ColumnHeaders.CITIZENSHIP_RENUNCIATION_DATE.getValue());
    boolean hasCitizenshipCeased = isValid(citizenshipRenunciationDateStr);
    if (hasCitizenshipCeased) {
      LocalDate ceasedDate =
          LocalDate.parse(citizenshipRenunciationDateStr, dateUtils.DATETIME_FORMATTER_YYYYMMDD);
      result.setCitizenshipRenunciationDate(dateUtils.beginningOfDayToTimestamp(ceasedDate));
      result.setBiTemporalData(
          new BiTemporalData()
              .generateNewBiTemporalData(
                  dateUtils.beginningOfDayToTimestamp(ceasedDate.plusDays(1))));
    }
    return result;
  }

  public PersonDetail getPersonDetail(Map<String, String> m, Person person) {
    PersonDetail result =
        PersonDetail.create(
            getBatch(),
            person,
            new BiTemporalData()
                .generateNewBiTemporalData(
                    dateUtils.beginningOfDayToTimestamp(dateUtils.yearsBeforeToday(10))));

    String citizenshipAttainmentDateStr =
        m.get(ColumnHeaders.CITIZENSHIP_ATTAINMENT_DATE.getValue());
    boolean hasAttainmentDate = isValid(citizenshipAttainmentDateStr);
    if (hasAttainmentDate) {
      result.setBiTemporalData(
          new BiTemporalData()
              .generateNewBiTemporalData(
                  dateUtils.beginningOfDayToTimestamp(
                      LocalDate.parse(
                          citizenshipAttainmentDateStr, dateUtils.DATETIME_FORMATTER_YYYYMMDD))));
    }

    String deathDateStr = m.get(ColumnHeaders.DATE_OF_DEATH.getValue());
    boolean hasDeathDate = isValid(deathDateStr);
    if (hasDeathDate) {
      LocalDate deathDate = LocalDate.parse(deathDateStr, dateUtils.DATETIME_FORMATTER_YYYYMMDD);
      result.setDateOfDeath(deathDate);
      result.setBiTemporalData(
          new BiTemporalData()
              .generateNewBiTemporalData(dateUtils.beginningOfDayToTimestamp(deathDate)));
    }

    String citizenshipRenunciationDateStr =
        m.get(ColumnHeaders.CITIZENSHIP_RENUNCIATION_DATE.getValue());
    boolean hasCitizenshipCeased = isValid(citizenshipRenunciationDateStr);
    if (hasCitizenshipCeased) {
      result.setBiTemporalData(
          new BiTemporalData()
              .generateNewBiTemporalData(
                  dateUtils.beginningOfDayToTimestamp(
                      LocalDate.parse(
                              citizenshipRenunciationDateStr, dateUtils.DATETIME_FORMATTER_YYYYMMDD)
                          .plusDays(1))));
    }
    return result;
  }

  public PersonId getPersonId(Map<String, String> m, Person person) {
    PersonId result =
        PersonId.create(
            batch,
            PersonIdTypeEnum.valueOf(m.get(ColumnHeaders.TYPE.getValue())),
            person,
            m.get(ColumnHeaders.NATURAL_ID.getValue()),
            new BiTemporalData()
                .generateNewBiTemporalData(
                    dateUtils.beginningOfDayToTimestamp(dateUtils.yearsBeforeToday(10))));

    String citizenshipAttainmentDateStr =
        m.get(ColumnHeaders.CITIZENSHIP_ATTAINMENT_DATE.getValue());
    boolean hasAttainmentDate = isValid(citizenshipAttainmentDateStr);
    if (hasAttainmentDate) {
      result.setBiTemporalData(
          new BiTemporalData()
              .generateNewBiTemporalData(
                  dateUtils.beginningOfDayToTimestamp(
                      LocalDate.parse(
                          citizenshipAttainmentDateStr, dateUtils.DATETIME_FORMATTER_YYYYMMDD))));
    }

    return result;
  }

  public PersonName getPersonName(Map<String, String> m, Person person) {
    String name = m.get(ColumnHeaders.NAME.getValue());
    String generatedName = name.equalsIgnoreCase("AUTO") ? Phaker.validName() : name;
    return PersonName.create(
        getBatch(),
        person,
        generatedName,
        new BiTemporalData()
            .generateNewBiTemporalData(
                dateUtils.beginningOfDayToTimestamp(dateUtils.yearsBeforeToday(10))));
  }

  public Person getPerson() {
    return Person.create();
  }
}
