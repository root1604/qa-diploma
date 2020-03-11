package aqashop;

import aqashop.data.Card;
import aqashop.data.DataHelper;
import aqashop.pages.IndexPage;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.*;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import java.sql.Connection;

import static aqashop.data.DataHelper.getProperty;
import static aqashop.db.DB.*;
import static com.codeborne.selenide.Selenide.open;

@Feature("Тестирование UI и базы данных при покупке в кредит")
public class UICreditTest {

    static Connection connection;
    static String url;
    static IndexPage indexPage = new IndexPage();

    @Step("Открытие веб-страницы ")
    @BeforeEach
    void setUpEach() {
        open(url);
    }

    @Step("Удаление записей из всех таблиц базы данных")
    @AfterEach()
    void removeAllRowsFromDBTables() {
        clearDBTables(connection);
    }

    @Step("Установка соединения с базой данных")
    @BeforeAll
    static void setUpAll() {
        Configuration.screenshots=false;
        SelenideLogger.addListener("allure", new AllureSelenide());
        url = getProperty("environment.properties", "aqa-shop.url");
        connection = getDBConnection();
    }

    @Step("Закрытие соединения с базой данных")
    @AfterAll
    static void closeAll() {
        SelenideLogger.removeListener("allure");
        closeDBConnection(connection);
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
        creditApproved(DataHelper.PaymentResult.APPROVED, connection);
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
        creditDeclined(DataHelper.PaymentResult.DECLINED, connection);
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
        indexPage.assertMessageMonth(getProperty("data.properties", "indexPage.blankField"));
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
        indexPage.assertMessageHolder(getProperty("data.properties", "indexPage.blankField"));
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
        indexPage.fillAndSendForm(approvedCard);
        indexPage.assertMessageCard(getProperty("data.properties", "indexPage.blankField"));
        indexPage.assertMessageMonth(getProperty("data.properties", "indexPage.blankField"));
        indexPage.assertMessageYear(getProperty("data.properties", "indexPage.blankField"));
        indexPage.assertMessageHolder(getProperty("data.properties", "indexPage.blankField"));
        indexPage.assertMessageCVC(getProperty("data.properties", "indexPage.blankField"));
    }
}
