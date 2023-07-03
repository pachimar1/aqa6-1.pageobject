package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPageV2;


import java.util.Random;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTransferTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        var loginPage = new LoginPageV2();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @AfterEach
    void memoryClear() {
        clearBrowserCookies();
        clearBrowserLocalStorage();
    }

    @Test
    void positiveTransferSecondCardToFirst() {
        val dashboardPage = new DashboardPage();

        int balanceFirstCard = dashboardPage.getFirstCardBalance();
        int balanceSecondCard = dashboardPage.getSecondCardBalance();
        val moneyTransfer = dashboardPage.firstCardButton();
        val infoCard = DataHelper.getSecondCardNumber();
        String sum = String.valueOf(DataHelper.generateValidAmount(balanceFirstCard));
        moneyTransfer.transferForm(sum, infoCard);

        assertEquals(balanceFirstCard + Integer.parseInt(sum), dashboardPage.getFirstCardBalance());
        assertEquals(balanceSecondCard - Integer.parseInt(sum), dashboardPage.getSecondCardBalance());

    }

    @Test
    void positiveTransferFirstCardToSecond() {
        val dashboardPage = new DashboardPage();

        int balanceFirstCard = dashboardPage.getFirstCardBalance();
        int balanceSecondCard = dashboardPage.getSecondCardBalance();
        val moneyTransfer = dashboardPage.secondCardButton();
        val infoCard = DataHelper.getFirstCardNumber();
        String sum = String.valueOf(DataHelper.generateValidAmount(balanceSecondCard));
        moneyTransfer.transferForm(sum, infoCard);

        assertEquals(balanceFirstCard - Integer.parseInt(sum), dashboardPage.getFirstCardBalance());
        assertEquals(balanceSecondCard + Integer.parseInt(sum), dashboardPage.getSecondCardBalance());

    }

    @Test
    void clickCancel() {
        val dashboardPage = new DashboardPage();

        int balanceFirstCard = dashboardPage.getFirstCardBalance();
        int balanceSecondCard = dashboardPage.getSecondCardBalance();

        val moneyTransfer = dashboardPage.firstCardButton();
        moneyTransfer.cancelButton();

        assertEquals(balanceFirstCard, dashboardPage.getFirstCardBalance());
        assertEquals(balanceSecondCard, dashboardPage.getSecondCardBalance());
    }

    @Test
    void negativeTransferSecondCardToFirst() {
        val dashboardPage = new DashboardPage();

        int balanceSecondCard = dashboardPage.getSecondCardBalance();

        val moneyTransfer = dashboardPage.secondCardButton();
        val infoCard = DataHelper.getFirstCardNumber();
        String sum = String.valueOf(DataHelper.generateInvalidAmount(balanceSecondCard));
        moneyTransfer.transferForm(sum, infoCard);
        moneyTransfer.findErrorMessage("Выполнена попытка перевести денежные средства, превыщающие остаток на карте списания");
    }

    @Test
    void errorToTransferSecondCardToSecond() {
        val dashboardPage = new DashboardPage();

        int balanceSecondCard = dashboardPage.getSecondCardBalance();

        val moneyTransfer = dashboardPage.secondCardButton();
        val infoCard = DataHelper.getSecondCardNumber();
        String sum = String.valueOf(DataHelper.generateValidAmount(balanceSecondCard));
        moneyTransfer.transferForm(sum, infoCard);
        moneyTransfer.findErrorMessage("Выполнена попытка перевести денежные средства с той же карты, в которую переводите");
    }

    @Test
    void negativeTransferFirstCardToSecond() {
        val dashboardPage = new DashboardPage();

        int balanceFirstCard = dashboardPage.getFirstCardBalance();

        val moneyTransfer = dashboardPage.firstCardButton();
        val infoCard = DataHelper.getSecondCardNumber();
        String sum = String.valueOf(DataHelper.generateInvalidAmount(balanceFirstCard));
        moneyTransfer.transferForm(sum, infoCard);
        moneyTransfer.findErrorMessage("Выполнена попытка перевести денежные средства, превыщающие остаток на карте списания");
    }

    @Test
    void negativeTransferZeroValue() {
        val dashboardPage = new DashboardPage();

        int balanceSecondCard = dashboardPage.getSecondCardBalance();

        val moneyTransfer = dashboardPage.secondCardButton();
        val infoCard = DataHelper.getFirstCardNumber();
        String sum = "0";
        moneyTransfer.transferForm(sum, infoCard);
        moneyTransfer.findErrorMessage("Невозможно перевести 0 руб.");
    }
}


