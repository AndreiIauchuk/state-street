package com.statestreet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class Rent {
    private CarType carType;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
}
