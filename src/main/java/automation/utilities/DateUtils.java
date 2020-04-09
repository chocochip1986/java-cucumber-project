package automation.utilities;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DateUtils {

    public static final DateTimeFormatter DATETIME_FORMATTER_YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter DATETIME_FORMATTER_DDMMYYYY = DateTimeFormatter.ofPattern("ddMMyyyy");

    public Timestamp beginningOfDayToTimestamp(LocalDate inputDate) {
        if (inputDate == null) {
            return null;
        }

        return Timestamp.valueOf(inputDate.atStartOfDay().withNano(0));
    }

    public Timestamp endOfDayToTimestamp(LocalDate inputDate) {
        if (inputDate == null) {
            return null;
        }
        return Timestamp.valueOf(inputDate.atTime(LocalTime.MAX).withNano(0));
    }

    public LocalDateTime beginningOfDayToLocalDateTime(LocalDate inputDate) {
        if (inputDate == null) {
            return null;
        }

        return inputDate.atStartOfDay().withNano(0);
    }

    public LocalDateTime endOfDayToLocalDateTime(LocalDate inputDate) {
        if (inputDate == null) {
            return null;
        }

        return inputDate.atTime(LocalTime.MAX).withNano(0);
    }

    public LocalDate now() {
        return LocalDate.now();
    }

    public LocalDate yesterday() {
        return LocalDate.now().minusDays(1);
    }

    public LocalDate tomorrow() {
        return LocalDate.now().plusDays(1);
    }

    public LocalDate daysBeforeToday(long days) {
        return LocalDate.now().minusDays(days);
    }

    public LocalDate daysAfterToday(long days) {
        return LocalDate.now().plusDays(days);
    }

    public LocalDate monthsBeforeToday(long months) {
        return LocalDate.now().minusMonths(months);
    }

    public LocalDate monthsAfterToday(long months) {
        return LocalDate.now().plusMonths(months);
    }

    public LocalDate yearsBeforeToday(long years) {
        return LocalDate.now().minusYears(years);
    }

    public LocalDate yearsAfterToday(long years) {
        return LocalDate.now().plusYears(years);
    }

    public LocalDate daysBeforeDate(LocalDate date, long days) {
        return date.minusDays(days);
    }

    public LocalDate daysAfterDate(LocalDate date, long days) {
        return date.plusDays(days);
    }

    public LocalDate monthsBeforeDate(LocalDate date, long months) {
        return date.minusMonths(months);
    }

    public LocalDate monthsAfterDate(LocalDate date, long months) {
        return date.plusMonths(months);
    }

    public LocalDate yearsBeforeDate(LocalDate date, long years) {
        return date.minusYears(years);
    }

    public LocalDate yearsAfterDate(LocalDate date, long years) {
        return date.plusYears(years);
    }

    public Date localDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public LocalDate parse(String date) {
        try {
            return LocalDate.parse(date, DATETIME_FORMATTER_YYYYMMDD);
        } catch ( IllegalArgumentException e ) {
            log.error("Unable to parse string input "+date+" into a LocalDate object");
            return null;
        }
    }
}
