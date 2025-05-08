package io.github.fort4.bootweb.controller;

import io.github.fort4.bootweb.dto.UserSignupRequest;
import io.github.fort4.bootweb.dto.ResendVerificationRequest;
import io.github.fort4.bootweb.dto.UserLoginRequest;
import io.github.fort4.bootweb.dto.UserResponse;
import io.github.fort4.bootweb.entity.User;
import io.github.fort4.bootweb.repository.UserRepository;
import io.github.fort4.bootweb.service.MailService;
import io.github.fort4.bootweb.service.TokenService;
import io.github.fort4.bootweb.service.UserService;
import io.github.fort4.bootweb.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final MailService mailService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@RequestBody UserSignupRequest request) {
        User user = userService.registerUser(request);
        UserResponse response = UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .emailVerified(user.isEmailVerified())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        boolean result = userService.verifyEmail(token);
        if (result) {
            return ResponseEntity.ok("이메일이 성공적으로 인증되었습니다!!");
        } else {
            return ResponseEntity.badRequest().body("Invalid verification token.");
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 메일 또는 비밀번호 입니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("유효하지 않은 메일 또는 비밀번호 입니다.");
        }

        if (!user.isEmailVerified()) {
            throw new IllegalStateException("이메일이 인증되지 않았습니다.");
        }

        String token = JwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok(token);
    }
    
    // 이메일 인증 재전송
    @PostMapping("/resend-verification")
    public ResponseEntity<String> resendVerification(@RequestBody ResendVerificationRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("이 이메일로 가입한 사용자가 없습니다."));

        if (user.isEmailVerified()) {
            return ResponseEntity.badRequest().body("이미 인증된 사용자입니다.");
        }

        String newToken = tokenService.createVerificationToken();

        // 기존 토큰 업데이트 또는 새로 저장 (DB에 토큰 저장 방식 따라 다름)
        user.setVerificationToken(newToken);
        userRepository.save(user);

        String verificationLink = "https://0706-115-91-129-236.ngrok-free.app/api/users/verify?token=" + newToken;
        mailService.sendVerificationEmail(user.getEmail(), verificationLink);

        return ResponseEntity.ok("인증 메일이 재발송되었습니다.");
    }



}
