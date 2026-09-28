package com.jamesstevens.rms.tests;

import com.jamesstevens.rms.Account;
import com.jamesstevens.rms.Address;
import com.jamesstevens.rms.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.jamesstevens.rms.exceptions.IllegalParameterException;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;
import com.jamesstevens.rms.exceptions.DuplicateObjectException;
import com.jamesstevens.rms.exceptions.IllegalLoadException;
import com.jamesstevens.rms.reservation.CabinReservation;

import java.time.LocalDate;

/**
 * Unit tests for {@link Account} account profile updates.
 */
public class AccountTest {

    @TempDir
    Path tempDir;

    private Account account;

    /**
     * Creates a baseline account before each test.
     */
    @BeforeEach
    public void setUp() {
        System.setProperty("RMS_DATA_DIR", tempDir.toString());

        Name name = new Name("James", "Stevens");
        Address address = new Address("123 Main St", "New York", "NY", "10001");

        account = new Account(
                "A900000000",
                name,
                address,
                "123-456-7890",
                "test@example.com"
        );
    }

    /**
     * Verifies that updating an account address works using both overloads:
     * updating via {@link Address} and updating via individual address fields.
     */
    @Test
    public void testUpdateAddress() {
        Address newAddress = new Address("456 Elm St", "Los Angeles", "CA", "90001");
        account.updateAddress(newAddress);

        assertEquals("456 Elm St", account.getAddress().getStreet());
        assertEquals("Los Angeles", account.getAddress().getCity());
        assertEquals("CA", account.getAddress().getState());
        assertEquals("90001", account.getAddress().getZipCode());

        Address secondUpdate = new Address("789 Pine St", "San Francisco", "CA", "94102");
        account.updateAddress(
                secondUpdate.getStreet(),
                secondUpdate.getCity(),
                secondUpdate.getState(),
                secondUpdate.getZipCode()
        );

        assertEquals("789 Pine St", account.getAddress().getStreet());
        assertEquals("San Francisco", account.getAddress().getCity());
        assertEquals("CA", account.getAddress().getState());
        assertEquals("94102", account.getAddress().getZipCode());
    }

    @Test
    public void testAccountNumberIsNormalized() {
        Name name = new Name("James", "Stevens");
        Address address = new Address("123 Main St", "New York", "NY", "10001");

        Account normalizedAccount = new Account(
                "  a900000001  ",
                name,
                address,
                "123-456-7890",
                "test@example.com"
        );

        assertEquals("A900000001", normalizedAccount.getAccountNumber());
    }

    @Test
    public void testBlankAccountNumberIsRejected() {
        Name name = new Name("James", "Stevens");
        Address address = new Address("123 Main St", "New York", "NY", "10001");

        assertThrows(
                IllegalParameterException.class,
                () -> new Account(
                        "   ",
                        name,
                        address,
                        "123-456-7890",
                        "test@example.com"
                )
        );
    }

    @Test
    public void testNullAccountNumberIsRejected() {
        Name name = new Name("James", "Stevens");
        Address address = new Address("123 Main St", "New York", "NY", "10001");

        assertThrows(
                IllegalParameterException.class,
                () -> new Account(
                        null,
                        name,
                        address,
                        "123-456-7890",
                        "test@example.com"
                )
        );
    }

    @Test
    public void testPhoneNumberIsTrimmed() {
        account.setPhoneNumber("  123-456-7890  ");

        assertEquals("123-456-7890", account.getPhoneNumber());
    }

    @Test
    public void testPhoneNumberWithTenDigitsIsAccepted() {
        account.setPhoneNumber("(123) 456-7890");

        assertEquals("(123) 456-7890", account.getPhoneNumber());
    }

    @Test
    public void testInvalidPhoneNumberIsRejected() {
        assertThrows(
                IllegalParameterException.class,
                () -> account.setPhoneNumber("123-456-789")
        );
    }

    @Test
    public void testBlankPhoneNumberIsRejected() {
        assertThrows(
                IllegalParameterException.class,
                () -> account.setPhoneNumber("   ")
        );
    }

    @Test
    public void testNullPhoneNumberIsRejected() {
        assertThrows(
                IllegalParameterException.class,
                () -> account.setPhoneNumber(null)
        );
    }

    @Test
    public void testEmailIsTrimmed() {
        account.setEmail("  user@example.com  ");

        assertEquals("user@example.com", account.getEmail());
    }

    @Test
    public void testValidEmailIsAccepted() {
        account.setEmail("james.stevens@example.com");

        assertEquals("james.stevens@example.com", account.getEmail());
    }

    @Test
    public void testEmailWithoutAtSymbolIsRejected() {
        assertThrows(
                IllegalParameterException.class,
                () -> account.setEmail("userexample.com")
        );
    }

    @Test
    public void testEmailWithoutDomainSuffixIsRejected() {
        assertThrows(
                IllegalParameterException.class,
                () -> account.setEmail("user@example")
        );
    }

    @Test
    public void testBlankEmailIsRejected() {
        assertThrows(
                IllegalParameterException.class,
                () -> account.setEmail("   ")
        );
    }

    @Test
    public void testNullEmailIsRejected() {
        assertThrows(
                IllegalParameterException.class,
                () -> account.setEmail(null)
        );
    }

    /**
     * Verifies that an account rejects a reservation belonging to a different account.
     */
    @Test
    public void testAddReservationRejectsDifferentAccount() {
        Address lodgingAddress = new Address(
                "10 Main St",
                "Bangor",
                "ME",
                "04401"
        );

        CabinReservation reservation = new CabinReservation(
                "res-CAB90000001",
                "A900000001",
                lodgingAddress,
                lodgingAddress,
                LocalDate.of(2026, 10, 1),
                3,
                2,
                1,
                1,
                600,
                150.0,
                true,
                false
        );

        assertThrows(
                IllegalParameterException.class,
                () -> account.addReservation(reservation)
        );
    }

    /**
     * Verifies that an account rejects a duplicate reservation.
     */
    @Test
    public void testAddReservationRejectsDuplicate() {
        Address lodgingAddress = new Address(
                "10 Main St",
                "Bangor",
                "ME",
                "04401"
        );

        CabinReservation reservation = new CabinReservation(
                "res-CAB90000001",
                account.getAccountNumber(),
                lodgingAddress,
                lodgingAddress,
                LocalDate.of(2026, 10, 1),
                3,
                2,
                1,
                1,
                600,
                150.0,
                true,
                false
        );

        account.addReservation(reservation);

        assertThrows(
                DuplicateObjectException.class,
                () -> account.addReservation(reservation)
        );
    }

    /**
     * Verifies that malformed persisted account data is reported as a load failure
     * while preserving the underlying validation exception.
     */
    @Test
    public void testMalformedPersistedAccountPreservesCause() {
        IllegalLoadException exception = assertThrows(
                IllegalLoadException.class,
                () -> Account.fromString(
                        "A900000001,James,Stevens,10 Main St,Bangor,ME,INVALID,207-555-1234,user@example.com"
                )
        );

        assertInstanceOf(IllegalParameterException.class, exception.getCause());
    }

    @Test
    public void testSerializationRoundTrip() {
        Name name = new Name("James", "Santiago-Martinez");
        Address address = new Address("10 Main St", "Bangor", "ME", "04401");

        Account original = new Account(
                "a900000001",
                name,
                address,
                "(207) 555-1234",
                "user@example.com"
        );

        Account restored = Account.fromString(original.toString());

        assertEquals(original.getAccountNumber(), restored.getAccountNumber());
        assertEquals(original.getName().getFirstName(), restored.getName().getFirstName());
        assertEquals(original.getName().getLastName(), restored.getName().getLastName());
        assertEquals(original.getAddress().getStreet(), restored.getAddress().getStreet());
        assertEquals(original.getAddress().getCity(), restored.getAddress().getCity());
        assertEquals(original.getAddress().getState(), restored.getAddress().getState());
        assertEquals(original.getAddress().getZipCode(), restored.getAddress().getZipCode());
        assertEquals(original.getPhoneNumber(), restored.getPhoneNumber());
        assertEquals(original.getEmail(), restored.getEmail());
    }
}
