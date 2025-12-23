package com.jamesstevens.rms;

import com.jamesstevens.rms.exceptions.IllegalParameter_Exception;

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
    private int zipCode;

    /**
     * Constructs a new {@code Address}.
     *
     * @param street  street name and number (required)
     * @param city    city name (required)
     * @param state   2-letter state abbreviation (required)
     * @param zipCode 5-digit zip code
     * @throws IllegalParameter_Exception if any field is invalid
     */
    public Address(String street, String city, String state, int zipCode) {
        validateAddress(street, city, state, zipCode);
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
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
    public int getZipCode() {
        return zipCode;
    }

    /**
     * Updates all address fields after validating the provided values.
     *
     * @param street  street name and number (required)
     * @param city    city name (required)
     * @param state   2-letter state abbreviation (required)
     * @param zipCode 5-digit zip code
     * @throws IllegalParameter_Exception if any field is invalid
     */
    public void setAddress(String street, String city, String state, int zipCode) {
        validateAddress(street, city, state, zipCode);
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    /**
     * Validates that all address values meet basic formatting requirements.
     *
     * @param street  street name and number
     * @param city    city name
     * @param state   2-letter state abbreviation
     * @param zipCode zip code as a 5-digit integer
     * @throws IllegalParameter_Exception if any parameter is invalid
     */
    private void validateAddress(String street, String city, String state, int zipCode) {
        if (street == null || street.isEmpty()) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Street cannot be empty.");
        }
        if (city == null || city.isEmpty()) {
            throw new IllegalParameter_Exception("N/A", "N/A", "City cannot be empty.");
        }
        if (state == null || state.length() != 2) {
            throw new IllegalParameter_Exception("N/A", "N/A", "State must be a valid 2-letter abbreviation.");
        }
        if (zipCode < 500 || zipCode > 99999) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Zip code must be a 5-digit number.");
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
