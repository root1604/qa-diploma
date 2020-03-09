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

import static aqashop.data.DataHelper.getProperty;
import static org.junit.jupiter.api.Assertions.*;

public class DB {

    @Step("Создание подключения к базе данных")
    public static Connection getDBConnection() {
        Connection conn = null;
        String mysqlUrl = getProperty("environment.mysql.url");
        String postgresUrl = getProperty("environment.postgresql.url");
        String username = getProperty("environment.jdbc.username");
        String password = getProperty("environment.jdbc.password");
        try {
            conn = DriverManager.getConnection(mysqlUrl, username, password);
            System.out.println("Mysql database detected");
        } catch (SQLException e) {
            System.out.println(e.toString());
            try {
                conn = DriverManager.getConnection(postgresUrl, username, password);
                System.out.println("Postgresql database detected");
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }
        return conn;
    }

    @Step("43. Покупка не в кредит по действительной карте. Данные записываются в таблицы payment_entity и order_entity")
    public static void purchaseApproved(DataHelper.PaymentResult paymentResult, int price, Connection conn){
        testPurchaseCase(paymentResult, price, conn);
    }

    @Step("44. Покупка не в кредит по недействительной карте. Данные записываются в таблицы payment_entity и order_entity")
    public static void purchaseDeclined(DataHelper.PaymentResult paymentResult, int price, Connection conn){
        testPurchaseCase(paymentResult, price, conn);
    }

    @Step("45. Покупка в кредит по действительной карте. Данные записываются в таблицы credit_request_entity и order_entity")
    public static void creditApproved(DataHelper.PaymentResult paymentResult, Connection conn){
        testCreditCase(paymentResult, conn);
    }

    @Step("46. Покупка в кредит по недействительной карте. Данные записываются в таблицы credit_request_entity и order_entity (?) ")
    public static void creditDeclined(DataHelper.PaymentResult paymentResult, Connection conn){
        testCreditCase(paymentResult, conn);
    }

    @Step("Удаление записей из всех таблиц базы данных")
    public static void clearDBTables(Connection conn) {
        QueryRunner runner = new QueryRunner();
        try  {
            runner.update(conn, "DELETE FROM credit_request_entity;");
            runner.update(conn, "DELETE FROM payment_entity;");
            runner.update(conn, "DELETE FROM order_entity;");
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    @Step("Закрыть соединение с базой данных")
    public static void closeDBConnection(Connection conn) {
        try {
            DbUtils.close(conn);
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    @Step("Проверка информации в базе данных при покупке не в кредит ")
    private static void testPurchaseCase(DataHelper.PaymentResult paymentResult, int price, Connection conn){
        String creditId;
        String paymentId;
        int amount;
        String status;
        String transactionId;

        try {
            OrderEntity queryResultOrder = getDataFromOrderEntityTable(conn);
            creditId = queryResultOrder.getCredit_id();
            paymentId = queryResultOrder.getPayment_id();
        }
        catch (Exception e){
            creditId = null;
            paymentId = null;
        }
        assertNull(creditId, "Присутствует идентификатор кредита в базе данных в таблице order_entity, хотя" +
                " это покупка не в кредит");
        assertNotNull(paymentId, "Отсутствует идентификатор оплаты в базе данных в таблице order_entity, хотя" +
                " это покупка не в кредит");
        try {
            PaymentEntity queryResultPayment = getDataFromPaymentEntityTable(conn, paymentId);
            amount = queryResultPayment.getAmount();
            status = queryResultPayment.getStatus();
            transactionId = queryResultPayment.getTransaction_id();
        }
        catch (Exception e){
            amount = 0;
            status = null;
            transactionId = null;
        }
        assertEquals(amount, price, "Цена тура в базе данных в таблице payment_entity " +
                "не соответствует цене, указанной на веб-странице");
        assertEquals(status, paymentResult.toString(), "Неправильный статус платежа в базе данных в таблице " +
                "payment_entity");
        assertEquals(transactionId, paymentId, "Неправильный номер транзакции в базе данных в таблице" +
                " payment_entity");
    }

    @Step("Проверка информации в базе данных при покупке в кредит ")
    public static void testCreditCase(DataHelper.PaymentResult paymentResult, Connection conn) {
        String creditId;
        String paymentId;
        String status;
        String transactionId;

        try {
            OrderEntity queryResultOrder = getDataFromOrderEntityTable(conn);
            creditId = queryResultOrder.getCredit_id();
            paymentId = queryResultOrder.getPayment_id();
        } catch (Exception e) {
            creditId = null;
            paymentId = null;
        }
        assertNotNull(creditId, "Отсутствует идентификатор кредита в базе данных в таблице order_entity, хотя" +
                " это покупка в кредит");
        assertNull(paymentId, "Присутствует идентификатор оплаты в базе данных в таблице order_entity, хотя" +
                " это покупка в кредит");
        try {
            CreditEntity queryResultCredit = getDataFromCreditEntityTable(conn, creditId);
            status = queryResultCredit.getStatus();
            transactionId = queryResultCredit.getBank_id();
        } catch (Exception e) {
            status = null;
            transactionId = null;
        }
        assertEquals(status, paymentResult.toString(), "Неправильный статус платежа в базе данных в таблице " +
                "credit_request_entity");
        assertEquals(transactionId, paymentId, "Неправильный номер транзакции в базе данных в таблице" +
                " credit_request_entity");
    }

    @Step("Запрос из таблицы order_entity базы данных")
    public static OrderEntity getDataFromOrderEntityTable(Connection conn) {
        String orderSQL = "select credit_id, payment_id from order_entity limit 1;";
        QueryRunner runner = new QueryRunner();
        OrderEntity orderEntity = new OrderEntity();
        
        ResultSetHandler<OrderEntity> resultHandlerOrder =
                new BeanHandler<OrderEntity>(OrderEntity.class);
        try {
            orderEntity = runner.query(conn, orderSQL, resultHandlerOrder);
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        return orderEntity;
    }

    @Step("Запрос из таблицы payment_entity базы данных при покупке не в кредит")
    public static PaymentEntity getDataFromPaymentEntityTable(Connection conn, String paymentId) {
        PaymentEntity paymentEntity = new PaymentEntity();
        QueryRunner runner = new QueryRunner();
        String paymentSQL = "select amount, status, transaction_id from payment_entity where transaction_id = '"
                + paymentId + "' limit 1;";
        ResultSetHandler<PaymentEntity> resultHandlerPayment =
                new BeanHandler<PaymentEntity>(PaymentEntity.class);
        try {
            paymentEntity = runner.query(conn, paymentSQL, resultHandlerPayment);
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        return paymentEntity;
    }

    @Step("Запрос из таблицы credit_request_entity базы данных при покупке в кредит")
    public static CreditEntity getDataFromCreditEntityTable(Connection conn, String creditId) {
        CreditEntity creditEntity = new CreditEntity();
        QueryRunner runner = new QueryRunner();
        String creditSQL = "select status, bank_id from credit_request_entity where bank_id = '"
                + creditId + "' limit 1;";
        ResultSetHandler<CreditEntity> resultHandlerCredit =
                new BeanHandler<CreditEntity>(CreditEntity.class);
        try {
            creditEntity = runner.query(conn, creditSQL, resultHandlerCredit);
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        return creditEntity;
    }
}
