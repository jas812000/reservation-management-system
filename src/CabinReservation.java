import java.time.LocalDate;     // Import for handing reservation start dates

/**
 * Represents a cabin reservation.
 * Inherits from the Reservation class.
 * Includes additional attributes for kitchen and loft availability.
 */
public class CabinReservation extends Reservation {

    // Attributes
    private boolean hasFullKitchen;     // Indicates whether the cabin includes a full kitchen
    private boolean hasLoft;            // Indicates whether the cabin has a loft area

    /**
     * Constructor to initialize a CabinReservation object.
     * @param reservationNumber Unique identifier for the reservation
     * @param lodgingPhysicalAddress Physical address of the reserved cabin
     * @param startDate The start date of the reservation
     * @param numNights Number of nights for the reservation
     * @param hasFullKitchen Indicates if the cabin includes a full kitchen
     * @param hasLoft Indicates if the cabin includes a loft
     */
    public CabinReservation(String reservationNumber, String lodgingPhysicalAddress, LocalDate startDate,
                            int numNights, boolean hasFullKitchen, boolean hasLoft){}

    /**
     * Calculates the price per night for the cabin.
     * Implementation will likely depend on specific pricing logic.
     * @return The price per night as a double
     */
    @Override
    public double calculatePricePerNight(){}

    /**
     * Returns a string representation of the cabin reservation details.
     * @return A formatted string containing reservation details
     */
    @Override
    public String toString(){}

    /**
     * Creates a CabinReservation object from a formatted string.
     * @param data A string containing cabin reservation details in a predefined format
     * @return A CabinReservation object created from the provided data
     */
    public static CabinReservation fromString(String data){}

} // end class CabinReservation
