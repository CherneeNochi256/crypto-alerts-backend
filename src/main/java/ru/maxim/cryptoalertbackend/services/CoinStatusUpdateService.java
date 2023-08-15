package ru.maxim.cryptoalertbackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.maxim.cryptoalertbackend.dto.coin.CoinCapResponseDto;
import ru.maxim.cryptoalertbackend.entities.CoinAlert;
import ru.maxim.cryptoalertbackend.repository.CoinAlertRepository;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CoinStatusUpdateService {
    private static final String REQUEST_URL = "https://api.coincap.io/v2/assets?ids=";

    private final CoinAlertRepository coinAlertRepository;
    private final RestTemplate restTemplate;
    private final EmailService emailService;


    @Scheduled(fixedDelay = 60000) // Run every minute
    public void checkAndUpdateRequestItems() throws IOException {
        List<CoinAlert> coinAlerts = coinAlertRepository.findAllByCoinReachedDesiredPrice(false).orElse(null);


        if (coinAlerts == null) {
            return;
        }

        StringBuilder allIds = new StringBuilder();

        for (int i = 0; i < coinAlerts.size(); i++) {
            String currentCoinId = coinAlerts.get(i).getCoinId();
            if (currentCoinId.equals("ripple"))//for some reason coinCap API writes ripple id as xrp
                currentCoinId = "xrp";

            if (i == coinAlerts.size() - 1)
                allIds.append(currentCoinId);
            else
                allIds.append(currentCoinId).append(",");
        }

        ResponseEntity<CoinCapResponseDto> response = restTemplate.getForEntity(REQUEST_URL + allIds, CoinCapResponseDto.class);


        checkPriceInterceptions(coinAlerts, response.getBody());

    }

    private void checkPriceInterceptions(List<CoinAlert> coinAlerts, CoinCapResponseDto coinDto) {
        for (CoinAlert coinAlert : coinAlerts) {
            CoinCapResponseDto.Data coinData = coinDto.getData().stream()
                    .filter(currentCoinData -> currentCoinData.getId().equals(coinAlert.getCoinId()))
                    .findFirst()
                    .orElse(null);


            if (coinAlert.getStartPrice() < coinAlert.getDesiredPrice() && coinData != null && coinAlert.getDesiredPrice() <= Double.parseDouble(coinData.getPriceUsd())) {
                coinAlert.setCoinReachedDesiredPrice(true);
                coinAlert.setSeen(false);
                coinAlertRepository.save(coinAlert);
                emailService.sendCoinAlertMessage(coinAlert.getUser().getUsername(), coinAlert.getUser().getEmail(), coinAlert);
            } else if (coinAlert.getStartPrice() > coinAlert.getDesiredPrice() && coinData != null && coinAlert.getDesiredPrice() >= Double.parseDouble(coinData.getPriceUsd())) {
                coinAlert.setCoinReachedDesiredPrice(true);
                coinAlertRepository.save(coinAlert);
                emailService.sendCoinAlertMessage(coinAlert.getUser().getUsername(), coinAlert.getUser().getEmail(), coinAlert);
            }
        }
    }
}
