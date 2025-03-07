package com.swen_646_project_1.TestPackage;

import com.swen_646_project_1.Account;
import com.swen_646_project_1.Address;
import com.swen_646_project_1.exceptions.DuplicateObject_Exception;
import com.swen_646_project_1.reservation.HotelReservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {
    private Account account;
    private HotelReservation reservation;

    @BeforeEach
    public void setUp() {
        Address address = new Address("123 Main St", "New York", "NY", 10001);
        account = new Account("ACC100", address, "123-456-7890", "test@example.com");

        reservation = new HotelReservation(
                "RES123",
                "ACC100",
                address,
                address,
                LocalDate.of(2025, 6, 15),
                5,
                2,
                1,
                1,
                500,
                150.0,
                true  // Kitchenette available
        );
    }

    @Test
    public void testGetAllReservations() throws DuplicateObject_Exception {
        account.addReservation(reservation);
        List<com.swen_646_project_1.reservation.Reservation> reservations = account.getAllReservations();
        assertEquals(1, reservations.size());
        assertEquals("RES123", reservations.getFirst().getReservationNumber());
    }

    @Test
    public void testUpdateAddress() {
        // Test updating using Address object
        Address newAddress = new Address("456 Elm St", "Los Angeles", "CA", 90001);
        account.updateAddress(newAddress);

        assertEquals("456 Elm St", account.getAddress().getStreet());
        assertEquals("Los Angeles", account.getAddress().getCity());
        assertEquals("CA", account.getAddress().getState());
        assertEquals(90001, account.getAddress().getZipCode());

        // Test updating using individual fields
        account.updateAddress("789 Pine St", "San Francisco", "CA", 94102);

        assertEquals("789 Pine St", account.getAddress().getStreet());
        assertEquals("San Francisco", account.getAddress().getCity());
        assertEquals("CA", account.getAddress().getState());
        assertEquals(94102, account.getAddress().getZipCode());
    }
}
