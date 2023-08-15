package ru.maxim.cryptoalertbackend.services;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;
import ru.maxim.cryptoalertbackend.dto.ApiResponse;
import ru.maxim.cryptoalertbackend.dto.auth.AuthenticationRequest;
import ru.maxim.cryptoalertbackend.dto.auth.AuthenticationResponse;
import ru.maxim.cryptoalertbackend.dto.auth.RegisterRequest;
import ru.maxim.cryptoalertbackend.entities.Confirmation;
import ru.maxim.cryptoalertbackend.entities.Role;
import ru.maxim.cryptoalertbackend.entities.User;
import ru.maxim.cryptoalertbackend.exception.CanNotPerformActionException;
import ru.maxim.cryptoalertbackend.exception.ResourceNotFoundException;
import ru.maxim.cryptoalertbackend.repository.ConfirmationRepository;
import ru.maxim.cryptoalertbackend.repository.TokenRepository;
import ru.maxim.cryptoalertbackend.repository.UserRepository;
import ru.maxim.cryptoalertbackend.security.JwtService;
import ru.maxim.cryptoalertbackend.security.token.Token;
import ru.maxim.cryptoalertbackend.security.token.TokenType;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static ru.maxim.cryptoalertbackend.utills.AppConstants.NAME;
import static ru.maxim.cryptoalertbackend.utills.AppConstants.USER;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationRepository confirmationRepository;
    private final EmailService emailService;

    public AuthenticationResponse register(RegisterRequest request) {

        Optional<User> userFromDbByEmail = repository.findByEmailIgnoreCase(request.getEmail());
        Optional<User> userFromDbByName = repository.findByUsernameIgnoreCase(request.getUsername());

        if (userFromDbByEmail.isPresent())
            throw new CanNotPerformActionException("User with email " + request.getEmail() + " already exists!");
        else if (userFromDbByName.isPresent())
            throw new CanNotPerformActionException("User with name " + request.getUsername() + " already exists!");

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .emailConfirmed(false)
                .roles(Collections.singleton(Role.USER))
                .build();

        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        saveUserToken(savedUser, jwtToken);

        Confirmation confirmation = new Confirmation(user);
        confirmationRepository.save(confirmation);

        emailService.sendConfirmEmailMessage(user.getUsername(), user.getEmail(), confirmation.getToken());

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        var authenticationToken = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

        authenticationManager.authenticate(authenticationToken);

        var user = repository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {

        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final String refreshToken;
        final String userName;

        Cookie refreshTokenCookie = WebUtils.getCookie(request, "refresh-token");

        if (refreshTokenCookie == null) {
            return null;
        }
        System.out.println(refreshTokenCookie);

        refreshToken = refreshTokenCookie.getValue();
        userName = jwtService.extractUsername(refreshToken);

        if (userName != null) {

            var user = this.repository.findByUsername(userName)
                    .orElseThrow(()->new ResourceNotFoundException(USER,NAME,userName));

            if (jwtService.isTokenValid(refreshToken, user)) {

                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);

                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

                return authResponse;
            }
        }
        throw new CanNotPerformActionException("Refresh token is invalid");
    }
}

