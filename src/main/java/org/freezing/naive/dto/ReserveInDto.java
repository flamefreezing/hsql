package org.freezing.naive.dto;

import lombok.Data;

@Data
public class ReserveInDto {
	private String date;
    private String startTime;
    private String endTime;
    private Integer seatId;
}	
