package ru.maxim.cryptoalertbackend.dto.coin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoinAlertRequest implements Serializable {
    private String coinId;
    private Double startPrice;
    private Double desiredPrice;
    private Boolean sendOnEmail;
    private String image;
}
