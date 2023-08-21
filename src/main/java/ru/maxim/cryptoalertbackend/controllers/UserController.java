package ru.maxim.cryptoalertbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.maxim.cryptoalertbackend.dto.user.UserResponseDto;
import ru.maxim.cryptoalertbackend.entities.User;
import ru.maxim.cryptoalertbackend.services.UserDetailsServiceImpl;
import ru.maxim.cryptoalertbackend.services.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = {"https://crypto-alerts-app.vercel.app", "http://localhost:3000"}, allowCredentials = "true")
public class UserController {

    private final UserService userService;

    @GetMapping("currentUser")
    public ResponseEntity<UserResponseDto> getCurrentUserData(@AuthenticationPrincipal User currentUser){
        UserResponseDto currentUserData = userService.getCurrentUserData(currentUser);
        return ResponseEntity.ok(currentUserData);
    }
}
