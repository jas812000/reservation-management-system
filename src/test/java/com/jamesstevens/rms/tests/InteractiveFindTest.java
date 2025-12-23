package com.jamesstevens.rms.tests;

import com.jamesstevens.rms.Account;
import com.jamesstevens.rms.Manager;
import com.jamesstevens.rms.exceptions.NullAccount_Exception;
import com.jamesstevens.rms.exceptions.NullReservation_Exception;

import java.util.List;
import java.util.Scanner;

/**
 * Interactive/manual test utility for validating the find-account and find-reservation flows.
 */
@SuppressWarnings("unused")
public class InteractiveFindTest {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Manager manager = new Manager();

    /**
     * Interactive test for finding an account by account number.
     * <p>
     * Displays all accounts, prompts for an account number, and calls {@link Manager#findAccount(String)}.
     * </p>
     */
    public static void findAccountInteractive() {
        displayAllAccounts(manager);

        System.out.print("\nEnter Account Number to search: ");
        String accountNumber = scanner.nextLine().trim();

        try {
            manager.findAccount(accountNumber);
        } catch (NullAccount_Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Interactive test for finding a reservation by account number and reservation number.
     * <p>
     * Displays all accounts, prompts for an account number, displays that account's reservations,
     * and calls {@link Manager#findReservation(String, String)}.
     * </p>
     */
    public static void findReservationInteractive() {
        displayAllAccounts(manager);

        System.out.print("\nEnter Account Number for the reservation: ");
        String accountNumber = scanner.nextLine().trim();

        Account account = manager.getAccount(accountNumber);
        if (account == null) {
            System.out.println("Error: Account not found.");
            return;
        }

        displayReservations(account);

        System.out.print("Enter Reservation Number: ");
        String reservationNumber = scanner.nextLine().trim();

        try {
            manager.findReservation(accountNumber, reservationNumber);
        } catch (NullReservation_Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Displays all accounts currently available from the provided manager.
     *
     * @param manager manager instance used to retrieve accounts
     */
    public static void displayAllAccounts(Manager manager) {
        List<Account> accounts = manager.getAccounts();

        if (accounts.isEmpty()) {
            System.out.println("No existing accounts found.");
            return;
        }

        System.out.println("\nExisting Accounts:");
        for (Account acc : accounts) {
            System.out.println("- " + acc.getAccountNumber());
        }
    }

    /**
     * Displays all reservation numbers associated with an account.
     *
     * @param account account whose reservations will be displayed
     */
    public static void displayReservations(Account account) {
        List<String> reservations = account.getReservationNumbers();

        if (reservations.isEmpty()) {
            System.out.println("No reservations found for this account.");
            return;
        }

        System.out.println("\nReservations for Account " + account.getAccountNumber() + ":");
        for (String reservation : reservations) {
            System.out.println("- " + reservation);
        }
    }
}
