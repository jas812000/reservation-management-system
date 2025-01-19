import java.time.LocalDate;     // Import for handing reservation start dates

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
     * @param reservationNumber Unique identifier for the reservation
     * @param lodgingPhysicalAddress Physical address of the house
     * @param startDate The start date of the reservation
     * @param numNights Number of nights for the reservation
     * @param numFloors Number of floors in the reserved house
     */
    public HouseReservation(String reservationNumber, String lodgingPhysicalAddress, LocalDate startDate,
                            int numNights, int numFloors){}

    /**
     * Calculates the price per night for the house reservation.
     * Implementation will likely depend on specific pricing logic.
     * @return The price per night as a double
     */
    @Override
    public double calculatePricePerNight(){}

    /**
     * Returns a string representation of the house reservation details.
     * @return A formatted string containing reservation details
     */
    @Override
    public String toString(){}

    /**
     * Creates a HouseReservation object from a formatted string.
     * @param data A string containing house reservation details in a predefined format
     * @return A HouseReservation object created from the provided data
     */
    public static HouseReservation fromString(String data){}

} // end class HouseReservation
