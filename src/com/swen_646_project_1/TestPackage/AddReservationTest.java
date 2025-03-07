package com.swen_646_project_1.TestPackage;

import com.swen_646_project_1.Manager;
import com.swen_646_project_1.exceptions.DuplicateObject_Exception;
import com.swen_646_project_1.reservation.CabinReservation;
import com.swen_646_project_1.reservation.HotelReservation;
import com.swen_646_project_1.reservation.HouseReservation;
import com.swen_646_project_1.reservation.Reservation;
import com.swen_646_project_1.Address;

import java.time.LocalDate;
import java.util.Scanner;

public class AddReservationTest {

    public static void testAddReservation() {
        Scanner scanner = new Scanner(System.in);
        Manager manager = new Manager();

        System.out.print("\nEnter account number for reservation: ");
        String accountNumber = scanner.nextLine().trim();
        System.out.print("Enter reservation number: ");
        String reservationNumber = scanner.nextLine().trim();

        System.out.println("Select reservation type: ");
        System.out.println("1 - Hotel");
        System.out.println("2 - House");
        System.out.println("3 - Cabin");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Common reservation details
        Address physicalAddress = new Address("123 Main St", "New York", "NY", 10001);
        Address mailingAddress = new Address("123 Main St", "New York", "NY", 10001);
        LocalDate startDate = LocalDate.of(2025, 6, 15);
        int numNights = 5;
        int numBeds = 2;
        int numBedrooms = 1;
        int numBathrooms = 1;
        int lodgingSizeSqFt = 500;
        double pricePerNight = 150.0;

        try {
            Reservation newReservation = null;

            // Create the reservation based on user's choice
            switch (choice) {
                case 1 -> newReservation = new HotelReservation(
                        reservationNumber, accountNumber, physicalAddress, mailingAddress,
                        startDate, numNights, numBeds, numBedrooms, numBathrooms, lodgingSizeSqFt, pricePerNight,
                        true
                );
                case 2 -> newReservation = new HouseReservation(
                        reservationNumber, accountNumber, physicalAddress, mailingAddress,
                        startDate, numNights, numBeds, numBedrooms, numBathrooms, lodgingSizeSqFt, pricePerNight,
                        2
                );
                case 3 -> newReservation = new CabinReservation(
                        reservationNumber, accountNumber, physicalAddress, mailingAddress,
                        startDate, numNights, numBeds, numBedrooms, numBathrooms, lodgingSizeSqFt, pricePerNight,
                        true,
                        false
                );
                default -> {
                    System.out.println("Invalid selection. Reservation not created.");
                    return;
                }
            }


            // Add reservation to the account
            manager.getAccount(accountNumber).addReservation(newReservation);
            System.out.println("Reservation " + reservationNumber + " added successfully.");
        } catch (DuplicateObject_Exception e) {
            System.out.println("Reservation already exists.");
        }
    }
}
