// Imports all classes from the TestPackage inside the com.swen_646_project_1 package.
package com.jamesstevens.rms.tests;

/*
 * Imports the `Manager` class for managing accounts and reservations.
 * - `Manager`: Provides system-wide management of accounts and reservations.
 */
import com.jamesstevens.rms.Manager;

/*
 * Singleton class to provide a shared `Manager` instance across tests.
 * The `TestManager` class ensures that all test cases use the same instance of
 * `Manager` to maintain consistency in account and reservation management.
 */
public class TestManager {
    // Creates an instance of manager
    private static Manager manager = null;

    /**
     * Returns the shared Manager instance.
     * Ensures only one instance is used throughout the application.
     * @return The Manager instance.
     */
    public static Manager getManager() {
        if (manager == null) {
            manager = new Manager(); // Create only if not already initialized
        } // End if statement

        // Ensure accounts are loaded
        if (manager.getAccounts().isEmpty()) {
            System.out.println("Reloading accounts to ensure consistency...");
            manager.reloadAccounts();
        } // End if statement

        // Check again after reload, if still empty, warn the user
        if (manager.getAccounts().isEmpty()) {
            System.out.println("Warning: No accounts were loaded after reloading storage.");
        }  // End if statement

        return manager;
    } // End getManager method
} // End TestManager class

