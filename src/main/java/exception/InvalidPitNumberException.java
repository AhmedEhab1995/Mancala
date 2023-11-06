package exception;

public class InvalidPitNumberException extends Exception {
    @Override
    public String getMessage() {
        return "Please enter a valid pit number";
    }
}
