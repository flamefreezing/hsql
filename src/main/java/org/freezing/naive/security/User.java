package org.freezing.naive.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;;

@Data
public class User {
    private Integer id;
    private String email;
    private String password;
    private LocalDateTime createdAt;
}
