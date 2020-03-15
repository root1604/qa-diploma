package aqashop.api;

import aqashop.data.Card;
import aqashop.data.DataHelper;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiClient {
    private static String baseUrl = DataHelper.getEnvironmentProperty("aqa-shop.base-url");
    private static int port = Integer.parseInt(DataHelper.getEnvironmentProperty(
            "aqa-shop.app-port"));
    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri(baseUrl)
            .setPort(port)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .addFilter(new AllureRestAssured())
            .build();

    @Step("Отправка POST запроса на сервер")
    public static String sendPaymentQuery(Card card, String url) {
        DataHelper.ResponseApi bodyResponse = given()
                .spec(requestSpec)
                .body(card)
                .when()
                .post(url)
                .then()
                .statusCode(200)
                .extract()
                .body().as(DataHelper.ResponseApi.class);
        return bodyResponse.getStatus();
    }

    @Step("Отправка неправильного POST запроса на сервер")
    public static int sendWrongPaymentQuery(Card card, String url) {
        Response bodyResponse = given()
                .spec(requestSpec)
                .body(card)
                .when()
                .post(url);

        return bodyResponse.getStatusCode();
    }

    @Step("Проверка статуса платежа")
    public static void assertPaymentStatus(String expect, String fact) {
        assertEquals(expect, fact, "Статус платежа должен быть " + expect);
    }

    @Step("Проверка кода ответа POST-запроса")
    public static void assertStatusCode(int expect, int fact) {
        assertEquals(expect, fact, "Статус платежа должен быть " + expect);
    }
}
