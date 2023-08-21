package ru.maxim.cryptoalertbackend.controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.maxim.cryptoalertbackend.dto.ApiResponse;
import ru.maxim.cryptoalertbackend.dto.auth.AuthenticationRequest;
import ru.maxim.cryptoalertbackend.dto.auth.AuthenticationResponse;
import ru.maxim.cryptoalertbackend.dto.auth.RegisterRequest;
import ru.maxim.cryptoalertbackend.security.LogoutService;
import ru.maxim.cryptoalertbackend.services.AuthenticationService;

import java.io.IOException;

import static ru.maxim.cryptoalertbackend.utills.CookieUtils.deleteRefreshTokenCookie;
import static ru.maxim.cryptoalertbackend.utills.CookieUtils.setRefreshTokenToCookie;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = {"https://crypto-alerts-app.vercel.app", "http://localhost:3000"}, allowCredentials = "true")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final LogoutService logoutService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request, HttpServletResponse response) {
        AuthenticationResponse registrationResponse = service.register(request);


        setRefreshTokenToCookie(response, registrationResponse);

        return ResponseEntity.ok().body(registrationResponse.getAccessToken());
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AuthenticationRequest request, HttpServletResponse response) {
        AuthenticationResponse authenticationResponse = service.authenticate(request);


        setRefreshTokenToCookie(response, authenticationResponse);

        return ResponseEntity.ok().body(authenticationResponse.getAccessToken());
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        AuthenticationResponse refreshTokenResponse = service.refreshToken(request, response);

        return ResponseEntity.ok().body(refreshTokenResponse.getAccessToken());
    }


    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal Authentication authentication) throws IOException {
        logoutService.logout(request, response, authentication);// for some reason you need to delete cookie inside logout method
    }

}
