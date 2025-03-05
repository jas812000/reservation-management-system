// Declares the package name for the project, grouping related classes together.
package com.swen_646_project_1.reservation;
// Import for handing reservation start dates
import java.time.LocalDate;
// Import Address class to handle lodging and mailing addresses in reservations
import com.swen_646_project_1.Address;

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
         * return price per night for the hotel room * may include additional cost if kitchenette is available
         */
        return 0.0d;

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
        return "ReservationNumber: " + this.reservationNumber +
                ", AccountNumber: " + this.accountNumber +
                ", LodgingPhysicalAddress: " + this.lodgingPhysicalAddress +
                ", LodgingMailingAddress: " + (this.lodgingMailingAddress != null ? this.lodgingMailingAddress : "N/A") +
                ", StartDate: " + this.startDate +
                ", NumNights: " + this.numNights +
                ", NumBeds: " + this.numBeds +
                ", NumBedrooms: " + this.numBedrooms +
                ", NumBathrooms: " + this.numBathrooms +
                ", LodgingSizeSqFt: " + this.lodgingSizeSqFt +
                ", LodgingPrice: " + this.lodgingPrice +
                ", Status: " + this.status +
                ", KitchenetteAvailable: " + this.kitchenetteAvailable;
    } // End toString method

    /**
     * Creates a HotelReservation object from a formatted string.
     * @param data A string containing hotel reservation details in a predefined format
     * @return A HotelReservation object created from the provided data
     */
    public static HotelReservation fromString(String data) {

        /*
         * parse data string
         * extract hotel reservation details
         * return new HotelReservation object with extracted details
         */
        String[] parts = data.split(",");
        if (parts.length < 12) {
            throw new IllegalArgumentException("Invalid data format for HotelReservation.");
        } // End if statement

        Address lodgingPhysicalAddress = new Address(parts[2], parts[3], parts[4], Integer.parseInt(parts[5]));
        Address lodgingMailingAddress = parts[6].equals("null") ? null :
                new Address(parts[6], parts[7], parts[8], Integer.parseInt(parts[9]));

        return new HotelReservation(parts[0], parts[1], lodgingPhysicalAddress, lodgingMailingAddress,
                LocalDate.parse(parts[10]), Integer.parseInt(parts[11]), Integer.parseInt(parts[12]),
                Integer.parseInt(parts[13]), Integer.parseInt(parts[14]), Integer.parseInt(parts[15]),
                Double.parseDouble(parts[16]), Boolean.parseBoolean(parts[17]));
    } // End fromString method

} // End class HotelReservation
