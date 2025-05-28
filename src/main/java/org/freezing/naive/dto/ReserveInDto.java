package org.freezing.naive.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReserveInDto {
    private Integer seatId;
    private String date;
    private String startTime;
    private String endTime;
}
