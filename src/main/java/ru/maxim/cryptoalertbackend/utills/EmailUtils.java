package ru.maxim.cryptoalertbackend.utills;

import ru.maxim.cryptoalertbackend.entities.CoinAlert;

public class EmailUtils {

    public static String getConfirmationEmailMessage(String name, String host, String token) {
        return String.format("""
                        Hello %s,

                        Your new account has been created. Please click the link below to verify your account.\s

                         %s

                        The support Team""",
                name,
                getVerificationUrl(host, token));
    }

    public static String getCoinAlertEmailMessage(String host, String name, CoinAlert coinAlert) {
        return String.format("""
                        Hello %s,

                        Your alert on %s has been triggered at price %.3f. Please click the link to go to the chart.\s

                         %s

                        The support Team""",
                name,
                coinAlert.getCoinId().toUpperCase(),
                coinAlert.getDesiredPrice(),
                getTriggeredCoinUrl(host, coinAlert.getCoinId()));
    }

    private static String getTriggeredCoinUrl(String host, String coinId) {
        return host + "/" + coinId;
    }

    public static String getVerificationUrl(String host, String token) {
        return host + "/api/v1/emails/confirmation?token=" + token;
    }
}
