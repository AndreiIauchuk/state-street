package com.statestreet;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static com.statestreet.CarRentalSystem.*;
import static org.junit.jupiter.api.Assertions.*;

import static com.statestreet.CarType.*;

public class CarRentalSystemTest {

    private final CarRentalSystem carRentalSystem = new CarRentalSystem(
            Map.of(
                    SEDAN, 1,
                    SUV, 2,
                    VAN, 3
            ));

    @Test
    void shouldThrowWhenCarTypeIsNull() {
        Exception exc = assertThrows(
                IllegalArgumentException.class,
                () -> carRentalSystem.rent(null, LocalDateTime.MAX, 3));
        assertEquals(WRONG_CAR_TYPE_ERROR_MSG, exc.getMessage());
    }

    @Test
    void shouldThrowWhenStartAtIsInPast() {
        LocalDateTime past = LocalDateTime.now().minusHours(2);
        Exception exc = assertThrows(
                IllegalArgumentException.class,
                () -> carRentalSystem.rent(CarType.SEDAN, past, 3));
        assertEquals(WRONG_START_AT_ERROR_MSG + past, exc.getMessage());
    }

    @Test
    void shouldThrowWhenRentDaysLessThanOne() {
        int zeroDays = 0;
        Exception exc = assertThrows(
                IllegalArgumentException.class,
                () -> carRentalSystem.rent(CarType.SEDAN, LocalDateTime.MAX, zeroDays));
        assertEquals(WRONG_RENT_DAYS_AMOUNT_ERROR_MSG + zeroDays, exc.getMessage());
    }
}
