package ru.maxim.cryptoalertbackend.dto.coin;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CoinCapResponseDto implements Serializable {
    private List<Data> data;
    private float timestamp;

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Data {
        private String id;
        private String rank;
        private String symbol;
        private String name;
        private String supply;
        private String maxSupply;
        private String marketCapUsd;
        private String volumeUsd24Hr;
        private String priceUsd;
        private String changePercent24Hr;
        private String vwap24Hr;
        private String explorer;


    }
}


