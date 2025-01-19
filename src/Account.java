import java.util.List;      // import to handle a list of reservation numbers

/**
 * Represents a user account in the system.
 * Each account has a unique ID, contact details, and a list of associated reservation numbers.
 */
public class Account {

    // Attributes
    private final String accountNumber; // Unique identifier for account that cannot be changed
    private String mailingAddress;      // Stores the mailing address
    private String phoneNumber;         // Stores the phone number
    private String email;               // Stores the email address
    private List<String> reservationNumbers;    // List of reservation numbers associated with this account

    /**
     * Constructor to initialize an Account object with required details.
     * @param accountNumber Unique identifier for the account
     * @param mailingAddress Mailing address of the user
     * @param phoneNumber Contact phone number of the user
     * @param email Email address of the user
     */
    public Account(String accountNumber, String mailingAddress, String phoneNumber, String email) {}

    /**
     * Retrieves the unique account number.
     * @return Account number as a String
     */
    public String getAccountNumber() {}

    /**
     * Updates the mailing address of the account holder.
     * @param newMailingAddress The new mailing address to be set
     */
    public void updateMailingAddress(String newMailingAddress) {}

    /**
     * Updates the phone number associated with the account.
     * @param newPhoneNumber The new phone number to be set
     */
    public void updatePhoneNumber(String newPhoneNumber) {}

    /**
     * Updates the email address associated with the account.
     * @param newEmail The new email address to be set
     */
    public void updateEmail(String newEmail) {}

    /**
     * Adds a new reservation number to the list of associated reservations.
     * @param reservationNumber The reservation number to be added
     */
    public void addReservation(String reservationNumber) {}

    /**
     * Returns a string representation of the account details.
     * @return A formatted string containing account details
     */
    @Override
    public String toString() {}

    /**
     * Creates an Account object from a formatted string.
     * @param data A string containing account details in a predefined format
     * @return An Account object constructed from the provided data
     */
    public static Account fromString(String data) {}

} // end class Account
