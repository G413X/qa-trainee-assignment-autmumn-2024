package api;

import io.restassured.response.Response;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.List;


public class PostAdsTest {

    static {
        RestAssured.baseURI = "https://qa-internship.avito.com";
    }
    @Test
    public void shouldCreateAdSuccessfully() {
        System.out.println("\n" + "Check that ads with valid data are created successfully ");

        String requestBody = "{\n" +
                "  \"name\": \"Телефон\",\n" +
                "  \"price\": 85566,\n" +
                "  \"sellerId\": 600100,\n" +
                "  \"statistics\": {\n" +
                "    \"contacts\": 10,\n" +
                "    \"likes\": 10,\n" +
                "    \"viewCount\": 10\n" +
                "  }\n" +
                "}";

        String statusMessage = given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(requestBody)  // Тело запроса
                .when()
                .post("/api/1/item")
                .then()
                .log().body()
                .statusCode(200)
                .body("status", containsString("Сохранили объявление"))  // Проверяем наличие сообщения
                .body("status", matchesPattern(".*[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}$"))
                .extract()
                .path("status");
        String adId = statusMessage.replaceAll(".*-\\s", "").replace("\"", "");
        System.out.println(adId);

        given()
                .pathParam("id", adId)
                .when()
                .get("/api/1/item/{id}")
                .then()
                .log().body()
                .statusCode(200)
                .body("id[0]", equalTo(adId))
                .body("name[0]", equalTo("Телефон"))
                .body("price[0]", equalTo(85566))
                .body("sellerId[0]", equalTo(600100))
                .body("statistics[0].contacts", equalTo(10))
                .body("statistics[0].likes", equalTo(10))
                .body("statistics[0].viewCount", equalTo(10));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            // invalid name values
            "{ \"name\": -10, \"price\": 85566, \"sellerId\": 600100, \"statistics\": { \"contacts\": 32, \"likes\": 35, \"viewCount\": 14 }}",
            "{ \"name\": null, \"price\": 85566, \"sellerId\": 600100, \"statistics\": { \"contacts\": 32, \"likes\": 35, \"viewCount\": 14 }}",
            "{ \"name\": \"\", \"price\": 85566, \"sellerId\": 600100, \"statistics\": { \"contacts\": 32, \"likes\": 35, \"viewCount\": 14 }}",
            "{ \"name\": \" \", \"price\": 85566, \"sellerId\": 600100, \"statistics\": { \"contacts\": 32, \"likes\": 35, \"viewCount\": 14 }}",
            "{ \"name\": \"@@@@!!!\", \"price\": 85566, \"sellerId\": 600100, \"statistics\": { \"contacts\": 32, \"likes\": 35, \"viewCount\": 14 }}",
            "{ \"name\": [\"Телефон\"], \"price\": 85566, \"sellerId\": 600100, \"statistics\": { \"contacts\": 32, \"likes\": 35, \"viewCount\": 14 }}",
            "{ \"name\": { \"type\": \"Телефон\" }, \"price\": 85566, \"sellerId\": 600100, \"statistics\": { \"contacts\": 32, \"likes\": 35, \"viewCount\": 14 }}",
    })
    public void shouldReturn400WhenNameIsInvalid(String requestBody) {
        System.out.println("Request body is the following: " + "\n" + requestBody);

        given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(requestBody)
                .when()
                .post("/api/1/item")
                .then()
                .log().status()
                .statusCode(400);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {
            //invalid prices
            "{ \"name\": \"Телефон\", \"price\": -100, \"sellerId\": 600100, \"statistics\": " +
                    "{ \"contacts\": 32, \"likes\": 35, \"viewCount\": 14 }}", // negative number

            "{ \"name\": \"Телефон\", \"price\": null, \"sellerId\": 600100, \"statistics\": " +
                    "{ \"contacts\": 32, \"likes\": 35, \"viewCount\": 14 }}", // null

            "{ \"name\": \"Телефон\", \"price\": \"\", \"sellerId\": 600100, \"statistics\": " +
                    "{ \"contacts\": 32, \"likes\": 35, \"viewCount\": 14 }}", // empty string

            "{ \"name\": \"Телефон\", \"price\": \"text\", \"sellerId\": 600100, \"statistics\": " +
                    "{ \"contacts\": 32, \"likes\": 35, \"viewCount\": 14 }}", // text

            "{ \"name\": \"Телефон\", \"price\": [100], \"sellerId\": 600100, \"statistics\": " +
                    "{ \"contacts\": 32, \"likes\": 35, \"viewCount\": 14 }}", // array

            "{ \"name\": \"Телефон\", \"price\": { \"amount\": 100 }, \"sellerId\": 600100, \"statistics\": " +
                    "{ \"contacts\": 32, \"likes\": 35, \"viewCount\": 14 }}", // object

            "{ \"name\": \"Телефон\", \"price\": 10 10, \"sellerId\": 600100, \"statistics\": " +
                    "{ \"contacts\": 32, \"likes\": 35, \"viewCount\": 14 }}", // separate numbers

    })
    public void shouldReturn400WhenPriceIsInvalid(String requestBody) {
        System.out.println("Request body is the following: " + "\n" + requestBody);

        given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(requestBody)
                .when()
                .post("/api/1/item")
                .then()
                .log().status()
                .statusCode(400);
        try {
            Thread.sleep(1000);  // Задержка
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @ParameterizedTest
    @ValueSource(strings = {
            //invalid sellerIds
            "{ \"name\": \"Телефон\", \"price\": 85566, \"sellerId\": -100, \"statistics\": " +
                    "{ \"contacts\": 32, \"likes\": 35, \"viewCount\": 14 }}", // negative number

            "{ \"name\": \"Телефон\", \"price\": 85566, \"sellerId\": null, \"statistics\": {" +
                    " \"contacts\": 32, \"likes\": 35, \"viewCount\": 14 }}", // null

            "{ \"name\": \"Телефон\", \"price\": 85566, \"sellerId\": \"\", \"statistics\": " +
                    "{ \"contacts\": 32, \"likes\": 35, \"viewCount\": 14 }}", // empty string

            "{ \"name\": \"Телефон\", \"price\": 85566, \"sellerId\": \"text\", \"statistics\": " +
                    "{ \"contacts\": 32, \"likes\": 35, \"viewCount\": 14 }}", // text

            "{ \"name\": \"Телефон\", \"price\": 85566, \"sellerId\": 600100.5, \"statistics\": " +
                    "{ \"contacts\": 32, \"likes\": 35, \"viewCount\": 14 }}", // floating point number

            "{ \"name\": \"Телефон\", \"price\": 85566, \"sellerId\": [600100], \"statistics\": " +
                    "{ \"contacts\": 32, \"likes\": 35, \"viewCount\": 14 }}", // array

            "{ \"name\": \"Телефон\", \"price\": 85566, \"sellerId\": { \"id\": 600100 }, \"statistics\": " +
                    "{ \"contacts\": 32, \"likes\": 35, \"viewCount\": 14 }}", // object
    })
    public void shouldReturn400WhenSellerIdIsInvalid(String requestBody) {
        System.out.println("Request body is the following: " + "\n" + requestBody);

        given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(requestBody)
                .when()
                .post("/api/1/item")
                .then()
                .log().status()
                .statusCode(400);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {
            //invalid sellerIds
            "{ \"name\": \"Телефон\", \"price\": 85566, \"sellerId\": 600100, \"statistics\": " +
                    "{ \"contacts\": -10, \"likes\": 35, \"viewCount\": 14 }}", // negative number

            "{ \"name\": \"Телефон\", \"price\": 85566, \"sellerId\": 600100, \"statistics\": " +
                    "{ \"contacts\": null, \"likes\": 35, \"viewCount\": 14 }}", // null

            "{ \"name\": \"Телефон\", \"price\": 85566, \"sellerId\": 600100, \"statistics\": " +
                    "{ \"contacts\": \"\", \"likes\": 35, \"viewCount\": 14 }}", // empty string

            "{ \"name\": \"Телефон\", \"price\": 85566, \"sellerId\": 600100, \"statistics\": " +
                    "{ \"contacts\": \"text\", \"likes\": 35, \"viewCount\": 14 }}", // text

            "{ \"name\": \"Телефон\", \"price\": 85566, \"sellerId\": 600100, \"statistics\": " +
                    "{ \"contacts\": [32], \"likes\": 35, \"viewCount\": 14 }}", // array

            "{ \"name\": \"Телефон\", \"price\": 85566, \"sellerId\": 600100, \"statistics\": " +
                    "{ \"contacts\": { \"count\": 32 }, \"likes\": 35, \"viewCount\": 14 }}", // object
    })
    public void shouldReturn400WhenContactsIsInvalid(String requestBody) {
        System.out.println("Request body is the following: " + "\n" + requestBody);

        given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(requestBody)
                .when()
                .post("/api/1/item")
                .then()
                .log().status()
                .statusCode(400);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {
            // invalid likes
            "{ \"name\": \"Телефон\", \"price\": 85566, \"sellerId\": 600100, \"statistics\": " +
                    "{ \"contacts\": 32, \"likes\": -1, \"viewCount\": 14 }}", // negative

            "{ \"name\": \"Телефон\", \"price\": 85566, \"sellerId\": 600100, \"statistics\": " +
                    "{ \"contacts\": 32, \"likes\": null, \"viewCount\": 14 }}", // null

            "{ \"name\": \"Телефон\", \"price\": 85566, \"sellerId\": 600100, \"statistics\": " +
                    "{ \"contacts\": 32, \"likes\": \"\", \"viewCount\": 14 }}", // empty string

            "{ \"name\": \"Телефон\", \"price\": 85566, \"sellerId\": 600100, \"statistics\": " +
                    "{ \"contacts\": 32, \"likes\": \"text\", \"viewCount\": 14 }}", // text

            "{ \"name\": \"Телефон\", \"price\": 85566, \"sellerId\": 600100, \"statistics\": " +
                    "{ \"contacts\": 32, \"likes\": [35], \"viewCount\": 14 }}", // array

            "{ \"name\": \"Телефон\", \"price\": 85566, \"sellerId\": 600100, \"statistics\": " +
                    "{ \"contacts\": 32, \"likes\": { \"count\": 35 }, \"viewCount\": 14 }}", // object

            "{ \"name\": \"Телефон\", \"price\": 85566, \"sellerId\": 600100, \"statistics\": " +
                    "{ \"contacts\": 32, \"likes\": 30.5, \"viewCount\": 14 }}", // floating point
    })
    public void shouldReturn400WhenLikesIsInvalid(String requestBody) {
        System.out.println("Request body is the following: " + "\n" + requestBody);

        given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(requestBody)
                .when()
                .post("/api/1/item")
                .then()
                .log().status()
                .statusCode(400);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @ParameterizedTest
    @ValueSource(strings = {
            // invalid viewCount
            "{ \"name\": \"Телефон\", \"price\": 85566, \"sellerId\": 600100, \"statistics\": " +
                    "{ \"contacts\": 32, \"likes\": 35, \"viewCount\": -10 }}", // negative

            "{ \"name\": \"Телефон\", \"price\": 85566, \"sellerId\": 600100, \"statistics\": " +
                    "{ \"contacts\": 32, \"likes\": 35, \"viewCount\": null }}", // null

            "{ \"name\": \"Телефон\", \"price\": 85566, \"sellerId\": 600100, \"statistics\": " +
                    "{ \"contacts\": 32, \"likes\": 35, \"viewCount\": \"\" }}", // empty string

            "{ \"name\": \"Телефон\", \"price\": 85566, \"sellerId\": 600100, \"statistics\": " +
                    "{ \"contacts\": 32, \"likes\": 35, \"viewCount\": \"text\" }}", // text

            "{ \"name\": \"Телефон\", \"price\": 85566, \"sellerId\": 600100, \"statistics\": " +
                    "{ \"contacts\": 32, \"likes\": 35, \"viewCount\": [14] }}", // array

            "{ \"name\": \"Телефон\", \"price\": 85566, \"sellerId\": 600100, \"statistics\": " +
                    "{ \"contacts\": 32, \"likes\": 35, \"viewCount\": 15.5}}", // floating point

            "{ \"name\": \"Телефон\", \"price\": 85566, \"sellerId\": 600100, \"statistics\": " +
                    "{ \"contacts\": 32, \"likes\": 35, \"viewCount\": { \"count\": 14 }}}" // object
    })
    public void shouldReturn400WhenViewCountIsInvalid(String requestBody) {
        System.out.println("Request body is the following: " + "\n" + requestBody);

        given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(requestBody)
                .when()
                .post("/api/1/item")
                .then()
                .log().status()
                .statusCode(400);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"GET", "PUT", "PATCH", "DELETE", "HEAD", "OPTIONS"})

    public void shouldReturn405ForInvalidHttpMethods(String method) {
        System.out.println("Request method is the following: " + method);

        given()
                .header("Content-Type", "application/json")
                .when()
                .request(method, "/api/1/item")  // Используем разные HTTP-методы
                .then()
                .log().body()
                .statusCode(405);  // Ожидаем 405 Method Not Allowed

        System.out.println("PASS" + "\n");
    }

    @Test
    public void shouldReturn400ForInvalidContentType() {
        String requestBody = "{\n" +
                "  \"name\": \"Телефон\",\n" +
                "  \"price\": 10,\n" +
                "  \"sellerId\": 600100,\n" +
                "  \"statistics\": {\n" +
                "    \"contacts\": 100,\n" +
                "    \"likes\": 24,\n" +
                "    \"viewCount\": 14\n" +
                "  }\n" +
                "}";

        given()
                .header("Content-Type", "text/plain")  // Указываем неправильный Content-Type
                .header("Accept", "application/json")
                .body(requestBody)  // Передаём тело запроса
                .when()
                .post("/api/1/item")  // Выполняем POST-запрос
                .then()
                .log().body()
                .statusCode(400);  // Ожидаем, что сервер вернёт 415 Unsupported Media Type
    }
}





