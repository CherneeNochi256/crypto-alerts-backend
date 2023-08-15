package ru.maxim.cryptoalertbackend.utills;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import ru.maxim.cryptoalertbackend.dto.auth.AuthenticationResponse;

public class CookieUtils {
    private static final int ONE_WEEK = 1000 * 60 * 60 * 24 * 7;
    private static final int TWO_MINUTES = 1000 * 120;

    public static void setRefreshTokenToCookie(HttpServletResponse response, AuthenticationResponse authenticationResponse) {
        Cookie cookie = new Cookie("refresh-token", authenticationResponse.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setAttribute("SameSite", "None");
//        cookie.setMaxAge(ONE_WEEK);
        cookie.setMaxAge(TWO_MINUTES);
        cookie.setSecure(true);
        cookie.setPath("/");


        response.addCookie(cookie);
    }

    public static void deleteRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("refresh-token", null);
        cookie.setHttpOnly(true);
        cookie.setAttribute("SameSite", "None");
        cookie.setMaxAge(0);
        cookie.setSecure(true);
        cookie.setPath("/");


        response.addCookie(cookie);
    }
}
