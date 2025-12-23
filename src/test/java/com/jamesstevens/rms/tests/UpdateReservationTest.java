package com.jamesstevens.rms.tests;

import com.jamesstevens.rms.Account;
import com.jamesstevens.rms.Address;
import com.jamesstevens.rms.exceptions.IllegalOperation_Exception;
import com.jamesstevens.rms.exceptions.IllegalState_Exception;
import com.jamesstevens.rms.reservation.*;

import java.time.LocalDate;
import java.util.Scanner;

/**
 * Manual/interactive test utility for updating existing reservations.
 * <p>
 * This test constructs a new updated reservation instance, applies user-entered
 * changes, and submits it through {@link Account#updateReservation(String, Reservation)}
 * to validate the full update pipeline (including address rules).
 * </p>
 */
@SuppressWarnings("unused")
public class UpdateReservationTest {

    /**
     * Executes the interactive update flow for a single reservation.
     */
    public static void testUpdateReservation() {
        Scanner scanner = new Scanner(System.in);

        String reservationNumber = TestHelper.getValidatedReservation();
        if (reservationNumber == null) return;

        Account account = TestHelper.getAccountFromReservation(reservationNumber);
        if (account == null) return;

        Reservation current = account.getReservation(reservationNumber);
        if (current == null) {
            System.out.println("Reservation not found.");
            return;
        }

        System.out.println("\nUpdating " + current.getClass().getSimpleName());

        Reservation updated = createReservationCopy(current);

        updateCommonFields(scanner, updated);
        updateAddresses(scanner, updated);

        if (updated instanceof CabinReservation cabin) {
            System.out.print("Full kitchen available (true/false): ");
            cabin.setFullKitchenAvailable(Boolean.parseBoolean(scanner.nextLine()));

            System.out.print("Loft available (true/false): ");
            cabin.setLoftAvailable(Boolean.parseBoolean(scanner.nextLine()));

            cabin.setLodgingPrice(cabin.calculatePricePerNight());
        } else if (updated instanceof HotelReservation hotel) {
            System.out.print("Kitchenette available (true/false): ");
            hotel.setKitchenetteAvailable(Boolean.parseBoolean(scanner.nextLine()));

            hotel.setLodgingPrice(hotel.calculatePricePerNight());
        } else if (updated instanceof HouseReservation house) {
            System.out.print("Number of floors: ");
            house.setNumFloors(Integer.parseInt(scanner.nextLine()));

            house.setLodgingPrice(house.calculatePricePerNight());
        }

        try {
            account.updateReservation(reservationNumber, updated);
            System.out.println("Reservation updated successfully.");
        } catch (IllegalState_Exception | IllegalOperation_Exception e) {
            System.out.println("Update failed: " + e.getMessage());
        }
    }

    private static void updateCommonFields(Scanner scanner, Reservation r) {
        System.out.print("Start date (yyyy-MM-dd): ");
        r.setStartDate(LocalDate.parse(scanner.nextLine()));

        System.out.print("Number of nights: ");
        r.setNumNights(Integer.parseInt(scanner.nextLine()));

        System.out.print("Beds: ");
        r.setNumBeds(Integer.parseInt(scanner.nextLine()));

        System.out.print("Bedrooms: ");
        r.setNumBedrooms(Integer.parseInt(scanner.nextLine()));

        System.out.print("Bathrooms: ");
        r.setNumBathrooms(Integer.parseInt(scanner.nextLine()));

        System.out.print("Square footage: ");
        r.setLodgingSizeSqFt(Integer.parseInt(scanner.nextLine()));
    }

    private static void updateAddresses(Scanner scanner, Reservation r) {
        System.out.println("\nUpdate physical address:");
        Address physical = TestHelper.getUserAddressInput();
        r.setLodgingPhysicalAddress(
                physical.getStreet(),
                physical.getCity(),
                physical.getState(),
                physical.getZipCode()
        );

        System.out.println("\nUpdate mailing address:");
        Address mailing = TestHelper.getUserAddressInput();
        r.setLodgingMailingAddress(
                mailing.getStreet(),
                mailing.getCity(),
                mailing.getState(),
                mailing.getZipCode()
        );
    }

    private static Reservation createReservationCopy(Reservation r) {
        if (r instanceof CabinReservation c) {
            return new CabinReservation(
                    c.getReservationNumber(),
                    c.getAccountNumber(),
                    c.getLodgingPhysicalAddress(),
                    c.getLodgingMailingAddress(),
                    c.getStartDate(),
                    c.getNumNights(),
                    c.getNumBeds(),
                    c.getNumBedrooms(),
                    c.getNumBathrooms(),
                    c.getLodgingSizeSqFt(),
                    c.getLodgingPrice(),
                    c.isFullKitchenAvailable(),
                    c.isLoftAvailable()
            );
        } else if (r instanceof HotelReservation h) {
            return new HotelReservation(
                    h.getReservationNumber(),
                    h.getAccountNumber(),
                    h.getLodgingPhysicalAddress(),
                    h.getLodgingMailingAddress(),
                    h.getStartDate(),
                    h.getNumNights(),
                    h.getNumBeds(),
                    h.getNumBedrooms(),
                    h.getNumBathrooms(),
                    h.getLodgingSizeSqFt(),
                    h.getLodgingPrice(),
                    h.hasKitchenette()
            );
        } else if (r instanceof HouseReservation h) {
            return new HouseReservation(
                    h.getReservationNumber(),
                    h.getAccountNumber(),
                    h.getLodgingPhysicalAddress(),
                    h.getLodgingMailingAddress(),
                    h.getStartDate(),
                    h.getNumNights(),
                    h.getNumBeds(),
                    h.getNumBedrooms(),
                    h.getNumBathrooms(),
                    h.getLodgingSizeSqFt(),
                    h.getLodgingPrice(),
                    h.getNumFloors()
            );
        }
        throw new IllegalStateException("Unsupported reservation type.");
    }
}
