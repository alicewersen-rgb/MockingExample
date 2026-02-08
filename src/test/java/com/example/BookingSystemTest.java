package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class BookingSystemTest {

    private TimeProvider timeProvider;
    private RoomRepository roomRepository;
    private NotificationService notificationService;
    private BookingSystem bookingSystem;

    @BeforeEach
    void setUp() {
        timeProvider = Mockito.mock(TimeProvider.class);
        roomRepository = Mockito.mock(RoomRepository.class);
        notificationService = Mockito.mock(NotificationService.class);

        bookingSystem = new BookingSystem(timeProvider, roomRepository, notificationService);
    }

    @Test
    void shouldBookRoomSuccessfully() throws NotificationException {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(1).plusHours(1);

        Room room = Mockito.mock(Room.class);
        Mockito.when(roomRepository.findById("R1")).thenReturn(Optional.of(room));
        Mockito.when(room.isAvailable(start, end)).thenReturn(true);
        Mockito.when(timeProvider.getCurrentTime()).thenReturn(now);

        boolean result = bookingSystem.bookRoom("R1", start, end);


        assertThat(result).isTrue();
        Mockito.verify(room).addBooking(Mockito.any());
        Mockito.verify(roomRepository).save(room);
        Mockito.verify(notificationService).sendBookingConfirmation(Mockito.any());
    }
}
