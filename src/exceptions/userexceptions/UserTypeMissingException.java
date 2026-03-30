package exceptions.userexceptions;

public class UserTypeMissingException extends RuntimeException {
    public UserTypeMissingException(String message) {
        super(message);
    }
}
