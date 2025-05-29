package org.freezing.naive.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
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
