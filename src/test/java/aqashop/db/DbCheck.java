package aqashop.db;

import aqashop.data.CreditEntity;
import aqashop.data.DataHelper;
import aqashop.data.OrderEntity;
import aqashop.data.PaymentEntity;
import io.qameta.allure.Step;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static aqashop.data.DataHelper.getEnvironmentProperty;
import static org.junit.jupiter.api.Assertions.*;

public class DbCheck {

    private static Connection connection;

    @Step("Создание подключения к базе данных")
    public static void createDBConnection() {
        String mysqlUrl = getEnvironmentProperty("mysql.url");
        String postgresUrl = getEnvironmentProperty("postgresql.url");
        String username = getEnvironmentProperty("jdbc.username");
        String password = getEnvironmentProperty("jdbc.password");
        try {
            connection = DriverManager.getConnection(mysqlUrl, username, password);
            System.out.println("Mysql database detected");
        } catch (SQLException e) {
            System.out.println(e.toString());
            try {
                connection = DriverManager.getConnection(postgresUrl, username, password);
                System.out.println("Postgresql database detected");
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }
    }

    @Step("Покупка не в кредит по действительной карте. Данные записываются в таблицы payment_entity и order_entity")
    public static void purchaseApproved(DataHelper.PaymentResult paymentResult, int price){
        testPurchaseCase(paymentResult, price);
    }

    @Step("Покупка не в кредит по недействительной карте. Данные записываются в таблицы payment_entity и order_entity")
    public static void purchaseDeclined(DataHelper.PaymentResult paymentResult, int price){
        testPurchaseCase(paymentResult, price);
    }

    @Step("Покупка в кредит по действительной карте. Данные записываются в таблицы credit_request_entity " +
            "и order_entity")
    public static void creditApproved(DataHelper.PaymentResult paymentResult){
        testCreditCase(paymentResult);
    }

    @Step("Покупка в кредит по недействительной карте. Данные записываются в таблицы credit_request_entity " +
            "и order_entity")
    public static void creditDeclined(DataHelper.PaymentResult paymentResult){
        testCreditCase(paymentResult);
    }

    @Step("Удаление записей из всех таблиц базы данных")
    public static void clearDBTables() {
        QueryRunner runner = new QueryRunner();
        try  {
            runner.update(connection, "DELETE FROM credit_request_entity;");
            runner.update(connection, "DELETE FROM payment_entity;");
            runner.update(connection, "DELETE FROM order_entity;");
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    @Step("Закрыть соединение с базой данных")
    public static void closeDBConnection() {
        try {
            DbUtils.close(connection);
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    @Step("Проверка информации в базе данных при покупке не в кредит ")
    private static void testPurchaseCase(DataHelper.PaymentResult paymentResult, int price){
        OrderEntity queryResultOrder = getDataFromOrderEntityTable();
        assertsPurchaseCaseOrderEntity(queryResultOrder);
        PaymentEntity queryResultPayment = getDataFromPaymentEntityTable(queryResultOrder.getPayment_id());
        assertsPurchaseCasePaymentEntity(queryResultPayment, queryResultOrder, paymentResult, price);
    }

    @Step("Проверка наличия идентификаторов в таблице order_entity базы данных при покупке")
    private static void assertsPurchaseCaseOrderEntity(OrderEntity queryResultOrder){
        assertNull(queryResultOrder.getCredit_id(), "Присутствует идентификатор кредита в базе данных " +
                "в таблице order_entity, хотя" +
                " это покупка не в кредит");
        assertNotNull(queryResultOrder.getPayment_id(), "Отсутствует идентификатор оплаты в базе данных " +
                "в таблице order_entity, хотя" +
                " это покупка не в кредит");
    }

    @Step("Проверка правильности данных в таблице payment_entity базы данных")
    private static void assertsPurchaseCasePaymentEntity(PaymentEntity queryResultPayment,
                                                         OrderEntity queryResultOrder,
                                                         DataHelper.PaymentResult paymentResult, int price){
        assertEquals(queryResultPayment.getAmount(), price, "Цена тура в базе данных " +
                "в таблице payment_entity не соответствует цене, указанной на веб-странице");
        assertEquals(queryResultPayment.getStatus(), paymentResult.toString(), "Неправильный статус платежа" +
                " в базе данных в таблице payment_entity");
        assertEquals(queryResultPayment.getTransaction_id(), queryResultOrder.getPayment_id(),
                "Неправильный номер транзакции в базе данных в таблице payment_entity");
    }

    @Step("Проверка информации в базе данных при покупке в кредит ")
    public static void testCreditCase(DataHelper.PaymentResult paymentResult) {
        OrderEntity queryResultOrder = getDataFromOrderEntityTable();
        assertsCreditCaseOrderEntity(queryResultOrder);
        CreditEntity queryResultCredit = getDataFromCreditEntityTable(queryResultOrder.getPayment_id());
        assertsCreditCaseCreditEntity(queryResultCredit, queryResultOrder, paymentResult);
    }

    @Step("Проверка наличия идентификаторов в таблице order_entity базы данных при заявке на кредит")
    private static void assertsCreditCaseOrderEntity(OrderEntity queryResultOrder){
        assertNotNull(queryResultOrder.getCredit_id(), "Отсутствует идентификатор кредита в базе данных " +
                "в таблице order_entity, хотя это покупка в кредит");
        assertNull(queryResultOrder.getPayment_id(), "Присутствует идентификатор оплаты в базе данных " +
                "в таблице order_entity, хотя это покупка в кредит");
    }

    @Step("Проверка правильности данных в таблице credit_entity базы данных")
    private static void assertsCreditCaseCreditEntity(CreditEntity queryResultCredit,
                                                         OrderEntity queryResultOrder,
                                                         DataHelper.PaymentResult paymentResult){
        assertEquals(queryResultCredit.getStatus(), paymentResult.toString(), "Неправильный статус платежа" +
                " в базе данных в таблице credit_request_entity");
        assertEquals(queryResultCredit.getBank_id(), queryResultOrder.getPayment_id(),
                "Неправильный номер транзакции в базе данных в таблице credit_request_entity");
    }

    @Step("Запрос из таблицы order_entity базы данных")
    public static OrderEntity getDataFromOrderEntityTable() {
        String orderSQL = "select credit_id, payment_id from order_entity limit 1;";
        QueryRunner runner = new QueryRunner();
        OrderEntity orderEntity = new OrderEntity();
        
        ResultSetHandler<OrderEntity> resultHandlerOrder =
                new BeanHandler<OrderEntity>(OrderEntity.class);
        try {
            orderEntity = runner.query(connection, orderSQL, resultHandlerOrder);
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return orderEntity;
    }

    @Step("Запрос из таблицы payment_entity базы данных при покупке не в кредит")
    public static PaymentEntity getDataFromPaymentEntityTable(String paymentId) {
        PaymentEntity paymentEntity = new PaymentEntity();
        QueryRunner runner = new QueryRunner();
        String paymentSQL = "select amount, status, transaction_id from payment_entity where transaction_id = '"
                + paymentId + "' limit 1;";
        ResultSetHandler<PaymentEntity> resultHandlerPayment =
                new BeanHandler<PaymentEntity>(PaymentEntity.class);
        try {
            paymentEntity = runner.query(connection, paymentSQL, resultHandlerPayment);
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        return paymentEntity;
    }

    @Step("Запрос из таблицы credit_request_entity базы данных при покупке в кредит")
    public static CreditEntity getDataFromCreditEntityTable(String creditId) {
        CreditEntity creditEntity = new CreditEntity();
        QueryRunner runner = new QueryRunner();
        String creditSQL = "select status, bank_id from credit_request_entity where bank_id = '"
                + creditId + "' limit 1;";
        ResultSetHandler<CreditEntity> resultHandlerCredit =
                new BeanHandler<CreditEntity>(CreditEntity.class);
        try {
            creditEntity = runner.query(connection, creditSQL, resultHandlerCredit);
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        return creditEntity;
    }
}
