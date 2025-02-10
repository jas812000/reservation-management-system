package com.swen_646_project_1;

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
    }

    /**
     * Getters that retrieve the street, city, state and zip code.
     */
    public String getStreet() { return street; } // End getStreet method
    public String getCity() { return city; } // End getCity method
    public String getState() { return state; } // End getState method
    public int getZipCode() { return zipCode; } // End getZipCode method

    /**
     * Setters that update the street, city, state and zip code.
     */
    public void setStreet(String street) { this.street = street; } // End setStreet method
    public void setCity(String city) { this.city = city; } // End setCity method
    public void setState(String state) { this.state = state; } // End setState method
    public void setZipCode(int zipCode) { this.zipCode = zipCode; } // End setZipCode method

    /**
     * Returns a string representation of the address.
     * @return Formatted address as a string.
     */
    @Override
    public String toString() {
        return street + ", " + city + ", " + state + " " + zipCode;
    } // End toString method

} // End Address class