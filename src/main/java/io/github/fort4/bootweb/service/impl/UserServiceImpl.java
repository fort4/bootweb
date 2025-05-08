package io.github.fort4.bootweb.service.impl;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.fort4.bootweb.dto.UserSignupRequest;
import io.github.fort4.bootweb.entity.User;
import io.github.fort4.bootweb.repository.UserRepository;
import io.github.fort4.bootweb.service.MailService;
import io.github.fort4.bootweb.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Transactional
    @Override
    public User registerUser(UserSignupRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        String verificationToken = UUID.randomUUID().toString();

        User user = User.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .name(request.getName())
                .emailVerified(false)
                .verificationToken(verificationToken)
                .build();

        User savedUser = userRepository.save(user);

        // 이메일 전송
        //String verificationLink = "http://localhost:8080/api/users/verify?token=" + verificationToken;
        String verificationLink = "https://0706-115-91-129-236.ngrok-free.app/api/users/verify?token=" + verificationToken;
        mailService.sendVerificationEmail(savedUser.getEmail(), verificationLink);

        return savedUser;
    }

    @Transactional
    @Override
    public boolean verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid verification token"));

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        userRepository.save(user);
        return true;
    }
}