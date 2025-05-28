package org.freezing.naive.dto;

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

    public DataResponse(Object data, Integer status) {
        this.data = data;
        this.status = status;
    }
}
