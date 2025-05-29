package org.freezing.naive.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class DataResponse {
    private Integer status = 200;
    private Object data;

    public DataResponse() {
    }

    public DataResponse(Object data) {
        this.data = data;
    }
}
