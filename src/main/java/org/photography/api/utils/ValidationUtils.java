package org.photography.api.utils;

public class ValidationUtils {

    // Name
    public static void validateName(String name) {
        validateNullOrEmptyName(name);
        validateCharacterTypesName(name);
        validateLengthName(name);
    }

    private static void validateNullOrEmptyName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
    }

    private static void validateCharacterTypesName(String name) {
        if (!name.matches("^[a-zA-Z0-9 ]+$")) {
            throw new IllegalArgumentException("Name can only contain letters and numbers");
        }
    }

    private static void validateLengthName(String name) {
        if (name.length() > 255) {
            throw new IllegalArgumentException("Name length cannot exceed 255 characters");
        }
    }

    //Id
    public static void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be a positive value");
        }
    }

    // Text
    public static void validateText(String text) {
        validateNullOrEmptyText(text);
        validateCharacterTypesText(text);
    }

    private static void validateNullOrEmptyText(String text) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        }
    }

    private static void validateCharacterTypesText(String text) {
        if (!text.matches("^[a-zA-Z0-9., ]+$")) {
            throw new IllegalArgumentException("Text can only contain letters, numbers, periods, and commas");
        }
    }

}
