// Declares the package name for the project, grouping related classes together.
package com.jamesstevens.rms.enums;

/**
 * Enum representing reservation statuses.
 */
public enum ReservationStatus {
    DRAFT,          // The reservation is in draft mode and has not been finalized.
    COMPLETED,      // The reservation has been completed successfully.
    CANCELLED      // The reservation has been cancelled and is no longer active.
} // End Enum
