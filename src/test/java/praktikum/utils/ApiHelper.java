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
    // Формируем BASE_URI: base.url + "/api"
    private static final String BASE_URI = TestUtils.getBaseUrl() + "/api";

    static {
        RestAssured.baseURI = BASE_URI;
        System.out.println("[ApiHelper] RestAssured.baseURI set to: " + BASE_URI);
    }

    public static ValidatableResponse registerUser(User user) {
        return given()
                .log().all()                 // <-- ПОКАЗЫВАЕТ В КОНСОЛИ ПОЛНЫЙ ЗАПРОС (URL, заголовки, тело)
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/auth/register")
                .then();
    }

    public static ValidatableResponse loginUser(LoginCredentials credentials) {
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(credentials)
                .when()
                .post("/auth/login")
                .then();
    }

    public static Response deleteUser(String accessToken) {
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", accessToken)
                .when()
                .delete("/auth/user");
    }

    public static String extractAccessToken(ValidatableResponse loginResponse) {
        loginResponse.assertThat().body("accessToken", notNullValue());
        String fullToken = loginResponse.extract().path("accessToken");
        if (fullToken != null && fullToken.startsWith("Bearer ")) {
            return fullToken.substring(7);
        }
        return fullToken;
    }
}
