package cdit_automation.exceptions;

public class UnsupportedWebDriverException extends RuntimeException {
    public UnsupportedWebDriverException (String message) {
        super(message);
    }
}