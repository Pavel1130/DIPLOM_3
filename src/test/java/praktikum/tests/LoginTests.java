package praktikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.*;
import praktikum.models.LoginCredentials;
import praktikum.models.User;
import praktikum.pages.ForgotPasswordPage;
import praktikum.pages.LoginPage;
import praktikum.pages.MainPage;
import praktikum.pages.RegistrationPage;
import praktikum.utils.ApiHelper;
import praktikum.utils.TestUtils;
import static org.junit.Assert.assertTrue;

@DisplayName("Тесты входа")
public class LoginTests extends BaseTest {
    private String accessTokenForCleanup;
    private User testUser;

    @Before
    public void setUp() {
        super.setUp();
        // Создание уникального пользователя через API перед каждым тестом
        String email = TestUtils.generateRandomEmail();
        String password = TestUtils.generateRandomPassword();
        String name = TestUtils.generateRandomName();
        testUser = new User(email, password, name);
        ApiHelper.registerUser(testUser).assertThat().statusCode(200);
        var loginResponse = ApiHelper.loginUser(new LoginCredentials(email, password));
        loginResponse.assertThat().statusCode(200);
        accessTokenForCleanup = ApiHelper.extractAccessToken(loginResponse);
    }

    @Test
    @DisplayName("Вход по кнопке «Войти в аккаунт» на главной")
    @Description("Проверка входа через кнопку на главной странице")
    public void testLoginViaMainPageButton() {
        driver.get(baseUrl);
        MainPage mainPage = new MainPage(driver);
        mainPage.waitForPageToLoad();
        mainPage.clickLoginButton();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForPageToLoad();
        loginPage.login(testUser.getEmail(), testUser.getPassword());
        mainPage.waitForPageToLoad();
        assertTrue("Конструктор не отображается после входа", mainPage.isLoggedIn());
    }

    @Test
    @DisplayName("Вход через кнопку «Личный кабинет»")
    @Description("Проверка входа через личный кабинет")
    public void testLoginViaPersonalAccount() {
        driver.get(baseUrl);
        MainPage mainPage = new MainPage(driver);
        mainPage.waitForPageToLoad();
        mainPage.clickPersonalAccountButton();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForPageToLoad();
        loginPage.login(testUser.getEmail(), testUser.getPassword());
        mainPage.waitForPageToLoad();
        assertTrue("Конструктор не отображается после входа", mainPage.isLoggedIn());
    }

    @Test
    @DisplayName("Вход через кнопку в форме регистрации")
    @Description("Проверка входа через ссылку в форме регистрации")
    public void testLoginViaRegistrationForm() {
        driver.get(baseUrl + "/register");
        RegistrationPage registrationPage = new RegistrationPage(driver);
        registrationPage.waitForPageToLoad();
        registrationPage.clickLoginLink();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForPageToLoad();
        loginPage.login(testUser.getEmail(), testUser.getPassword());
        MainPage mainPage = new MainPage(driver);
        mainPage.waitForPageToLoad();
        assertTrue("Конструктор не отображается после входа", mainPage.isLoggedIn());
    }

    @Test
    @DisplayName("Вход через кнопку в форме восстановления пароля")
    @Description("Проверка входа через ссылку в форме восстановления пароля")
    public void testLoginViaForgotPasswordForm() {
        driver.get(baseUrl + "/forgot-password");
        ForgotPasswordPage forgotPasswordPage = new ForgotPasswordPage(driver);
        forgotPasswordPage.clickLoginLink();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForPageToLoad();
        loginPage.login(testUser.getEmail(), testUser.getPassword());
        MainPage mainPage = new MainPage(driver);
        mainPage.waitForPageToLoad();
        assertTrue("Конструктор не отображается после входа", mainPage.isLoggedIn());
    }

    @After
    public void tearDown() {
        if (accessTokenForCleanup != null && !accessTokenForCleanup.isEmpty()) {
            System.out.println("Attempting to delete user via API...");
            try {
                var deleteResponse = ApiHelper.deleteUser("Bearer " + accessTokenForCleanup);
                System.out.println("Delete user API response status: " + deleteResponse.getStatusCode());
            } catch (Exception e) {
                System.err.println("Error during user cleanup via API: " + e.getMessage());
            }
        }
        super.tearDown();
    }
}