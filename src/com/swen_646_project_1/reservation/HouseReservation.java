// Declares the package name for the project, grouping related classes together.
package com.swen_646_project_1.reservation;

/*
 * Imports the following:
 * - Time utility for handling reservation start dates
 * - Address class to handle lodging and mailing addresses in reservations
 * - ReservationStatus enum to manage different states of reservations.
 * - Custom exception class to handle various error scenarios related to reservations.
 */
import java.time.LocalDate;
import com.swen_646_project_1.Address;
import com.swen_646_project_1.enums.ReservationStatus;
import com.swen_646_project_1.exceptions.IllegalLoad_Exception;
import com.swen_646_project_1.exceptions.IllegalParameter_Exception;

/**
 * Represents a house reservation with multiple floors.
 * Inherits from Reservation.
 * Includes an additional attribute for number of floors.
 */
public class HouseReservation extends Reservation {

    // Attributes
    private int numFloors;      // Number of floors in the reserved house

    /**
     * Constructor to initialize a HouseReservation object.
     *
     * @param reservationNumber      Unique identifier for the reservation (cannot be null or empty).
     * @param accountNumber          Account number associated with the reservation (cannot be null or empty).
     * @param lodgingPhysicalAddress Physical address of the reserved house (cannot be null or empty).
     * @param lodgingMailingAddress  Mailing address of the lodging (optional, can be null).
     * @param startDate              The start date of the reservation (cannot be null).
     * @param numNights              Number of nights for the reservation (must be positive).
     * @param numBeds                Number of beds in the house.
     * @param numBedrooms            Number of bedrooms in the house.
     * @param numBathrooms           Number of bathrooms in the house.
     * @param lodgingSizeSqFt        Size of the lodging in square feet (must be positive).
     * @param lodgingPrice           Price per night for the lodging (must be positive).
     * @param numFloors              Number of floors in the reserved house (must be positive).
     */
    public HouseReservation(String reservationNumber, String accountNumber, Address lodgingPhysicalAddress,
                            Address lodgingMailingAddress, LocalDate startDate, int numNights, int numBeds,
                            int numBedrooms, int numBathrooms, int lodgingSizeSqFt, double lodgingPrice,
                            int numFloors){
        // Calls the superclass (Reservation) constructor to initialize common reservation attributes.
        super(reservationNumber, accountNumber, lodgingPhysicalAddress, lodgingMailingAddress, startDate, numNights,
                numBeds, numBedrooms, numBathrooms, lodgingSizeSqFt, lodgingPrice);

        // Validate numFloors to ensure it is a positive number.
        /*
         * if numFloors is less than or equal to 0
         *      throw an IllegalArgumentException: "Number of floors must be positive."
         */
        if (numFloors <= 0) {
            throw new IllegalParameter_Exception("N/A", "N/A",
                    "A house cannot have zero or negative floors. Please enter a valid number..");
        } // End if statement

        // Assign specific attributes for HouseReservation.
        this.numFloors = numFloors;

    } // End HouseReservation constructor

    /**
     * Retrieves the number of floors in the reserved house.
     * @return The number of floors.
     */
    public int getNumFloors() {
        return numFloors;
    } // End getNumFloors method

    /**
     * Updates the number of floors in the reserved house.
     * @param numFloors The new number of floors.
     */
    public void setNumFloors(int numFloors) {
        if (numFloors <= 0) {
            throw new IllegalParameter_Exception("N/A", "N/A",
                    "A house cannot have zero or negative floors. Please enter a valid number..");
        } // End if statement
        this.numFloors = numFloors;
    } // End setNumFloors method

    /**
     * Calculates the price per night for the house reservation.
     * Implementation will likely depend on specific pricing logic.
     * @return The price per night as a double
     */
    @Override
    public double calculatePricePerNight(){
        /*
         * return price per night for the house
         * may include additional cost based on the number of floors
         */
        // If reservation is cancelled, price should be $0.00
        if (this.status == ReservationStatus.CANCELLED) {
            return 0.00;
        } // End if statement
        double basePrice = 120.0; // Base price

        if (this.lodgingSizeSqFt > 900) {
            basePrice += 15.0; // Additional fee for large lodging
        } // End if statement
        return basePrice;

    } // End calculatePricePerNight method

    /**
     * Returns a string representation of the house reservation details.
     * @return A formatted string containing reservation details
     */
    @Override
    public String toString(){
        /*
         * format and return house reservation details as a string
         */
        return String.format("HouseReservation,%s,%s,\"%s\"%s,%s,%d,%d,%d,%d,%d,%.2f,%s,%d",
                reservationNumber, accountNumber,
                String.join(";", lodgingPhysicalAddress.getStreet(), lodgingPhysicalAddress.getCity(),
                        lodgingPhysicalAddress.getState(), String.valueOf(lodgingPhysicalAddress.getZipCode())),
                (lodgingMailingAddress != null ?
                        "," + "\"" + String.join(";", lodgingMailingAddress.getStreet(), lodgingMailingAddress.getCity(),
                                lodgingMailingAddress.getState(), String.valueOf(lodgingMailingAddress.getZipCode())) + "\""
                        : ",N/A"),
                startDate, numNights, numBeds, numBedrooms, numBathrooms,
                lodgingSizeSqFt, lodgingPrice, status, numFloors);
    } // End toString method

    /**
     * Creates a HouseReservation object from a formatted string.
     * @param data A string containing house reservation details in a predefined format
     * @return A HouseReservation object created from the provided data
     */
    public static HouseReservation fromString(String data) {
        /*
         * Parse data string
         * Extract house reservation details
         * Return new HouseReservation object with extracted details
         */
        // Use regex to split while preserving quoted substrings
        String[] parts = data.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

        // Ensure correct number of fields
        if (parts.length < 14) {
            throw new IllegalLoad_Exception("HouseReservation Data", "N/A",
                    "Invalid data format. Found: " + parts.length);
        } // End if statement

        // Extract lodging physical address components
        Address lodgingPhysicalAddress = getAddress(parts);

        // Extract lodging mailing address components
        Address lodgingMailingAddress = getLodgingMailingAddress(parts);

        // Parse reservation status correctly
        ReservationStatus status = ReservationStatus.valueOf(parts[12].trim());

        // Create HouseReservation object
        HouseReservation reservation = new HouseReservation(
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
                Integer.parseInt(parts[13]) // numFloors
        );
        // ✅ Set the parsed status
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
        // Check if a valid mailing address is provided
        if (!parts[4].equals("N/A")) {
            String[] mailingAddressParts = parts[4].replace("\"", "").split(";");

            // Validate the mailing address format (should contain at least 4 components)
            if (mailingAddressParts.length < 4) {
                throw new IllegalLoad_Exception("HouseReservation Address", "N/A", "Invalid mailing address format.");
            } // End if statement

            // Return an Address object with extracted components
            return new Address(mailingAddressParts[0], mailingAddressParts[1],
                    mailingAddressParts[2], Integer.parseInt(mailingAddressParts[3]));
        } // End if statement

        // Return null if mailing address is "N/A"
        return null;
    } // End getLodgingMailingAddress method

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
            throw new IllegalLoad_Exception("HouseReservation Data", "N/A",
                    "Invalid data format. Found: " + parts.length);
        } // End if statement

        // Extract lodging physical address components
        String[] physicalAddressParts = parts[3].replace("\"", "").split(";");

        // Validate the physical address format (should contain at least 4 components)
        if (physicalAddressParts.length < 4) {
            throw new IllegalLoad_Exception("HouseReservation Address", "N/A", "Invalid physical address format.");
        } // End if statement

        // Return an Address object with extracted components
        return new Address(physicalAddressParts[0], physicalAddressParts[1],
                physicalAddressParts[2], Integer.parseInt(physicalAddressParts[3]));

    } // End getAddress method

} // End class HouseReservation
