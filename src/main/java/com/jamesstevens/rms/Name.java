package com.jamesstevens.rms;

import com.jamesstevens.rms.exceptions.IllegalParameterException;

/**
 * Represents a person's first and last name.
 * <p>
 * A name consists of a required first name and last name. Name values are
 * validated and normalized when the object is created. Normalization converts
 * each name component to title case while preserving valid hyphens and
 * apostrophes.
 * </p>
 * <p>
 * Examples of normalized names include {@code James}, {@code Santiago-Martinez},
 * and {@code O'Brien}.
 * </p>
 */
public class Name {

    private final String firstName;
    private final String lastName;

    /**
     * Constructs a new {@code Name}.
     *
     * @param firstName person's first name
     * @param lastName  person's last name
     * @throws IllegalParameterException if either name is null, blank, or
     *                                   contains invalid characters
     */
    public Name(String firstName, String lastName) {
        this.firstName = validateAndNormalize(firstName, "First name");
        this.lastName = validateAndNormalize(lastName, "Last name");
    }

    /**
     * Returns the normalized first name.
     *
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the normalized last name.
     *
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Validates and normalizes a name value.
     * <p>
     * Valid names may contain letters with optional internal hyphens or
     * apostrophes. Each portion separated by a hyphen or apostrophe is
     * normalized so that its first letter is uppercase and its remaining
     * letters are lowercase.
     * </p>
     *
     * @param value     name value to validate
     * @param fieldName field description used in validation messages
     * @return validated and normalized name
     * @throws IllegalParameterException if the value is null, blank, or
     *                                   contains invalid characters
     */
    private String validateAndNormalize(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalParameterException(
                    "N/A",
                    "N/A",
                    fieldName + " cannot be empty."
            );
        }

        String trimmedValue = value.trim();

        if (!trimmedValue.matches("[A-Za-z]+(?:[-'][A-Za-z]+)*")) {
            throw new IllegalParameterException(
                    "N/A",
                    "N/A",
                    fieldName + " may contain only letters, hyphens, and apostrophes."
            );
        }

        return normalize(trimmedValue);
    }

    /**
     * Normalizes a validated name while preserving hyphens and apostrophes.
     *
     * @param value validated name value
     * @return normalized name
     */
    private String normalize(String value) {
        StringBuilder normalized = new StringBuilder();
        boolean capitalizeNext = true;

        for (char character : value.toLowerCase().toCharArray()) {
            if (capitalizeNext && Character.isLetter(character)) {
                normalized.append(Character.toUpperCase(character));
                capitalizeNext = false;
            } else {
                normalized.append(character);
            }

            if (character == '-' || character == '\'') {
                capitalizeNext = true;
            }
        }

        return normalized.toString();
    }

    /**
     * Returns the person's full normalized name.
     *
     * @return first and last name separated by a space
     */
    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
