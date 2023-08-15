package ru.maxim.cryptoalertbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.maxim.cryptoalertbackend.dto.ApiResponse;
import ru.maxim.cryptoalertbackend.services.EmailService;

@RestController
@RequestMapping("/api/v1/emails")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @GetMapping("confirmation")
    public ResponseEntity<ApiResponse> confirmUserAccount(@RequestParam("token") String token) {
        return ResponseEntity.ok(emailService.verifyEmailByToken(token));
    }
}
