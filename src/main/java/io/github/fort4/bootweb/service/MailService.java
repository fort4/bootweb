package io.github.fort4.bootweb.service;

public interface MailService {
    void sendVerificationEmail(String to, String verificationLink);
}
