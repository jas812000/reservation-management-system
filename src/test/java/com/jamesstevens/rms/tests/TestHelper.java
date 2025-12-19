// Imports all classes from the TestPackage inside the com.swen_646_project_1 package.
package com.jamesstevens.rms.tests;

/*
 * Imports necessary classes for handling common test utilities.
 * - `Address`: Helps retrieve and store address details.
 * - `Manager`: Provides system-wide access to account and reservation data.
 * - `Account`: Represents user accounts in the reservation system.
 * - `Reservation`: Represents a booking associated with an account.
 * - `List`: Stores multiple accounts or reservations in memory.
 * - `Scanner`: Captures user input for interactive selection in test cases.
 */
import com.jamesstevens.rms.Address;
import com.jamesstevens.rms.Manager;
import com.jamesstevens.rms.Account;
import com.jamesstevens.rms.reservation.Reservation;
import java.util.List;
import java.util.Scanner;


/**
 * Utility class to handle common test functionalities, such as user input processing,
 * selecting accounts and reservations, and handling errors consistently.
 */
public class TestHelper {
    // Instantiate scanner and manager objects
    private static final Scanner scanner = new Scanner(System.in);
    private static final Manager manager = TestManager.getManager();

    /**
     * Displays a list of all accounts and prompts the user to select one.
     * If no accounts exist, a message is displayed, and `null` is returned.
     * @return The selected account number, or `null` if no valid account is selected.
     */
    public static String selectAccount() {
        // Retrieves a list of all existing accounts from the system.
        // This list is used for displaying available accounts to the user
        // and for selecting an account to perform operations like reservation management.
        List<Account> accounts = manager.getAccounts();

        // Checks if the accounts list is empty, meaning there are no registered accounts in the system.
        if (accounts.isEmpty()) {
            System.out.println("\nNo accounts found. Reloading.....");
            manager.reloadAccounts();

            // Check again after reload
            accounts = manager.getAccounts();
            if (accounts.isEmpty()) {
                System.out.println("\nNo accounts available. Please create an account first.");
                return null;
            } // End if statement
        } // End if statement

        System.out.println("\nExisting Accounts:");
        // Loops through the list of all accounts in the system,
        // used to display account numbers
        for (Account acc : accounts) {
            System.out.println("- " + acc.getAccountNumber());
        } // End for loop

        System.out.print("\nEnter account number: ");
        String selectedAccount = scanner.nextLine().trim().toUpperCase();

        // Debugging: Confirm if the selected account exists
        boolean exists = accounts.stream().anyMatch(acc -> acc.getAccountNumber().equals(selectedAccount));
        if (!exists) {
            System.out.println("Invalid account number selected: " + selectedAccount);
            return null; // Prevent invalid account selection
        } // End if statement

        return selectedAccount;
    } // End selectAccount method


    /**
     * Displays a list of reservations under a selected account and prompts the user to choose one.
     * If the account has no reservations, a message is displayed, and `null` is returned.
     * @param account The account to retrieve reservations from.
     * @return The selected reservation number, or `null` if no valid reservation is selected.
     */

    public static String selectReservation(Account account) {
        // Retrieves all reservations associated with a specific account.
        // This allows the user to view and select a reservation for further actions
        // such as updating, canceling, or completing the reservation.

        if (account == null) {
            System.out.println("Invalid account. Cannot retrieve reservations.");
            return null;
        } // End if statement

        List<Reservation> reservations = account.getAllReservations();
        if (reservations.isEmpty()) {
            System.out.println("\nNo reservations found for this account.");
            return null;
        } // End if statement

        System.out.println("\nReservations under account " + account.getAccountNumber() + ":");

        // Loops through the list of reservations associated with an account,
        // used to display each reservation's details.
        for (Reservation res : reservations) {
            System.out.println("- " + res.getReservationNumber() + " (Status: " + res.getStatus() + ")");
        } // End for loop

        System.out.print("\nEnter reservation number: ");
        String userInput = scanner.nextLine().trim();

        // Normalize the reservation number
        return normalizeReservationNumber(userInput);
    } // End selectReservation method


    /**
     * Centralized exception handling to prevent redundant try-catch blocks in multiple test classes.
     * Prints a standardized error message.
     * @param e The exception that was thrown.
     * @param action The action that was attempted when the error occurred.
     */
    public static void handleException(Exception e, String action) {
        System.out.println("Error during " + action + ": " + e.getMessage());
    } // End handleException method

    /**
     * Prompts the user to enter an address and returns an Address object.
     * This method helps avoid redundant address input prompts.
     * @return A new Address object based on user input.
     */
    public static Address getUserAddressInput() {
        System.out.print("Enter street address: ");
        String street = scanner.nextLine().trim();
        System.out.print("Enter city: ");
        String city = scanner.nextLine().trim();
        System.out.print("Enter state (e.g., CA, NY): ");
        String state = scanner.nextLine().trim().toUpperCase();
        System.out.print("Enter ZIP code: ");
        int zipCode = Integer.parseInt(scanner.nextLine().trim());

        return new Address(street, city, state, zipCode);
    } // End getUserAddressInput method

    /**
     * Generates a new unique account number by checking existing accounts.
     * Ensures sequential numbering for new accounts.
     * @param accounts The list of existing accounts in the system.
     * @return A new unique account number.
     */
    public static String generateNewAccountNumber(List<Account> accounts) {
        int accountCounter = 100; // Starting account number
        for (Account acc : accounts) {
            int num = Integer.parseInt(acc.getAccountNumber().replaceAll("[^0-9]", ""));
            if (num >= accountCounter) {
                accountCounter = num + 1;
            } // End if statement
        } // End for loop
        return "ACC" + accountCounter; // Returns formatted unique account number
    } // End generateNewAccountNumber method

    /**
     * Retrieves an account using the reservation number.
     * Ensures the reservation exists before attempting any operation.
     * @param reservationNumber The reservation number used to find the account.
     * @return The associated Account object, or null if not found.
     */
    public static Account getAccountFromReservation(String reservationNumber) {

        if (reservationNumber == null || reservationNumber.trim().isEmpty()) {
            System.out.println("Invalid reservation number.");
            return null;
        } // End if statement

        // Ensure accounts are loaded before searching
        if (manager.getAccounts().isEmpty()) {
            System.out.println("No accounts found in memory. Reloading...");
            manager.reloadAccounts();
        } // End if statement

        String formattedReservationNumber = normalizeReservationNumber(reservationNumber); // Normalize user input
        if (formattedReservationNumber == null) return null; // Invalid format

        for (Account acc : manager.getAccounts()) {  // Loop through all accounts
            for (Reservation res : acc.getAllReservations()) {
                if (res.getReservationNumber().equals(formattedReservationNumber)) {
                    return acc; // Return the matched account
                } // End if statement
            }  // End for loop
        }  // End for loop
        System.out.println("No account found for reservation: " + formattedReservationNumber);
        return null; // Return null if no matching account is found
    } // End getAccountFromReservation method

    /**
     * Prompts the user to select an account and ensures a valid selection.
     * If no valid account is chosen, a message is printed, and `null` is returned.
     * @return The selected account number, or null if no valid account is chosen.
     */
    public static String getValidatedAccount() {
        String accountNumber = selectAccount(); // Calls existing account selection method
        if (accountNumber == null) {
            System.out.println("No valid account selected. Operation aborted.");
            return null;
        } // End if statement
        return accountNumber;   // Returns validated account number
    } // End getValidatedAccount

    /**
     * Prompts the user to select an account and a reservation.
     * If either selection is invalid, it prints a message and returns `null`.
     * @return A valid reservation number, or `null` if no valid selection is made.
     */
    public static String getValidatedReservation() {

        // Get a valid account number first or Exit if no valid account is selected
        String accountNumber = getValidatedAccount();
        if (accountNumber == null) return null;

        // Retrieve the selected account or Exit if account does not exist
        Account account = manager.getAccount(accountNumber);
        if (account == null) {
            System.out.println("Invalid account number.");
            return null;
        } // End if statement

        // Get a reservation number, normalize it before returning
        String reservationNumber = selectReservation(account);
        return reservationNumber == null ? null : normalizeReservationNumber(reservationNumber);
    } // End getValidatedReservation method

    /**
     * Provides access to the scanner instance.
     * @return The shared Scanner instance.
     */
    public static Scanner getScanner() {
        return scanner;
    } // End getScanner method

    /**
     * Ensures the reservation number follows the correct format:
     * - The "res-" prefix is always lowercase.
     * - The reservation type prefix (CAB, HOT, HOU) remains uppercase.
     * - The numerical part remains unchanged.
     *
     * @param reservationNumber The original user input reservation number.
     * @return Formatted reservation number in the correct format.
     */
    public static String normalizeReservationNumber(String reservationNumber) {
        if (reservationNumber == null || reservationNumber.isEmpty()) return reservationNumber;

        // Ensure "res-" is lowercase and the reservation type is uppercase
        return "res-" + reservationNumber.substring(4, 7).toUpperCase() + reservationNumber.substring(7);
    } // End normalizeReservationNumber method

} // End TestHelper class
