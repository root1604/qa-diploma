package aqashop.tests;

import aqashop.data.Card;
import aqashop.data.DataHelper;
import aqashop.pages.IndexPage;

import com.github.javafaker.Faker;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import java.util.Locale;

import static aqashop.data.DataHelper.*;
import static aqashop.db.DbCheck.*;
import static com.codeborne.selenide.Selenide.open;


@Feature("Тестирование UI и базы данных при покупке не в кредит")
public class UIPurchaseTest extends BaseTest{

    private static String url = getEnvironmentProperty("aqa-shop.url");
    private static IndexPage indexPage = new IndexPage();

    @Step("Открытие веб-страницы ")
    @BeforeEach
    void setUpEach() {
        open(url);
    }

    @Story("1. Покупка тура по кнопке \"Купить\" по данным действительной карты (статус карты \"APPROVED\")")
    @Severity(SeverityLevel.BLOCKER)
    @Test
    @DisplayName("1. Покупка тура по кнопке \"Купить\" по данным действительной карты (статус карты \"APPROVED\")")
    void shouldPurchaseByApprovedCard() {
        indexPage.purchaseButtonClick();
        int price = indexPage.getPrice() * 100;
        Card approvedCard = Card.generateApprovedCard("en");
        indexPage.fillAndSendForm(approvedCard);
        indexPage.waitForOkWindow();
        purchaseApproved(DataHelper.PaymentResult.APPROVED, price);
    }

    @Story("3. Покупка тура по кнопке \"Купить\" по данным недействительной карты (статус карты \"DECLINED\")")
    @Severity(SeverityLevel.BLOCKER)
    @Test
    @DisplayName("3. Покупка тура по кнопке \"Купить\" по данным недействительной карты (статус карты \"DECLINED\")")
    void shouldNotPurchaseByDeclinedCard() {
        indexPage.purchaseButtonClick();
        int price = indexPage.getPrice() * 100;
        Card declinedCard = Card.generateDeclinedCard("en");
        indexPage.fillAndSendForm(declinedCard);
        indexPage.waitForErrorWindow();
        purchaseDeclined(DataHelper.PaymentResult.DECLINED, price);
    }

    @Story("5. Отправка формы с незаполненным полем \"Номер карты\", остальные поля заполнены валидными данными.")
    @Severity(SeverityLevel.NORMAL)
    @Test
    @DisplayName("5. Отправка формы с незаполненным полем \"Номер карты\", остальные поля заполнены валидными данными.")
    void shouldNotPurchaseWithoutCardNumber() {
        indexPage.purchaseButtonClick();
        Card approvedCard = Card.generateApprovedCard("en");
        approvedCard.setNumber("");
        indexPage.fillAndSendForm(approvedCard);
        indexPage.assertMessageCard(getDataProperty("indexPage.blankField"));
    }

    @Story("6. Отправка формы с незаполненным полем \"Год\", остальные поля заполнены валидными данными.")
    @Severity(SeverityLevel.NORMAL)
    @Test
    @DisplayName("6. Отправка формы с незаполненным полем \"Год\", остальные поля заполнены валидными данными.")
    void shouldNotPurchaseWithoutYear() {
        indexPage.purchaseButtonClick();
        Card approvedCard = Card.generateApprovedCard("en");
        approvedCard.setYear("");
        indexPage.fillAndSendForm(approvedCard);
        indexPage.assertMessageYear(getDataProperty("indexPage.blankField"));
    }

    @Story("7. Отправка формы с незаполненным полем \"CVC/CVV\", остальные поля заполнены валидными данными.")
    @Severity(SeverityLevel.NORMAL)
    @Test
    @DisplayName("7. Отправка формы с незаполненным полем \"CVC/CVV\", остальные поля заполнены валидными данными.")
    void shouldNotPurchaseWithoutCVC() {
        indexPage.purchaseButtonClick();
        Card approvedCard = Card.generateApprovedCard("en");
        approvedCard.setCvc("");
        indexPage.fillAndSendForm(approvedCard);
        indexPage.assertNoMessageHolder();
        indexPage.assertMessageCVC(getDataProperty("indexPage.blankField"));
    }

    @Story("8. Отправка формы с полем \"Номер карты\", содержащим 15 символов, остальные поля " +
            "заполнены валидными данными.")
    @Severity(SeverityLevel.NORMAL)
    @Test
    @DisplayName("8. Отправка формы с полем \"Номер карты\", содержащим 15 символов, остальные поля " +
            "заполнены валидными данными.")
    void shouldNotPurchaseWithWrongCardNumber() {
        Faker faker = new Faker(new Locale("en"));
        indexPage.purchaseButtonClick();
        Card approvedCard = Card.generateApprovedCard("en");
        approvedCard.setNumber(faker.numerify("###############"));
        indexPage.fillAndSendForm(approvedCard);
        indexPage.assertMessageCard(getDataProperty("indexPage.wrongFormat"));
    }

    @Story("9. Отправка формы со значением больше 12 и меньше 100 в поле \"Месяц\", остальные поля заполнены" +
            " валидными данными.")
    @Severity(SeverityLevel.NORMAL)
    @Test
    @DisplayName("9. Отправка формы со значением больше 12 и меньше 100 в поле \"Месяц\", остальные поля заполнены" +
            " валидными данными.")
    void shouldNotPurchaseWithWrongMonth() {
        Faker faker = new Faker(new Locale("en"));
        indexPage.purchaseButtonClick();
        Card approvedCard = Card.generateApprovedCard("en");
        approvedCard.setMonth(faker.regexify("[1-9][3-9]|[2-9][0-9]"));
        indexPage.fillAndSendForm(approvedCard);
        indexPage.assertMessageMonth(getDataProperty("indexPage.wrongDate"));
    }

    @Story("10. Отправка формы со значением больше 30 и меньше 100 в поле \"Год\", остальные поля заполнены " +
            "валидными данными.")
    @Severity(SeverityLevel.NORMAL)
    @Test
    @DisplayName("10. Отправка формы со значением больше 30 и меньше 100 в поле \"Год\", остальные поля " +
            "заполнены валидными данными.")
    void shouldNotPurchaseWithWrongYear() {
        Faker faker = new Faker(new Locale("en"));
        indexPage.purchaseButtonClick();
        Card approvedCard = Card.generateApprovedCard("en");
        approvedCard.setYear(faker.regexify("[3-9][0-9]"));
        indexPage.fillAndSendForm(approvedCard);
        indexPage.assertMessageYear(getDataProperty("indexPage.wrongDate"));
    }

    @Story("11. Отправка формы с заполненным на кириллице полем \"Владелец\", остальные поля заполнены " +
            "валидными данными.")
    @Severity(SeverityLevel.NORMAL)
    @Test
    @DisplayName("11. Отправка формы с заполненным на кириллице полем \"Владелец\", остальные поля заполнены" +
            " валидными данными.")
    void shouldNotPurchaseWithWrongHolder() {
        Faker faker = new Faker(new Locale("ru"));
        indexPage.purchaseButtonClick();
        Card approvedCard = Card.generateApprovedCard("en");
        approvedCard.setHolder(faker.name().fullName());
        indexPage.fillAndSendForm(approvedCard);
        indexPage.assertMessageHolderWrongFormat();
        indexPage.assertMessageHolder(getDataProperty("indexPage.wrongFormat"));
    }

    @Story("12. Отправка формы со значением, содержащим 2 символа в поле \"CVC/CVV\", остальные поля заполнены" +
            " валидными данными.")
    @Severity(SeverityLevel.NORMAL)
    @Test
    @DisplayName("12. Отправка формы со значением, содержащим 2 символа в поле \"CVC/CVV\", остальные поля " +
            "заполнены валидными данными.")
    void shouldNotPurchaseWithWrongCVC() {
        Faker faker = new Faker(new Locale("en"));
        indexPage.purchaseButtonClick();
        Card approvedCard = Card.generateApprovedCard("en");
        approvedCard.setCvc(faker.numerify("##"));
        indexPage.fillAndSendForm(approvedCard);
        indexPage.assertMessageCVC(getDataProperty("indexPage.wrongFormat"));
    }
}
