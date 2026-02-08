package com.statestreet;

import java.time.LocalDateTime;

public record Rent(CarType carType, LocalDateTime startAt, LocalDateTime endAt) {
}
