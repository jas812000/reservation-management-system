package com.swen_646_project_1.TestPackage;

import com.swen_646_project_1.Account;
import com.swen_646_project_1.exceptions.IllegalState_Exception;

public class CancelReservationTest extends BaseTest {
    public static void testCancelReservation() {
        String accountNumber = TestHelper.selectAccount();
        if (accountNumber == null) return;

        Account account = manager.getAccount(accountNumber);
        if (account == null) {
            System.out.println("Invalid account number.");
            return;
        }

        String reservationNumber = TestHelper.selectReservation(account);
        if (reservationNumber == null) return;

        try {
            account.cancelReservation(reservationNumber);
            System.out.println("Reservation cancelled successfully.");
        } catch (IllegalState_Exception e) {
            System.out.println("Error cancelling reservation: " + e.getMessage());
        }
    }
}
