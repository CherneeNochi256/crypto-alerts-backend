package ru.maxim.cryptoalertbackend.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.maxim.cryptoalertbackend.dto.ApiResponse;
import ru.maxim.cryptoalertbackend.dto.coin.CoinAlertRequest;
import ru.maxim.cryptoalertbackend.dto.coin.CoinAlertResponse;
import ru.maxim.cryptoalertbackend.entities.CoinAlert;
import ru.maxim.cryptoalertbackend.entities.User;
import ru.maxim.cryptoalertbackend.exception.CanNotPerformActionException;
import ru.maxim.cryptoalertbackend.exception.ResourceNotFoundException;
import ru.maxim.cryptoalertbackend.repository.CoinAlertRepository;

import java.util.ArrayList;
import java.util.List;

import static ru.maxim.cryptoalertbackend.utills.AppConstants.COIN;
import static ru.maxim.cryptoalertbackend.utills.AppConstants.ID;

@Service
@RequiredArgsConstructor
public class CoinAlertService {

    private final CoinAlertRepository coinAlertRepository;
    private final ModelMapper mapper;

    public List<CoinAlertResponse> findAllByUser(User user) {
        List<CoinAlert> coinsFromDb = coinAlertRepository.findAllByUser(user)
                .orElse(new ArrayList<>());

        return coinsFromDb.stream()
                .map(coin -> mapper.map(coin, CoinAlertResponse.class))
                .toList();
    }


    public CoinAlertResponse createCoinForCurrentUser(User currentUser, CoinAlertRequest request) {
        if (request.getSendOnEmail() && !currentUser.getEmailConfirmed())
            throw new CanNotPerformActionException("Email isn't confirmed!");

        System.out.println(request);
        CoinAlert coinAlert = mapper.map(request, CoinAlert.class);

        coinAlert.setUser(currentUser);
        coinAlert.setCoinReachedDesiredPrice(false);
        coinAlert.setSeen(true);


        CoinAlert savedCoinAlert = coinAlertRepository.save(coinAlert);

        return mapper.map(savedCoinAlert, CoinAlertResponse.class);
    }

    public ApiResponse deleteCoinForCurrentUser(User currentUser, Long id) {
        CoinAlert coinAlertFromDb = coinAlertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(COIN, ID, id));

        if (currentUser.getCoinAlerts().contains(coinAlertFromDb)){
            coinAlertRepository.deleteById(id);
            return new ApiResponse(Boolean.TRUE, "You have successfully deleted the coin with id: " + id);
        }

        return new ApiResponse(Boolean.FALSE, "Failed: coin with id: " + id + "isn't yours.");
    }

    public ApiResponse setAllCoinAlertsSeenForCurrentUser(User currentUser) {
        List<CoinAlert> coinsFromDb = coinAlertRepository.findAllBySeenAndUser(false,currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("No unseen coin alert was found"));

        coinsFromDb.forEach(coinAlert -> {
            coinAlert.setSeen(true);
            coinAlertRepository.save(coinAlert);
        });
        return new ApiResponse(Boolean.TRUE, "You have successfully seen all coinAlerts for user with id: " + currentUser.getId());
    }
}
