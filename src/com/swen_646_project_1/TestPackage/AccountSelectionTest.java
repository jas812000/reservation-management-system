package com.swen_646_project_1.TestPackage;

import com.swen_646_project_1.Account;
import com.swen_646_project_1.Manager;
import com.swen_646_project_1.exceptions.DuplicateObject_Exception;
import com.swen_646_project_1.Address;
import java.util.*;

/**
 * Test class for creating or selecting an account.
 */
public class AccountSelectionTest {

    /**
     * Tests creating or selecting an account.
     * If accounts exist, allows user to select one, create a new one, or exit.
     * New accounts start at ACC100 and increment sequentially.
     */
    public static void testCreateOrSelectAccount() {
        System.out.println("\n>> Test: Create or Select an Account");

        Scanner scanner = new Scanner(System.in);
        Manager manager = new Manager();

        // Fetch and sort accounts numerically
        List<Account> accounts = new ArrayList<>(manager.getAccounts());
        accounts.sort(Comparator.comparingInt(a -> Integer.parseInt(a.getAccountNumber().replaceAll("[^0-9]", ""))));

        String selectedAccountNumber = null;

        while (true) {
            // If accounts exist, display them and prompt user for selection
            if (!accounts.isEmpty()) {
                System.out.println("\nExisting Accounts:");
                for (Account acc : accounts) {
                    System.out.println("- " + acc.getAccountNumber());
                }
            } else {
                System.out.println("\nNo existing accounts found.");
            }

            System.out.print("\nEnter an account number, type 'new' to create one, or 'exit' to leave: ");
            selectedAccountNumber = scanner.nextLine().trim();

            if (selectedAccountNumber.equalsIgnoreCase("exit")) {
                System.out.println("Exiting account selection.");
                return;
            }

            if (selectedAccountNumber.equalsIgnoreCase("new")) {
                selectedAccountNumber = generateNewAccountNumber(accounts);
                break;
            }

            // Validate selection
            String finalSelectedAccountNumber = selectedAccountNumber;
            boolean accountExists = accounts.stream()
                    .anyMatch(acc -> acc.getAccountNumber().equals(finalSelectedAccountNumber));

            if (!accountExists) {
                System.out.println("Invalid selection. Please try again.");
            } else {
                break;
            }
        }

        // If the account doesn't exist yet, create it
        if (manager.getAccount(selectedAccountNumber) == null) {
            try {
                // Prompt user for account details
                System.out.println("\nCreating new account: " + selectedAccountNumber);

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

                // Create and add the new account
                Address address = new Address(street, city, state, zipCode);
                Account newAccount = new Account(selectedAccountNumber, address, phoneNumber, email);

                manager.addAccount(newAccount);
                System.out.println("\nAccount " + selectedAccountNumber + " successfully created.");
            } catch (DuplicateObject_Exception e) {
                System.out.println("Account " + selectedAccountNumber + " already exists.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid ZIP code. Please enter a numeric value.");
            } catch (Exception e) {
                System.out.println("Exception in Account Creation: " + e.getMessage());
            }
        } else {
            System.out.println("\nUsing existing account: " + selectedAccountNumber);
        }

        // Refresh and sort accounts again after modification
        accounts = new ArrayList<>(manager.getAccounts());
        accounts.sort(Comparator.comparingInt(a -> Integer.parseInt(a.getAccountNumber().replaceAll("[^0-9]", ""))));

        System.out.println("\nFinal Account List (Sorted):");
        for (Account acc : accounts) {
            System.out.println("- " + acc.getAccountNumber());
        }
    }

    /**
     * Generates the next available account number, starting from ACC100.
     * @param accounts List of existing accounts
     * @return The next available account number (e.g., ACC101, ACC102, etc.)
     */
    private static String generateNewAccountNumber(List<Account> accounts) {
        int accountCounter = 100; // Start at ACC100

        // Find the highest account number and increment
        for (Account acc : accounts) {
            String accNum = acc.getAccountNumber().replaceAll("[^0-9]", ""); // Extract numeric part
            try {
                int num = Integer.parseInt(accNum);
                if (num >= accountCounter) {
                    accountCounter = num + 1; // Increment to the next available number
                }
            } catch (NumberFormatException ignored) {
                // Ignore malformed account numbers
            }
        }

        return "ACC" + accountCounter;
    }
}
