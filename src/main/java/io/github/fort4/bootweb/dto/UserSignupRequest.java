package io.github.fort4.bootweb.dto;

import lombok.Data;

@Data
public class UserSignupRequest {
    private String email;
    private String password;
    private String name;
}
