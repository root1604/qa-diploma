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

@Feature("Тестирование API при покупке в кредит")
public class APICreditTest {
    static Connection connection;
    private static String apiCreditUrl;

    @Step("Установка соединения с базой данных")
    @BeforeAll
    static void setUpAll() {
        Configuration.screenshots=false;
        SelenideLogger.addListener("allure", new AllureSelenide());
        apiCreditUrl = getProperty("environment.properties", "aqa-shop.apiCreditUrl");
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

    @Story("31. Отправка POST-запроса на покупку в кредит с незаполненным полем \"Номер карты\", " +
                "остальные поля заполнены валидными данными.")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @DisplayName("31. Отправка POST-запроса на покупку в кредит с незаполненным полем \"Номер карты\", " +
            "остальные поля заполнены валидными данными.")
    void shouldNotCreditApiWithoutHolder() {
        Card approvedCard = Card.generateApprovedCard("en");
        approvedCard.setHolder("");
        String response = API.sendPaymentQuery(approvedCard, apiCreditUrl);
        API.assertStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
    }

    @Story("32. Отправка POST-запроса на покупку в кредит с незаполненным полем \"Год\", " +
            "остальные поля заполнены валидными данными.")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @DisplayName("32. Отправка POST-запроса на покупку в кредит с незаполненным полем \"Год\", " +
            "остальные поля заполнены валидными данными.")
    void shouldNotCreditApiWithoutYear() {
        Card approvedCard = Card.generateApprovedCard("en");
        approvedCard.setYear("");
        String response = API.sendPaymentQuery(approvedCard, apiCreditUrl);
        API.assertStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
    }

    @Story("33. Отправка POST-запроса на покупку в кредит с незаполненным полем \"CVC/CVV\", " +
            "остальные поля заполнены валидными данными.")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @DisplayName("33. Отправка POST-запроса на покупку в кредит с незаполненным полем \"CVC/CVV\", " +
            "остальные поля заполнены валидными данными.")
    void shouldNotCreditApiWithoutCvc() {
        Card approvedCard = Card.generateApprovedCard("en");
        approvedCard.setCvc("");
        String response = API.sendPaymentQuery(approvedCard, apiCreditUrl);
        API.assertStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
    }

    @Story("34. Отправка POST-запроса на покупку в кредит с полем \"Номер карты\", содержащим 15 символов," +
            " остальные поля заполнены валидными данными.")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @DisplayName("34. Отправка POST-запроса на покупку в кредит с полем \"Номер карты\", содержащим 15 символов, " +
            "остальные поля заполнены валидными данными.")
    void shouldNotCreditApiWithNumberTooShort() {
        Faker faker = new Faker(new Locale("en"));
        Card approvedCard = Card.generateApprovedCard("en");
        approvedCard.setNumber(faker.numerify("###############"));
        String response = API.sendPaymentQuery(approvedCard, apiCreditUrl);
        API.assertStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
    }

    @Story("35. Отправка POST-запроса на покупку в кредит со значением больше 12 и меньше 100 в поле \"Месяц\", " +
    "остальные поля заполнены валидными данными.")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @DisplayName("35. Отправка POST-запроса на покупку в кредит со значением больше 12 и меньше 100 " +
    "в поле \"Месяц\", остальные поля заполнены валидными данными.")
    void shouldNotCreditApiWithWrongMonth() {
    Faker faker = new Faker(new Locale("en"));
    Card approvedCard = Card.generateApprovedCard("en");
    approvedCard.setMonth(faker.regexify("[1-9][3-9]|[2-9][0-9]"));
    String response = API.sendPaymentQuery(approvedCard, apiCreditUrl);
    API.assertStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
    }

    @Story("36. Отправка POST-запроса на покупку в кредит со значением больше 30 и меньше 100 в поле \"Год\", " +
            "остальные поля заполнены валидными данными.")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @DisplayName("36. Отправка POST-запроса на покупку в кредит со значением больше 30 и меньше 100 в поле \"Год\"," +
            " остальные поля заполнены валидными данными.")
    void shouldNotCreditApiWithWrongYear() {
        Faker faker = new Faker(new Locale("en"));
        Card approvedCard = Card.generateApprovedCard("en");
        approvedCard.setYear(faker.regexify("[3-9][0-9]"));
        String response = API.sendPaymentQuery(approvedCard, apiCreditUrl);
        API.assertStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
    }

    @Story("37. Отправка POST-запроса на покупку в кредит с заполненным на кириллице полем \"Владелец\", остальные " +
            "поля заполнены валидными данными.")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @DisplayName("37. Отправка POST-запроса на покупку в кредит с заполненным на кириллице полем \"Владелец\", " +
            "остальные поля заполнены валидными данными.")
    void shouldNotCreditApiWithWrongHolder() {
        Faker faker = new Faker(new Locale("ru"));
        Card approvedCard = Card.generateApprovedCard("en");
        approvedCard.setHolder(faker.name().fullName());
        String response = API.sendPaymentQuery(approvedCard, apiCreditUrl);
        API.assertStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
    }

    @Story("38. Отправка POST-запроса на покупку в кредит со значением, содержащим 2 символа в поле \"CVC/CVV\", " +
            "остальные поля заполнены валидными данными.")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @DisplayName("38. Отправка POST-запроса на покупку в кредит со значением, содержащим 2 символа " +
            "в поле \"CVC/CVV\", остальные поля заполнены валидными данными.")
    void shouldNotCreditApiWithCvcTooShort() {
        Faker faker = new Faker(new Locale("en"));
        Card approvedCard = Card.generateApprovedCard("en");
        approvedCard.setCvc(faker.numerify("##"));
        String response = API.sendPaymentQuery(approvedCard, apiCreditUrl);
        API.assertStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
    }

    @Story("39. Отправка POST-запроса на покупку в кредит с полем \"Номер карты\", содержащим 16 символов, " +
            "с отличающимися от цифр значениями, остальные поля заполнены валидными данными.")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @DisplayName("39. Отправка POST-запроса на покупку в кредит с полем \"Номер карты\", содержащим 16 символов, " +
            "с отличающимися от цифр значениями, остальные поля заполнены валидными данными.")
    void shouldNotCreditApiWithWrongNumber() {
        Faker faker = new Faker(new Locale("en"));
        Card approvedCard = Card.generateApprovedCard("en");
        approvedCard.setNumber(faker.regexify("[a-zA-Z!#$%^0-9]{16}"));
        String response = API.sendPaymentQuery(approvedCard, apiCreditUrl);
        API.assertStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
    }

    @Story("40. Отправка POST-запроса на покупку в кредит с полем \"Месяц\", содержащим 2 символа, " +
            "с отличающимися от цифр значениями, остальные поля заполнены валидными данными.")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @DisplayName("40. Отправка POST-запроса на покупку в кредит с полем \"Месяц\", содержащим 2 символа, " +
            "с отличающимися от цифр значениями, остальные поля заполнены валидными данными.")
    void shouldNotCreditApiWithWrongMonthSymbols() {
        Faker faker = new Faker(new Locale("en"));
        Card approvedCard = Card.generateApprovedCard("en");
        approvedCard.setMonth(faker.regexify("[a-zA-Z!#$%^]{2}"));
        String response = API.sendPaymentQuery(approvedCard, apiCreditUrl);
        API.assertStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
    }

    @Story("41. Отправка POST-запроса на покупку в кредит с полем \"Год\", содержащим 2 символа, " +
            "с отличающимися от цифр значениями, остальные поля заполнены валидными данными.")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @DisplayName("41. Отправка POST-запроса на покупку в кредит с полем \"Год\", содержащим 2 символа, " +
            "с отличающимися от цифр значениями, остальные поля заполнены валидными данными.")
    void shouldNotCreditApiWithWrongYearSymbols() {
        Faker faker = new Faker(new Locale("en"));
        Card approvedCard = Card.generateApprovedCard("en");
        approvedCard.setYear(faker.regexify("[a-zA-Z!#$%^]{2}"));
        String response = API.sendPaymentQuery(approvedCard, apiCreditUrl);
        API.assertStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
    }

    @Story("42. Отправка POST-запроса на покупку в кредит с полем \"CVC/CVV\", содержащим 3 символа," +
            " с отличающимися от цифр значениями, остальные поля заполнены валидными данными.")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @DisplayName("42. Отправка POST-запроса на покупку в кредит с полем \"CVC/CVV\", содержащим 3 символа, " +
            "с отличающимися от цифр значениями, остальные поля заполнены валидными данными.")
    void shouldNotCreditApiWithWrongCvcSymbols() {
        Faker faker = new Faker(new Locale("en"));
        Card approvedCard = Card.generateApprovedCard("en");
        approvedCard.setCvc(faker.regexify("[a-zA-Z!#$%^]{3}"));
        String response = API.sendPaymentQuery(approvedCard, apiCreditUrl);
        API.assertStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
    }
}
