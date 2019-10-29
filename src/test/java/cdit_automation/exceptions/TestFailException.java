package cdit_automation.exceptions;

public class TestFailException extends RuntimeException {
    public TestFailException (String message) {
        super(message);
    }
}
