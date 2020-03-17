package aqashop.tests;

import aqashop.api.ApiClient;
import aqashop.data.Card;
import aqashop.data.DataHelper;
import com.github.javafaker.Faker;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import java.util.Locale;

@Feature("Тестирование API при покупке в кредит")
public class ApiClientCreditTest extends BaseTest{

    @Story("31. Отправка POST-запроса на покупку в кредит с незаполненным полем \"Номер карты\", " +
                "остальные поля заполнены валидными данными.")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @DisplayName("31. Отправка POST-запроса на покупку в кредит с незаполненным полем \"Номер карты\", " +
            "остальные поля заполнены валидными данными.")
    void shouldNotCreditApiWithoutHolder() {
        Card approvedCard = Card.generateApprovedCard("en");
        approvedCard.setHolder("");
        String response = ApiClient.callSendCreditQuery(approvedCard);
        ApiClient.assertPaymentStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
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
        String response = ApiClient.callSendCreditQuery(approvedCard);
        ApiClient.assertPaymentStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
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
        String response = ApiClient.callSendCreditQuery(approvedCard);
        ApiClient.assertPaymentStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
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
        String response = ApiClient.callSendCreditQuery(approvedCard);
        ApiClient.assertPaymentStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
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
    String response = ApiClient.callSendCreditQuery(approvedCard);
    ApiClient.assertPaymentStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
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
        String response = ApiClient.callSendCreditQuery(approvedCard);
        ApiClient.assertPaymentStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
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
        String response = ApiClient.callSendCreditQuery(approvedCard);
        ApiClient.assertPaymentStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
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
        String response = ApiClient.callSendCreditQuery(approvedCard);
        ApiClient.assertPaymentStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
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
        String response = ApiClient.callSendCreditQuery(approvedCard);
        ApiClient.assertPaymentStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
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
        int factStatusCode = ApiClient.callSendWrongCreditQuery(approvedCard);
        int expectedStatusCode = 400;
        ApiClient.assertStatusCode(expectedStatusCode, factStatusCode);
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
        int factStatusCode = ApiClient.callSendWrongCreditQuery(approvedCard);
        int expectedStatusCode = 400;
        ApiClient.assertStatusCode(expectedStatusCode, factStatusCode);
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
        int factStatusCode = ApiClient.callSendWrongCreditQuery(approvedCard);
        int expectedStatusCode = 400;
        ApiClient.assertStatusCode(expectedStatusCode, factStatusCode);
    }
}
