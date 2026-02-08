package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BookingSystemTest {

    private TimeProvider timeProvider;
    private RoomRepository roomRepository;
    private NotificationService notificationService;
    private BookingSystem bookingSystem;

    @BeforeEach
    void setUp() {
        timeProvider = mock(TimeProvider.class);
        roomRepository = mock(RoomRepository.class);
        notificationService = mock(NotificationService.class);

        bookingSystem = new BookingSystem(timeProvider, roomRepository, notificationService);
    }

    @Test
    void shouldBookRoomSuccessfully() throws NotificationException {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = start.plusHours(1);

        Room room = mock(Room.class);
        when(timeProvider.getCurrentTime()).thenReturn(now);
        when(roomRepository.findById("R1")).thenReturn(Optional.of(room));
        when(room.isAvailable(start, end)).thenReturn(true);

        boolean result = bookingSystem.bookRoom("R1", start, end);

        assertThat(result).isTrue();
        verify(room).addBooking(any());
        verify(roomRepository).save(room);
        verify(notificationService).sendBookingConfirmation(any());
    }

    @Test
    void shouldReturnFalseWhenRoomIsNotAvailable() throws NotificationException {

        LocalDateTime now = LocalDateTime.now();
        when(timeProvider.getCurrentTime()).thenReturn(now);

        Room room = mock(Room.class);
        when(room.isAvailable(any(), any())).thenReturn(false);
        when(roomRepository.findById("room1")).thenReturn(Optional.of(room));

        boolean result = bookingSystem.bookRoom(
                "room1",
                now.plusHours(1),
                now.plusHours(2)
        );

        assertThat(result).isFalse();
        verify(room, never()).addBooking(any());
        verify(notificationService, never()).sendBookingConfirmation(any());
    }
}
