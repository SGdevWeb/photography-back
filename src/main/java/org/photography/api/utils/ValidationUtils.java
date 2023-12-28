package org.photography.api.utils;

public class ValidationUtils {

    public static void validateName(String name) {
        validateNullOrEmpty(name);
        validateCharacterTypes(name);
        validateLength(name);
    }

    private static void validateNullOrEmpty(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
    }

    private static void validateCharacterTypes(String name) {
        if (!name.matches("^[a-zA-Z0-9 ]+$")) {
            throw new IllegalArgumentException("Name can only contain letters and numbers");
        }
    }

    private static void validateLength(String name) {
        if (name.length() > 255) {
            throw new IllegalArgumentException("Name length cannot exceed 255 characters");
        }
    }

    public static void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be a positive value");
        }
    }

}
