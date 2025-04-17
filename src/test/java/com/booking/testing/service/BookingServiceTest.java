package com.booking.testing.service;

import com.booking.testing.dto.BookingDTO;
import com.booking.testing.model.BookingEntity;
import com.booking.testing.model.LocationEntity;
import com.booking.testing.repository.BookingRepository;
import com.booking.testing.repository.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private BookingService bookingService;

    private LocationEntity availableLocation;
    private LocationEntity unavailableLocation;
    private BookingEntity bookingEntity;

    @BeforeEach
    void setUp() {
        // Setup available location
        availableLocation = new LocationEntity();
        availableLocation.setId(1L);
        availableLocation.setName("Test Location");
        availableLocation.setAvailable(true);

        // Setup unavailable location
        unavailableLocation = new LocationEntity();
        unavailableLocation.setId(2L);
        unavailableLocation.setName("Unavailable Location");
        unavailableLocation.setAvailable(false);

        // Setup booking entity
        bookingEntity = new BookingEntity();
        bookingEntity.setId(1L);
        bookingEntity.setLocationId(1L);
        bookingEntity.setUserId("user1");
        bookingEntity.setBookingDate("2024-04-16");
        bookingEntity.setBookingStatus(BookingEntity.PENDING);
    }

    @Test
    void bookLocation_Success() {
        // Arrange
        when(locationRepository.findById(1L)).thenReturn(Optional.of(availableLocation));
        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(bookingEntity);

        // Act
        BookingDTO result = bookingService.bookLocation("user1", "1", "2024-04-16");

        // Assert
        assertNotNull(result);
        assertEquals("1", result.getLocationId());
        assertEquals("user1", result.getUserId());
        assertEquals("2024-04-16", result.getBookingDate());
        assertEquals(BookingEntity.PENDING, result.getStatus());
        
        verify(locationRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1)).save(any(BookingEntity.class));
    }

    @Test
    void bookLocation_LocationNotFound() {
        // Arrange
        when(locationRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookingService.bookLocation("user1", "999", "2024-04-16");
        });

        assertEquals("Location not found", exception.getMessage());
        verify(locationRepository, times(1)).findById(999L);
        verify(bookingRepository, never()).save(any(BookingEntity.class));
    }

    @Test
    void bookLocation_LocationNotAvailable() {
        // Arrange
        when(locationRepository.findById(2L)).thenReturn(Optional.of(unavailableLocation));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookingService.bookLocation("user1", "2", "2024-04-16");
        });

        assertEquals("Location not available", exception.getMessage());
        verify(locationRepository, times(1)).findById(2L);
        verify(bookingRepository, never()).save(any(BookingEntity.class));
    }

    @Test
    void getBookingHistory_Success() {
        // Arrange
        List<BookingEntity> bookings = Arrays.asList(bookingEntity);
        when(bookingRepository.findByUserId("user1")).thenReturn(bookings);

        // Act
        List<BookingDTO> result = bookingService.getBookingHistory("user1");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getLocationId());
        assertEquals("user1", result.get(0).getUserId());
        
        verify(bookingRepository, times(1)).findByUserId("user1");
    }

    @Test
    void getBookingHistory_EmptyList() {
        // Arrange
        when(bookingRepository.findByUserId("user1")).thenReturn(Arrays.asList());

        // Act
        List<BookingDTO> result = bookingService.getBookingHistory("user1");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookingRepository, times(1)).findByUserId("user1");
    }
} 