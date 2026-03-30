// Created by Shivom Bhanot (1319202)

package validation;
public class ValidationUtils {
    
    // Throws an exception if the object is null
    public static void requireNonNull(Object obj, String message) {
        if (obj == null) throw new IllegalArgumentException(message);
    }

    // Throws an exception if the string is null or blank
    public static void requireNonBlank(String str, String message) {
        if (str == null || str.isBlank()) throw new IllegalArgumentException(message);
    }

    // Checks if an email is in a valid format
    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9._+-]+@(.+)$");
    }
}