package org.freezing.naive.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetSeatsByFloorIdOutDto {
    private Integer seatId;
    private String seatName;
    private String status;
    private String floor;
    private String buildingName;
}
