package aqashop.tests;

import aqashop.api.ApiClient;
import aqashop.data.Card;
import aqashop.data.DataHelper;
import com.github.javafaker.Faker;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import java.util.Locale;

@Feature("Тестирование API при покупке не в кредит")
public class ApiClientPurchaseTest extends BaseTest{

    @Story("20. Отправка POST-запроса на покупку с незаполненным полем \"Месяц\", остальные поля заполнены " +
            "валидными данными.")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @DisplayName("20. Отправка POST-запроса на покупку с незаполненным полем \"Месяц\", остальные поля заполнены " +
            "валидными данными.")
    void shouldNotPurchaseApiWithoutMonth() {
        Card approvedCard = Card.generateApprovedCard("en");
        approvedCard.setMonth("");
        String response = ApiClient.callSendPaymentQuery(approvedCard);
        ApiClient.assertPaymentStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
    }

    @Story("21. Отправка POST-запроса на покупку с незаполненным полем \"Владелец\", остальные поля заполнены " +
            "валидными данными.")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @DisplayName("21. Отправка POST-запроса на покупку с незаполненным полем \"Владелец\", остальные поля заполнены " +
            "валидными данными.")
    void shouldNotPurchaseApiWithoutHolder() {
        Card approvedCard = Card.generateApprovedCard("en");
        approvedCard.setHolder("");
        String response = ApiClient.callSendPaymentQuery(approvedCard);
        ApiClient.assertPaymentStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
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
        String response = ApiClient.callSendPaymentQuery(approvedCard);
        ApiClient.assertPaymentStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
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
        String response = ApiClient.callSendPaymentQuery(approvedCard);
        ApiClient.assertPaymentStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
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
        String response = ApiClient.callSendPaymentQuery(approvedCard);
        ApiClient.assertPaymentStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
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
        String response = ApiClient.callSendPaymentQuery(approvedCard);
        ApiClient.assertPaymentStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
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
        String response = ApiClient.callSendPaymentQuery(approvedCard);
        ApiClient.assertPaymentStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
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
        String response = ApiClient.callSendPaymentQuery(approvedCard);
        ApiClient.assertPaymentStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
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
        String response = ApiClient.callSendPaymentQuery(approvedCard);
        ApiClient.assertPaymentStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
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
        String response = ApiClient.callSendPaymentQuery(approvedCard);
        ApiClient.assertPaymentStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
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
        String response = ApiClient.callSendPaymentQuery(approvedCard);
        ApiClient.assertPaymentStatus(DataHelper.PaymentResult.DECLINED.toString(), response);
    }
}
