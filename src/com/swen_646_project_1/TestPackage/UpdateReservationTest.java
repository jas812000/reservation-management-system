// Imports all classes from the TestPackage inside the com.swen_646_project_1 package.
package com.swen_646_project_1.TestPackage;

/*
 * Imports necessary classes for testing reservation updates.
 * - `Account`: Needed to retrieve and modify reservation data.
 * - `IllegalState_Exception`: Exception thrown when modifying a reservation that is already completed or canceled.
 * - `IllegalOperation_Exception`: Exception thrown when an update is attempted on a non-existent reservation.
 * - `CabinReservation`, `HotelReservation`, `HouseReservation`: Specific reservation types with unique attributes.
 * - `Reservation`: The parent class representing a generic reservation.
 * - `java.time.*`: Provides date and time utilities for handling reservation start dates.
 * - `Scanner`: Enables user input for modifying reservation details.
 */
import com.swen_646_project_1.Account;
import com.swen_646_project_1.exceptions.IllegalState_Exception;
import com.swen_646_project_1.exceptions.IllegalOperation_Exception;
import com.swen_646_project_1.reservation.CabinReservation;
import com.swen_646_project_1.reservation.HotelReservation;
import com.swen_646_project_1.reservation.HouseReservation;
import com.swen_646_project_1.reservation.Reservation;
import java.time.*;
import java.util.Scanner;

/**
 * Test class for updating reservations.
 * Ensures proper validation when modifying reservations.
 */
public class UpdateReservationTest {
    /**
     * Tests updating an existing reservation.
     * - Prompts the user to select an account.
     * - Retrieves the selected reservation.
     * - Attempts to update the reservation, handling exceptions if the update is not allowed.
     */
    public static void testUpdateReservation() {

        Scanner scanner = new Scanner(System.in);

        // Select an account from available accounts
        String reservationNumber = TestHelper.getValidatedReservation();
        if (reservationNumber == null) return; // Exits early if no valid reservation is selected

        // Retrieve the associated account from the reservation number
        Account account = TestHelper.getAccountFromReservation(reservationNumber);
        if (account == null) {
            System.out.println("No account found for reservation: " + reservationNumber);
            return;
        } // End if statement

        // Retrieve the existing reservation
        Reservation currentReservation = account.getReservation(reservationNumber);
        if (currentReservation == null) {
            System.out.println("Error: Reservation not found.");
            return;
        } // End if statement

        System.out.println("\n========== Updating Reservation ==========");
        System.out.println("Reservation Type: " + currentReservation.getClass().getSimpleName());

        // Manually copy the original reservation instead of cloning
        Reservation originalReservation = createReservationCopy(currentReservation);

        // Update common fields
        updateCommonReservationFields(scanner, currentReservation);

        // Handle update based on reservation type
        switch (currentReservation) {
            case CabinReservation cabin -> {
                System.out.print("Full Kitchen Available (true/false): ");
                cabin.setFullKitchenAvailable(Boolean.parseBoolean(scanner.nextLine().trim()));
                System.out.print("Loft Available (true/false): ");
                cabin.setLoftAvailable(Boolean.parseBoolean(scanner.nextLine().trim()));
                cabin.setLodgingPrice(cabin.calculatePricePerNight());
            }
            case HotelReservation hotel -> {
                System.out.print("Kitchenette Available (true/false): ");
                hotel.setKitchenetteAvailable(Boolean.parseBoolean(scanner.nextLine().trim()));
                hotel.setLodgingPrice(hotel.calculatePricePerNight());
            }
            case HouseReservation house -> {
                System.out.print("Enter number of floors: ");
                house.setNumFloors(Integer.parseInt(scanner.nextLine().trim()));
                house.setLodgingPrice(house.calculatePricePerNight());
            }
            default -> {
                System.out.println("Error: Unsupported reservation type.");
                return;
            }
        } // End switch statements

        assert originalReservation != null;
        if (!originalReservation.equals(currentReservation)) {
            try {
                account.updateReservation(reservationNumber, currentReservation);
                System.out.println("Reservation updated successfully.");
            } catch (IllegalState_Exception | IllegalOperation_Exception e) {
                System.out.println("Error updating reservation: " + e.getMessage());
            } // End try-catch statements
        } else {
            System.out.println("No changes detected. Reservation not updated.");
        } // End if-else statements
    } // End testUpdateReservation method


    /**
     * Prompts the user for common reservation fields and updates the values.
     * Applies to all reservation types (Cabin, Hotel, House).
     *
     * @param scanner Scanner instance for user input.
     * @param reservation The reservation object to update.
     */
    private static void updateCommonReservationFields(Scanner scanner, Reservation reservation) {
        System.out.print("Enter reservation start date (yyyy-MM-dd): ");
        reservation.setStartDate(LocalDate.parse(scanner.nextLine().trim()));

        System.out.print("Enter new number of nights: ");
        reservation.setNumNights(Integer.parseInt(scanner.nextLine().trim()));

        System.out.print("Enter new number of beds: ");
        reservation.setNumBeds(Integer.parseInt(scanner.nextLine().trim()));

        System.out.print("Enter new number of bedrooms: ");
        reservation.setNumBedrooms(Integer.parseInt(scanner.nextLine().trim()));

        System.out.print("Enter new number of bathrooms: ");
        reservation.setNumBathrooms(Integer.parseInt(scanner.nextLine().trim()));

        System.out.print("Enter new square footage: ");
        reservation.setLodgingSizeSqFt(Integer.parseInt(scanner.nextLine().trim()));
    } // End updateCommonReservationFields method


    /**
     * Creates a deep copy of a given reservation.
     * - This method ensures that modifications to the copied reservation do not affect the original instance.
     * - Uses instanceof checks to determine the specific subclass of the reservation and creates an appropriate copy.
     * - If the reservation type is unrecognized, it returns null.
     *
     * @param reservation The reservation instance to be copied.
     * @return A new Reservation object that is an independent copy of the original.
     */
    private static Reservation createReservationCopy(Reservation reservation) {
        if (reservation instanceof CabinReservation cabin) {
            return new CabinReservation(
                    cabin.getReservationNumber(),
                    cabin.getAccountNumber(),
                    cabin.getLodgingPhysicalAddress(),
                    cabin.getLodgingMailingAddress(),
                    cabin.getStartDate(),
                    cabin.getNumNights(),
                    cabin.getNumBeds(),
                    cabin.getNumBedrooms(),
                    cabin.getNumBathrooms(),
                    cabin.getLodgingSizeSqFt(),
                    cabin.getLodgingPrice(),
                    cabin.isFullKitchenAvailable(),
                    cabin.isLoftAvailable()
            );
        } else if (reservation instanceof HotelReservation hotel) {
            return new HotelReservation(
                    hotel.getReservationNumber(),
                    hotel.getAccountNumber(),
                    hotel.getLodgingPhysicalAddress(),
                    hotel.getLodgingMailingAddress(),
                    hotel.getStartDate(),
                    hotel.getNumNights(),
                    hotel.getNumBeds(),
                    hotel.getNumBedrooms(),
                    hotel.getNumBathrooms(),
                    hotel.getLodgingSizeSqFt(),
                    hotel.getLodgingPrice(),
                    hotel.hasKitchenette()
            );
        } else if (reservation instanceof HouseReservation house) {
            return new HouseReservation(
                    house.getReservationNumber(),
                    house.getAccountNumber(),
                    house.getLodgingPhysicalAddress(),
                    house.getLodgingMailingAddress(),
                    house.getStartDate(),
                    house.getNumNights(),
                    house.getNumBeds(),
                    house.getNumBedrooms(),
                    house.getNumBathrooms(),
                    house.getLodgingSizeSqFt(),
                    house.getLodgingPrice(),
                    house.getNumFloors()
            );
        } // End if-else statements
        return null;
    } // End createReservationCopy method

} // End UpdateReservationTest class
