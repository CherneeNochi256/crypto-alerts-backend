package ru.maxim.cryptoalertbackend.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.maxim.cryptoalertbackend.dto.coin.CoinAlertResponse;
import ru.maxim.cryptoalertbackend.dto.user.UserResponseDto;
import ru.maxim.cryptoalertbackend.entities.User;

import java.util.HashSet;
import java.util.Set;
@Service
@RequiredArgsConstructor

public class UserService {
    private  final ModelMapper mapper;
    private final CoinAlertService coinAlertService;

    public UserResponseDto getCurrentUserData(User currentUser) {
        UserResponseDto userResponseDto = mapper.map(currentUser, UserResponseDto.class);
        Set<CoinAlertResponse> coinAlerts = new HashSet<>(coinAlertService.findAllByUser(currentUser));
        userResponseDto.setCoinAlerts(coinAlerts);

        return userResponseDto;
    }
}
