package com.jamesstevens.rms.enums;

/**
 * Represents the possible lifecycle states of a reservation.
 * <p>
 * This enum is used to track the current status of a reservation
 * from creation through completion or cancellation.
 * </p>
 */
public enum ReservationStatus {

    /**
     * The reservation has been created but not yet finalized.
     */
    DRAFT,

    /**
     * The reservation has been fully completed and confirmed.
     */
    COMPLETED,

    /**
     * The reservation has been cancelled and is no longer active.
     */
    CANCELLED
}


