package com.jamesstevens.rms.tests;

import com.jamesstevens.rms.Address;
import com.jamesstevens.rms.enums.ReservationStatus;
import com.jamesstevens.rms.exceptions.IllegalOperationException;
import com.jamesstevens.rms.reservation.CabinReservation;
import com.jamesstevens.rms.reservation.HotelReservation;
import com.jamesstevens.rms.reservation.HouseReservation;
import com.jamesstevens.rms.reservation.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.jamesstevens.rms.exceptions.IllegalStateException;
import com.jamesstevens.rms.exceptions.IllegalParameterException;
import com.jamesstevens.rms.exceptions.IllegalLoadException;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for verifying core reservation attributes across all reservation types.
 * <p>
 * This test suite validates that {@link CabinReservation}, {@link HotelReservation},
 * and {@link HouseReservation} correctly store and expose their fields.
 * </p>
 * <p>
 * It also validates the system's address invariants:
 * </p>
 * <ul>
 *     <li><b>Hotel/House</b>: Mailing address must never diverge from physical address.</li>
 *     <li><b>Cabin</b>: Mailing address may diverge from physical address.</li>
 * </ul>
 */
public class ReservationTest {

    private HotelReservation hotelReservation;
    private CabinReservation cabinReservation;
    private HouseReservation houseReservation;

    @TempDir
    Path tempDir;

    /**
     * Initializes sample reservations before each test.
     * <p>
     * Cabin reservations use separate physical and mailing addresses.
     * Hotel and House reservations use the same address for both physical and mailing.
     * </p>
     */
    @BeforeEach
    public void setUp() {

        System.setProperty("RMS_DATA_DIR", tempDir.toString());

        Address physicalAddress = new Address(
                "43-179 Day Mountain Road",
                "Temple",
                "ME",
                "04984"
        );

        Address mailingAddress = new Address(
                "PO Box 43179",
                "Waterville",
                "ME",
                "04901"
        );

        cabinReservation = new CabinReservation(
                "res-CAB90000000",
                "A900000000",
                physicalAddress,
                mailingAddress,
                LocalDate.of(2025, 7, 10),
                7,
                3,
                2,
                2,
                800,
                200.0,
                true,
                true
        );

        hotelReservation = new HotelReservation(
                "res-HOT90000000",
                "A900000001",
                physicalAddress,
                physicalAddress,
                LocalDate.of(2025, 6, 15),
                5,
                2,
                1,
                1,
                500,
                150.0,
                true
        );

        houseReservation = new HouseReservation(
                "res-HOU90000000",
                "A900000002",
                physicalAddress,
                physicalAddress,
                LocalDate.of(2025, 8, 1),
                10,
                4,
                3,
                3,
                1200,
                300.0,
                2
        );
    }

    /* ====================== CABIN TESTS ====================== */

    /**
     * Verifies that a cabin reservation stores its identifiers correctly.
     */
    @Test
    public void testCabinReservationIdentifiers() {
        assertEquals("res-CAB90000000", cabinReservation.getReservationNumber());
        assertEquals("A900000000", cabinReservation.getAccountNumber());
    }

    /**
     * Verifies that a cabin reservation stores separate physical and mailing addresses.
     */
    @Test
    public void testCabinAddresses() {
        Address physical = cabinReservation.getLodgingPhysicalAddress();
        Address mailing = cabinReservation.getLodgingMailingAddress();

        assertEquals("43-179 Day Mountain Road", physical.getStreet());
        assertEquals("Temple", physical.getCity());
        assertEquals("ME", physical.getState());
        assertEquals("04984", physical.getZipCode());

        assertEquals("PO Box 43179", mailing.getStreet());
        assertEquals("Waterville", mailing.getCity());
        assertEquals("ME", mailing.getState());
        assertEquals("04901", mailing.getZipCode());
    }

    /**
     * Verifies that a cabin reservation stores its non-address attributes correctly.
     */
    @Test
    public void testCabinAttributes() {
        assertEquals(LocalDate.of(2025, 7, 10), cabinReservation.getStartDate());
        assertEquals(7, cabinReservation.getNumNights());
        assertEquals(3, cabinReservation.getNumBeds());
        assertEquals(2, cabinReservation.getNumBedrooms());
        assertEquals(2, cabinReservation.getNumBathrooms());
        assertEquals(800, cabinReservation.getLodgingSizeSqFt());
        assertEquals(200.0, cabinReservation.getLodgingPrice(), 0.01);
        assertTrue(cabinReservation.isFullKitchenAvailable());
        assertTrue(cabinReservation.isLoftAvailable());
    }

    /**
     * Verifies that cabin reservations allow mailing address to diverge from physical address.
     * <p>
     * This enforces the business rule that cabins may have distinct mailing and physical addresses.
     * </p>
     */
    @Test
    public void testCabinMailingCanDivergeFromPhysical() {
        cabinReservation.setLodgingPhysicalAddress("10 Pine", "Portland", "ME", "04101");
        cabinReservation.setLodgingMailingAddress("PO Box 77", "Bangor", "ME", "04401");

        assertNotEquals(
                cabinReservation.getLodgingPhysicalAddress().toString(),
                cabinReservation.getLodgingMailingAddress().toString()
        );
    }

    /* ====================== HOTEL TESTS ====================== */

    /**
     * Verifies that a hotel reservation stores its identifiers correctly.
     */
    @Test
    public void testHotelReservationIdentifiers() {
        assertEquals("res-HOT90000000", hotelReservation.getReservationNumber());
        assertEquals("A900000001", hotelReservation.getAccountNumber());
    }

    /**
     * Verifies that a hotel reservation stores its non-address attributes correctly.
     */
    @Test
    public void testHotelAttributes() {
        assertEquals(LocalDate.of(2025, 6, 15), hotelReservation.getStartDate());
        assertEquals(5, hotelReservation.getNumNights());
        assertEquals(2, hotelReservation.getNumBeds());
        assertEquals(1, hotelReservation.getNumBedrooms());
        assertEquals(1, hotelReservation.getNumBathrooms());
        assertEquals(500, hotelReservation.getLodgingSizeSqFt());
        assertEquals(150.0, hotelReservation.getLodgingPrice(), 0.01);
        assertTrue(hotelReservation.hasKitchenette());
    }

    /**
     * Verifies the hotel invariant that mailing address must not diverge from physical address.
     * <p>
     * Updating physical address must also update mailing to match.
     * Attempting to explicitly set a separate mailing address should be rejected.
     * </p>
     */
    @Test
    public void testHotelMailingCannotDivergeFromPhysical() {
        hotelReservation.setLodgingPhysicalAddress("1 Main", "Dallas", "TX", "75001");

        assertEquals(
                hotelReservation.getLodgingPhysicalAddress().toString(),
                hotelReservation.getLodgingMailingAddress().toString()
        );

        assertThrows(IllegalOperationException.class, () ->
                hotelReservation.setLodgingMailingAddress("PO Box 9", "Dallas", "TX", "75002")
        );
    }

    /* ====================== HOUSE TESTS ====================== */

    /**
     * Verifies that a house reservation stores its identifiers correctly.
     */
    @Test
    public void testHouseReservationIdentifiers() {
        assertEquals("res-HOU90000000", houseReservation.getReservationNumber());
        assertEquals("A900000002", houseReservation.getAccountNumber());
    }

    /**
     * Verifies that a house reservation stores its non-address attributes correctly.
     */
    @Test
    public void testHouseAttributes() {
        assertEquals(LocalDate.of(2025, 8, 1), houseReservation.getStartDate());
        assertEquals(10, houseReservation.getNumNights());
        assertEquals(4, houseReservation.getNumBeds());
        assertEquals(3, houseReservation.getNumBedrooms());
        assertEquals(3, houseReservation.getNumBathrooms());
        assertEquals(1200, houseReservation.getLodgingSizeSqFt());
        assertEquals(300.0, houseReservation.getLodgingPrice(), 0.01);
        assertEquals(2, houseReservation.getNumFloors());
    }

    /**
     * Verifies the house invariant that mailing address must not diverge from physical address.
     * <p>
     * Updating physical address must also update mailing to match.
     * </p>
     */
    @Test
    public void testHouseMailingCannotDivergeFromPhysical() {
        houseReservation.setLodgingPhysicalAddress("2 Oak", "Austin", "TX", "73301");

        assertEquals(
                houseReservation.getLodgingPhysicalAddress().toString(),
                houseReservation.getLodgingMailingAddress().toString()
        );
    }

    /**
     * Verifies that a completed reservation is locked against further modification.
     */
    @Test
    public void testCompletedReservationCannotBeModified() {
        hotelReservation.completeReservation();

        assertTrue(hotelReservation.isLocked());

        assertThrows(
                IllegalStateException.class,
                () -> hotelReservation.setNumNights(10)
        );

        assertThrows(
                IllegalStateException.class,
                () -> hotelReservation.setKitchenetteAvailable(false)
        );
    }

    /**
     * Verifies that a canceled reservation is locked against further modification.
     */
    @Test
    public void testCancelledReservationCannotBeModified() {
        cabinReservation.cancelReservation();

        assertTrue(cabinReservation.isLocked());

        assertThrows(
                IllegalStateException.class,
                () -> cabinReservation.setStartDate(LocalDate.of(2025, 9, 1))
        );

        assertThrows(
                IllegalStateException.class,
                () -> cabinReservation.setFullKitchenAvailable(false)
        );

        assertThrows(
                IllegalStateException.class,
                () -> cabinReservation.setLoftAvailable(false)
        );
    }

    /**
     * Verifies that reservation numbers must use the canonical format.
     */
    @Test
    public void testInvalidReservationNumberIsRejected() {
        assertThrows(
                IllegalParameterException.class,
                () -> new CabinReservation(
                        "invalid",
                        "A900000000",
                        new Address("1 Main", "Dallas", "TX", "75001"),
                        new Address("1 Main", "Dallas", "TX", "75001"),
                        LocalDate.of(2025, 7, 10),
                        1,
                        1,
                        1,
                        1,
                        500,
                        100.0,
                        false,
                        false
                )
        );
    }

    /**
     * Verifies that a reservation number must match its reservation subtype.
     */
    @Test
    public void testReservationNumberMustMatchSubtype() {
        assertThrows(
                IllegalParameterException.class,
                () -> new CabinReservation(
                        "res-HOT90000000",
                        "A900000000",
                        new Address("1 Main", "Dallas", "TX", "75001"),
                        new Address("1 Main", "Dallas", "TX", "75001"),
                        LocalDate.of(2025, 7, 10),
                        1,
                        1,
                        1,
                        1,
                        500,
                        100.0,
                        false,
                        false
                )
        );
    }

    /**
     * Verifies that cancelling a reservation preserves its recorded lodging price.
     */
    @Test
    public void testCancelledReservationPreservesLodgingPrice() {
        double originalPrice = cabinReservation.getLodgingPrice();

        cabinReservation.cancelReservation();

        assertEquals(ReservationStatus.CANCELLED, cabinReservation.getStatus());
        assertEquals(originalPrice, cabinReservation.getLodgingPrice());
    }

    /**
     * Verifies that changing square footage recalculates the stored lodging price.
     */
    @Test
    public void testSquareFootageChangeRecalculatesLodgingPrice() {
        cabinReservation.setLodgingSizeSqFt(901);

        assertEquals(
                cabinReservation.calculatePricePerNight(),
                cabinReservation.getLodgingPrice()
        );
    }

    /**
     * Verifies that changing the cabin kitchen option recalculates the stored lodging price.
     */
    @Test
    public void testCabinKitchenChangeRecalculatesLodgingPrice() {
        cabinReservation.setFullKitchenAvailable(true);

        assertEquals(
                cabinReservation.calculatePricePerNight(),
                cabinReservation.getLodgingPrice()
        );
    }

    /**
     * Verifies that changing the hotel kitchenette option recalculates the stored lodging price.
     */
    @Test
    public void testHotelKitchenetteChangeRecalculatesLodgingPrice() {
        hotelReservation.setKitchenetteAvailable(true);

        assertEquals(
                hotelReservation.calculatePricePerNight(),
                hotelReservation.getLodgingPrice()
        );
    }

    /**
     * Verifies that cancellation preserves the historical lodging price
     * and does not alter the reservation's pricing calculation.
     */
    @Test
    public void testCancellationDoesNotChangePricingCalculation() {
        double originalLodgingPrice = cabinReservation.getLodgingPrice();
        double calculatedPrice = cabinReservation.calculatePricePerNight();

        cabinReservation.cancelReservation();

        assertEquals(originalLodgingPrice, cabinReservation.getLodgingPrice());
        assertEquals(calculatedPrice, cabinReservation.calculatePricePerNight());
    }

    /**
     * Verifies that malformed persisted reservation data is reported as a load
     * failure while preserving the underlying parsing or validation exception.
     */
    @Test
    public void testMalformedPersistedReservationPreservesCause() {
        String malformedData = cabinReservation.toString()
                .replace("res-CAB90000000", "invalid");

        IllegalLoadException exception = assertThrows(
                IllegalLoadException.class,
                () -> Reservation.fromString(malformedData)
        );

        assertNotNull(exception.getCause());
    }
}
