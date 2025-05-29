package org.freezing.naive.dto;

import lombok.Data;

@Data
public class Seat {
	private Integer id;
	private Integer floorId;
	private String name;
	private String status;
}
