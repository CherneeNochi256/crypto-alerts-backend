package ru.maxim.cryptoalertbackend.dto.coin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoinAlertResponse implements Serializable {
    private Long id;
    private String coinId;
    private Double desiredPrice;
    private Boolean coinReachedDesiredPrice;
    private String image;
    private Date updatedDate;
    private Boolean seen;
}
