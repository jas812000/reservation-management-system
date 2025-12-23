package com.jamesstevens.rms.tests;

import com.jamesstevens.rms.Manager;

/**
 * Provides a shared {@link Manager} instance for interactive/manual test classes.
 * <p>
 * This class lazily initializes a single {@code Manager} instance. It does not repeatedly
 * reload storage because {@link Manager#getAccounts()} already triggers a reload in your
 * current implementation.
 * </p>
 */
public final class TestManager {

    private static Manager manager;

    private TestManager() {
        // Utility class; no instances
    }

    /**
     * Returns the shared {@link Manager} instance for interactive/manual tests.
     * <p>
     * A {@link Manager} loads persisted state during construction. Since your
     * {@link Manager#getAccounts()} method already reloads from disk, this method avoids
     * extra reload calls to prevent redundant I/O and confusing test behavior.
     * </p>
     *
     * @return shared manager instance
     */
    public static Manager getManager() {
        if (manager == null) {
            manager = new Manager();
        }
        return manager;
    }
}
