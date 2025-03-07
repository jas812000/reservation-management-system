package com.swen_646_project_1.TestPackage;

import com.swen_646_project_1.Manager;
import com.swen_646_project_1.Account;
import com.swen_646_project_1.reservation.Reservation;

import java.util.List;
import java.util.Scanner;

/**
 * Utility class to handle common test functionalities.
 */
public class TestHelper {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Manager manager = TestManager.getManager();

    /**
     * Lists all accounts and lets the user select one.
     * @return The selected account number, or null if no valid account is selected.
     */
    public static String selectAccount() {
        List<Account> accounts = manager.getAccounts();
        if (accounts.isEmpty()) {
            System.out.println("\nNo accounts found. Please create an account first.");
            return null;
        }

        System.out.println("\nExisting Accounts:");
        for (Account acc : accounts) {
            System.out.println("- " + acc.getAccountNumber());
        }

        System.out.print("\nEnter account number: ");
        return scanner.nextLine().trim();
    }

    /**
     * Lists all reservations under an account and lets the user select one.
     * @param account The account to get reservations from.
     * @return The selected reservation number, or null if no valid reservation is selected.
     */
    public static String selectReservation(Account account) {
        List<Reservation> reservations = account.getAllReservations();
        if (reservations.isEmpty()) {
            System.out.println("\nNo reservations found for this account.");
            return null;
        }

        System.out.println("\nReservations under account " + account.getAccountNumber() + ":");
        for (Reservation res : reservations) {
            System.out.println("- " + res.getReservationNumber() + " (Status: " + res.getStatus() + ")");
        }

        System.out.print("\nEnter reservation number: ");
        return scanner.nextLine().trim();
    }

    /**
     * Gets the shared Manager instance.
     * @return The shared Manager instance.
     */
    public static Manager getManager() {
        return manager;
    }
}
