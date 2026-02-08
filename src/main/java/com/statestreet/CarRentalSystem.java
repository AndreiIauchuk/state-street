package com.statestreet;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class CarRentalSystem {
    public static final String WRONG_CAR_TYPE_ERROR_MSG = "Rent car type cannot be null!";
    public static final String WRONG_START_AT_ERROR_MSG = "Rent start date and time cannot be in the past! Given ";
    //TODO Not in far future
    public static final String WRONG_RENT_DAYS_AMOUNT_ERROR_MSG = "Amount of rent days cannot be less that one! Given ";
    //TODO Less than year
    public static final String NO_AVAILABLE_CARS_LEFT = "No available cars left for given time! Given ";

    private final Map<CarType, Integer> availableCars;
    private final List<Rent> rentedCars = new CopyOnWriteArrayList<>();

    public CarRentalSystem(Map<CarType, Integer> rentableCars) {
        availableCars = new ConcurrentHashMap<>(rentableCars);
    }

    public void rent(CarType carType, LocalDateTime startAt, int rentDaysAmount) {
        verify(carType, startAt, rentDaysAmount);

        availableCars.computeIfPresent(carType, (type, availableCarsCount) ->
        {
            try {
                Thread.sleep(200); //Just to simulate renting
                if (availableCarsCount < 1) {
                    LocalDateTime endAt = startAt.plusDays(rentDaysAmount);
                    rentedCars.stream()
                            .filter(rent ->
                                    carType.equals(rent.getCarType()) &&
                                            (endAt.isBefore(rent.getStartAt()) || startAt.isAfter(rent.getEndAt())))
                            .findFirst()
                            .map(rent -> Optional.of(new Rent(carType, startAt, endAt)))//FIXME Prev rent will be rewritten
                            .orElseThrow(() -> new RuntimeException(NO_AVAILABLE_CARS_LEFT + carType));
                }
                rentedCars.add(new Rent(carType, startAt, startAt.plusDays(rentDaysAmount)));
                return availableCarsCount - 1;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
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
