package com.statestreet;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.statestreet.CarRentalSystem.*;
import static com.statestreet.CarType.*;
import static org.junit.jupiter.api.Assertions.*;

public class CarRentalSystemTest {

    private final CarRentalSystem carRentalSystem = new CarRentalSystem(
            Map.of(
                    SEDAN, 1,
                    SUV, 2,
                    VAN, 3
            ));

    @Test
    void shouldRentCar() {
        carRentalSystem.rent(SEDAN, LocalDateTime.now().plusHours(1), 3);
    }

    @Test
    void shouldThrowIfAllCarsAreRentedInSameGivenTimeSync() {
        carRentalSystem.rent(SEDAN, LocalDateTime.now().plusHours(1), 3);
        Exception exc = assertThrows(
                RuntimeException.class,
                () -> carRentalSystem.rent(SEDAN, LocalDateTime.now().plusHours(1), 3));
        assertEquals(NO_AVAILABLE_CARS_LEFT + SEDAN, exc.getMessage());
    }

    @Test
    void shouldRentIfAllCarsAreRentedButTimeButGivenTimeIsAvailable() {
        carRentalSystem.rent(SEDAN, LocalDateTime.now().plusHours(1), 1);
        assertDoesNotThrow(() -> carRentalSystem.rent(SEDAN, LocalDateTime.now().plusDays(2), 3));
    }

    @Test
    void shouldThrowIfAllCarsAreRentedInSameGivenTimeAsync() {
        CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> carRentalSystem.rent(SEDAN, LocalDateTime.now().plusHours(1), 1)),
                CompletableFuture.runAsync(() -> carRentalSystem.rent(SEDAN, LocalDateTime.now().plusHours(1), 1))
        ).handle((rent, exc) ->
                {
                    assertEquals(NO_AVAILABLE_CARS_LEFT + SEDAN, exc.getMessage());
                    return rent;
                }
        );
    }

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
