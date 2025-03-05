package com.swen_646_project_1;

import com.swen_646_project_1.exceptions.*;
import com.swen_646_project_1.reservation.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Test class for the reservation system.
 * Contains separate test methods to verify different functionalities.
 * Tests can be run independently by commenting out unwanted test calls in main().
 */
public class ReservationSystemTest {

    public static void main(String[] args) {
        System.out.println("=== Running Reservation System Tests ===\n");

        testCreateAccount();
        testAddReservation();
        testRetrieveReservation();
        testCancelReservation();
        testCompleteReservation();
        testUpdateReservation();
        testCalculatePrices();
        testExceptionHandling();
        testFilePersistence();

        // Feature-specific tests
        testCabinReservationFeatures();
        testHotelReservationFeatures();
        testHouseReservationFeatures();
    }

    /**
     * Tests creating an account and verifies its existence.
     */
    public static void testCreateAccount() {
        System.out.println("\n>> Test: Create Account");

        Manager manager = new Manager();
        String accountNumber = "ACC123";

        System.out.println("Before Account Creation (" + accountNumber + "): " +
                (manager.getAccount(accountNumber) == null ? "Does not exist" : "Exists"));

        try {
            // Create a valid Address object
            Address address = new Address("123 Main St", "New York", "NY", 10001);

            // Create a new account with valid data
            Account account = new Account(accountNumber, address, "1234567890", "johndoe@email.com");

            // Add account to the manager
            manager.addAccount(account);
            System.out.println("Account saved: " + accountNumber);
        } catch (Exception e) {
            System.out.println("Exception in Account Creation: " + e.getMessage());
        }

        System.out.println("After Account Creation (" + accountNumber + "): " +
                (manager.getAccount(accountNumber) != null ? "Exists" : "Does not exist"));
    }

    /**
     * Tests adding a reservation to an account.
     */
    public static void testAddReservation() {
        System.out.println("\n>> Test: Add Reservation");

        Manager manager = new Manager();
        String accountNumber = "ACC123";
        String cabinResNumber = "RES001";
        String hotelResNumber = "RES002";
        String houseResNumber = "RES003";

        // Before Adding Reservation - Check if they exist
        boolean cabinExists = manager.calculatePricePerNight(accountNumber, cabinResNumber) > 0;
        boolean hotelExists = manager.calculatePricePerNight(accountNumber, hotelResNumber) > 0;
        boolean houseExists = manager.calculatePricePerNight(accountNumber, houseResNumber) > 0;

        System.out.println("Before Adding Cabin Reservation (" + cabinResNumber + "): " + (cabinExists ? "Exists" : "Does not exist"));
        System.out.println("Before Adding Hotel Reservation (" + hotelResNumber + "): " + (hotelExists ? "Exists" : "Does not exist"));
        System.out.println("Before Adding House Reservation (" + houseResNumber + "): " + (houseExists ? "Exists" : "Does not exist"));

        try {
            // Unique addresses for each reservation
            Address cabinAddress = new Address("123 Cabin St", "Denver", "CO", 80202);
            Address hotelAddress = new Address("789 Hotel Lane", "New York", "NY", 10001);
            Address houseAddress = new Address("456 Home Dr", "Austin", "TX", 73301);

            // Create different types of reservations with unique addresses
            Reservation cabinReservation = new CabinReservation(
                    cabinResNumber, accountNumber, cabinAddress, null, LocalDate.now(),
                    4, 3, 2, 2, 1000, 200.00, true, true
            );
            Reservation hotelReservation = new HotelReservation(
                    hotelResNumber, accountNumber, hotelAddress, null, LocalDate.now(),
                    3, 2, 1, 1, 400, 150.00, true
            );
            Reservation houseReservation = new HouseReservation(
                    houseResNumber, accountNumber, houseAddress, null, LocalDate.now(),
                    5, 4, 3, 3, 1800, 250.00, 2
            );

            // Add all reservations
            manager.addReservation(accountNumber, cabinReservation);
            manager.addReservation(accountNumber, hotelReservation);
            manager.addReservation(accountNumber, houseReservation);

            System.out.println("Reservation saved: " + cabinResNumber + " at " + cabinAddress);
            System.out.println("Reservation saved: " + hotelResNumber + " at " + hotelAddress);
            System.out.println("Reservation saved: " + houseResNumber + " at " + houseAddress);

        } catch (Exception e) {
            System.out.println("Exception in Adding Reservation: " + e.getMessage());
        }

        // After Adding Reservation - Check if they exist now
        cabinExists = manager.calculatePricePerNight(accountNumber, cabinResNumber) > 0;
        hotelExists = manager.calculatePricePerNight(accountNumber, hotelResNumber) > 0;
        houseExists = manager.calculatePricePerNight(accountNumber, houseResNumber) > 0;

        System.out.println("After Adding Cabin Reservation (" + cabinResNumber + "): " + (cabinExists ? "Exists" : "Does not exist"));
        System.out.println("After Adding Hotel Reservation (" + hotelResNumber + "): " + (hotelExists ? "Exists" : "Does not exist"));
        System.out.println("After Adding House Reservation (" + houseResNumber + "): " + (houseExists ? "Exists" : "Does not exist"));
    }


    /**
     * Tests retrieving a reservation.
     */
    public static void testRetrieveReservation() {
        System.out.println("\n>> Test: Retrieve Reservation");

        String accountNumber = "ACC123";
        String[] reservationNumbers = {"RES001", "RES002", "RES003"};

        List<Reservation> reservations = new ArrayList<>();

        for (String reservationNumber : reservationNumbers) {
            try {
                Reservation reservation = Manager.loadReservationFromFile(accountNumber, reservationNumber);
                reservations.add(reservation);
            } catch (Exception e) {
                System.out.println("Exception in Retrieving Reservation: " + e.getMessage());
            }  // End try-catch statements loop
        } // End for loop

        // Sort reservations before displaying
        reservations.sort(Comparator.comparing(Reservation::getReservationNumber));

        // Print in order
        for (Reservation reservation : reservations) {
            System.out.println("Retrieved Reservation: " + reservation);
        } // End for loop
    }


    /**
     * Tests canceling a reservation.
     */
    public static void testCancelReservation() {
        System.out.println("\n>> Test: Cancel Reservation");

        Manager manager = new Manager();
        String accountNumber = "ACC123";
        String[] reservationNumbers = {"RES001", "RES002", "RES003"};

        for (String reservationNumber : reservationNumbers) {
            try {
                System.out.println("Before Cancellation ("+ reservationNumber +") - Price per night: $" + String.format("%.2f",manager.calculatePricePerNight(accountNumber, reservationNumber)));
                manager.cancelReservation(accountNumber, reservationNumber);
                System.out.println("Reservation cancelled: " + reservationNumber);
                System.out.println("After Cancellation ("+ reservationNumber +") - Price per night: $" + String.format("%.2f",manager.calculatePricePerNight(accountNumber, reservationNumber)));
            } catch (Exception e) {
                System.out.println("Exception in Cancelling Reservation: " + e.getMessage());
            }
        }
    }


    /**
     * Tests completing a reservation.
     */
    public static void testCompleteReservation() {
        System.out.println("\n>> Test: Complete Reservation");

        Manager manager = new Manager();
        String accountNumber = "ACC123";
        String[] reservationNumbers = {"RES001", "RES002", "RES003"};

        for (String reservationNumber : reservationNumbers) {
            try {
                System.out.println("Before Completion ("+ reservationNumber +") - Price per night: $" + String.format("%.2f",manager.calculatePricePerNight(accountNumber, reservationNumber)));
                manager.completeReservation(accountNumber, reservationNumber);
                System.out.println("Reservation completed: " + reservationNumber);
                System.out.println("After Completion ("+ reservationNumber +") - Price per night: $" + String.format("%.2f",manager.calculatePricePerNight(accountNumber, reservationNumber)));
            } catch (Exception e) {
                System.out.println("Exception in Completing Reservation: " + e.getMessage());
            }
        }
    }


    /**
     * Tests updating a reservation.
     */
    public static void testUpdateReservation() {
        System.out.println("\n>> Test: Update Reservation");

        Manager manager = new Manager();
        String accountNumber = "ACC123";

        String[] reservationNumbers = {"RES001", "RES002", "RES003"};

        for (String reservationNumber : reservationNumbers) {
            try {
                System.out.println("Before Update ("+ reservationNumber +") - Price per night: $" + String.format("%.2f",manager.calculatePricePerNight(accountNumber, reservationNumber)));

                Address newAddress = new Address("789 New Ave", "New York", "NY", 10001);
                Reservation updatedReservation = new HotelReservation(reservationNumber, accountNumber, newAddress, null, LocalDate.now(),
                        5, 3, 2, 2, 600, 180.00, true);

                manager.updateReservation(accountNumber, reservationNumber, updatedReservation);
                System.out.println("Updated reservation: " + reservationNumber);

                System.out.println("After Update ("+ reservationNumber +")- Price per night: $" + String.format("%.2f",manager.calculatePricePerNight(accountNumber, reservationNumber)));

            } catch (Exception e) {
                System.out.println("Exception in Updating Reservation: " + e.getMessage());
            }
        }
    }


    /**
     * Tests price calculation.
     */
    public static void testCalculatePrices() {
        System.out.println("\n>> Test: Calculate Prices");

        Manager manager = new Manager();
        String accountNumber = "ACC123";

        // Verify Hotel, Cabin, and House reservations
        String[] reservationNumbers = {"RES001", "RES002", "RES003"};

        for (String reservationNumber : reservationNumbers) {
            double price = manager.calculatePricePerNight(accountNumber, reservationNumber);
            System.out.println("Price per night for " + reservationNumber + ": $" + String.format("%.2f",price));
        }
    }


    /**
     * Tests exception handling.
     */
    public static void testExceptionHandling() {
        System.out.println("\n>> Test: Exception Handling");

        try {
            throw new IllegalParameter_Exception("ACC123", "RES001", "Test exception");
        } catch (Exception e) {
            System.out.println("Caught Exception: " + e.getMessage());
        }
    }

    /**
     * Tests file persistence.
     */
    public static void testFilePersistence() {
        System.out.println("\n>> Test: File Persistence");

        Manager manager = new Manager();
        String accountNumber = "ACC123";
        String[] reservationNumbers = {"RES001", "RES002", "RES003"};

        for (String reservationNumber : reservationNumbers) {
            try {
                Reservation reservation = manager.loadReservationFromFile(accountNumber, reservationNumber);
                System.out.println("Loaded from file: " + reservation.toString());
            } catch (Exception e) {
                System.out.println("Exception in File Persistence Test: " + e.getMessage());
            }
        }
    }


    /**
     * Tests features specific to CabinReservation.
     */
    public static void testCabinReservationFeatures() {
        System.out.println("\n>> Test: Cabin Reservation Features");

        Address address = new Address("123 Cabin St", "Denver", "CO", 80202);
        CabinReservation cabin = new CabinReservation("RES002", "ACC123", address, null, LocalDate.now(), 4, 3, 2, 2, 1000, 200.00, true, true);

        System.out.println("Cabin Reservation Details: " + cabin);
    }

    /**
     * Tests features specific to HotelReservation.
     */
    public static void testHotelReservationFeatures() {
        System.out.println("\n>> Test: Hotel Reservation Features");

        Address address = new Address("789 Hotel Lane", "New York", "NY", 10001);
        HotelReservation hotel = new HotelReservation("RES003", "ACC123", address, null, LocalDate.now(), 2, 1, 1, 1, 300, 100.00, true);

        System.out.println("Hotel Reservation Details: " + hotel);
    }

    /**
     * Tests features specific to HouseReservation.
     */
    public static void testHouseReservationFeatures() {
        System.out.println("\n>> Test: House Reservation Features");

        Address address = new Address("456 Home Dr", "Austin", "TX", 73301);
        HouseReservation house = new HouseReservation("RES004", "ACC123", address, null, LocalDate.now(), 5, 4, 3, 3, 2500, 400.00, 2);

        System.out.println("House Reservation Details: " + house);
    }
}
