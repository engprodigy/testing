package com.booking.testing.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class BookingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer bookingStatus;
    private String cancellationReason;
    private Long locationId;
    private String userId;
    private String bookingDate;

    public static final int PENDING = 1;
    public static final int UPCOMING = 2;
    public static final int COMPLETED = 3;
    public static final int REJECTED_BY_HOST_BEFORE_ACCEPT = 4;
    public static final int REJECTED_BY_GUEST_BEFORE_ACCEPT = 5;
    public static final int CANCELLED_BY_HOST_AFTER_ACCEPT = 6;
    public static final int CANCELLED_BY_GUEST_AFTER_ACCEPT = 7;
}
