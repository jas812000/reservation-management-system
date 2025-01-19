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
     * @param reservationNumber Unique identifier for the reservation.
     * @param lodgingPhysicalAddress Physical address of the lodging.
     * @param startDate Start date of the reservation.
     * @param numNights Number of nights for the stay.
     * @param lodgingSizeSqFt Size of the lodging in square feet.
     */
    public Reservation(String reservationNumber, String lodgingPhysicalAddress, LocalDate startDate,
                       int numNights, int lodgingSizeSqFt) {}

    /**
     * Retrieves the unique reservation number.
     * @return The reservation number as a String.
     */
    public String getReservationNumber() {}

    /**
     * Marks the reservation as completed.
     * Throws IllegalStateException if the reservation is already completed or cancelled.
     */
    public void completeReservation() {}

    /**
     * Cancels the reservation.
     * Throws IllegalStateException if the reservation is already completed or cancelled.
     */
    public void cancelReservation() {}

    /**
     * Abstract method to calculate the price per night for the reservation.
     * Must be implemented by subclasses.
     * @return The price per night as a double.
     */
    public double calculatePricePerNight() {}

    /**
     * Returns a string representation of the reservation details.
     * @return A formatted string containing reservation details.
     */
    @Override
    public String toString() {}

    /**
     * Creates a Reservation object from a formatted string.
     * This method may return different reservation subtypes based on the data.
     * @param data A string containing reservation details in a predefined format.
     * @return A Reservation object constructed from the provided data.
     */
    public static Reservation fromString(String data) {}

} // end abstract class Reservation
