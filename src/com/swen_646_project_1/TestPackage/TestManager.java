package com.swen_646_project_1.TestPackage;

import com.swen_646_project_1.Manager;

/**
 * Singleton class to provide a shared Manager instance across tests.
 */
public class TestManager {
    private static final Manager manager = new Manager();

    /**
     * Returns the shared Manager instance.
     * @return The Manager instance.
     */
    public static Manager getManager() {
        return manager;
    }
}

