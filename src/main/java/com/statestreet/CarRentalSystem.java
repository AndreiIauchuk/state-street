package com.statestreet;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.statestreet.CarType.*;

public class CarRentalSystem {
    public static final String WRONG_CAR_TYPE_ERROR_MSG = "Rent car type cannot be null!";
    public static final String WRONG_START_AT_ERROR_MSG = "Rent start date and time cannot be in the past! Given ";
    //TODO Not in far future
    public static final String WRONG_RENT_DAYS_AMOUNT_ERROR_MSG = "Amount of rent days cannot be less that one! Given ";
    //TODO Less than year

    private static final int SEDAN_MAX_AMOUNT = 100;
    private static final int SUV_MAX_AMOUNT = 250;
    private static final int VAN_MAX_AMOUNT = 50;


    private final Map<CarType, Integer> availableCars = new ConcurrentHashMap<>(
            Map.of(
                    SEDAN, SEDAN_MAX_AMOUNT,
                    SUV, SUV_MAX_AMOUNT,
                    VAN, VAN_MAX_AMOUNT
            ));

    public void rent(CarType carType, LocalDateTime startAt, int rentDaysAmount) {
        verify(carType, startAt, rentDaysAmount);
    }

    private void verify(CarType carType, LocalDateTime startAt, int rentDaysAmount) {
        verifyCarType(carType);
        verifyStartAt(startAt);
        verifyRentDaysAmount(rentDaysAmount);
    }

    private void verifyCarType(CarType carType) {
        if (carType == null) {
            throw new IllegalArgumentException(WRONG_CAR_TYPE_ERROR_MSG);
        }
    }

    private void verifyStartAt(LocalDateTime startAt) {
        if (startAt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(WRONG_START_AT_ERROR_MSG + startAt);
        }
    }

    private void verifyRentDaysAmount(int rentDaysAmount) {
        if (rentDaysAmount < 1) {
            throw new IllegalArgumentException(WRONG_RENT_DAYS_AMOUNT_ERROR_MSG + rentDaysAmount);
        }
    }
}
