package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @Test
    void shouldThrowExceptionWhenRoomDoesNotExist() {
        LocalDateTime now = LocalDateTime.now();
        when(timeProvider.getCurrentTime()).thenReturn(now);
        when(roomRepository.findById("R1")).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                bookingSystem.bookRoom(
                        "R1",
                        now.plusHours(1),
                        now.plusHours(2)
                )
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Rummet existerar inte");
    }

    @Test
    void shouldThrowExceptionWhenStartTimeIsInThePast() {
        LocalDateTime now = LocalDateTime.now();
        when(timeProvider.getCurrentTime()).thenReturn(now);
        when(roomRepository.findById("R1")).thenReturn(Optional.of(mock(Room.class)));

        assertThatThrownBy(() ->
                bookingSystem.bookRoom(
                        "R1",
                        now.minusHours(1),
                        now.plusHours(1)
                )
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("dåtid");
    }

    @ParameterizedTest
    @MethodSource("invalidBookingTimes")
    void shouldThrowExceptionForInvalidBookingTimes(
            LocalDateTime start,
            LocalDateTime end
    ) {
        LocalDateTime now = LocalDateTime.now();
        when(timeProvider.getCurrentTime()).thenReturn(now);
        when(roomRepository.findById("R1")).thenReturn(Optional.of(mock(Room.class)));

        assertThatThrownBy(() ->
                bookingSystem.bookRoom("R1", start, end)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    public static Stream<Arguments> invalidBookingTimes() {
        LocalDateTime now = LocalDateTime.now();
        return Stream.of(
                Arguments.of(now.plusHours(2), now.plusHours(1)), // sluttid innan starttid
                Arguments.of(now.minusHours(1), now.plusHours(1)) // starttid i dåtid
        );
    }
}
