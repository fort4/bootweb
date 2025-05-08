package io.github.fort4.bootweb.service;

import io.github.fort4.bootweb.dto.UserSignupRequest;
import io.github.fort4.bootweb.entity.User;

public interface UserService {
    User registerUser(UserSignupRequest request);
    boolean verifyEmail(String token);
}

