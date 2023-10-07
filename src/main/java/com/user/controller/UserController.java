package com.user.controller;

import com.user.model.User;
import com.user.repo.UserRepo;
import io.micrometer.observation.annotation.Observed;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepo userRepo;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String save(@RequestBody User user) {
        log.info("Request received for saving user with id: {}", user.getId());
        Optional.of(user)
                .map(u -> {
                    u.setId(user.getId() != null ? user.getId() : UUID.randomUUID().toString());
                    return u;
                }).ifPresent(userRepo::save);
        return "User saved successfully with id : " + user.getId();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable String id) {
        log.info("Request received for get the user with id: {}", id);
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id:" + id));
    }
}
