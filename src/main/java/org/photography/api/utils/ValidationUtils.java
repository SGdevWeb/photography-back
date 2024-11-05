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
            throw new IllegalArgumentException("Le nom ne peut pas être vide");
        }
    }

    private static void validateCharacterTypesName(String name) {
        if (!name.matches("^[\\p{L}0-9 ]+$")) {
            throw new IllegalArgumentException("Le nom doit contenir uniquement des lettres et des chiffres");
        }
    }

    private static void validateLengthName(String name) {
        if (name.length() > 255) {
            throw new IllegalArgumentException("Le nom ne peut pas excéder 255 caractères");
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
            throw new IllegalArgumentException("Le texte ne peut pas être vide");
        }
    }

    private static void validateCharacterTypesText(String text) {
        if (!text.matches("[\\p{L}0-9.,<>:/!' ]*")) {
            throw new IllegalArgumentException("Le texte peut contenir uniquement des lettres, des nombres, points et virgules");
        }
    }

    // Theme years
    public static void validateYearRange(int yearFrom, int yearTo) {
        if (yearFrom > yearTo) {
            throw new IllegalArgumentException("La première année saisie doit être inférieure à l'année suivante.");
        }

        if (yearTo - yearFrom != 1) {
            throw new IllegalArgumentException("L'intervalle d'années doit être d'une année exactement");
        }
    }


}
