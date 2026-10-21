package praktikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.*;
import praktikum.models.LoginCredentials;
import praktikum.models.User;
import praktikum.pages.LoginPage;
import praktikum.pages.RegistrationPage;
import praktikum.utils.ApiHelper;
import praktikum.utils.TestUtils;
import static org.junit.Assert.assertTrue;

@DisplayName("Тесты регистрации")
public class RegistrationTests extends BaseTest {

    @Test
    @DisplayName("Успешная регистрация")
    @Description("Проверка успешной регистрации пользователя с уникальными данными")
    public void testSuccessfulRegistration() {
        String email = TestUtils.generateRandomEmail();
        String password = TestUtils.generateRandomPassword();
        String name = TestUtils.generateRandomName();
        User testUser = new User(email, password, name);
        driver.get(baseUrl + "/register");
        RegistrationPage registrationPage = new RegistrationPage(driver);
        registrationPage.waitForPageToLoad();
        registrationPage.register(testUser.getName(), testUser.getEmail(), testUser.getPassword());
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForPageToLoad();
        assertTrue("Не произошел переход на страницу входа после успешной регистрации",
                loginPage.isLoginFormDisplayed());
        // Очистка: Удаление пользователя через API после теста
        var loginResponse = ApiHelper.loginUser(new LoginCredentials(email, password));
        if (loginResponse.extract().statusCode() == 200) {
            String token = ApiHelper.extractAccessToken(loginResponse);
            if (token != null && !token.isEmpty()) {
                try {
                    var deleteResponse = ApiHelper.deleteUser("Bearer " + token);
                    System.out.println("Delete user API response status (registration test): " + deleteResponse.getStatusCode());
                } catch (Exception e) {
                    System.err.println("Error deleting user after registration test: " + e.getMessage());
                }
            }
        }
    }

    @Test
    @DisplayName("Ошибка для некорректного пароля")
    @Description("Проверка ошибки при регистрации с паролем менее 6 символов")
    public void testRegistrationWithShortPassword() {
        String email = TestUtils.generateRandomEmail();
        String password = "123"; // Некорректный пароль
        String name = TestUtils.generateRandomName();
        driver.get(baseUrl + "/register");
        RegistrationPage registrationPage = new RegistrationPage(driver);
        registrationPage.waitForPageToLoad();
        registrationPage.register(name, email, password);
        assertTrue("Не отображается ошибка валидации пароля 'Некорректный пароль'",
                registrationPage.isErrorDisplayed());
    }
}