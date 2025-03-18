// Imports all classes from the TestPackage inside the com.swen_646_project_1 package.
package com.swen_646_project_1.TestPackage;

/*
 * Imports necessary classes for managing reservations in the Reservation Management System.
 * - `Account`: Represents a user account in the reservation system.
 * - `Manager`: Handles system-wide account and reservation management.
 * - `DuplicateObject_Exception`: Custom exception for handling duplicate reservations.
 * - `CabinReservation`, `HotelReservation`, `HouseReservation`, `Reservation`:
 *   Different types of lodging reservations supported in the system.
 * - `Address`: Represents the physical address associated with reservations.
 * - `LocalDate`: Used to manage reservation start dates and durations.
 * - `Scanner`: Facilitates user input for interactive command-line operations.
 */
import com.swen_646_project_1.Account;
import com.swen_646_project_1.Manager;
import com.swen_646_project_1.exceptions.DuplicateObject_Exception;
import com.swen_646_project_1.reservation.CabinReservation;
import com.swen_646_project_1.reservation.HotelReservation;
import com.swen_646_project_1.reservation.HouseReservation;
import com.swen_646_project_1.reservation.Reservation;
import com.swen_646_project_1.Address;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * Handles adding reservations for different lodging types.
 */
public class AddReservationTest {
    /**
     * Prompts the user to enter reservation details and creates a new reservation.
     */
    public static void testAddReservation() {
        // Instantiate scanner and manager objects
        Scanner scanner = new Scanner(System.in);
        Manager manager = TestManager.getManager(); // Ensure using shared instance


        System.out.print("\nEnter account number for reservation: ");
        String accountNumber = scanner.nextLine().trim();

        // Retrieve the account from the manager
        Account account = manager.getAccount(accountNumber);
        if (account == null) {
            System.out.println("Error: Account not found. Please check the account number.");
            return;
        } // End if statement

        System.out.println("Select reservation type: ");
        System.out.println("1 - Cabin");
        System.out.println("2 - Hotel");
        System.out.println("3 - House");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String reservationType = switch (choice) {
            case 1 -> "Cabin";
            case 2 -> "Hotel";
            case 3 -> "House";
            default -> throw new IllegalArgumentException("Unknown reservation type: " + choice);
        }; // End switch statement

        // Generate unique reservation number
        String reservationNumber = manager.getNewReservationNumber(reservationType);

        // Declare variables to store reservation details
        // Stores the physical address of the lodging for the reservation
        Address physicalAddress;

        // Stores the mailing address of the lodging
        // This may be different from the physical address for certain reservation types
        Address mailingAddress;

        // Specifies the start date of the reservation
        // Here, it's set to June 15, 2025
        LocalDate startDate = LocalDate.of(2025, 6, 15);

        // Defines the total number of nights the reservation will last
        int numNights = 8;

        // Stores the price per night for the lodging
        // The price is set to $150.00 per night
        double pricePerNight = 150.0;

        // Cabin Reservation requires both physical and mailing addresses
        if (choice == 1) {
            System.out.println("\nEnter Physical Address");
            physicalAddress = TestHelper.getUserAddressInput();

            System.out.println("\nEnter Mailing Address");
            mailingAddress = TestHelper.getUserAddressInput();
        } else {
            System.out.println("\nEnter Address:");
            physicalAddress = TestHelper.getUserAddressInput();
            mailingAddress = physicalAddress; // Mailing address is the same for Hotel and House
        } // End if-else statements

        // Create appropriate reservation type based on user choice
        Reservation newReservation;
        switch (choice) {
            case 1 -> newReservation = new CabinReservation(
                    reservationNumber, accountNumber, physicalAddress, mailingAddress,
                    startDate, numNights, 2, 1, 1, 500, pricePerNight, true, false);
            case 2 -> newReservation = new HotelReservation(
                    reservationNumber, accountNumber, physicalAddress, mailingAddress,
                    startDate, numNights, 2, 1, 1, 500, pricePerNight, true);
            case 3 -> newReservation = new HouseReservation(
                    reservationNumber, accountNumber, physicalAddress, mailingAddress,
                    startDate, numNights, 2, 1, 1, 500, pricePerNight, 2);
            default -> throw new IllegalStateException("Unexpected value: " + choice);
        } // End switch statements

        // Try adding the reservation to the account and handle exceptions
        try {
            manager.getAccount(accountNumber).addReservation(newReservation);
            System.out.println("Reservation " + reservationNumber + " added successfully.");
        } catch (DuplicateObject_Exception e) {
            System.out.println("Reservation already exists.");
        } // End try-catch statements
    } // End testAddReservation method
} // End AddReservationTest class
