package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.TransferPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static ru.netology.data.DataHelper.getCardInfo1;
import static ru.netology.data.DataHelper.getCardInfo2;
import static ru.netology.page.DashboardPage.pushFirstCardButton;
import static ru.netology.page.DashboardPage.pushSecondCardButton;

public class MoneyTransferTest {

    DataHelper.UserInfo authInfo;
    DashboardPage dashboardPage;

    @BeforeEach
    public void auth() {
        open("http://localhost:9999/");
        val login = new LoginPage();
        val authInfo = DataHelper.getUserInfo();
        val verificationPage = login.login(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        val dashboardPage = verificationPage.verify(verificationCode);
    }

    @Test
    public void translationFromTheFirstToTheSecond() {
        int amount = 1_000;
        val DashboardPage = new DashboardPage();
        val firstCardBalanceStart = dashboardPage.getFirstCardBalance();
        val secondCardBalanceStart = dashboardPage.getSecondCardBalance();
        val transferPage = pushSecondCardButton();
        TransferPage.transferMoney(amount, getCardInfo1());
        val firstCardBalanceFinish = firstCardBalanceStart - amount;
        val secondCardBalanceFinish = secondCardBalanceStart + amount;

        assertEquals(firstCardBalanceFinish, dashboardPage.getFirstCardBalance());
        assertEquals(secondCardBalanceFinish, dashboardPage.getSecondCardBalance());
    }

    @Test
    public void translationFromTheSecondToTheFirst() {
        int amount = 9_000;
        val DashboardPage = new DashboardPage();
        val firstCardBalanceStart = dashboardPage.getFirstCardBalance();
        val secondCardBalanceStart = dashboardPage.getSecondCardBalance();
        val transferPage = pushFirstCardButton();
        TransferPage.transferMoney(amount, getCardInfo2());
        val firstCardBalanceFinish = firstCardBalanceStart + amount;
        val secondCardBalanceFinish = secondCardBalanceStart - amount;

        assertEquals(firstCardBalanceFinish, dashboardPage.getFirstCardBalance());
        assertEquals(secondCardBalanceFinish, dashboardPage.getSecondCardBalance());
    }

    @Test
    public void transferMoreThanTheBalance() {
        int amount = 20_000;
        val DashboardPage = new DashboardPage();
        val firstCardBalanceStart = dashboardPage.getFirstCardBalance();
        val secondCardBalanceStart = dashboardPage.getSecondCardBalance();
        val transferPage = pushSecondCardButton();
        TransferPage.transferMoney(amount, getCardInfo2());
        TransferPage.errorLimit();
    }

    @Test
    public void shouldTransferFromSecondToSecondCard() {
        int amount = 8_000;
        val dashboardPage = new DashboardPage();
        val firstCardBalanceStart = DashboardPage.getFirstCardBalance();
        val secondCardBalanceStart = DashboardPage.getSecondCardBalance();
        val transferPage = pushSecondCardButton();
        TransferPage.transferMoney(amount, getCardInfo2());
        TransferPage.invalidCard();
    }
}
