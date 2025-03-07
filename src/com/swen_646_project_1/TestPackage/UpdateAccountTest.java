package com.swen_646_project_1.TestPackage;

import com.swen_646_project_1.exceptions.IllegalSave_Exception;

public class UpdateAccountTest {
    public static void testUpdateExistingAccount() {
        var manager = TestHelper.getManager();

        String accountNumber = TestHelper.selectAccount();
        if (accountNumber == null) return;

        try {
            manager.updateAccount(accountNumber);
            System.out.println("Account " + accountNumber + " updated successfully.");
        } catch (IllegalSave_Exception e) {
            System.out.println("Error updating account: " + e.getMessage());
        }
    }
}
