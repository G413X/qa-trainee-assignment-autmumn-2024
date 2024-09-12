package api;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class GetAdsByIdsTest {

    static {
        RestAssured.baseURI = "https://qa-internship.avito.com/api/1"; // Укажите базовый URL
    }

    @ParameterizedTest
    @ValueSource(strings = {"d3523e5d-2c17-46b7-857e-cd59f79a1598"})
    public void shouldReturn200WhenIdIsExisting(String validIDs) {

        System.out.println("""
        
        Check that response status code for existing IDs is 200:
        """);

        given()
                .when()
                .get("/item/{:id}", validIDs)
                .then()
                .log().status()
                .statusCode(200);

        System.out.println("""

        GET response for existing id is OK
        """);
    }

    @ParameterizedTest
    @ValueSource(strings = {""})
    public void ShouldReturn404WhenIdIsInvalid(String invalidIDs) {

        System.out.println("""
            
            Check that status code for invalid IDs is 404:
            Invalid id: \"\"
            """);

        given()
                .when()
                .get("/item/{:id}", invalidIDs)
                .then()
                .log().status()
                .statusCode(404);

        System.out.println("""
             
            GET response for invalid \"\" id returns 404 - OK
            """);
    }

    @Test
    public void ShouldContainExpectedFieldsAndNonNullValues_WhenExistingIdIsProvided() {
        String adId = "d3523e5d-2c17-46b7-857e-cd59f79a1598";

        System.out.println("""
        
            Check response structure and mandatory values:
            """);

        given()
                .when()
                .get("/item/{:id}", adId)
                .then()
                .log().body()
                .statusCode(200)  // Убеждаемся, что запрос успешный
                .body("[0].createdAt", notNullValue())  // Проверяем, что первый элемент массива содержит поле "createdAt"
                .body("[0]", hasKey("createdAt"))
                .body("[0].id", notNullValue())
                .body("[0]", hasKey("id"))
                .body("[0]", hasKey("createdAt"))
                .body("[0].name", notNullValue())
                .body("[0]", hasKey("name"))
                .body("[0].price", notNullValue())
                .body("[0]", hasKey("price"))
                .body("[0].sellerId", notNullValue())
                .body("[0]", hasKey("sellerId"))
                .body("[0].statistics.contacts", notNullValue())
                .body("[0].statistics", hasKey("contacts"))
                .body("[0].statistics.likes", notNullValue())
                .body("[0].statistics", hasKey("likes"))
                .body("[0].statistics.viewCount", notNullValue())
                .body("[0].statistics", hasKey("viewCount"));

        System.out.println("""

            Response structure - OK
            Values are not NULL - OK
            """);
    }

    @Test
    public void shouldReturnSameIdInResponse_WhenExistingIsProvided() {
        String adId = "d3523e5d-2c17-46b7-857e-cd59f79a1598";

        System.out.println("""
        
            Check request ad id match with response ad id
            Request ad Id: " + adId);
            """);

        given()
                .when()
                .get("/item/{:id}", adId)
                .then()
                .log().body()
                .statusCode(200)
                .body("id[0]", equalTo(adId));  // Поле "id" совпадает с запрошенным

        System.out.println("""
         
            Response ad ID matches request ad ID - OK
            """);
    }

    @ParameterizedTest
    @ValueSource(strings = {"POST", "PUT", "PATCH", "DELETE", "HEAD", "OPTIONS"})
    public void shouldReturn405ForInvalidHttpMethodsOnGetAd(String method) {
        String adId = "d3523e5d-2c17-46b7-857e-cd59f79a1598";
        System.out.println("Request method is the following: " + method);

        given()
                .pathParam("id", adId)
                .when()
                .request(method, "/item/{id}")  // Используем разные HTTP-методы
                .then()
                .log().status()
                .statusCode(405);  // Ожидаем 405 Method Not Allowed

        System.out.println("PASS" + "\n");
    }
}



