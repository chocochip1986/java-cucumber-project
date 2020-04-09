package automation.utilities;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Timer {
    public static LocalDateTime time;
    private Timer() {
        //Not needed
    }

    public static void startTimer() {
        time = LocalDateTime.now();
    }

    public static Long stopTimer() {
        if ( time == null ) {
            return null;
        } else {
            return time.until(LocalDateTime.now(), ChronoUnit.SECONDS);
        }
    }
}
