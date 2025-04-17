package com.booking.testing.controller;

import com.booking.testing.dto.BookingDTO;
import com.booking.testing.model.BookingEntity;
import com.booking.testing.repository.BookingRepository;
import com.booking.testing.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    private final BookingService bookingService;
    private final BookingRepository bookingRepository;

    @Autowired
    public BookingController(BookingService bookingService, BookingRepository bookingRepository) {
        this.bookingService = bookingService;
        this.bookingRepository = bookingRepository;
    }

    @PostMapping("/book")
    public BookingDTO bookLocation(@RequestParam String locationId, @RequestParam String userId, @RequestParam String bookingDate) {
        return bookingService.bookLocation(userId, locationId, bookingDate);
    }

    @GetMapping("/history")
    public List<BookingDTO> getBookingHistory(@RequestParam String userId) {
        return bookingService.getBookingHistory(userId);
    }

    @PatchMapping("/update-status")
    public void updateBookingStatus(@RequestParam String bookingId, @RequestParam int status) {
        bookingService.updateBookingStatus(bookingId, status );
    }
}