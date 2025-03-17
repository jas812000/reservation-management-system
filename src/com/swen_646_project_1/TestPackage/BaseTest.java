// Imports all classes from the TestPackage inside the com.swen_646_project_1 package.
package com.swen_646_project_1.TestPackage;

/*
 * Imports the `Manager` class for managing accounts and reservations.
 * - `Manager`: Provides system-wide management of accounts and reservations.
 */
import com.swen_646_project_1.Manager;

/**
 * Base test class to provide a shared `Manager` instance.
 * This ensures that all test cases use the same instance of `Manager`,
 * allowing consistent data access and reducing redundant object creation.
 */
public class BaseTest {
    // Shared instance of `Manager` for all tests that extend `BaseTest`
    protected static final Manager manager = TestManager.getManager();
} // End BaseTest class
