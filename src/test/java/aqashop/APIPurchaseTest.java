package aqashop;

import aqashop.api.API;
import aqashop.data.Card;
import aqashop.data.DataHelper;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.*;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import java.sql.Connection;

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
}
