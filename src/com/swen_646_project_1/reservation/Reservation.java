// Declares the package name for the project, grouping related classes together.
package com.swen_646_project_1.reservation;
/*
 * Imports the following:
 * - Address class to handle lodging and mailing addresses in reservations
 * - Custom exception class to handle various error scenarios related to reservations.
 * - ReservationStatus enum to manage different states of reservations.
 * - Time utility for handling reservation start dates
 */
import com.swen_646_project_1.Address;
import com.swen_646_project_1.exceptions.IllegalOperation_Exception;
import com.swen_646_project_1.exceptions.IllegalParameter_Exception;
import com.swen_646_project_1.enums.ReservationStatus;
import com.swen_646_project_1.exceptions.IllegalState_Exception;

import java.time.LocalDate;

/**
 * Abstract base class representing a reservation.
 * This class defines common attributes and methods for all reservations.
 * Specific reservation types (child classes) must extend this class and implement price calculation.
 */
public abstract class Reservation {

    // Attributes
    protected final String reservationNumber;   // Unique identifier for reservation that cannot be changed
    protected String accountNumber;             // Account number associated with this reservation
    protected Address lodgingPhysicalAddress;    // Physical address of the lodging for this reservation
    protected Address lodgingMailingAddress;     // Mailing address of the lodging if different from physical address
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
    public Reservation(String reservationNumber, String accountNumber, Address lodgingPhysicalAddress,
                       Address lodgingMailingAddress, LocalDate startDate, int numNights, int numBeds,
                       int numBedrooms, int numBathrooms, int lodgingSizeSqFt, double lodgingPrice) {

        /*
 		 * Validate input parameters to ensure they are not null, empty, or	invalid.
 		 *
 		 * 1. Check if the reservation number is null or empty.
 		 *      If true, throw an IllegalParameter_Exception with an appropriate message.
 		 *
 		 * 2. Check if the account number is null or empty.
		 *      If true, throw an IllegalParameter_Exception with an appropriate message.
		 *
 		 * 3. Check if the lodging physical address is null or empty.
 		 *      If true, throw an IllegalParameter_Exception with an appropriate message.
 		 *
 		 * 4. Check if the start date is null.
 		 *      If true, throw an IllegalParameter_Exception with an appropriate message.
 		 *
 		 * 5. Check if the number of nights is negative.
 		 *      If true, throw an IllegalParameter_Exception with an appropriate message.
 		 *
 		 * 6. Check if the lodging size in square feet is zero or negative.
 		 *      If true, throw an IllegalParameter_Exception with an appropriate message.
 		 *
 		 * 7. Check if the lodging price is negative.
 		 *      If true, throw an IllegalParameter_Exception with an appropriate message.
 		 */
        // Validate input parameters
        if (reservationNumber == null || reservationNumber.isEmpty()) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Reservation number cannot be empty.");
        } // End if statement
        if (accountNumber == null || accountNumber.isEmpty()) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Account number cannot be empty.");
        } // End if statement
        if (lodgingPhysicalAddress == null) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Lodging physical address cannot be empty.");
        } // End if statement
        if (startDate == null) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Start date cannot be null.");
        } // End if statement
        if (numNights <= 0) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Number of nights must be positive.");
        } // End if statement
        if (lodgingSizeSqFt <= 0) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Lodging size must be positive.");
        } // End if statement
        if (lodgingPrice < 0) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Lodging price cannot be negative.");
        } // End if statement

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
     * Getter that retrieves the unique reservation number.
     * @return The reservation number as a String.
     */
    public String getReservationNumber() {
        return this.reservationNumber;
    } // End getReservationNumber method

    /**
     * Getter that retrieves the account number associated with this reservation.
     */
    public String getAccountNumber() {
        return this.accountNumber;
    } // End getAccountNumber method

    /**
     * Setter that updates the account number.
     */
    public void setAccountNumber(String accountNumber) {
        if (accountNumber != null && !accountNumber.isEmpty()) {
            this.accountNumber = accountNumber;
        } // End if statement

    } // End setAccountNumber method

    /**
     * Getter that retrieves the reservation status.
     * @return The current status of the reservation.
     */
    public ReservationStatus getStatus() {
        return this.status;
    } // End getStatus method

    /**
     * Setter that updates the reservation status.
     */
    public void setStatus(ReservationStatus status) {
        if (status == null) {
            throw new IllegalParameter_Exception("N/A", "N/A", "Status cannot be null.");
        } // End if statement
        this.status = status;
    } // End setStatus method

    /**
     * Getter that retrieves the lodging physical address.
     * @return The lodging physical address as a String.
     */
    public Address getLodgingPhysicalAddress() {
        return this.lodgingPhysicalAddress;
    } // End getLodgingPhysicalAddress method

    /**
     * Setter that updates the lodging physical address.
     */
    public void setLodgingPhysicalAddress(Address lodgingPhysicalAddress) {
        this.lodgingPhysicalAddress = lodgingPhysicalAddress;
    } // End setLodgingPhysicalAddress method

    /**
     * Getter that retrieves the lodging mailing address.
     * @return The lodging mailing address as a String.
     */
    public Address getLodgingMailingAddress() {
        return this.lodgingMailingAddress;
    } // End getLodgingMailingAddress method

    /**
     * Setter that updates the lodging mailing address.
     */
    public void setLodgingMailingAddress(Address lodgingMailingAddress) {
        this.lodgingMailingAddress = lodgingMailingAddress;
    } // End setLodgingMailingAddress method

    /**
     * Getter that retrieves the start date of the reservation.
     * @return The start date as a LocalDate.
     */
    public LocalDate getStartDate() {return this.startDate; } // End getStartDate method

    /**
     * Setter that updates the start date.
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    } // End setStartDate method

    /**
     * Getter that retrieves the number of nights for the stay.
     * @return The number of nights as an integer.
     */
    public int getNumNights() {
        return this.numNights;
    } // End getNumNights method

    /**
     * Getter that retrieves the number of beds available.
     * @return The number of beds as an integer.
     */
    public int getNumBeds() {
        return this.numBeds;
    } // End getNumBeds method

    /**
     * Getter that retrieves the number of bedrooms in the lodging.
     * @return The number of bedrooms as an integer.
     */
    public int getNumBedrooms() {
        return this.numBedrooms;
    } // End getNumBedrooms method

    /**
     * Getter that retrieves the number of bathrooms in the lodging.
     * @return The number of bathrooms as an integer.
     */
    public int getNumBathrooms() {
        return this.numBathrooms;
    } // End getNumBathrooms method

    /**
     * Getter that retrieves the size of the lodging in square feet.
     * @return The lodging size in square feet as an integer.
     */
    public int getLodgingSizeSqFt() {
        return this.lodgingSizeSqFt;
    } // End getLodgingSizeSqFt method

    /**
     * Getter that retrieves the price per night.
     * @return The price per night as a double.
     */
    public double getLodgingPrice() {
        return this.lodgingPrice;
    } // End getLodgingPrice method

    /**
     * Setter that updates the price per night for the lodging.
     * Ensures the lodging price is not set to a negative value.
     *
     * @param lodgingPrice The new price per night as a double.
     */
    public void setLodgingPrice(double lodgingPrice) {
        this.lodgingPrice = lodgingPrice;
    } // End setLodgingPrice method

    /**
     * Marks the reservation as completed.
     * Throws IllegalState_Exception if the reservation is already completed or cancelled.
     */
    public void completeReservation() {

        /*
         * if reservation is already completed or cancelled
         * 	    throw IllegalState_Exception
         * else
         * 	    update reservation status to completed
         */
        if (this.status == ReservationStatus.COMPLETED || this.status == ReservationStatus.CANCELLED) {
            throw new IllegalState_Exception(this.accountNumber, this.reservationNumber,
                    "Cannot complete a cancelled or already completed reservation.");
        } // End if statement

        this.status = ReservationStatus.COMPLETED;

    } // End completeReservation method

    /**
     * Cancels the reservation.
     * Throws IllegalState_Exception if the reservation is already completed or cancelled.
     */
    public void cancelReservation() {

        /*
         * if reservation is already completed or cancelled
         * 	    throw IllegalState_Exception
         * else
         * 	    update reservation status to cancelled
         */
        if (this.status == ReservationStatus.COMPLETED || this.status == ReservationStatus.CANCELLED) {
            throw new IllegalState_Exception(this.accountNumber, this.reservationNumber,
                    "Cannot cancel a completed or already cancelled reservation.");
        } // End if statement

        this.status = ReservationStatus.CANCELLED;

    } // End cancelReservation method

    /**
     * Updates reservation details with new data.
     * @param newReservationData The new Reservation object containing updated details.
     */
    public void updateReservation(Reservation newReservationData) {
        this.lodgingPhysicalAddress = newReservationData.lodgingPhysicalAddress;
        this.lodgingMailingAddress = newReservationData.lodgingMailingAddress;
        this.startDate = newReservationData.startDate;
        this.numNights = newReservationData.numNights;
        this.numBeds = newReservationData.numBeds;
        this.numBedrooms = newReservationData.numBedrooms;
        this.numBathrooms = newReservationData.numBathrooms;
        this.lodgingSizeSqFt = newReservationData.lodgingSizeSqFt;
        this.lodgingPrice = newReservationData.lodgingPrice;
    } // End updateReservation method

    /**
     * Abstract method to calculate the price per night for the reservation.
     * Must be implemented by subclasses.
     * @return The price per night as a double.
     */
    public abstract double calculatePricePerNight();

    /**
     * Abstract method that creates a string representation of the reservation details.
     * Must be implemented by subclasses.
     * @return A formatted string containing reservation details.
     */
    @Override
    public abstract String toString();

    /**
     * Method for creating a Reservation object from a string.
     * Subclasses must implement this method to handle their unique data formats.
     * @param data A string containing reservation details.
     * Calling this method from the base class will result in an IllegalOperation_Exception.
     */
    public static Reservation fromString(String data) throws IllegalOperation_Exception {

        /*
         * throw IllegalOperation_Exception
         * fromString() must be implemented by subclasses.
         */
        throw new IllegalOperation_Exception("fromString", "N/A", "N/A", "Must be implemented by subclasses.");

    } // End fromString method

} // end abstract class Reservation
