// Declares the package name for the project, grouping related classes together.
package com.swen_646_project_1;

// Custom exception classes to handle various error scenarios related to addresses.
import com.swen_646_project_1.exceptions.IllegalParameter_Exception;

/**
 * Represents a physical address.
 * Contains details such as street, city, state, and zip code.
 */
public class Address {
    private String street;
    private String city;
    private String state;
    private int zipCode;


    /**
     * Constructor to initialize an Address object.
     * @param street The street name and number.
     * @param city The city of the address.
     * @param state The state of the address.
     * @param zipCode The zip code of the address.
     */
    public Address(String street, String city, String state, int zipCode) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    } // End Constructor


    /**
     * Getters that retrieve the street, city, state and zip code.
     */
    public String getStreet() { return street; } // End getStreet method
    public String getCity() { return city; } // End getCity method
    public String getState() { return state; } // End getState method
    public int getZipCode() { return zipCode; } // End getZipCode method


    /**
     * Setters that update the street, city, state, and zip code.
     * Reuse validateAddress method to avoid redundant checks.
     */
    public void setAddress(String street, String city, String state, int zipCode) {
        validateAddress(street, city, state, zipCode); // Validate new address values
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    } // End setAddress method


    /**
     * Validates address parameters to ensure they are properly formatted.
     * @throws IllegalParameter_Exception If any parameter is invalid.
     */
    private void validateAddress(String street, String city, String state, int zipCode) {
        if (street == null || street.isEmpty()) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Street cannot be empty.");
        } // End if statement
        if (city == null || city.isEmpty()) {
            throw new IllegalParameter_Exception("N/A", "N/A", "City cannot be empty.");
        } // End if statement
        if (state == null || state.length() != 2) {
            throw new IllegalParameter_Exception("N/A", "N/A", "State must be a valid 2-letter abbreviation.");
        } // End if statement
        if (zipCode < Integer.parseInt("00500") || zipCode > Integer.parseInt("99999")) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Zip code must be a 5-digit number.");
        } // End if statement
    } // End validateAddress method


    /**
     * Returns a string representation of the address.
     * @return Formatted address as a string.
     */
    @Override
    public String toString() {
        return street + ", " + city + ", " + state + " " + zipCode;
    } // End toString method

} // End Address class
