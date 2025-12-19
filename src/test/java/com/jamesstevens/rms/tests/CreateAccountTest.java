// Imports all classes from the TestPackage inside the com.swen_646_project_1 package.
package com.jamesstevens.rms.tests;

/*
 * Imports required classes for account creation testing.
 * - `Account`: Represents a user account in the reservation system.
 * - `Address`: Stores the physical address associated with an account.
 * - `Manager`: Manages accounts and reservations in the system.
 * - `DuplicateObject_Exception`: Exception thrown when attempting to create an account that already exists.
 * - `Scanner`: Used to capture user input for interactive account creation.
 */
import com.jamesstevens.rms.Account;
import com.jamesstevens.rms.Address;
import com.jamesstevens.rms.Manager;
import com.jamesstevens.rms.exceptions.DuplicateObject_Exception;
import java.util.Scanner;

/**
 * Handles account creation testing.
 */
public class CreateAccountTest {
    /**
     * Prompts the user to enter account details and creates a new account.
     */
    public static void testCreateNewAccount() {
        // Instantiate scanner and manager objects
        Scanner scanner = new Scanner(System.in);
        Manager manager = TestManager.getManager(); // Ensure using shared instance

        // Generate a new unique account number
        //String accountNumber = TestHelper.generateNewAccountNumber(manager.getAccounts());
        String accountNumber = manager.getNewAccountNumber();

        System.out.println("\nCreating new account: " + accountNumber);

        // Prompt user for address details
        Address address = TestHelper.getUserAddressInput();

        // Prompt user for phone number and email
        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine().trim();
        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();

        try {
            // Creates a new Account object with the provided account number, address, phone number, and email.
            Account newAccount = new Account(accountNumber, address, phoneNumber, email);

            // Adds the new account to the system using the manager.
            manager.addAccount(newAccount);

            // Ensure correct file format when saving
            String accountFile = "acc-" + accountNumber + ".txt";

            // Displays a confirmation message if the account is successfully created.
            System.out.println("Account " + accountNumber + " successfully created.");
        } catch (DuplicateObject_Exception e) {
            // Handles the case where an account with the same number already exists.
            System.out.println("Account " + accountNumber + " already exists.");
        } // End try-catch statements

    } // End testCreateNewAccount method

} // End CreateAccountTest class
