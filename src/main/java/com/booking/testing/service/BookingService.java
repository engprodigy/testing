package com.booking.testing.service;

import com.booking.testing.dto.BookingDTO;
import com.booking.testing.model.BookingEntity;
import com.booking.testing.model.LocationEntity;
import com.booking.testing.repository.BookingRepository;
import com.booking.testing.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, LocationRepository locationRepository) {
        this.bookingRepository = bookingRepository;
        this.locationRepository = locationRepository;
    }

    public BookingDTO bookLocation(String userId, String locationId, String bookingDate) {
        LocationEntity location = locationRepository.findById(Long.parseLong(locationId))
                .orElseThrow(() -> new RuntimeException("Location not found"));

        if (!location.isAvailable()) {
            throw new RuntimeException("Location not available");
        }

        BookingEntity booking = new BookingEntity();
        booking.setLocationId(Long.parseLong(locationId));
        booking.setUserId(userId);
        booking.setBookingDate(bookingDate);
        booking.setBookingStatus(BookingEntity.PENDING);

        bookingRepository.save(booking);
        return new BookingDTO(booking);
    }

    public List<BookingDTO> getBookingHistory(String userId) {
        List<BookingEntity> bookings = bookingRepository.findByUserId(userId);
        return bookings.stream().map(BookingDTO::new).collect(Collectors.toList());
    }

    public void updateBookingStatus(String bookingId, int status) {
        Optional<BookingEntity> optionalBooking = bookingRepository.findById(bookingId);
        if (!optionalBooking.isPresent()) {
            throw new RuntimeException("Booking not found for ID: " + bookingId);
        }

        BookingEntity booking = optionalBooking.get();

        //if already saved booking status is same as status to update to return immediately
        if(booking.getBookingStatus() == status) { 
            return;
        }

        if (status == 6 || status == 7) {
            if (booking.getBookingStatus() == 3) {
                System.out.println("Booking ID " + booking.getId() + "has already beign completed. Completed "
                + "booking status cannot be changed to: " + status);
                throw new RuntimeException("Booking with Completed Status can not be changed : " + bookingId);
            } else {
                booking.setBookingStatus(status);
                System.out.println("Booking ID " + booking.getId() + " has been cancelled with status: " + status);
            }
        } else if (status == 1 || status == 2 || status == 4 || status == 5) {
            booking.setBookingStatus(status);
        } else {
            if (booking.getBookingStatus() == 6 || booking.getBookingStatus() == 7) {
                throw new RuntimeException("Booking with Cancelled Status can not be changed : " + bookingId);
            } else {
                booking.setBookingStatus(status);
            }
        }

        // Step 4: Save the updated booking
        bookingRepository.save(booking);
    }

}
