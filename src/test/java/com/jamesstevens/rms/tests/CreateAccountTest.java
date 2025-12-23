package com.jamesstevens.rms.tests;

import com.jamesstevens.rms.Account;
import com.jamesstevens.rms.Address;
import com.jamesstevens.rms.Manager;
import com.jamesstevens.rms.exceptions.DuplicateObject_Exception;

import java.util.Scanner;

/**
 * Interactive/manual test utility for creating a new account.
 * <p>
 * Prompts the user for address, phone number, and email, then attempts to persist a new account.
 * </p>
 */
@SuppressWarnings("unused")
public class CreateAccountTest {

    /**
     * Prompts the user for account fields and creates a new account through the shared manager.
     */
    public static void testCreateNewAccount() {
        Scanner scanner = new Scanner(System.in);
        Manager manager = TestManager.getManager();

        String accountNumber = manager.getNewAccountNumber();

        System.out.println("\nCreating new account: " + accountNumber);

        Address address = TestHelper.getUserAddressInput();

        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine().trim();

        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();

        try {
            Account newAccount = new Account(accountNumber, address, phoneNumber, email);
            manager.addAccount(newAccount);
            System.out.println("Account " + accountNumber + " successfully created.");
        } catch (DuplicateObject_Exception e) {
            System.out.println("Account " + accountNumber + " already exists.");
        }
    }
}
