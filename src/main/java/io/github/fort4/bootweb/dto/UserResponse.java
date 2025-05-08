package io.github.fort4.bootweb.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String email;
    private String name;
    private boolean emailVerified;
}
