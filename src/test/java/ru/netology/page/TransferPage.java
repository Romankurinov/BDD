package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static java.lang.String.valueOf;

public class TransferPage {

    private SelenideElement amountInput = $x("//span[@data-test-id='amount']//input");
    private SelenideElement formInput = $x("//span[@data-test-id='from']//input");
    private SelenideElement transferButton = $x("//button[@data-test-id='action-transfer']");
    private SelenideElement cancelButton = $x("//button[@data-test-id='action-cancel']");
    private SelenideElement errorNotification = $x("//div[@data-test-id='error-notification']");
    private SelenideElement errorButton = $x("//div[@data-test-id='error-notification']/button");


    public DashboardPage transferMoney(int amount, DataHelper.CardInfo from) {
        amountInput.val(valueOf(amount));
        formInput.setValue(valueOf(from));
        transferButton.click();
        return new DashboardPage();
    }

    public void errorLimit() {
        $(".notification__content").should(Condition.exactText("Ошибка"));
    }

    public void invalidCard() {
        $(".notification__content").should(Condition.text("Ошибка! Произошла ошибка"));
    }
}