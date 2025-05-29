package org.freezing.naive.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetOwnReservationsOutDto {
    private Integer reservationId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String seatName;
    private String floor;
    private String buildingName;
}
