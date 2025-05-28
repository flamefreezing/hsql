package org.freezing.naive.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetReservationHistoryOutDto {
    private Integer id;
    private Timestamp startTime;
    private Timestamp endTime;
    private String status;
    private String name;
    private String floor;
}
