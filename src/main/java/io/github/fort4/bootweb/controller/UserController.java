package io.github.fort4.bootweb.controller;

import io.github.fort4.bootweb.dto.UserSignupRequest;
import io.github.fort4.bootweb.dto.UserResponse;
import io.github.fort4.bootweb.entity.User;
import io.github.fort4.bootweb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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
}
