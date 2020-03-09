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


@Feature("Тестирование UI и базы данных при покупке не в кредит")
public class UIPurchaseTest {

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

    @Story("1. Покупка тура по кнопке \"Купить\" по данным действительной карты (статус карты \"APPROVED\")")
    @Severity(SeverityLevel.BLOCKER)
    @Test
    @DisplayName("1. Покупка тура по кнопке \"Купить\" по данным действительной карты (статус карты \"APPROVED\")")
    void shouldPurchaseByApprovedCard() {
        IndexPage indexPage = new IndexPage();
        indexPage.purchaseButtonClick();
        int price = indexPage.getPrice() * 100;
        Card approvedCard = Card.generateApprovedCard("en");
        indexPage.fillAndSendForm(approvedCard);
        indexPage.waitForOkWindow();
        purchaseApproved(DataHelper.PaymentResult.APPROVED, price, connection);
    }

    @Story("3. Покупка тура по кнопке \"Купить\" по данным недействительной карты (статус карты \"DECLINED\")")
    @Severity(SeverityLevel.BLOCKER)
    @Test
    @DisplayName("3. Покупка тура по кнопке \"Купить\" по данным недействительной карты (статус карты \"DECLINED\")")
    void shouldNotPurchaseByDeclinedCard() {
        IndexPage indexPage = new IndexPage();
        indexPage.purchaseButtonClick();
        int price = indexPage.getPrice() * 100;
        Card declinedCard = Card.generateDeclinedCard("en");
        indexPage.fillAndSendForm(declinedCard);
        indexPage.waitForErrorWindow();
        purchaseDeclined(DataHelper.PaymentResult.DECLINED, price, connection);
    }
}
