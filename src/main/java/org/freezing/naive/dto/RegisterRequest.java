package org.freezing.naive.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
}
