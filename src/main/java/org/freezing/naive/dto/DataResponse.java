package org.freezing.naive.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataResponse {
	private Integer status = 200;
    private Object data;
    
    public DataResponse(Object data) {
    	this.data = data;
    }
}
