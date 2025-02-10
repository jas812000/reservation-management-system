// Declares the package name for the project, grouping related classes together.
package com.swen_646_project_1.reservation;
// Import for handing reservation start dates
import java.time.LocalDate;
// Import Address class to handle lodging and mailing addresses in reservations
import com.swen_646_project_1.Address;

/**
 * Represents a cabin reservation.
 * Inherits from the Reservation class.
 * Includes additional attributes for kitchen and loft availability.
 */
public class CabinReservation extends Reservation {

    // Attributes
    private boolean fullKitchenAvailable;     // Indicates whether the cabin includes a full kitchen
    private boolean loftAvailable;            // Indicates whether the cabin has a loft area

    /**
     * Constructor to initialize a CabinReservation object.
     *
     * @param reservationNumber      Unique identifier for the reservation (cannot be null or empty).
     * @param accountNumber          Account number associated with the reservation (cannot be null or empty).
     * @param lodgingPhysicalAddress Physical address of the reserved cabin (cannot be null or empty).
     * @param lodgingMailingAddress  Mailing address of the lodging (optional, can be null).
     * @param startDate              The start date of the reservation (cannot be null).
     * @param numNights              Number of nights for the reservation (must be positive).
     * @param numBeds                Number of beds available in the cabin.
     * @param numBedrooms            Number of bedrooms in the cabin.
     * @param numBathrooms           Number of bathrooms in the cabin.
     * @param lodgingSizeSqFt        Size of the lodging in square feet (must be positive).
     * @param lodgingPrice           Price per night for the lodging (must be positive).
     * @param fullKitchenAvailable   Indicates if the cabin includes a full kitchen
     * @param loftAvailable          Indicates if the cabin includes a loft
     */
    public CabinReservation(String reservationNumber, String accountNumber, Address lodgingPhysicalAddress,
                            Address lodgingMailingAddress, LocalDate startDate, int numNights, int numBeds,
                            int numBedrooms, int numBathrooms, int lodgingSizeSqFt, double lodgingPrice,
                            boolean fullKitchenAvailable, boolean loftAvailable){

        // Calls the superclass (Reservation) constructor to initialize common reservation attributes.
        super(reservationNumber, accountNumber, lodgingPhysicalAddress, lodgingMailingAddress, startDate, numNights,
                numBeds, numBedrooms, numBathrooms, lodgingSizeSqFt, lodgingPrice);

        // Assign specific attributes for CabinReservation.
        this.fullKitchenAvailable = fullKitchenAvailable;
        this.loftAvailable = loftAvailable;

    } // End CabinReservation constructor

    /**
     * Retrieves whether a full kitchen is available in the cabin.
     * @return True if the cabin has a full kitchen, otherwise false.
     */
    public boolean isFullKitchenAvailable() {
        return fullKitchenAvailable;
    } // End isFullKitchenAvailable method

    /**
     * Retrieves whether a loft is available in the cabin.
     * @return True if the cabin has a loft, otherwise false.
     */
    public boolean isLoftAvailable() {
        return loftAvailable;
    } // End isLoftAvailable method

    /**
     * Updates the availability of a full kitchen in the cabin.
     * @param fullKitchenAvailable True if the cabin has a full kitchen, false otherwise.
     */
    public void setFullKitchenAvailable(boolean fullKitchenAvailable) {
        this.fullKitchenAvailable = fullKitchenAvailable;
    } // End setFullKitchenAvailable method

    /**
     * Updates the availability of a loft in the cabin.
     * @param loftAvailable True if the cabin has a loft, false otherwise.
     */
    public void setLoftAvailable(boolean loftAvailable) {
        this.loftAvailable = loftAvailable;
    } // End setLoftAvailable method

    /**
     * Calculates the price per night for the cabin.
     * Implementation will likely depend on specific pricing logic.
     * @return The price per night as a double
     */
    @Override
    public double calculatePricePerNight(){

        /*
         * return price per night for the cabin
         * may include additional cost if full kitchen or loft is available
         */
        return 0.0d;

    } // End calculatePricePerNight method

    /**
     * Returns a string representation of the cabin reservation details.
     * @return A formatted string containing reservation details
     */
    @Override
    public String toString(){

        /*
         * format and return cabin reservation details as a string
         */
        return null;

    } // End toString method

    /**
     * Creates a CabinReservation object from a formatted string.
     * @param data A string containing cabin reservation details in a predefined format
     * @return A CabinReservation object created from the provided data
     */
    public static CabinReservation fromString(String data){

        /*
         * parse data string
         * extract cabin reservation details
         * return new CabinReservation object with extracted details
         */
        return null;

    } // End fromString method

} // end class CabinReservation
