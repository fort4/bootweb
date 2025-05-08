package io.github.fort4.bootweb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.fort4.bootweb.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByVerificationToken(String token);
}
