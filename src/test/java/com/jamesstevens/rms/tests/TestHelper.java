package com.jamesstevens.rms.tests;

import com.jamesstevens.rms.Account;
import com.jamesstevens.rms.Address;
import com.jamesstevens.rms.Manager;
import com.jamesstevens.rms.reservation.Reservation;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Utility class providing shared helper methods for interactive test workflows.
 * <p>
 * Centralizes common operations such as account selection, reservation selection,
 * user input handling, and lightweight validation to avoid duplication across tests.
 * </p>
 */
public final class TestHelper {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Manager manager = TestManager.getManager();

    private TestHelper() {
        // Utility class; no instances
    }

    /**
     * Displays all available accounts and prompts the user to select one.
     *
     * @return selected account number (normalized), or {@code null} if selection is invalid
     */
    public static String selectAccount() {
        List<Account> accounts = manager.getAccounts();
        if (accounts.isEmpty()) {
            System.out.println("\nNo accounts available. Please create an account first.");
            return null;
        }

        System.out.println("\nExisting Accounts:");
        for (Account acc : accounts) {
            System.out.println("- " + acc.getAccountNumber());
        }

        System.out.print("\nEnter account number: ");
        String selectedAccount = scanner.nextLine().trim().toUpperCase(Locale.ROOT);

        boolean exists = accounts.stream()
                .anyMatch(acc -> acc.getAccountNumber().equalsIgnoreCase(selectedAccount));

        if (!exists) {
            System.out.println("Invalid account number selected: " + selectedAccount);
            return null;
        }

        return selectedAccount;
    }

    /**
     * Displays reservations for the given account and prompts the user to select one.
     *
     * @param account account whose reservations should be displayed
     * @return selected reservation number (normalized), or {@code null} if invalid
     */
    public static String selectReservation(Account account) {
        if (account == null) {
            System.out.println("Invalid account. Cannot retrieve reservations.");
            return null;
        }

        List<Reservation> reservations = account.getAllReservations();
        if (reservations.isEmpty()) {
            System.out.println("\nNo reservations found for this account.");
            return null;
        }

        System.out.println("\nReservations under account " + account.getAccountNumber() + ":");
        for (Reservation res : reservations) {
            System.out.println("- " + res.getReservationNumber() +
                    " (Status: " + res.getStatus() + ")");
        }

        System.out.print("\nEnter reservation number: ");
        return normalizeReservationNumber(scanner.nextLine().trim());
    }

    /**
     * Prints a standardized error message for test operations.
     *
     * @param e      exception that occurred
     * @param action description of the action being performed
     */
    public static void handleException(Exception e, String action) {
        System.out.println("Error during " + action + ": " + e.getMessage());
    }

    /**
     * Prompts the user to enter address information.
     *
     * @return newly created {@link Address} instance
     */
    public static Address getUserAddressInput() {
        System.out.print("Enter street address: ");
        String street = scanner.nextLine().trim();

        System.out.print("Enter city: ");
        String city = scanner.nextLine().trim();

        System.out.print("Enter state (e.g., CA, NY): ");
        String state = scanner.nextLine().trim().toUpperCase(Locale.ROOT);

        int zipCode = readZipCode();

        return new Address(street, city, state, zipCode);
    }

    /**
     * Finds the account associated with a given reservation number.
     *
     * @param reservationNumber reservation identifier
     * @return associated {@link Account}, or {@code null} if not found
     */
    public static Account getAccountFromReservation(String reservationNumber) {
        String normalized = normalizeReservationNumber(reservationNumber);
        if (normalized == null) {
            System.out.println("Invalid reservation number.");
            return null;
        }

        for (Account acc : manager.getAccounts()) {
            for (Reservation res : acc.getAllReservations()) {
                if (res.getReservationNumber().equalsIgnoreCase(normalized)) {
                    return acc;
                }
            }
        }

        System.out.println("No account found for reservation: " + normalized);
        return null;
    }

    /**
     * Prompts the user to select and validate an account.
     *
     * @return validated account number, or {@code null} if selection fails
     */
    public static String getValidatedAccount() {
        String accountNumber = selectAccount();
        if (accountNumber == null) {
            System.out.println("No valid account selected. Operation aborted.");
        }
        return accountNumber;
    }

    /**
     * Prompts the user to select and validate a reservation.
     *
     * @return validated reservation number, or {@code null} if selection fails
     */
    public static String getValidatedReservation() {
        String accountNumber = getValidatedAccount();
        if (accountNumber == null) {
            return null;
        }

        Account account = manager.getAccount(accountNumber);
        if (account == null) {
            System.out.println("Invalid account number.");
            return null;
        }

        return selectReservation(account);
    }

    /**
     * Returns the shared scanner instance used for test input.
     *
     * @return scanner instance
     */
    public static Scanner getScanner() {
        return scanner;
    }

    /**
     * Normalizes reservation numbers to the standard format {@code res-<TYPE><digits>}.
     *
     * @param reservationNumber raw reservation number
     * @return normalized reservation number, or {@code null} if invalid
     */
    public static String normalizeReservationNumber(String reservationNumber) {
        if (reservationNumber == null) {
            return null;
        }

        String s = reservationNumber.trim();
        if (s.isEmpty()) {
            return null;
        }

        if (s.length() >= 8 && s.regionMatches(true, 0, "res-", 0, 4)) {
            String type = s.substring(4, 7).toUpperCase(Locale.ROOT);
            return "res-" + type + s.substring(7);
        }

        if (s.length() >= 3) {
            String type = s.substring(0, 3).toUpperCase(Locale.ROOT);
            String rest = s.substring(3);
            if (type.matches("[A-Z]{3}") && rest.matches("\\d+")) {
                return "res-" + type + rest;
            }
        }

        return null;
    }

    /**
     * Reads and validates a ZIP code from standard input.
     *
     * @return valid ZIP code
     */
    private static int readZipCode() {
        while (true) {
            System.out.print("Enter ZIP code: ");
            String raw = scanner.nextLine().trim();
            try {
                return Integer.parseInt(raw);
            } catch (NumberFormatException e) {
                System.out.println("Invalid ZIP code: " + raw + ". Please try again.");
            }
        }
    }
}


