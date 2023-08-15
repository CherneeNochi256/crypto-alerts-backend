package ru.maxim.cryptoalertbackend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.maxim.cryptoalertbackend.dto.coin.CoinAlertResponse;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto implements Serializable {
    private Long id;
    private String username;
    private String email;
    private Boolean emailConfirmed;
    private Set<CoinAlertResponse> coinAlerts;
}
