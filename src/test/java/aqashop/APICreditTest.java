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
}
