package com.booking.testing.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Welcome to the Booking Service API! Available endpoints: /api/booking/book, /api/booking/history, /api/booking/update-status";
    }
} 