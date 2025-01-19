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
     * @param reservationNumber Unique identifier for the reservation
     * @param lodgingPhysicalAddress Physical address of the hotel
     * @param startDate The start date of the reservation
     * @param numNights Number of nights for the reservation
     * @param hasKitchenette Indicates if the hotel room includes a kitchenette
     */
    public HotelReservation(String reservationNumber, String lodgingPhysicalAddress, LocalDate startDate,
                            int numNights, boolean hasKitchenette) {}

    /**
     * Calculates the price per night for the hotel reservation.
     * Implementation will likely depend on specific pricing logic.
     * @return The price per night as a double
     */
    @Override
    public double calculatePricePerNight() {}

    /**
     * Returns a string representation of the hotel reservation details.
     * @return A formatted string containing reservation details
     */
    @Override
    public String toString() {}

    /**
     * Creates a HotelReservation object from a formatted string.
     * @param data A string containing hotel reservation details in a predefined format
     * @return A HotelReservation object created from the provided data
     */
    public static HotelReservation fromString(String data) {}

} // end class HotelReservation
