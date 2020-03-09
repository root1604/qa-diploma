package aqashop;

import aqashop.data.Card;
import aqashop.data.DataHelper;
import aqashop.pages.IndexPage;
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

    @Step("Удаление записей из всех таблиц базы данных")
    @AfterEach()
    void removeAllRowsFromDBTables() {
        clearDBTables(connection);
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
        open(getProperty("environment.aqa-shop.url"));
        connection = getDBConnection();
    }

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
        IndexPage indexPage = new IndexPage();
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
        IndexPage indexPage = new IndexPage();
        indexPage.creditButtonClick();
        Card declinedCard = Card.generateDeclinedCard("en");
        indexPage.fillAndSendForm(declinedCard);
        indexPage.waitForErrorWindow();
        creditDeclined(DataHelper.PaymentResult.DECLINED, connection);
    }
}
