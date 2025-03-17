// Imports all classes from the TestPackage inside the com.swen_646_project_1 package.
package com.swen_646_project_1.TestPackage;

/*
 * Imports necessary classes for testing reservation functionalities.
 * - `Address`: Represents the physical address associated with a reservation.
 * - `HotelReservation`, `CabinReservation`, `HouseReservation`: Different lodging types available in the system.
 * - `BeforeEach`: JUnit 5 annotation to set up test data before each test.
 * - `Test`: JUnit 5 annotation to define unit test cases.
 * - `LocalDate`: Used for handling reservation start dates.
 * - `Assertions.*`: Provides methods for verifying expected outcomes in unit tests.
 */
import com.swen_646_project_1.Address;
import com.swen_646_project_1.reservation.HotelReservation;
import com.swen_646_project_1.reservation.CabinReservation;
import com.swen_646_project_1.reservation.HouseReservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for testing reservation functionalities.
 * This class verifies that different types of reservations
 * (Hotel, Cabin, and House) correctly handle attributes like:
 * - Number of beds, bedrooms, and bathrooms.
 * - Lodging size in square feet.
 * - Special features such as a kitchenette, loft, or multiple floors.
 */
public class ReservationTest {
    // Declare attributes for different reservation types
    private HotelReservation hotelReservation;
    private CabinReservation cabinReservation;
    private HouseReservation houseReservation;

    /**
     * Initializes test data before each test case runs.
     * Creates sample reservations for a hotel, cabin, and house.
     */
    @BeforeEach
    public void setUp() {
        Address physicalAddress = new Address("43-179 Day Mountain Road", "Temple",
                "ME", Integer.parseInt("04984"));
        Address mailingAddress = new Address("PO Box 43179", "Waterville",
                "ME", Integer.parseInt("04901"));

        // Cabin Reservation
        cabinReservation = new CabinReservation(
                "res-CAB10000000",
                "A100000000",
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

        // Hotel Reservation
        hotelReservation = new HotelReservation(
                "res-HOT10000000",
                "A100000001",
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

        // House Reservation
        houseReservation = new HouseReservation(
                "res-HOU10000000",
                "A100000002",
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
    } // End setUp method

    // ====================== CABIN TESTS ======================

    /**
     * Tests if the cabin reservation returns the correct account number.
     */
    @Test
    public void testCabinGetReservationNumber() {
        System.out.println("Cabin Reservation Number Entered: res-CAB10000000");
        System.out.println("Cabin Reservation Number Retrieved: " + cabinReservation.getReservationNumber());
        assertEquals("res-CAB10000000", cabinReservation.getReservationNumber());
    } // End testCabinGetReservationNumber method

    /**
     * Tests if the cabin reservation returns the correct reservation number.
     */
    @Test
    public void testCabinGetAccountNumber() {
        System.out.println("Cabin Account Number Entered: A100000000");
        System.out.println("Cabin Account Number Retrieved: " + cabinReservation.getAccountNumber());
        assertEquals("A100000000", cabinReservation.getAccountNumber());
    } // End testCabinGetAccountNumber method

    /**
     * Tests if the cabin reservation returns the correct physical address.
     */
    @Test
    public void testCabinGetPhysicalAddress() {
        // Define expected values
        String expectedStreet = "43-179 Day Mountain Road";
        String expectedCity = "Temple";
        String expectedState = "ME";
        int expectedZipCode = 4984;  // ZIP code as an integer

        // Retrieve actual values from the reservation object
        Address retrievedAddress = cabinReservation.getLodgingPhysicalAddress();

        // Print entered vs. retrieved values
        System.out.println("Cabin Physical Address Entered: "
                + expectedStreet + ", " + expectedCity + ", " + expectedState + ", " + expectedZipCode);
        System.out.println("Cabin Physical Address Retrieved: " + retrievedAddress);

        // Assertions to verify correctness
        assertEquals(expectedStreet, retrievedAddress.getStreet());
        assertEquals(expectedCity, retrievedAddress.getCity());
        assertEquals(expectedState, retrievedAddress.getState());
        assertEquals(expectedZipCode, retrievedAddress.getZipCode());
    } // End testCabinGetPhysicalAddress method

    /**
     * Tests if the cabin reservation returns the correct mailing address.
     */
    @Test
    public void testCabinGetMailingAddress() {
        // Define expected values
        String expectedStreet = "PO Box 43179";
        String expectedCity = "Waterville";
        String expectedState = "ME";
        int expectedZipCode = 4901;  // ZIP code as an integer (04901 → 4901)

        // Retrieve actual values from the reservation object
        Address retrievedAddress = cabinReservation.getLodgingMailingAddress();

        // Print entered vs. retrieved values
        System.out.println("Cabin Mailing Address Entered: "
                + expectedStreet + ", " + expectedCity + ", " + expectedState + ", " + expectedZipCode);
        System.out.println("Cabin Mailing Address Retrieved: " + retrievedAddress);

        // Assertions to verify correctness
        assertEquals(expectedStreet, retrievedAddress.getStreet());
        assertEquals(expectedCity, retrievedAddress.getCity());
        assertEquals(expectedState, retrievedAddress.getState());
        assertEquals(expectedZipCode, retrievedAddress.getZipCode());
    } // End testCabinGetMailingAddress method

    /**
     * Tests if the cabin reservation returns the correct date.
     */
    @Test
    public void testCabinGetReservationDate() {
        System.out.println("Cabin Reservation Date Entered: 2025-07-10");
        System.out.println("Cabin Reservation Date Retrieved: " + cabinReservation.getStartDate());
        assertEquals(LocalDate.of(2025, 7, 10), cabinReservation.getStartDate());
    } // End testCabinGetReservationDate method

    /**
     * Tests if the cabin reservation returns the correct number of nights.
     */
    @Test
    public void testCabinGetNumNights() {
        System.out.println("Cabin Number of Nights Entered: 7");
        System.out.println("Cabin Number of Nights Retrieved: " + cabinReservation.getNumNights());
        assertEquals(7, cabinReservation.getNumNights());
    } // End testCabinGetNumNights method

    /**
     * Tests if the cabin reservation returns the correct number of beds.
     */
    @Test
    public void testCabinGetNumBeds() {
        System.out.println("Cabin Beds Entered: 3");
        System.out.println("Cabin Beds Retrieved: " + cabinReservation.getNumBeds());
        assertEquals(3, cabinReservation.getNumBeds());
    } // End testCabinGetNumBeds method

    /**
     * Tests if the cabin reservation returns the correct number of bathrooms.
     */
    @Test
    public void testCabinGetNumBedrooms() {
        System.out.println("Cabin Bedrooms Entered: 2");
        System.out.println("Cabin Bedrooms Retrieved: " + cabinReservation.getNumBedrooms());
        assertEquals(2, cabinReservation.getNumBedrooms());
    } // End testCabinGetNumBedrooms method

    /**
     * Tests if the cabin reservation returns the correct number of bathrooms.
     */
    @Test
    public void testCabinGetNumBathrooms() {
        System.out.println("Cabin Bathrooms Entered: 2");
        System.out.println("Cabin Bathrooms Retrieved: " + cabinReservation.getNumBathrooms());
        assertEquals(2, cabinReservation.getNumBathrooms());
    } // End testCabinGetNumBathrooms method

    /**
     * Tests if the cabin reservation returns the correct lodging size.
     */
    @Test
    public void testCabinGetLodgingSize() {
        System.out.println("Cabin Lodging Size Entered: 800 sqft");
        System.out.println("Cabin Lodging Size Retrieved: " + cabinReservation.getLodgingSizeSqFt() + " sqft");
        assertEquals(800, cabinReservation.getLodgingSizeSqFt());
    } // End testCabinGetLodgingSize method

    /**
     * Tests if the cabin reservation returns the correct price.
     */
    @Test
    public void testCabinGetLodgingPrice() {
        System.out.printf("Cabin Lodging Price Per Night Entered: $%.2f%n", 200.00);
        System.out.printf("Cabin Lodging Price Per Night Retrieved: $%.2f%n", cabinReservation.getLodgingPrice());
        assertEquals(200.00, cabinReservation.getLodgingPrice(), 0.01);
    } // End testCabinGetLodgingPrice method

    /**
     * Tests if the cabin has a full kitchen available.
     */
    @Test
    public void testCabinHasFullKitchen() {
        System.out.println("Cabin Kitchen Available: true");
        System.out.println("Retrieved: " + cabinReservation.isFullKitchenAvailable());
        assertTrue(cabinReservation.isFullKitchenAvailable());
    } // End testCabinHasFullKitchen method

    /**
     * Tests if the cabin has a loft available.
     */
    @Test
    public void testCabinHasLoft() {
        System.out.println("Cabin Kitchen Available: true");
        System.out.println("Retrieved: " + cabinReservation.isLoftAvailable());
        assertTrue(cabinReservation.isLoftAvailable());
    } // End testCabinHasLoft method


    // ====================== HOTEL TESTS ======================

    /**
     * Tests if the Hotel reservation returns the correct account number.
     */
    @Test
    public void testHotelGetReservationNumber() {
        System.out.println("Hotel Reservation Number Entered: res-HOT10000000");
        System.out.println("Hotel Reservation Number Retrieved: " + hotelReservation.getReservationNumber());
        assertEquals("res-HOT10000000", hotelReservation.getReservationNumber());
    } // End testHotelGetReservationNumber method

    /**
     * Tests if the Hotel reservation returns the correct reservation number.
     */
    @Test
    public void testHotelGetAccountNumber() {
        System.out.println("Hotel Account Number Entered: A100000001");
        System.out.println("Hotel Account Number Retrieved: " + hotelReservation.getAccountNumber());
        assertEquals("A100000001", hotelReservation.getAccountNumber());
    } // End testHotelGetAccountNumber method

    /**
     * Tests if the Hotel reservation returns the correct physical address.
     */
    @Test
    public void testHotelGetPhysicalAddress() {
        System.out.println("Hotel Physical Address Entered: 43-179 Day Mountain Road, Temple, ME, 04984");
        System.out.println("Hotel Physical Address Retrieved: " + hotelReservation.getLodgingPhysicalAddress());
        assertEquals("43-179 Day Mountain Road", hotelReservation.getLodgingPhysicalAddress().getStreet());
        assertEquals("Temple", hotelReservation.getLodgingPhysicalAddress().getCity());
        assertEquals("ME", hotelReservation.getLodgingPhysicalAddress().getState());
        assertEquals(4984, hotelReservation.getLodgingPhysicalAddress().getZipCode());
    } // End testHotelGetPhysicalAddress method

    /**
     * Tests if the Hotel reservation returns the correct date.
     */
    @Test
    public void testHotelGetReservationDate() {
        System.out.println("Hotel Reservation Date Entered: 2025-06-15");
        System.out.println("Hotel Reservation Date Retrieved: " + hotelReservation.getStartDate());
        assertEquals(LocalDate.of(2025, 6, 15), hotelReservation.getStartDate());
    } // End testHotelGetReservationDate method

    /**
     * Tests if the Hotel reservation returns the correct number of nights.
     */
    @Test
    public void testHotelGetNumNights() {
        System.out.println("Hotel Number of Nights Entered: 5");
        System.out.println("Hotel Number of Nights Retrieved: " + hotelReservation.getNumNights());
        assertEquals(5, hotelReservation.getNumNights());
    } // End testHotelGetNumNights method

    /**
     * Tests if the hotel reservation returns the correct number of beds.
     */
    @Test
    public void testHotelGetNumBeds() {
        System.out.println("Hotel Beds Entered: 2");
        System.out.println("Hotel Beds Retrieved: " + hotelReservation.getNumBeds());
        assertEquals(2, hotelReservation.getNumBeds());
    } // End testHotelGetNumBeds method

    /**
     * Tests if the hotel reservation returns the correct number of bedrooms.
     */
    @Test
    public void testHotelGetNumBedrooms() {
        System.out.println("Hotel Bedrooms Entered: 1");
        System.out.println("Hotel Bedrooms Retrieved: " + hotelReservation.getNumBedrooms());
        assertEquals(1, hotelReservation.getNumBedrooms());
    } // End testHotelGetNumBedrooms method

    /**
     * Tests if the hotel reservation returns the correct number of bathrooms.
     */
    @Test
    public void testHotelGetNumBathrooms() {
        System.out.println("Hotel Bathrooms Entered: 1");
        System.out.println("Hotel Bathrooms Retrieved: " + hotelReservation.getNumBathrooms());
        assertEquals(1, hotelReservation.getNumBathrooms());
    } // End testHotelGetNumBathrooms method

    /**
     * Tests if the hotel reservation returns the correct lodging size in square feet.
     */
    @Test
    public void testHotelGetLodgingSizeSqFt() {
        System.out.println("Hotel Square Footage Entered: 500 sqft");
        System.out.println("Hotel Square Footage Retrieved: " + hotelReservation.getLodgingSizeSqFt());
        assertEquals(500, hotelReservation.getLodgingSizeSqFt());
    } // End testHotelGetLodgingSizeSqFt method

    /**
     * Tests if the Hotel reservation returns the correct price.
     */
    @Test
    public void testHotelGetLodgingPrice() {
        System.out.printf("Hotel Lodging Price Per Night Entered: $%.2f%n", 150.00);
        System.out.printf("Hotel Lodging Price Per Night Retrieved: $%.2f%n", hotelReservation.getLodgingPrice());
        assertEquals(150.00, hotelReservation.getLodgingPrice(), 0.01);
    } // End testHotelGetLodgingPrice method

    /**
     * Tests if the Hotel has a full kitchenette available.
     */
    @Test
    public void testHotelHasFullKitchenette() {
        System.out.println("Hotel Kitchenette Available: true");
        System.out.println("Hotel Kitchenette Retrieved: " + hotelReservation.hasKitchenette());
        assertTrue(hotelReservation.hasKitchenette());
    } // End testHotelHasFullKitchen method


// ====================== HOUSE TESTS ======================

    /**
     * Tests if the Hotel reservation returns the correct account number.
     */
    @Test
    public void testHouseGetReservationNumber() {
        System.out.println("House Reservation Number Entered: res-HOU10000000");
        System.out.println("House Reservation Number Retrieved: " + houseReservation.getReservationNumber());
        assertEquals("res-HOU10000000", houseReservation.getReservationNumber());
    } // End testHouseGetReservationNumber method

    /**
     * Tests if the House reservation returns the correct reservation number.
     */
    @Test
    public void testHouseGetAccountNumber() {
        System.out.println("House Account Number Entered: A100000002");
        System.out.println("House Account Number Retrieved: " + houseReservation.getAccountNumber());
        assertEquals("A100000002", houseReservation.getAccountNumber());
    } // End testHouseGetAccountNumber method

    /**
     * Tests if the House reservation returns the correct physical address.
     */
    @Test
    public void testHouseGetPhysicalAddress() {
        System.out.println("House Physical Address Entered: 43-179 Day Mountain Road, Temple, ME, 04984");
        System.out.println("House Physical Address Retrieved: " + houseReservation.getLodgingPhysicalAddress());
        assertEquals("43-179 Day Mountain Road", houseReservation.getLodgingPhysicalAddress().getStreet());
        assertEquals("Temple", houseReservation.getLodgingPhysicalAddress().getCity());
        assertEquals("ME", houseReservation.getLodgingPhysicalAddress().getState());
        assertEquals(4984, houseReservation.getLodgingPhysicalAddress().getZipCode());
    } // End testHouseGetPhysicalAddress method

    /**
     * Tests if the House reservation returns the correct date.
     */
    @Test
    public void testHouseGetReservationDate() {
        System.out.println("House Reservation Date Entered: 2025-08-01");
        System.out.println("House Reservation Date Retrieved: " + houseReservation.getStartDate());
        assertEquals(LocalDate.of(2025, 8, 1), houseReservation.getStartDate());
    } // End testHouseGetReservationDate method

    /**
     * Tests if the House reservation returns the correct number of nights.
     */
    @Test
    public void testHouseGetNumNights() {
        System.out.println("House Number of Nights Entered: 10");
        System.out.println("House Number of Nights Retrieved: " + houseReservation.getNumNights());
        assertEquals(10, houseReservation.getNumNights());
    } // End testHouseGetNumNights method

    /**
     * Tests if the House reservation returns the correct number of beds.
     */
    @Test
    public void testHouseGetNumBeds() {
        System.out.println("House Beds Entered: 4");
        System.out.println("House Beds Retrieved: " + houseReservation.getNumBeds());
        assertEquals(4, houseReservation.getNumBeds());
    } // End testHouseGetNumBeds method

    /**
     * Tests if the house reservation returns the correct number of bedrooms.
     */
    @Test
    public void testHouseGetNumBedrooms() {
        System.out.println("House Bedrooms Entered: 3");
        System.out.println("House Bedrooms Retrieved: " + houseReservation.getNumBedrooms());
        assertEquals(3, houseReservation.getNumBedrooms());
    } // End testHouseGetNumBedrooms method

    /**
     * Tests if the House reservation returns the correct number of bathrooms.
     */
    @Test
    public void testHouseGetNumBathrooms() {
        System.out.println("House Bathrooms Entered: 3");
        System.out.println("House Bathrooms Retrieved: " + houseReservation.getNumBathrooms());
        assertEquals(3, houseReservation.getNumBathrooms());
    } // End testHouseGetNumBathrooms method

    /**
     * Tests if the house reservation returns the correct lodging size in square feet.
     */
    @Test
    public void testHouseGetLodgingSizeSqFt() {
        System.out.println("House Square Footage Entered: 1200 sqft");
        System.out.println("House Square Footage Retrieved: " + houseReservation.getLodgingSizeSqFt());
        assertEquals(1200, houseReservation.getLodgingSizeSqFt());
    } // End testHouseGetLodgingSizeSqFt method

    /**
     * Tests if the House reservation returns the correct price.
     */
    @Test
    public void testHouseGetLodgingPrice() {
        System.out.printf("House Lodging Price Per Night Entered: $%.2f%n", 300.00);
        System.out.printf("House Lodging Price Per Night Retrieved: $%.2f%n", houseReservation.getLodgingPrice());
        assertEquals(300.00, houseReservation.getLodgingPrice(), 0.01);
    } // End testHouseGetLodgingPrice method

    /**
     * Tests if the house reservation returns the correct number of floors.
     */
    @Test
    public void testHouseGetNumFloors() {
        System.out.println("House Floors Entered: 2");
        System.out.println("House Floors Retrieved: " + houseReservation.getNumFloors());
        assertEquals(2, houseReservation.getNumFloors());
    } // End testHouseGetNumFloors method

} // End ReservationTest class
