package ru.hse.tasktrackerapi.api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.tasktrackerapi.api.exceptions.BadRequestException;
import ru.hse.tasktrackerapi.repository.UserRepository;
import ru.hse.tasktrackerapi.store.UserEntity;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final String REGISTER_USER = "auth/register";

    @PostMapping(REGISTER_USER)
    public UserEntity registerUser(@RequestParam(value = "username") String username,
                                   @RequestParam(value = "password") String password,
                                   @RequestParam(value = "role") String role) {


        if (username.trim().isEmpty()) {
            throw new BadRequestException("Repository name is empty");
        }

        userRepository.findByUsername(username).ifPresent(user -> {
            throw new BadRequestException("Project " + user.getUsername() + " exists");
        });

        return userRepository.saveAndFlush(
                UserEntity.builder()
                        .username(username)
                        .password(passwordEncoder.encode(password))
                        .role(role)
                        .build()
        );


    }
}
