package com.swen_646_project_1.TestPackage;

import com.swen_646_project_1.Account;
import com.swen_646_project_1.exceptions.IllegalState_Exception;
import com.swen_646_project_1.exceptions.IllegalOperation_Exception;

public class UpdateReservationTest extends BaseTest {
    public static void testUpdateReservation() {
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
            account.updateReservation(reservationNumber, null); // Pass updated data
            System.out.println("Reservation updated successfully.");
        } catch (IllegalState_Exception | IllegalOperation_Exception e) {
            System.out.println("Error updating reservation: " + e.getMessage());
        }
    }
}
