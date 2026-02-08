package com.statestreet;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class CarRentalSystem {
    public static final String WRONG_CAR_TYPE_ERROR_MSG = "Rent car type cannot be null!";
    public static final String WRONG_START_AT_ERROR_MSG = "Rent start date and time cannot be in the past! Given ";
    //TODO Not in far future
    public static final String WRONG_RENT_DAYS_AMOUNT_ERROR_MSG = "Amount of rent days cannot be less that one! Given ";
    //TODO Less than year

    private final Map<CarType, Integer> availableCars;
    private final List<Rent> rentedCars = new CopyOnWriteArrayList<>();

    public CarRentalSystem(Map<CarType, Integer> rentableCars) {
        availableCars = new ConcurrentHashMap<>(rentableCars);
    }

    public void rent(CarType carType, LocalDateTime startAt, int rentDaysAmount) {
        verify(carType, startAt, rentDaysAmount);

        availableCars.computeIfPresent(carType, (type, availableCarsCount) ->
        {
            if (availableCarsCount < 1) {
                LocalDateTime expectedEndAt = startAt.plusDays(rentDaysAmount);
                rentedCars.stream()
                        .filter(rent -> carType.equals(rent.carType()) &&
                                (expectedEndAt.isBefore(rent.startAt()) || startAt.isAfter(rent.endAt())))
                        .findFirst()//FIXME Second rent won't be recorded
                        .orElseThrow(() -> new RuntimeException("No available cars " + carType + " left for given time!"));
            }
            rentedCars.add(new Rent(carType, startAt, startAt.plusDays(rentDaysAmount)));
            return availableCarsCount - 1;
        });
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
