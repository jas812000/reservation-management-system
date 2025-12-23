package com.jamesstevens.rms.tests;

import com.jamesstevens.rms.Account;
import com.jamesstevens.rms.Address;
import com.jamesstevens.rms.Manager;
import com.jamesstevens.rms.exceptions.DuplicateObject_Exception;
import com.jamesstevens.rms.reservation.CabinReservation;
import com.jamesstevens.rms.reservation.HotelReservation;
import com.jamesstevens.rms.reservation.HouseReservation;
import com.jamesstevens.rms.reservation.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Interactive/manual test utility for adding a new reservation to an existing account.
 * <p>
 * This class is intentionally <b>not</b> a JUnit test. It is used for manual runs from a main method
 * or test harness to exercise the "add reservation" workflow.
 * </p>
 * <p>
 * Address rules enforced here:
 * </p>
 * <ul>
 *     <li><b>Cabin</b>: physical and mailing addresses may differ.</li>
 *     <li><b>Hotel/House</b>: mailing address is forced to match physical address.</li>
 * </ul>
 */
@SuppressWarnings("unused")
public class AddReservationTest {

    /**
     * Prompts for account and reservation details and adds a new reservation to the selected account.
     * <p>
     * This method assumes:
     * </p>
     * <ul>
     *     <li>At least one account exists in storage.</li>
     *     <li>User input is provided via the console.</li>
     * </ul>
     * <p>
     * If the reservation is successfully added, it will be persisted through the normal save path
     * (via {@link Account#addReservation(Reservation)}).
     * </p>
     */
    public static void testAddReservation() {
        Scanner scanner = new Scanner(System.in);
        Manager manager = TestManager.getManager();

        List<Account> accounts = manager.getAccounts();
        if (accounts.isEmpty()) {
            System.out.println("No existing accounts found.");
            return;
        }

        System.out.println("\nExisting Accounts:");
        for (Account acc : accounts) {
            System.out.println("- " + acc.getAccountNumber());
        }

        System.out.print("\nEnter account number for reservation: ");
        String accountNumber = scanner.nextLine().trim().toUpperCase();

        Account account = manager.getAccount(accountNumber);
        if (account == null) {
            System.out.println("Error: Account not found. Please check the account number.");
            return;
        }

        System.out.println("Select reservation type: ");
        System.out.println("1 - Cabin");
        System.out.println("2 - Hotel");
        System.out.println("3 - House");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        String reservationType = switch (choice) {
            case 1 -> "Cabin";
            case 2 -> "Hotel";
            case 3 -> "House";
            default -> throw new IllegalArgumentException("Unknown reservation type: " + choice);
        };

        String reservationNumber = manager.getNewReservationNumber(reservationType);

        Address physicalAddress;
        Address mailingAddress;

        LocalDate startDate = LocalDate.of(2025, 6, 15);
        int numNights = 8;
        double pricePerNight = 150.0;

        if (choice == 1) {
            System.out.println("\nEnter Physical Address");
            physicalAddress = TestHelper.getUserAddressInput();

            System.out.println("\nEnter Mailing Address");
            mailingAddress = TestHelper.getUserAddressInput();
        } else {
            System.out.println("\nEnter Address:");
            physicalAddress = TestHelper.getUserAddressInput();

            /*
             * IMPORTANT: Hotel/House mailing address must never diverge from physical.
             * Force mailing to match physical.
             */
            mailingAddress = physicalAddress;
        }

        Reservation newReservation = switch (choice) {
            case 1 -> new CabinReservation(
                    reservationNumber, accountNumber, physicalAddress, mailingAddress,
                    startDate, numNights, 2, 1, 1,
                    500, pricePerNight, true, false
            );
            case 2 -> new HotelReservation(
                    reservationNumber, accountNumber, physicalAddress, physicalAddress,
                    startDate, numNights, 2, 1, 1,
                    500, pricePerNight, true
            );
            case 3 -> new HouseReservation(
                    reservationNumber, accountNumber, physicalAddress, physicalAddress,
                    startDate, numNights, 2, 1, 1,
                    500, pricePerNight, 2
            );
            default -> throw new IllegalStateException("Unexpected value: " + choice);
        };

        try {
            account.addReservation(newReservation);
            System.out.println("Reservation " + reservationNumber + " added successfully.");
        } catch (DuplicateObject_Exception e) {
            System.out.println("Reservation already exists.");
        }
    }
}
