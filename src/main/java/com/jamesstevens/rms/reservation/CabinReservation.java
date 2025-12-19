// Declares the package name for the project, grouping related classes together.
package com.jamesstevens.rms.reservation;

/*
 * Imports the following:
 * - Time utility for handling reservation start dates
 * - Address class to handle lodging and mailing addresses in reservations
 * - ReservationStatus enum to manage different states of reservations.
 * - Custom exception class to handle various error scenarios related to reservations.
 */
import java.time.LocalDate;
import com.jamesstevens.rms.Address;
import com.jamesstevens.rms.enums.ReservationStatus;
import com.jamesstevens.rms.exceptions.IllegalLoad_Exception;

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
        // If reservation is cancelled, price should be $0.00
        if (this.status == ReservationStatus.CANCELLED) {
            return 0.00;
        } // End if statement

        double basePrice = 120.0; // Base price

        if (this.lodgingSizeSqFt > 900) {
            basePrice += 15.0; // Additional fee for large lodging
        } // End if statement
        if (fullKitchenAvailable) {
            basePrice += 20.0; // Additional fee for full kitchen
        } // End if statement
        basePrice += (this.numBathrooms * 5); // Additional fee per bathroom

        return basePrice; // Updated price

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
        return String.format("CabinReservation,%s,%s,\"%s\"%s,%s,%d,%d,%d,%d,%d,%.2f,%s,%b,%b",
                reservationNumber, accountNumber,
                String.join(";",
                        lodgingPhysicalAddress.getStreet(), lodgingPhysicalAddress.getCity(),
                        lodgingPhysicalAddress.getState(), String.valueOf(lodgingPhysicalAddress.getZipCode())),
                (lodgingMailingAddress != null ?
                        "," + "\"" + String.join(";", lodgingMailingAddress.getStreet(),
                                lodgingMailingAddress.getCity(),
                                lodgingMailingAddress.getState(),
                                String.valueOf(lodgingMailingAddress.getZipCode())) + "\"": ",N/A"),
                startDate, numNights, numBeds, numBedrooms, numBathrooms,
                lodgingSizeSqFt, lodgingPrice, status, fullKitchenAvailable, loftAvailable);
    } // End toString method


    /**
     * Creates a CabinReservation object from a formatted string.
     * @param data A string containing cabin reservation details in a predefined format
     * @return A CabinReservation object created from the provided data
     */
    public static CabinReservation fromString(String data) {
        /*
         * Parse data string
         * Extract cabin reservation details
         * Return new CabinReservation object with extracted details
         */
        // Use regex to split while preserving quoted substrings
        String[] parts = data.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

        // Ensure correct number of fields
        if (parts.length < 15) {
            throw new IllegalLoad_Exception("CabinReservation Data", "N/A",
                    "Invalid data format. Found: " + parts.length);
        } // End if statement

        // Extract lodging physical address components
        Address lodgingPhysicalAddress = getAddress(parts);

        // Extract lodging mailing address components
        Address lodgingMailingAddress = getLodgingMailingAddress(parts);

        // Parse reservation status correctly
        ReservationStatus status = ReservationStatus.valueOf(parts[12].trim());

        // Create CabinReservation object
        CabinReservation reservation = new CabinReservation(
                parts[1], // reservationNumber
                parts[2], // accountNumber
                lodgingPhysicalAddress,
                lodgingMailingAddress,
                LocalDate.parse(parts[5]), // startDate
                Integer.parseInt(parts[6]), // numNights
                Integer.parseInt(parts[7]), // numBeds
                Integer.parseInt(parts[8]), // numBedrooms
                Integer.parseInt(parts[9]), // numBathrooms
                Integer.parseInt(parts[10]), // lodgingSizeSqFt
                Double.parseDouble(parts[11]), // lodgingPrice
                Boolean.parseBoolean(parts[13]), // fullKitchenAvailable
                Boolean.parseBoolean(parts[14]) // loftAvailable
        );
        // Set the parsed status
        reservation.setStatus(status);

        return reservation;
    } // End fromString method


    /**
     * Parses and retrieves the lodging mailing address from the given data parts.
     * If the mailing address is specified as "N/A", this method returns null.
     *
     * @param parts A string array containing reservation details, with the mailing address at index 4.
     * @return An Address object representing the lodging mailing address, or null if not available.
     * @throws IllegalLoad_Exception if the mailing address format is invalid.
     */
    private static Address getLodgingMailingAddress(String[] parts) {
        // Check if mailing address is provided
        if (!parts[4].equals("N/A")) {
            String[] mailingAddressParts = parts[4].replace("\"", "").split(";");

            // Validate the mailing address format (should contain at least 4 parts)
            if (mailingAddressParts.length < 4) {
                throw new IllegalLoad_Exception("CabinReservation Address", "N/A", "Invalid mailing address format.");
            } // End if statement

            // Return an Address object with extracted components
            return new Address(mailingAddressParts[0], mailingAddressParts[1],
                    mailingAddressParts[2], Integer.parseInt(mailingAddressParts[3]));
        } // End if statement

        // Return null if mailing address is "N/A"
        return null;
    } // End getlodgingMailingAddress method


    /**
     * Parses and retrieves the lodging physical address from the given data parts.
     *
     * @param parts A string array containing reservation details, expected to have at least 14 elements.
     * @return An Address object representing the lodging physical address.
     * @throws IllegalLoad_Exception if the data format is invalid or if the physical address format is incorrect.
     */
    private static Address getAddress(String[] parts) {
        // Ensure correct number of fields
        if (parts.length < 14) {
            throw new IllegalLoad_Exception("CabinReservation Data", "N/A",
                    "Invalid data format. Found: " + parts.length);
        } // End if statement

        // Extract lodging physical address components
        String[] physicalAddressParts = parts[3].replace("\"", "").split(";");

        // Validate the physical address format (should contain at least 4 parts)
        if (physicalAddressParts.length < 4) {
            throw new IllegalLoad_Exception("CabinReservation Address", "N/A", "Invalid physical address format.");
        } // End if statement

        // Return an Address object with extracted components
        return new Address(physicalAddressParts[0], physicalAddressParts[1],
                physicalAddressParts[2], Integer.parseInt(physicalAddressParts[3]));
    } // End getAddress method

} // End class CabinReservation
