// Imports all classes from the TestPackage inside the com.swen_646_project_1 package.
package com.jamesstevens.rms.tests;

/*
 * Imports necessary classes for managing accounts, reservations, and user interactions.
 * - `Manager`: Provides system-wide access to accounts and reservations.
 * - `NullAccount_Exception`: Thrown when an account cannot be found in the system.
 * - `NullReservation_Exception`: Thrown when a reservation is not associated with an account.
 * - `Scanner`: Captures user input for interactive menu selection in test cases.
 */
import com.jamesstevens.rms.Account;
import com.jamesstevens.rms.Manager;
import com.jamesstevens.rms.exceptions.NullAccount_Exception;
import com.jamesstevens.rms.exceptions.NullReservation_Exception;

import java.util.List;
import java.util.Scanner;

/**
 * Interactive test for findAccount and findReservation using menu system.
 */
public class InteractiveFindTest {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Manager manager = new Manager();

    /**
     * Interactive test for finding an account.
     */
    public static void findAccountInteractive() {

        // Displays the accounts
        displayAllAccounts(manager);

        System.out.print("\nEnter Account Number to search: ");
        String accountNumber = scanner.nextLine().trim();

        try {
            manager.findAccount(accountNumber);
        } catch (NullAccount_Exception e) {
            System.out.println("Error: " + e.getMessage());
        } // End try-catch statements
    } // End findAccountInteractive method

    /**
     * Interactive test for finding a reservation.
     */
    public static void findReservationInteractive() {

        // Displays the accounts
        displayAllAccounts(manager);

        System.out.print("\nEnter Account Number for the reservation: ");
        String accountNumber = scanner.nextLine().trim();

        // Retrieve the selected account from the manager
        Account account = manager.getAccount(accountNumber);

        // Display reservations associated with the selected account
        displayReservations(account);

        System.out.print("Enter Reservation Number: ");
        String reservationNumber = scanner.nextLine().trim();

        try {
            manager.findReservation(accountNumber, reservationNumber);
        } catch (NullReservation_Exception e) {
            System.out.println("Error: " + e.getMessage());
        } // End try-catch statements
    } // End findReservationInteractive method


    /**
     * Displays all existing accounts from the Manager.
     * If no accounts exist, a message is displayed, and the method exits early.
     *
     * @param manager The Manager instance used to retrieve accounts.
     */
    public static void displayAllAccounts(Manager manager) {
        // Retrieve all accounts from the Manager
        List<Account> accounts = manager.getAccounts();

        // Check if there are any accounts to display
        if (accounts.isEmpty()) {
            System.out.println("No existing accounts found.");
            return;
        } // End if statement

        // Loop to display all accounts for selection
        System.out.println("\nExisting Accounts:");
        for (Account acc : accounts) {
            System.out.println("- " + acc.getAccountNumber());
        } // End for loop
    } // End displayAllAccounts method


    /**
     * Displays all reservations associated with a given account.
     * If no reservations exist, a message is displayed.
     *
     * @param account The account whose reservations will be displayed.
     */
    public static void displayReservations(Account account) {
        List<String> reservations = account.getReservationNumbers(); // Get reservation numbers

        if (reservations.isEmpty()) {
            System.out.println("No reservations found for this account.");
            return;
        } // End if statement

        // Display available reservations
        System.out.println("\nReservations for Account " + account.getAccountNumber() + ":");
        for (String reservation : reservations) {
            System.out.println("- " + reservation);
        } // End for loop
    } // End displayReservations method

} // End InteractiveFindTest class
