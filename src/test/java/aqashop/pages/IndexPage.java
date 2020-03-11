package aqashop.pages;

import aqashop.data.Card;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Data
@RequiredArgsConstructor
public class IndexPage {
    SelenideElement purchaseButton = $$("button").find(exactText("Купить"));
    SelenideElement creditButton = $$("button").find(exactText("Купить в кредит"));
    SelenideElement priceListItem = $("ul :nth-child(4)");
    ElementsCollection formInputs = $$(".input");
    ElementsCollection formInputTops = $$(".input__top");
    SelenideElement cardNumberInput = formInputs.find(exactText("Номер карты")).$(".input__control");
    SelenideElement monthInput = formInputs.find(exactText("Месяц")).$(".input__control");
    SelenideElement yearInput = formInputs.find(exactText("Год")).$(".input__control");
    SelenideElement holderInput = formInputs.find(exactText("Владелец")).$(".input__control");
    SelenideElement cvcInput = formInputs.find(exactText("CVC/CVV")).$(".input__control");
    SelenideElement continueButton = $$("button").find(exactText("Продолжить"));
    SelenideElement paymentOkWindow = $(".notification_status_ok");
    SelenideElement paymentErrorWindow = $(".notification_status_error");
    SelenideElement cardNumberInputSub = formInputTops.find(exactText("Номер карты")).parent().$(".input__sub");
    SelenideElement yearInputSub = formInputTops.find(exactText("Год")).parent().$(".input__sub");
    SelenideElement cvcInputSub = formInputTops.find(exactText("CVC/CVV")).parent().$(".input__sub");
    SelenideElement holderInputSub = formInputTops.find(exactText("Владелец")).parent().$(".input__sub");
    SelenideElement monthInputSub = formInputTops.find(exactText("Месяц")).parent().$(".input__sub");

    @Step("Нажатие кнопки \"Купить\" на главной странице")
    public void purchaseButtonClick() {
        purchaseButton.click();
    }

    @Step("Нажатие кнопки \"Купить\" на главной странице")
    public void creditButtonClick() {
        creditButton.click();
    }

    @Step("Получение цены")
    public int getPrice() {
        String price = priceListItem.getText().replaceAll("\\D+", "");
        return Integer.parseInt(price);
    }

    @Step("Заполнение полей и отправка формы")
    public void fillAndSendForm(Card card) {
        cardNumberInput.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.BACK_SPACE));
        cardNumberInput.setValue(card.getNumber());
        monthInput.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.BACK_SPACE));
        monthInput.setValue(card.getMonth());
        yearInput.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.BACK_SPACE));
        yearInput.setValue(card.getYear());
        holderInput.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.BACK_SPACE));
        holderInput.setValue(card.getHolder());
        cvcInput.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.BACK_SPACE));
        cvcInput.setValue(card.getCvc());
        continueButton.click();
    }

    @Step("Ожидание уведомления об успешной оплате")
    public void waitForOkWindow() {
        paymentOkWindow.waitUntil(visible, 15000);
        paymentErrorWindow.shouldNotBe(visible.because("Не должно появляться уведомление об ошибке"));
    }

    @Step("Ожидание уведомления о неуспешной оплате")
    public void waitForErrorWindow() {
        paymentOkWindow.shouldNotBe(visible.because("Не должно появляться уведомление об успешной оплате"));
        paymentErrorWindow.waitUntil(visible, 15000);
    }

    @Step("Проверка сообщения об ошибке незаполненного поля с номером карты")
    public void assertMessageCard(String message) {
        cardNumberInputSub.shouldHave(exactText(message));
    }

    @Step("Проверка сообщения об ошибке незаполненного поля \"Год\"")
    public void assertMessageYear(String message) {
        yearInputSub.shouldHave(exactText(message));
    }

    @Step("Проверка сообщения об ошибке незаполненного поля \"CVC/CVV\"")
    public void assertMessageCVC(String message) {
        cvcInputSub.shouldHave(exactText(message));
    }

    @Step("Проверка отсутствия сообщения об ошибке для поля \"Владелец\"")
    public void assertNoMessageHolder() {
        holderInputSub.shouldNotBe(exist);
    }

    @Step("Проверка сообщения об ошибке незаполненного поля \"Месяц\"")
    public void assertMessageMonth(String message) {
        monthInputSub.shouldHave(exactText(message));
    }

    @Step("Проверка сообщения об ошибке незаполненного поля \"Владелец\"")
    public void assertMessageHolder(String message) {
        holderInputSub.shouldHave(exactText(message));
    }
}
