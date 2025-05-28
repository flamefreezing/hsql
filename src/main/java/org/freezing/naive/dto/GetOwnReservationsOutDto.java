package org.freezing.naive.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetOwnReservationsOutDto {
    private Integer reservationId;
    private Timestamp startTime;
    private Timestamp endTime;
    private String seatName;
    private String floor;
    private String buildingName;
    private String status;
    private Integer extendedFromReservationId;
}
