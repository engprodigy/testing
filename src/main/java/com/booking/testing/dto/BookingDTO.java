package com.booking.testing.dto;


import com.booking.testing.model.BookingEntity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookingDTO {
    private Long bookingId;
    private String locationId;
    private String userId;
    private String bookingDate;
    private Integer status;


    public BookingDTO(BookingEntity booking) {
        this.bookingId = booking.getId();
        this.locationId = booking.getLocationId().toString();
        this.userId = booking.getUserId();
        this.bookingDate = booking.getBookingDate();
        this.status = booking.getBookingStatus();
    }
}
