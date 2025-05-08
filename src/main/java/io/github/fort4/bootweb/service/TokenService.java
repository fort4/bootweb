package io.github.fort4.bootweb.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TokenService {
    public String createVerificationToken() {
        return UUID.randomUUID().toString();
    }
}
