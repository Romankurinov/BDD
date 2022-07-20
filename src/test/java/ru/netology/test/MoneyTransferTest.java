package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.*;


public class MoneyTransferTest {

    DataHelper.UserInfo authInfo;
    DashboardPage dashboardPage;

    @BeforeEach
    public void auth() {
        open("http://localhost:9999/");
        val login = new LoginPage();
        val authInfo = getUserInfo();
        val verificationPage = login.login(authInfo);
        val verificationCode = getVerificationCodeFor(authInfo);
        val dashboardPage = verificationPage.verify(verificationCode);
    }

    @Test
    public void translationFromTheFirstToTheSecond() {
        int amount = 1_000;
        val firstCardBalanceStart = dashboardPage.getFirstCardBalance();
        val secondCardBalanceStart = dashboardPage.getSecondCardBalance();
        val transferPage = dashboardPage.pushSecondCardButton();
        transferPage.transferMoney(amount, getCardInfo1());
        val firstCardBalanceFinish = firstCardBalanceStart - amount;
        val secondCardBalanceFinish = secondCardBalanceStart + amount;

        assertEquals(firstCardBalanceFinish, dashboardPage.getFirstCardBalance());
        assertEquals(secondCardBalanceFinish, dashboardPage.getSecondCardBalance());
    }

    @Test
    public void translationFromTheSecondToTheFirst() {
        int amount = 9_000;
        val firstCardBalanceStart = dashboardPage.getFirstCardBalance();
        val secondCardBalanceStart = dashboardPage.getSecondCardBalance();
        val transferPage = dashboardPage.pushFirstCardButton();
        transferPage.transferMoney(amount, getCardInfo2());
        val firstCardBalanceFinish = firstCardBalanceStart + amount;
        val secondCardBalanceFinish = secondCardBalanceStart - amount;

        assertEquals(firstCardBalanceFinish, dashboardPage.getFirstCardBalance());
        assertEquals(secondCardBalanceFinish, dashboardPage.getSecondCardBalance());
    }

    @Test
    public void transferMoreThanTheBalance() {
        int amount = 20_000;
        val firstCardBalanceStart = dashboardPage.getFirstCardBalance();
        val secondCardBalanceStart = dashboardPage.getSecondCardBalance();
        val transferPage = dashboardPage.pushSecondCardButton();
        transferPage.transferMoney(amount, getCardInfo2());
        transferPage.errorLimit();
    }


    @Test
    public void shouldTransferFromSecondToSecondCard() {
        int amount = 8_000;
        val firstCardBalanceStart = dashboardPage.getFirstCardBalance();
        val secondCardBalanceStart = dashboardPage.getSecondCardBalance();
        val transferPage = dashboardPage.pushSecondCardButton();
        transferPage.transferMoney(amount, getCardInfo2());
        transferPage.invalidCard();
    }
}