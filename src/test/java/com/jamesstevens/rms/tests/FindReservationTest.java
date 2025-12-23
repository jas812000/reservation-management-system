package com.jamesstevens.rms.tests;

import com.jamesstevens.rms.Account;
import com.jamesstevens.rms.Address;
import com.jamesstevens.rms.Manager;
import com.jamesstevens.rms.exceptions.NullReservation_Exception;
import com.jamesstevens.rms.reservation.CabinReservation;
import com.jamesstevens.rms.reservation.HotelReservation;
import com.jamesstevens.rms.reservation.HouseReservation;
import com.jamesstevens.rms.reservation.Reservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for validating {@link Manager#findReservation(String, String)}.
 * <p>
 * These tests confirm that reservations can be located for an existing account and that a
 * missing reservation triggers {@link NullReservation_Exception}.
 * </p>
 */
public class FindReservationTest {

	private static final String TEST_ACCOUNT_NUMBER = "A900000000";

	private Manager manager;
	private Account testAccount;
	private CabinReservation testCabinReservation;
	private HotelReservation testHotelReservation;
	private HouseReservation testHouseReservation;

	@TempDir
	Path tempDir;

	/**
	 * Creates an isolated test data directory, persists a test account, and saves three reservations.
	 * <p>
	 * Cabin uses separate physical/mailing addresses. Hotel and House use the same address for both.
	 * </p>
	 */
	@BeforeEach
	public void setUp() {
		System.setProperty("RMS_DATA_DIR", tempDir.toString());

		manager = new Manager();
		manager.clearAccounts();

		Address physicalAddress = new Address("43-179 Day Mountain Road", "Temple", "ME", 4984);
		Address cabinMailingAddress = new Address("PO Box 43179", "Waterville", "ME", 4901);

		// Account mailing address is unrelated to lodging mailing address
		testAccount = new Account(TEST_ACCOUNT_NUMBER, cabinMailingAddress,
				"123-456-7890", "test@email.com");
		manager.addAccount(testAccount);

		testCabinReservation = new CabinReservation(
				"res-CAB900000000",
				TEST_ACCOUNT_NUMBER,
				physicalAddress,
				cabinMailingAddress, // Cabin may diverge
				LocalDate.of(2025, 12, 12),
				3,
				2,
				1,
				1,
				900,
				0.0,
				true,
				false
		);

		// Hotel: mailing must match physical
		testHotelReservation = new HotelReservation(
				"res-HOT900000000",
				TEST_ACCOUNT_NUMBER,
				physicalAddress,
				physicalAddress,
				LocalDate.of(2025, 12, 12),
				2,
				1,
				1,
				1,
				300,
				0.0,
				true
		);

		// House: mailing must match physical
		testHouseReservation = new HouseReservation(
				"res-HOU900000000",
				TEST_ACCOUNT_NUMBER,
				physicalAddress,
				physicalAddress,
				LocalDate.of(2025, 12, 12),
				4,
				3,
				2,
				2,
				2000,
				0.0,
				2
		);

		testAccount.addReservation(testCabinReservation);
		testAccount.addReservation(testHotelReservation);
		testAccount.addReservation(testHouseReservation);

		assertNotNull(manager.getAccount(TEST_ACCOUNT_NUMBER),
				"Test account should exist in manager.");
	}

	/**
	 * Verifies that a cabin reservation can be found for the test account.
	 */
	@Test
	public void testFindCabinReservation() {
		assertDoesNotThrow(() ->
				manager.findReservation(TEST_ACCOUNT_NUMBER,
						testCabinReservation.getReservationNumber())
		);

		Reservation found = testAccount.getReservation(
				testCabinReservation.getReservationNumber());
		assertNotNull(found, "Reservation should exist after findReservation.");
		assertEquals("res-CAB900000000", found.getReservationNumber());
		assertEquals(TEST_ACCOUNT_NUMBER, found.getAccountNumber());
	}

	/**
	 * Verifies that a hotel reservation can be found for the test account.
	 */
	@Test
	public void testFindHotelReservation() {
		assertDoesNotThrow(() ->
				manager.findReservation(TEST_ACCOUNT_NUMBER,
						testHotelReservation.getReservationNumber())
		);

		Reservation found = testAccount.getReservation(
				testHotelReservation.getReservationNumber());
		assertNotNull(found, "Reservation should exist after findReservation.");
		assertEquals("res-HOT900000000", found.getReservationNumber());
		assertEquals(TEST_ACCOUNT_NUMBER, found.getAccountNumber());
	}

	/**
	 * Verifies that a house reservation can be found for the test account.
	 */
	@Test
	public void testFindHouseReservation() {
		assertDoesNotThrow(() ->
				manager.findReservation(TEST_ACCOUNT_NUMBER,
						testHouseReservation.getReservationNumber())
		);

		Reservation found = testAccount.getReservation(
				testHouseReservation.getReservationNumber());
		assertNotNull(found, "Reservation should exist after findReservation.");
		assertEquals("res-HOU900000000", found.getReservationNumber());
		assertEquals(TEST_ACCOUNT_NUMBER, found.getAccountNumber());
	}

	/**
	 * Verifies that searching for a missing reservation throws {@link NullReservation_Exception}.
	 */
	@Test
	public void testFindNonExistentReservation() {
		NullReservation_Exception ex = assertThrows(
				NullReservation_Exception.class,
				() -> manager.findReservation(TEST_ACCOUNT_NUMBER, "res-NONEXISTENT")
		);

		assertTrue(ex.getMessage().contains("Reservation not found"));
	}
}
