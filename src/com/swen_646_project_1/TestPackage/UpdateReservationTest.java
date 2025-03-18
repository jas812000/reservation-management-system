// Imports all classes from the TestPackage inside the com.swen_646_project_1 package.
package com.swen_646_project_1.TestPackage;

/*
 * Imports necessary classes for testing reservation updates.
 * - `Account`: Needed to retrieve and modify reservation data.
 * - `IllegalState_Exception`: Exception thrown when modifying a reservation that is already completed or canceled.
 * - `IllegalOperation_Exception`: Exception thrown when an update is attempted on a non-existent reservation.
 */
import com.swen_646_project_1.Account;
import com.swen_646_project_1.exceptions.IllegalState_Exception;
import com.swen_646_project_1.exceptions.IllegalOperation_Exception;
import com.swen_646_project_1.reservation.CabinReservation;
import com.swen_646_project_1.reservation.HotelReservation;
import com.swen_646_project_1.reservation.HouseReservation;
import com.swen_646_project_1.reservation.Reservation;
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
            System.out.println("❌ No account found for reservation: " + reservationNumber);
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

        // **Manually copy the original reservation instead of cloning**
        Reservation originalReservation = createReservationCopy(currentReservation);

        // Handle update based on reservation type
        switch (currentReservation) {
            case CabinReservation cabin -> {
                System.out.print("Enter new number of nights: ");
                cabin.setNumNights(Integer.parseInt(scanner.nextLine().trim()));
                System.out.print("Enter new number of beds: ");
                cabin.setNumBeds(Integer.parseInt(scanner.nextLine().trim()));
                System.out.print("Enter new number of bedrooms: ");
                cabin.setNumBedrooms(Integer.parseInt(scanner.nextLine().trim()));
                System.out.print("Enter new number of bathrooms: ");
                cabin.setNumBathrooms(Integer.parseInt(scanner.nextLine().trim()));
                System.out.print("Enter new square footage: ");
                cabin.setLodgingSizeSqFt(Integer.parseInt(scanner.nextLine().trim()));
                System.out.print("Full Kitchen Available (true/false): ");
                cabin.setFullKitchenAvailable(Boolean.parseBoolean(scanner.nextLine().trim()));
                System.out.print("Loft Available (true/false): ");
                cabin.setLoftAvailable(Boolean.parseBoolean(scanner.nextLine().trim()));
            }
            case HotelReservation hotel -> {
                System.out.print("Enter new number of nights: ");
                hotel.setNumNights(Integer.parseInt(scanner.nextLine().trim()));
                System.out.print("Enter new number of beds: ");
                hotel.setNumBeds(Integer.parseInt(scanner.nextLine().trim()));
                System.out.print("Enter new number of bedrooms: ");
                hotel.setNumBedrooms(Integer.parseInt(scanner.nextLine().trim()));
                System.out.print("Enter new number of bathrooms: ");
                hotel.setNumBathrooms(Integer.parseInt(scanner.nextLine().trim()));
                System.out.print("Enter new square footage: ");
                hotel.setLodgingSizeSqFt(Integer.parseInt(scanner.nextLine().trim()));
                System.out.print("Kitchenette Available (true/false): ");
                hotel.setKitchenetteAvailable(Boolean.parseBoolean(scanner.nextLine().trim()));
            }
            case HouseReservation house -> {
                System.out.print("Enter new number of nights: ");
                house.setNumNights(Integer.parseInt(scanner.nextLine().trim()));
                System.out.print("Enter new number of beds: ");
                house.setNumBeds(Integer.parseInt(scanner.nextLine().trim()));
                System.out.print("Enter new number of bedrooms: ");
                house.setNumBedrooms(Integer.parseInt(scanner.nextLine().trim()));
                System.out.print("Enter new number of bathrooms: ");
                house.setNumBathrooms(Integer.parseInt(scanner.nextLine().trim()));
                System.out.print("Enter new square footage: ");
                house.setLodgingSizeSqFt(Integer.parseInt(scanner.nextLine().trim()));
                System.out.print("Enter new number of floors: ");
                house.setNumFloors(Integer.parseInt(scanner.nextLine().trim()));
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
        }
        return null;
    }




} // End UpdateReservationTest class
