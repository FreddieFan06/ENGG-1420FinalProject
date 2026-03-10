package validation;

import exception.ValidationException;
import model.users.User;
import model.enums.UserType;

public class UserValidator {

    public void validateAddUser(User user) {

        if (user == null) {
            throw new ValidationException("User cannot be null.");
        }

        if (user.getUserId() == null || user.getUserId().isBlank()) {
            throw new ValidationException("User ID is required.");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            throw new ValidationException("User name is required.");
        }

        if (user.getEmail() == null || !isValidEmail(user.getEmail())) {
            throw new ValidationException("Invalid email format.");
        }

        if (user.getUserType() == null) {
            throw new ValidationException("User type is required.");
        }

        boolean validType = switch (user.getUserType()) {
            case STUDENT, STAFF, GUEST -> true;
            default -> false;
        };

        if (!validType) {
            throw new ValidationException("User type must be STUDENT, STAFF, or GUEST.");
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9._+-]+@(.+)$");
    }
}