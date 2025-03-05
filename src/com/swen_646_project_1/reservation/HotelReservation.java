// Declares the package name for the project, grouping related classes together.
package com.swen_646_project_1.reservation;
// Import for handing reservation start dates
import java.time.LocalDate;
// Import Address class to handle lodging and mailing addresses in reservations
import com.swen_646_project_1.Address;
import com.swen_646_project_1.enums.ReservationStatus;
import com.swen_646_project_1.exceptions.IllegalLoad_Exception;

/**
 * Represents a hotel reservation.
 * Inherits from Reservation class.
 * Includes an additional attribute for kitchenette availability.
 */
public class HotelReservation extends Reservation {

    // Attributes
    private boolean kitchenetteAvailable;     // Indicates whether the hotel room includes a kitchenette

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
    public HotelReservation(String reservationNumber, String accountNumber, Address lodgingPhysicalAddress,
                            Address lodgingMailingAddress, LocalDate startDate, int numNights, int numBeds,
                            int numBedrooms, int numBathrooms, int lodgingSizeSqFt, double lodgingPrice,
                            boolean kitchenetteAvailable) {

        // Calls the superclass (Reservation) constructor to initialize common reservation attributes.
        super(reservationNumber, accountNumber, lodgingPhysicalAddress, lodgingMailingAddress, startDate, numNights,
                numBeds, numBedrooms, numBathrooms, lodgingSizeSqFt, lodgingPrice);

        // Assign specific attributes for HotelReservation.
        this.kitchenetteAvailable = kitchenetteAvailable;

    } // End HotelReservation constructor

    /**
     * Retrieves whether the hotel room has a kitchenette.
     * @return True if the hotel room has a kitchenette, otherwise false.
     */
    public boolean hasKitchenette() {
        return kitchenetteAvailable;
    } // end hasKitchenette method

    /**
     * Updates the kitchenette availability in the hotel room.
     * @param kitchenetteAvailable True if the room has a kitchenette, false otherwise.
     */
    public void setKitchenetteAvailable(boolean kitchenetteAvailable) {
        this.kitchenetteAvailable = kitchenetteAvailable;
    } // End setKitchenetteAvailable method

    /**
     * Calculates the price per night for the hotel reservation.
     * Implementation will likely depend on specific pricing logic.
     * @return The price per night as a double
     */
    @Override
    public double calculatePricePerNight() {

        /*
         * return price per night for the hotel room
         * * may include additional cost if kitchenette is available
         */
        // If reservation is cancelled, price should be $0.00
        if (this.status == ReservationStatus.CANCELLED) {
            return 0.00;
        } // End if statement
        double basePrice = 120.0; // Base price

        if (this.lodgingSizeSqFt > 900) {
            basePrice += 15.0; // Additional fee for large lodging
        } // End if statement
        basePrice += 50.0; // Flat fee for hotel
        if (kitchenetteAvailable) {
            basePrice += 10.0; // Additional fee for kitchenette
        } // End if statement

        return basePrice;

    } // End calculatePricePerNight method

    /**
     * Returns a string representation of the hotel reservation details.
     * @return A formatted string containing reservation details
     */
    @Override
    public String toString() {

        /*
         * format and return hotel reservation details as a string
         */
        return String.format("HotelReservation,%s,%s,\"%s\"%s,%s,%d,%d,%d,%d,%d,%.2f,%s,%b",
                reservationNumber, accountNumber,
                String.join(";", lodgingPhysicalAddress.getStreet(), lodgingPhysicalAddress.getCity(),
                        lodgingPhysicalAddress.getState(), String.valueOf(lodgingPhysicalAddress.getZipCode())),
                (lodgingMailingAddress != null ?
                        "," + "\"" + String.join(";", lodgingMailingAddress.getStreet(), lodgingMailingAddress.getCity(),
                                lodgingMailingAddress.getState(), String.valueOf(lodgingMailingAddress.getZipCode())) + "\""
                        : ",N/A"),
                startDate, numNights, numBeds, numBedrooms, numBathrooms,
                lodgingSizeSqFt, lodgingPrice, status, kitchenetteAvailable);
    } // End toString method

    /**
     * Creates a HotelReservation object from a formatted string.
     * @param data A string containing hotel reservation details in a predefined format
     * @return A HotelReservation object created from the provided data
     */
    public static HotelReservation fromString(String data) {
        /*
         * Parse data string
         * Extract hotel reservation details
         * Return new HotelReservation object with extracted details
         */

        //System.out.println("Raw Data for Parsing: " + data); // Debugging output

        // Use regex to split while preserving quoted substrings
        String[] parts = data.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

        // Ensure correct number of fields
        if (parts.length < 14) {
            throw new IllegalLoad_Exception("HotelReservation Data", "N/A",
                    "Invalid data format. Found: " + parts.length);
        }

        // Extract lodging physical address components
        Address lodgingPhysicalAddress = getAddress(parts);

        // Extract lodging mailing address components
        Address lodgingMailingAddress = getLodgingMailingAddress(parts);

        // Fix: Ensure indexes are correct
        return new HotelReservation(
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
                Boolean.parseBoolean(parts[13]) // Fix: Make sure kitchenetteAvailable is boolean
        );
    }
    // End fromString method

    private static Address getLodgingMailingAddress(String[] parts) {
        Address lodgingMailingAddress = null;
        if (!parts[4].equals("N/A")) {
            String[] mailingAddressParts = parts[4].replace("\"", "").split(";");
            if (mailingAddressParts.length < 4) {
                throw new IllegalLoad_Exception("HotelReservation Address", "N/A", "Invalid mailing address format.");
            } // End if statement
            return new Address(mailingAddressParts[0], mailingAddressParts[1],
                    mailingAddressParts[2], Integer.parseInt(mailingAddressParts[3]));
        } // End if statement
       // return lodgingMailingAddress;
        return null;
    } // End getlodgingMailingAddress method

    private static Address getAddress(String[] parts) {
        if (parts.length < 14) { // Ensure correct number of fields
            throw new IllegalLoad_Exception("HotelReservation Data", "N/A", "Invalid data format. Found: " + parts.length);
        } // End if statement

        // Extract lodging physical address components
        String[] physicalAddressParts = parts[3].replace("\"", "").split(";");
        if (physicalAddressParts.length < 4) {
            throw new IllegalLoad_Exception("HotelReservation Address", "N/A", "Invalid physical address format.");
        } // End if statement
        return new Address(physicalAddressParts[0], physicalAddressParts[1],
                physicalAddressParts[2], Integer.parseInt(physicalAddressParts[3]));
    } // End getAddress method

} // End class HotelReservation
