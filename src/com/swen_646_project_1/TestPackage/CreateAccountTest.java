package com.swen_646_project_1.TestPackage;

import com.swen_646_project_1.Account;
import com.swen_646_project_1.Address;
import com.swen_646_project_1.Manager;
import com.swen_646_project_1.exceptions.DuplicateObject_Exception;

import java.util.List;
import java.util.Scanner;

public class CreateAccountTest {

    public static void testCreateNewAccount() {
        Scanner scanner = new Scanner(System.in);
        Manager manager = new Manager();

        // Generate new account number
        List<Account> accounts = manager.getAccounts();
        String newAccountNumber = generateNewAccountNumber(accounts);

        System.out.println("\nCreating new account: " + newAccountNumber);
        System.out.print("Enter street address: ");
        String street = scanner.nextLine().trim();
        System.out.print("Enter city: ");
        String city = scanner.nextLine().trim();
        System.out.print("Enter state (e.g., CA, NY): ");
        String state = scanner.nextLine().trim();
        System.out.print("Enter ZIP code: ");
        int zipCode = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine().trim();
        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();

        try {
            Address address = new Address(street, city, state, zipCode);
            Account newAccount = new Account(newAccountNumber, address, phoneNumber, email);
            manager.addAccount(newAccount);
            System.out.println("Account " + newAccountNumber + " successfully created.");
        } catch (DuplicateObject_Exception e) {
            System.out.println("Account " + newAccountNumber + " already exists.");
        }
    }

    private static String generateNewAccountNumber(List<Account> accounts) {
        int accountCounter = 100;
        for (Account acc : accounts) {
            int num = Integer.parseInt(acc.getAccountNumber().replaceAll("[^0-9]", ""));
            if (num >= accountCounter) {
                accountCounter = num + 1;
            }
        }
        return "ACC" + accountCounter;
    }
}

