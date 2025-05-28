package org.freezing.naive.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAvailableSetsInDto {
    private String startTime;
    private String endTime;
    private Integer skip;
    private Integer offset;
}
