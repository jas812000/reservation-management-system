package com.jamesstevens.rms.tests;

import com.jamesstevens.rms.Account;
import com.jamesstevens.rms.Address;
import com.jamesstevens.rms.Manager;
import com.jamesstevens.rms.exceptions.IllegalSave_Exception;

import java.util.List;

/**
 * Interactive/manual test utility for modifying an existing account.
 * <p>
 * Prompts the user to select an account and enter updated contact information,
 * then persists changes through {@link Manager#updateAccount(String)}.
 * </p>
 */
@SuppressWarnings("unused")
public class ModifyAccountTest {

    /**
     * Prompts the user to update an existing account's address, phone, and email.
     * <p>
     * If the user leaves phone/email blank, the current value is kept.
     * </p>
     */
    public static void testUpdateExistingAccount() {
        Manager manager = TestManager.getManager();

        List<Account> accounts = manager.getAccounts();
        if (accounts.isEmpty()) {
            System.out.println("No accounts found. Please create an account first.");
            return;
        }

        System.out.println("\nExisting Accounts:");
        for (Account acc : accounts) {
            System.out.println("- " + acc.getAccountNumber());
        }

        String accountNumber = TestHelper.selectAccount();
        if (accountNumber == null) {
            System.out.println("No valid account selected. Update operation aborted.");
            return;
        }

        Account account = manager.getAccount(accountNumber);
        if (account == null) {
            System.out.println("Error: Account not found.");
            return;
        }

        System.out.println("\nUpdating account: " + accountNumber);

        System.out.println("Current Address: " + account.getAddress());
        Address newAddress = TestHelper.getUserAddressInput();

        System.out.println("Current Phone Number: " + account.getPhoneNumber());
        System.out.print("Enter new phone number (leave blank to keep current): ");
        String newPhone = TestHelper.getScanner().nextLine().trim();
        if (newPhone.isEmpty()) {
            newPhone = account.getPhoneNumber();
        }

        System.out.println("Current Email: " + account.getEmail());
        System.out.print("Enter new email (leave blank to keep current): ");
        String newEmail = TestHelper.getScanner().nextLine().trim();
        if (newEmail.isEmpty()) {
            newEmail = account.getEmail();
        }

        account.updateAddress(newAddress);
        account.setPhoneNumber(newPhone);
        account.setEmail(newEmail);

        try {
            manager.updateAccount(accountNumber);
            System.out.println("Account " + accountNumber + " updated successfully.");
        } catch (IllegalSave_Exception e) {
            System.out.println("Error: Unable to update account " + accountNumber);
            TestHelper.handleException(e, "updating account");
        }
    }
}
