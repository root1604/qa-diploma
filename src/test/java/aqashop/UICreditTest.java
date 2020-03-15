package aqashop;

import aqashop.data.Card;
import aqashop.data.DataHelper;
import aqashop.pages.IndexPage;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.github.javafaker.Faker;
import io.qameta.allure.*;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import java.util.Locale;

import static aqashop.data.DataHelper.*;
import static aqashop.db.DbCheck.*;
import static com.codeborne.selenide.Selenide.open;

@Feature("Тестирование UI и базы данных при покупке в кредит")
public class UICreditTest {

    private static String url;
    private static IndexPage indexPage = new IndexPage();

    @Step("Открытие веб-страницы ")
    @BeforeEach
    void setUpEach() {
        open(url);
    }

    @Step("Удаление записей из всех таблиц базы данных")
    @AfterEach()
    void removeAllRowsFromDBTables() {
        clearDBTables();
    }

    @Step("Установка соединения с базой данных")
    @BeforeAll
    static void setUpAll() {
        Configuration.screenshots=false;
        SelenideLogger.addListener("allure", new AllureSelenide());
        url = getEnvironmentProperty("aqa-shop.url");
        getDBConnection();
    }

    @Step("Закрытие соединения с базой данных")
    @AfterAll
    static void closeAll() {
        SelenideLogger.removeListener("allure");
        closeDBConnection();
    }

    @Story("2. Покупка тура по кнопке \"Купить в кредит\" по данным действительной карты (статус карты \"APPROVED\")")
    @Severity(SeverityLevel.BLOCKER)
    @Test
    @DisplayName("2. Покупка тура по кнопке \"Купить в кредит\" по данным действительной карты (статус карты \"APPROVED\")")
    void shouldCreditByApprovedCard() {
        indexPage = new IndexPage();
        indexPage.creditButtonClick();
        Card approvedCard = Card.generateApprovedCard("en");
        indexPage.fillAndSendForm(approvedCard);
        indexPage.waitForOkWindow();
        creditApproved(DataHelper.PaymentResult.APPROVED);
    }

    @Story("4. Покупка тура по кнопке \"Купить в кредит\" по данным недействительной карты (статус карты \"DECLINED\")")
    @Severity(SeverityLevel.BLOCKER)
    @Test
    @DisplayName("4. Покупка тура по кнопке \"Купить в кредит\" по данным недействительной карты (статус карты \"DECLINED\")")
    void shouldNotCreditByDeclinedCard() {
        indexPage = new IndexPage();
        indexPage.creditButtonClick();
        Card declinedCard = Card.generateDeclinedCard("en");
        indexPage.fillAndSendForm(declinedCard);
        indexPage.waitForErrorWindow();
        creditDeclined(DataHelper.PaymentResult.DECLINED);
    }
    @Story("13. Отправка формы с незаполненным полем \"Месяц\", остальные поля заполнены валидными данными.")
    @Severity(SeverityLevel.NORMAL)
    @Test
    @DisplayName("13. Отправка формы с незаполненным полем \"Месяц\", остальные поля заполнены валидными данными.")
    void shouldNotPurchaseWithoutMonth() {
        indexPage.creditButtonClick();
        Card approvedCard = Card.generateApprovedCard("en");
        approvedCard.setMonth("");
        indexPage.fillAndSendForm(approvedCard);
        indexPage.assertMessageMonth(getDataProperty("indexPage.blankField"));
    }

    @Story("14. Отправка формы с незаполненным полем \"Владелец\", остальные поля заполнены валидными данными.")
    @Severity(SeverityLevel.NORMAL)
    @Test
    @DisplayName("14. Отправка формы с незаполненным полем \"Владелец\", остальные поля заполнены валидными данными.")
    void shouldNotPurchaseWithoutHolder() {
        indexPage.creditButtonClick();
        Card approvedCard = Card.generateApprovedCard("en");
        approvedCard.setHolder("");
        indexPage.fillAndSendForm(approvedCard);
        indexPage.assertMessageHolder(getDataProperty("indexPage.blankField"));
    }

    @Story("15. Отправка формы со всеми незаполненными полями.")
    @Severity(SeverityLevel.NORMAL)
    @Test
    @DisplayName("15. Отправка формы со всеми незаполненными полями.")
    void shouldNotPurchaseWithoutAll() {
        indexPage.creditButtonClick();
        Card approvedCard = Card.generateApprovedCard("en");
        approvedCard.setNumber("");
        approvedCard.setMonth("");
        approvedCard.setYear("");
        approvedCard.setHolder("");
        approvedCard.setCvc("");
        String blankField = getDataProperty("indexPage.blankField");
        indexPage.fillAndSendForm(approvedCard);
        indexPage.assertMessageCard(blankField);
        indexPage.assertMessageMonth(blankField);
        indexPage.assertMessageYear(blankField);
        indexPage.assertMessageHolder(blankField);
        indexPage.assertMessageCVC(blankField);
    }

    @Story("16. Отправка формы с полем \"Месяц\", содержащим 1 символ, остальные поля заполнены валидными данными.")
    @Severity(SeverityLevel.NORMAL)
    @Test
    @DisplayName("16. Отправка формы с полем \"Месяц\", содержащим 1 символ, остальные поля заполнены валидными данными.")
    void shouldNotCreditWithWrongMonth() {
        Faker faker = new Faker(new Locale("en"));
        indexPage.creditButtonClick();
        Card approvedCard = Card.generateApprovedCard("en");
        approvedCard.setMonth(faker.numerify("#"));
        indexPage.fillAndSendForm(approvedCard);
        indexPage.assertMessageMonth(getDataProperty("indexPage.wrongFormat"));
    }

    @Story("17. Отправка формы со значением, содержащим 1 символ в поле \"Год\", остальные поля заполнены валидными данными.")
    @Severity(SeverityLevel.NORMAL)
    @Test
    @DisplayName("17. Отправка формы со значением, содержащим 1 символ в поле \"Год\", остальные поля заполнены валидными данными.")
    void shouldNotCreditWithWrongYear() {
        Faker faker = new Faker(new Locale("en"));
        indexPage.creditButtonClick();
        Card approvedCard = Card.generateApprovedCard("en");
        approvedCard.setYear(faker.numerify("#"));
        indexPage.fillAndSendForm(approvedCard);
        indexPage.assertMessageYear(getDataProperty("indexPage.wrongFormat"));
    }

    @Story("18. Отправка формы со значением поля \"Владелец\" длиной 257 символов, остальные поля заполнены валидными данными.")
    @Severity(SeverityLevel.NORMAL)
    @Test
    @DisplayName("18. Отправка формы со значением поля \"Владелец\" длиной 257 символов, остальные поля заполнены валидными данными.")
    void shouldNotCreditWithWrongHolder() {
        Faker faker = new Faker(new Locale("en"));
        indexPage.creditButtonClick();
        Card approvedCard = Card.generateApprovedCard("en");
        approvedCard.setHolder(faker.regexify("[a-zA-Z ]{257}"));
        indexPage.fillAndSendForm(approvedCard);
        indexPage.assertMessageHolderWrongFormat();
        indexPage.assertMessageHolder(getDataProperty("indexPage.wrongFormat"));
    }

    @Story("19. Отправка формы с содержащим спецсимволы полем \"Владелец\", остальные поля заполнены валидными данными.")
    @Severity(SeverityLevel.NORMAL)
    @Test
    @DisplayName("19. Отправка формы с содержащим спецсимволы полем \"Владелец\", остальные поля заполнены валидными данными.")
    void shouldNotCreditWithSpecialSymbols() {
        Faker faker = new Faker(new Locale("en"));
        indexPage.creditButtonClick();
        Card approvedCard = Card.generateApprovedCard("en");
        approvedCard.setHolder(faker.regexify("[a-zA-Z!#$ %^0-9]{32}"));
        indexPage.fillAndSendForm(approvedCard);
        indexPage.assertMessageHolderWrongFormat();
        indexPage.assertMessageHolder(getDataProperty("indexPage.wrongFormat"));
    }
}
