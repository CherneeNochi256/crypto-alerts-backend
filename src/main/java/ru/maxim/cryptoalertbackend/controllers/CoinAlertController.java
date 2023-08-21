package ru.maxim.cryptoalertbackend.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.maxim.cryptoalertbackend.dto.ApiResponse;
import ru.maxim.cryptoalertbackend.dto.coin.CoinAlertRequest;
import ru.maxim.cryptoalertbackend.dto.coin.CoinAlertResponse;
import ru.maxim.cryptoalertbackend.entities.User;
import ru.maxim.cryptoalertbackend.services.CoinAlertService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coins")
@RequiredArgsConstructor
@CrossOrigin(origins = {"https://crypto-alerts-app.vercel.app", "http://localhost:3000"}, allowCredentials = "true")
public class CoinAlertController {

    private final CoinAlertService coinAlertService;

    @GetMapping
    public ResponseEntity<List<CoinAlertResponse>> allCoinsForCurrentUser(@AuthenticationPrincipal User currentUser) {



        return ResponseEntity.ok()
                .body(coinAlertService.findAllByUser(currentUser));
    }

    @PostMapping
    public ResponseEntity<CoinAlertResponse> addCoinForCurrentUser(@AuthenticationPrincipal User currentUser,
                                                                   @RequestBody CoinAlertRequest request) {
        return ResponseEntity.ok(coinAlertService.createCoinForCurrentUser(currentUser, request));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> deleteCoinForCurrentUser(@AuthenticationPrincipal User currentUser,
                                                                @PathVariable Long id) {
        return ResponseEntity.ok(coinAlertService.deleteCoinForCurrentUser(currentUser, id));
    }

    @PostMapping("seen")
    public ResponseEntity<ApiResponse> setAllSeenForCurrentUser(@AuthenticationPrincipal User currentUser){
        return ResponseEntity.ok(coinAlertService.setAllCoinAlertsSeenForCurrentUser(currentUser));
    }
}
