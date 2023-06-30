package ru.netology.web.data;

import lombok.Value;

import java.util.Random;

public class DataHelper {
    private DataHelper() {
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static AuthInfo getOtherAuthInfo(AuthInfo original) {
        return new AuthInfo("petya", "123qwerty");
    }

    public static VerificationCode getVerificationCodeFor(AuthInfo authInfo) {
        return new VerificationCode("12345");
    }

    public static CardNumber getFirstCardNumber() {

        return new CardNumber("5559 0000 0000 0001");
    }

    public static CardNumber getSecondCardNumber() {

        return new CardNumber("5559 0000 0000 0002");
    }

    public static TransferAmount getRandomTransferAmount() {
        Random random = new Random();
        int amount = random.nextInt(10_000); // Генерируем случайное число от 0 до 15000
        if (amount == 0 || amount > 10_000) {
            throw new IllegalArgumentException("Недопустимое значение для суммы перевода");
        }
        return new TransferAmount(String.valueOf(amount));
    }

    @Value
    public static class AuthInfo {
        private String login;
        private String password;

    }

    @Value
    public static class VerificationCode {
        private String code;
    }

    @Value
    public static class CardNumber {
        private String cardNumber;
    }

    @Value
    public static class TransferAmount {
        private String amount;
    }
}
