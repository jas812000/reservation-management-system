// Imports all classes from the TestPackage inside the com.swen_646_project_1 package.
package com.jamesstevens.rms.tests;

/*
 * Imports required classes for managing accounts and handling exceptions during modifications.
 * - `Account`: Represents user accounts in the reservation system.
 * - `Address`: Handles account-related address details.
 * - `Manager`: Provides system-wide access to account management functions.
 * - `IllegalSave_Exception`: Exception thrown when an error occurs while attempting
 *   to save modifications to an account, ensuring data integrity.
 * - `List`: Utilized for handling multiple account records.
 */
import com.jamesstevens.rms.Account;
import com.jamesstevens.rms.Address;
import com.jamesstevens.rms.Manager;
import com.jamesstevens.rms.exceptions.IllegalSave_Exception;
import java.util.List;

/**
 * Handles updating an existing account.
 * This class provides a method to update an account by:
 * - Allowing the user to select an account.
 * - Attempting to update the account details.
 * - Handling any exceptions if the update fails.
 */
public class ModifyAccountTest {
    /**
     * Tests updating an existing account by selecting it and applying modifications.
     * - If the account is found, the system attempts to update it.
     * - If an error occurs while saving, the exception is handled.
     */
    public static void testUpdateExistingAccount() {
        // Retrieve shared Manager instance
        Manager manager = TestManager.getManager();

        // Retrieves a list of all existing accounts from the manager.
        List<Account> accounts = manager.getAccounts();

        // Checks if the accounts list is empty, meaning there are no registered accounts in the system.
        // If no accounts exist, the user is prompted to create an account before performing further actions,
        // and the method terminates early.
        if (accounts.isEmpty()) {
            System.out.println("No accounts found. Please create an account first.");
            return;
        } // End if statement

        // Loop to display all accounts for selection
        System.out.println("\nExisting Accounts:");
        for (Account acc : accounts) {
            System.out.println("- " + acc.getAccountNumber());
        } // End for loop

        // Prompt user to select an account
        String accountNumber = TestHelper.selectAccount();

        // Handle case where no account is selected
        if (accountNumber == null) {
            System.out.println("No valid account selected. Update operation aborted.");
            return; // Exit if no account is selected
        } // End if statement

        // Retrieve the account object
        Account account = manager.getAccount(accountNumber);

        // Checks if the account retrieved from the manager is null.
        // If no account is found with the given account number, an error message is displayed,
        // and the method terminates early to prevent further execution.
        if (account == null) {
            System.out.println("Error: Account not found.");
            return;
        } // End if statement

        // Prompt user for new details
        System.out.println("\nUpdating account: " + accountNumber);
        System.out.println("Current Address: " + account.getAddress());
        Address newAddress = TestHelper.getUserAddressInput(); // Get new address input
        System.out.println("Current Phone Number: " + account.getPhoneNumber());
        System.out.print("Enter new phone number (leave blank to keep current): ");
        String newPhone = TestHelper.getScanner().nextLine().trim();
        if (newPhone.isEmpty()) {
            newPhone = account.getPhoneNumber(); // Keep current phone number
        } // End if statement

        System.out.println("Current Email: " + account.getEmail());
        System.out.print("Enter new email (leave blank to keep current): ");
        String newEmail = TestHelper.getScanner().nextLine().trim();
        if (newEmail.isEmpty()) {
            newEmail = account.getEmail(); // Keep current email
        } // End if statement

        // Apply updates
        account.updateAddress(newAddress);
        account.setPhoneNumber(newPhone);
        account.setEmail(newEmail);

        try {
            // Attempt to update the account
            manager.updateAccount(accountNumber);
            System.out.println("Account " + accountNumber + " updated successfully.");
        } catch (IllegalSave_Exception e) {
            // Print error message before handling exception
            System.out.println("Error: Unable to update account " + accountNumber);
            // Handle errors encountered during account update
            TestHelper.handleException(e, "updating account");
        } // End try-catch statements
    } // End testUpdateExistingAccount method
} // End ModifyAccountTest class
