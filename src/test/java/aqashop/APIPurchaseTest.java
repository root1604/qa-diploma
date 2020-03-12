package aqashop;

import aqashop.api.API;
import aqashop.data.Card;
import aqashop.data.DataHelper;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.github.javafaker.Faker;
import io.qameta.allure.*;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.util.Locale;

import static aqashop.data.DataHelper.getProperty;
import static aqashop.db.DB.*;

@Feature("Тестирование API при покупке не в кредит")
public class APIPurchaseTest {

    static Connection connection;
    private static String apiPurchaseUrl;

    @Step("Установка соединения с базой данных")
    @BeforeAll
    static void setUpAll() {
        Configuration.screenshots=false;
        SelenideLogger.addListener("allure", new AllureSelenide());
        apiPurchaseUrl = getProperty("environment.properties", "aqa-shop.apiPayUrl");
        connection = getDBConnection();
    }

    @Step("Закрытие соединения с базой данных")
    @AfterAll
    static void closeAll() {
        SelenideLogger.removeListener("allure");
        closeDBConnection(connection);
    }

    @Step("Удаление записей из всех таблиц базы данных")
    @AfterEach()
    void removeAllRowsFromDBTables() {
        clearDBTables(connection);
    }

    @Story("20. Отправка POST-запроса на покупку с незаполненным полем \"Месяц\", остальные поля заполнены валидными данными.")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @DisplayName("20. Отправка POST-запроса на покупку с незаполненным полем \"Месяц\", остальные поля заполнены валидными данными.")
    void shouldNotPurchaseApiWithoutMonth() {
        Card approvedCard = Card.generateApprovedCard("en");
        approvedCard.setMonth("");
        String response = API.sendPaymentQuery(approvedCard, apiPurchaseUrl);
        API.assertStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
    }

    @Story("21. Отправка POST-запроса на покупку с незаполненным полем \"Владелец\", остальные поля заполнены валидными данными.")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @DisplayName("21. Отправка POST-запроса на покупку с незаполненным полем \"Владелец\", остальные поля заполнены валидными данными.")
    void shouldNotPurchaseApiWithoutHolder() {
        Card approvedCard = Card.generateApprovedCard("en");
        approvedCard.setHolder("");
        String response = API.sendPaymentQuery(approvedCard, apiPurchaseUrl);
        API.assertStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
    }

    @Story("22. Отправка POST-запроса на покупку со всеми незаполненными полями.")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @DisplayName("22. Отправка POST-запроса на покупку со всеми незаполненными полями.")
    void shouldNotPurchaseApiWithoutAll() {
        Card approvedCard = Card.generateApprovedCard("en");
        approvedCard.setNumber("");
        approvedCard.setMonth("");
        approvedCard.setYear("");
        approvedCard.setHolder("");
        approvedCard.setCvc("");
        String response = API.sendPaymentQuery(approvedCard, apiPurchaseUrl);
        API.assertStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
    }

    @Story("23. Отправка POST-запроса на покупку с полем \"Месяц\", содержащим 1 символ, остальные поля " +
            "заполнены валидными данными.")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @DisplayName("23. Отправка POST-запроса на покупку с полем \"Месяц\", содержащим 1 символ, остальные " +
            "поля заполнены валидными данными.")
    void shouldNotPurchaseApiWithWrongMonth() {
        Card approvedCard = Card.generateApprovedCard("en");
        Faker faker = new Faker(new Locale("en"));
        approvedCard.setMonth(faker.numerify("#"));
        String response = API.sendPaymentQuery(approvedCard, apiPurchaseUrl);
        API.assertStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
    }

    @Story("24. Отправка POST-запроса на покупку со значением, содержащим 1 символ в поле \"Год\", остальные поля" +
            " заполнены валидными данными.")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @DisplayName("24. Отправка POST-запроса на покупку со значением, содержащим 1 символ в поле \"Год\", остальные" +
            " поля заполнены валидными данными.")
    void shouldNotPurchaseApiWithWrongYear() {
        Card approvedCard = Card.generateApprovedCard("en");
        Faker faker = new Faker(new Locale("en"));
        approvedCard.setYear(faker.numerify("#"));
        String response = API.sendPaymentQuery(approvedCard, apiPurchaseUrl);
        API.assertStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
    }

    @Story("25. Отправка POST-запроса на покупку со значением поля \"Владелец\" длиной 257 символов, остальные поля " +
            "заполнены валидными данными.")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @DisplayName("25. Отправка POST-запроса на покупку со значением поля \"Владелец\" длиной 257 символов, остальные " +
            "поля заполнены валидными данными.")
    void shouldNotPurchaseApiWithHolderTooLarge() {
        Card approvedCard = Card.generateApprovedCard("en");
        Faker faker = new Faker(new Locale("en"));
        approvedCard.setHolder(faker.regexify("[a-zA-Z ]{257}"));
        String response = API.sendPaymentQuery(approvedCard, apiPurchaseUrl);
        API.assertStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
    }

    @Story("26. Отправка POST-запроса на покупку с содержащим спецсимволы полем \"Владелец\", остальные поля " +
            "заполнены валидными данными.")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @DisplayName("26. Отправка POST-запроса на покупку с содержащим спецсимволы полем \"Владелец\", остальные поля " +
            "заполнены валидными данными.")
    void shouldNotPurchaseApiWithHolderContainsSpecialSymbols() {
        Card approvedCard = Card.generateApprovedCard("en");
        Faker faker = new Faker(new Locale("en"));
        approvedCard.setHolder(faker.regexify("[a-zA-Z!#$ %^0-9]{32}"));
        String response = API.sendPaymentQuery(approvedCard, apiPurchaseUrl);
        API.assertStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
    }

    @Story("27. Отправка POST-запроса на покупку с полем \"Номер карты\", содержащим 17 символов, остальные поля " +
            "заполнены валидными данными.")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @DisplayName("27. Отправка POST-запроса на покупку с полем \"Номер карты\", содержащим 17 символов, остальные " +
            "поля заполнены валидными данными.")
    void shouldNotPurchaseApiWithNumberTooLarge() {
        Card approvedCard = Card.generateApprovedCard("en");
        Faker faker = new Faker(new Locale("en"));
        approvedCard.setNumber(faker.numerify("#################"));
        String response = API.sendPaymentQuery(approvedCard, apiPurchaseUrl);
        API.assertStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
    }

    @Story("28. Отправка POST-запроса на покупку с полем \"Месяц\", содержащим 3 символа, остальные поля заполнены" +
            " валидными данными.")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @DisplayName("28. Отправка POST-запроса на покупку с полем \"Месяц\", содержащим 3 символа, остальные поля " +
            "заполнены валидными данными.")
    void shouldNotPurchaseApiWithMonthTooLarge() {
        Card approvedCard = Card.generateApprovedCard("en");
        Faker faker = new Faker(new Locale("en"));
        approvedCard.setMonth(faker.numerify("###"));
        String response = API.sendPaymentQuery(approvedCard, apiPurchaseUrl);
        API.assertStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
    }

    @Story("29. Отправка POST-запроса на покупку с полем \"Год\", содержащим 3 символа, остальные поля заполнены " +
            "валидными данными.")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @DisplayName("29. Отправка POST-запроса на покупку с полем \"Год\", содержащим 3 символа, остальные поля " +
            "заполнены валидными данными.")
    void shouldNotPurchaseApiWithYearTooLarge() {
        Card approvedCard = Card.generateApprovedCard("en");
        Faker faker = new Faker(new Locale("en"));
        approvedCard.setYear(faker.numerify("###"));
        String response = API.sendPaymentQuery(approvedCard, apiPurchaseUrl);
        API.assertStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
    }

    @Story("30. Отправка POST-запроса на покупку с полем \"CVC/CVV\", содержащим 4 символа, остальные поля " +
            "заполнены валидными данными.")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @DisplayName("30. Отправка POST-запроса на покупку с полем \"CVC/CVV\", содержащим 4 символа, остальные поля " +
            "заполнены валидными данными.")
    void shouldNotPurchaseApiWithCVCTooLarge() {
        Card approvedCard = Card.generateApprovedCard("en");
        Faker faker = new Faker(new Locale("en"));
        approvedCard.setCvc(faker.numerify("####"));
        String response = API.sendPaymentQuery(approvedCard, apiPurchaseUrl);
        API.assertStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
    }
}
