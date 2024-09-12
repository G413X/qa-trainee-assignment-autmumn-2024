package api;

import model.Ad;
import io.restassured.response.Response;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

public class GetSellersAdsTest {

    static {
        RestAssured.baseURI = "https://qa-internship.avito.com";
    }

    @Test
    public void testAllAdsBelongToTheSameSeller() {
        int sellerID = 3452; //  specify sellersID
        System.out.println("""

        Check that all seller's ads have the same matching sellerId:
        """);
        System.out.println("sellerID is: " + sellerID);

        // Выполняем GET запрос и десериализуем только поле sellerId
        Response response =
                given()
                        .pathParam("sellerID", sellerID)
                        .when()
                        .get("/api/1/{sellerID}/item")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        // Преобразуем JSON массив в список объектов Ad с одним полем sellerId
        List<Ad> ads = response.jsonPath().getList("", Ad.class);

        // Проверяем, что sellerId в каждом объявлении совпадает с ожидаемым sellerID
        for (Ad ad : ads) {
            //System.out.println(sellerID);
            assertEquals(sellerID, ad.getSellerId(),
                    "Seller ID in ad does not match expected seller ID.");
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {
            //lower positive boundary
            111111,
            // positive equivalence class representative
            500000,
            //upper positive boundary
            999999})

       public void shouldReturn200WhenSellerIdIsValid(int sellerID) {
           System.out.println("\n" + "Check that response status codes are 200 for valid sellerID: "
                   + sellerID);

           given()
                   .pathParam("sellerID", sellerID)
                   .when()
                   .get("/api/1/{sellerID}/item")
                   .then()
                   .statusCode(200);

        System.out.println("Passed");
       }

    @ParameterizedTest
    @ValueSource(ints = {
            //one step below lower positive boundary
            111110,
            // zero
            0,
            //negative value
            -1,
            //one step above upper positive boundary
            1000000
            })
    public void shouldNotReturn200WhenSellerIdIsInvalid(int sellerID) {
        System.out.println("\n" + "Check that response status code is NOT 200 for Invalid sellerID: "
                + sellerID);

        given()
                .pathParam("sellerID", sellerID)  // Передаём sellerID как строку
                .when()
                .get("/api/1/{sellerID}/item")
                .then()
                .statusCode(allOf(not(greaterThanOrEqualTo(200)), not(lessThan(300))));

        System.out.println("Passed");
    }

    @Test
    public void shouldReturnCorrectContentType() {
        int sellerID = 3452; //  specify sellersID
        System.out.println("\n" + "Check that response headers contain content-type: application/json"
                + " " + "for sellerId: " + sellerID);

        given()
                .pathParam("sellerID", sellerID)  // Пример sellerID
                .when()
                .get("/api/1/{sellerID}/item")  // Выполняем GET запрос
                .then()
                .log().headers()
                .statusCode(200)  // Проверяем, что статус-код 200 OK
                .header("Content-Type", equalTo("application/json"));
    }

    @Test
    public void shouldReturnArrayOfAds() {
        int sellerID = 3452; //  specify sellersID
        System.out.println("\n" + "Check that response contain JSON array"
                + " " + "for sellerId: " + sellerID);

        given()
                .pathParam("sellerID", sellerID)  // Пример sellerID
                .when()
                .get("/api/1/{sellerID}/item")  // Выполняем GET-запрос
                .then()
                .statusCode(200)
                .body("$", instanceOf(List.class))
                .body("$", hasSize(greaterThanOrEqualTo(0)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"POST", "PUT", "PATCH", "DELETE", "HEAD", "OPTIONS"})
    public void shouldReturn405ForInvalidHttpMethods(String method) {
        String sellerID = "3452";  // Используем тестовый ID продавца
        System.out.println("Request method is the following: " + method);

        given()
                .pathParam("sellerID", sellerID)
                .when()
                .request(method, "/api/1/{sellerID}/item")  // Используем разные HTTP-методы
                .then()
                .log().body()
                .statusCode(405);  // Ожидаем 405 Method Not Allowed

        System.out.println("PASS" + "\n");
    }

}



