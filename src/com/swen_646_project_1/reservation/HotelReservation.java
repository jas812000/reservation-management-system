// Declares the package name for the project, grouping related classes together.
package com.swen_646_project_1.reservation;

import java.time.LocalDate;     // Import for handing reservation start dates

/**
 * Represents a hotel reservation.
 * Inherits from Reservation class.
 * Includes an additional attribute for kitchenette availability.
 */
public class HotelReservation extends Reservation {

    // Attributes
    private boolean hasKitchenette;     // Indicates whether the hotel room includes a kitchenette

    /**
     * Constructor to initialize a HotelReservation object.
     *
     * @param reservationNumber      Unique identifier for the reservation (cannot be null or empty).
     * @param accountNumber          Account number associated with the reservation (cannot be null or empty).
     * @param lodgingPhysicalAddress Physical address of the hotel (cannot be null or empty).
     * @param lodgingMailingAddress  Mailing address of the lodging (optional, can be null).
     * @param startDate              The start date of the reservation (cannot be null).
     * @param numNights              Number of nights for the reservation (must be positive).
     * @param numBeds                Number of beds in the hotel room.
     * @param numBedrooms            Number of bedrooms in the hotel room.
     * @param numBathrooms           Number of bathrooms in the hotel room.
     * @param lodgingSizeSqFt        Size of the hotel room in square feet (must be positive).
     * @param lodgingPrice           Price per night for the hotel room (must be positive).
     * @param kitchenetteAvailable   Indicates if the hotel room includes a kitchenette
     */
    public HotelReservation(String reservationNumber, String accountNumber, String lodgingPhysicalAddress,
                            String lodgingMailingAddress, LocalDate startDate, int numNights, int numBeds,
                            int numBedrooms, int numBathrooms, int lodgingSizeSqFt, double lodgingPrice,
                            boolean kitchenetteAvailable) {

        // Calls the superclass (Reservation) constructor to initialize common reservation attributes.
        super(reservationNumber, accountNumber, lodgingPhysicalAddress, lodgingMailingAddress, startDate, numNights,
                numBeds, numBedrooms, numBathrooms, lodgingSizeSqFt, lodgingPrice);

        // Assign specific attributes for HotelReservation.
        this.hasKitchenette = kitchenetteAvailable;

    } // End HotelReservation constructor

    /**
     * Retrieves whether the hotel room has a kitchenette.
     * @return True if the hotel room has a kitchenette, otherwise false.
     */
    public boolean hasKitchenette() {return hasKitchenette;} // end hasKitchenette method

    /**
     * Calculates the price per night for the hotel reservation.
     * Implementation will likely depend on specific pricing logic.
     * @return The price per night as a double
     */
    @Override
    public double calculatePricePerNight() {return 0.0d;} // End calculatePricePerNight method

    /**
     * Returns a string representation of the hotel reservation details.
     * @return A formatted string containing reservation details
     */
    @Override
    public String toString() {return null;} // End toString method

    /**
     * Creates a HotelReservation object from a formatted string.
     * @param data A string containing hotel reservation details in a predefined format
     * @return A HotelReservation object created from the provided data
     */
    public static HotelReservation fromString(String data) {return null;} // End fromString method

} // end class HotelReservation
