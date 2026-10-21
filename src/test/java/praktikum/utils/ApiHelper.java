package praktikum.utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import praktikum.models.LoginCredentials;
import praktikum.models.User;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class ApiHelper {
    private static final String BASE_URI = TestUtils.getBaseUrl() + "/api";

    static {
        RestAssured.baseURI = BASE_URI;
    }

    // Используем POJO для сериализации
    public static ValidatableResponse registerUser(User user) {
        return given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/auth/register")
                .then();
    }

    // Используем POJO для сериализации
    public static ValidatableResponse loginUser(LoginCredentials credentials) {
        return given()
                .contentType(ContentType.JSON)
                .body(credentials)
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
        loginResponse.assertThat().body("accessToken", notNullValue());
        // Извлекаем токен (убираем "Bearer " префикс, если он есть)
        String fullToken = loginResponse.extract().path("accessToken");
        if (fullToken != null && fullToken.startsWith("Bearer ")) {
            return fullToken.substring(7); // "Bearer ".length()
        }
        return fullToken;
    }
}