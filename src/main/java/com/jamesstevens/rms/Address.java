package com.jamesstevens.rms;

import com.jamesstevens.rms.exceptions.IllegalParameterException;

/**
 * Represents a physical mailing or lodging address.
 * <p>
 * An address consists of a street, city, 2-letter state abbreviation, and a 5-digit zip code.
 * </p>
 */
public class Address {

    private String street;
    private String city;
    private String state;
    private String zipCode;

    /**
     * Constructs a new {@code Address}.
     *
     * @param street  street name and number (required)
     * @param city    city name (required)
     * @param state   2-letter state abbreviation (required)
     * @param zipCode 5-digit zip code
     * @throws IllegalParameterException if any field is invalid
     */
    public Address(String street, String city, String state, String zipCode) {
        validateAddress(street, city, state, zipCode);
        this.street = street.trim();
        this.city = city.trim();
        this.state = state.trim().toUpperCase();
        this.zipCode = zipCode.trim();
    }

    /**
     * Returns the street line.
     *
     * @return street
     */
    public String getStreet() {
        return street;
    }

    /**
     * Returns the city name.
     *
     * @return city
     */
    public String getCity() {
        return city;
    }

    /**
     * Returns the 2-letter state abbreviation.
     *
     * @return state abbreviation
     */
    public String getState() {
        return state;
    }

    /**
     * Returns the zip code.
     *
     * @return zip code
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Updates all address fields after validating the provided values.
     *
     * @param street  street name and number (required)
     * @param city    city name (required)
     * @param state   2-letter state abbreviation (required)
     * @param zipCode 5-digit zip code
     * @throws IllegalParameterException if any field is invalid
     */
    public void setAddress(String street, String city, String state, String zipCode) {
        validateAddress(street, city, state, zipCode);
        this.street = street.trim();
        this.city = city.trim();
        this.state = state.trim().toUpperCase();
        this.zipCode = zipCode.trim();
    }

    /**
     * Validates that all address values meet basic formatting requirements.
     *
     * @param street  street name and number
     * @param city    city name
     * @param state   2-letter state abbreviation
     * @param zipCode 5-digit zip code
     * @throws IllegalParameterException if any parameter is invalid
     */
    private void validateAddress(String street, String city, String state, String zipCode) {
        if (street == null || street.isBlank()) {
            throw new IllegalParameterException("N/A", "N/A", "Street cannot be empty.");
        }

        if (city == null || city.isBlank()) {
            throw new IllegalParameterException("N/A", "N/A", "City cannot be empty.");
        }

        if (state == null || !state.trim().matches("[A-Za-z]{2}")) {
            throw new IllegalParameterException("N/A", "N/A", "State must be a valid 2-letter abbreviation.");
        }

        if (zipCode == null || !zipCode.trim().matches("\\d{5}")) {
            throw new IllegalParameterException("N/A", "N/A", "Zip code must be a 5-digit number.");
        }
    }

    /**
     * Returns a human-readable address string.
     *
     * @return formatted address string
     */
    @Override
    public String toString() {
        return street + ", " + city + ", " + state + " " + zipCode;
    }
}
