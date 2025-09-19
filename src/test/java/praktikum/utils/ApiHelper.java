package praktikum.utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class ApiHelper {
    private static final String BASE_URI = "https://stellarburgers.nomoreparties.site/api";

    static {
        RestAssured.baseURI = BASE_URI;
    }

    public static ValidatableResponse registerUser(String email, String password, String name) {
        return given()
                .contentType(ContentType.JSON)
                .body("{ \"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"" + name + "\" }")
                .when()
                .post("/auth/register")
                .then();
    }

    public static ValidatableResponse loginUser(String email, String password) {
        return given()
                .contentType(ContentType.JSON)
                .body("{ \"email\": \"" + email + "\", \"password\": \"" + password + "\" }")
                .when()
                .post("/auth/login")
                .then();
    }

    public static Response deleteUser(String accessToken) {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", accessToken) // Ожидается формат "Bearer <token>"
                .when()
                .delete("/auth/user");
    }

    // Метод для удобного получения токена из ответа логина
    public static String extractAccessToken(ValidatableResponse loginResponse) {
        // Проверяем, что токен присутствует
        loginResponse.assertThat().body("accessToken", notNullValue());
        // Извлекаем токен (убираем "Bearer " префикс, если он есть)
        String fullToken = loginResponse.extract().path("accessToken");
        if (fullToken.startsWith("Bearer ")) {
            return fullToken.substring(7); // "Bearer ".length()
        }
        return fullToken;
    }
}