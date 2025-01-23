// Declares the package name for the project, grouping related classes together.
package com.swen_646_project_1.reservation;

// Imports the ReservationStatus enum which defines different statuses a reservation can have.
import com.swen_646_project_1.enums.ReservationStatus;
import java.time.LocalDate;     // Import for handling reservation start dates

/**
 * Abstract base class representing a reservation.
 * This class defines common attributes and methods for all reservations.
 * Specific reservation types (child classes) must extend this class and implement price calculation.
 */
public abstract class Reservation {

    // Attributes
    protected final String reservationNumber;   // Unique identifier for reservation that cannot be changed
    protected String accountNumber;             // Account number associated with this reservation
    protected String lodgingPhysicalAddress;    // Physical address of the lodging for this reservation
    protected String lodgingMailingAddress;     // Mailing address of the lodging if different from physical address
    protected LocalDate startDate;              // Start date of the reservation
    protected int numNights;                    // Number of nights for the stay
    protected int numBeds;                      // Number of beds available in the lodging
    protected int numBedrooms;                  // Number of bedrooms in the lodging
    protected int numBathrooms;                 // Number of bathrooms in the lodging
    protected int lodgingSizeSqFt;              // Size of the lodging in square feet
    protected double lodgingPrice;              // Price per night for the lodging
    protected ReservationStatus status;         // Current status of the reservation

    /**
     * Constructor to initialize a Reservation object.
     * @param reservationNumber      Unique identifier for the reservation (cannot be null or empty).
     * @param accountNumber          Account number associated with the reservation (cannot be null or empty).
     * @param lodgingPhysicalAddress Physical address of the lodging (cannot be null or empty).
     * @param lodgingMailingAddress  Mailing address of the lodging (optional).
     * @param startDate              Start date of the reservation (cannot be null).
     * @param numNights              Number of nights for the stay (must be positive).
     * @param numBeds                Number of beds available in the lodging.
     * @param numBedrooms            Number of bedrooms in the lodging.
     * @param numBathrooms           Number of bathrooms in the lodging.
     * @param lodgingSizeSqFt        Size of the lodging in square feet (must be positive).
     * @param lodgingPrice           Price per night for the lodging (must be positive).
     */
    public Reservation(String reservationNumber, String accountNumber, String lodgingPhysicalAddress,
                       String lodgingMailingAddress, LocalDate startDate, int numNights, int numBeds,
                       int numBedrooms, int numBathrooms, int lodgingSizeSqFt, double lodgingPrice) {

        // Throws Illegal Argument Exception if any parameter is null or empty.
        if (reservationNumber == null || reservationNumber.isEmpty()) {
            throw new IllegalArgumentException("Reservation number cannot be null or empty.");
        } else if (accountNumber == null || accountNumber.isEmpty()) {
            throw new IllegalArgumentException("Account number cannot be null or empty.");
        } else if (lodgingPhysicalAddress == null || lodgingPhysicalAddress.isEmpty()) {
            throw new IllegalArgumentException("Lodging physical address cannot be null or empty.");
        } else if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null.");
        } else if (numNights < 0) {
            throw new IllegalArgumentException("Number of nights must be positive.");
        } else if (lodgingSizeSqFt <= 0) {
            throw new IllegalArgumentException("Lodging size must be positive.");
        } else if (lodgingPrice < 0) {
            throw new IllegalArgumentException("Lodging price cannot be negative.");
        } // end if/else statements

        // Assign values after validation
        this.reservationNumber = reservationNumber;
        this.accountNumber = accountNumber;
        this.lodgingPhysicalAddress = lodgingPhysicalAddress;
        this.lodgingMailingAddress = lodgingMailingAddress;
        this.startDate = startDate;
        this.numNights = numNights;
        this.numBeds = numBeds;
        this.numBedrooms = numBedrooms;
        this.numBathrooms = numBathrooms;
        this.lodgingSizeSqFt = lodgingSizeSqFt;
        this.lodgingPrice = lodgingPrice;
        this.status = ReservationStatus.DRAFT;

    } // End Reservation constructor

    /**
     * Retrieves the unique reservation number.
     * @return The reservation number as a String.
     */
    public String getReservationNumber() {return null;} // End getReservationNumber method

    /**
     * Marks the reservation as completed.
     * Throws IllegalStateException if the reservation is already completed or cancelled.
     */
    public void completeReservation() {} // End completeReservation method

    /**
     * Cancels the reservation.
     * Throws IllegalStateException if the reservation is already completed or cancelled.
     */
    public void cancelReservation() {} // End cancelReservation method

    /**
     * Abstract method to calculate the price per night for the reservation.
     * Must be implemented by subclasses.
     * @return The price per night as a double.
     */
    public double calculatePricePerNight() {return 0.0d;} // End calculatePricePerNight method

    /**
     * Returns a string representation of the reservation details.
     * @return A formatted string containing reservation details.
     */
    @Override
    public String toString() {return null;} // End toString method

    /**
     * Creates a Reservation object from a formatted string.
     * This method may return different reservation subtypes based on the data.
     * @param data A string containing reservation details in a predefined format.
     * @return A Reservation object constructed from the provided data.
     */
    public static Reservation fromString(String data) {return null;} // End fromString method

} // end abstract class Reservation
