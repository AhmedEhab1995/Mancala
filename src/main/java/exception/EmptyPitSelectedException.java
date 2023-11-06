package exception;

public class EmptyPitSelectedException extends Exception {
    @Override
    public String getMessage() {
        return "Please select a pit which is not empty";
    }
}
