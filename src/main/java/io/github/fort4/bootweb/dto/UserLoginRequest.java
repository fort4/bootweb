package io.github.fort4.bootweb.dto;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String email;
    private String password;
}
