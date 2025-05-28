package org.freezing.naive.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Reservation {
    private Integer id;
    private Integer seatId;
    private Integer employeeId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime checkInAt;
    private Integer extendedFromReservationId;
}
