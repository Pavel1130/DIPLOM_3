package praktikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.*;
import praktikum.models.User;
import praktikum.pages.ForgotPasswordPage;
import praktikum.pages.LoginPage;
import praktikum.pages.MainPage;
import praktikum.pages.RegistrationPage;
import praktikum.utils.ApiHelper;
import static org.junit.Assert.assertTrue;

@DisplayName("Тесты входа")
public class LoginTests extends BaseTest {
    private String accessTokenForCleanup;
    private User testUser;

    @Before
    public void setUp() {
        super.setUp();

        // Инициализируем страницу
        MainPage mainPage = new MainPage(driver);

        // Вызываем шаг подготовки пользователя, который теперь находится в MainPage
        // Внутри этого метода происходит генерация данных, регистрация и логин через ApiHelper
        mainPage.prepareTestUserForLogin();

        // Получаем данные для очистки через геттеры страницы
        this.accessTokenForCleanup = mainPage.getAccessTokenForCleanup();
        this.testUser = mainPage.getTestUser();
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
        // Сначала выполняем очистку, только потом базовый tearDown
        if (accessTokenForCleanup != null && !accessTokenForCleanup.isEmpty()) {
            System.out.println("Attempting to delete user via API...");
            try {
                var deleteResponse = ApiHelper.deleteUser("Bearer " + accessTokenForCleanup);
                int status = deleteResponse.getStatusCode();
                System.out.println("Delete user API response status: " + status);

                if (status != 200) {
                    Assert.fail("Failed to delete test user via API. Status code: " + status + ". Database cleanup failed.");
                }
            } catch (Exception e) {
                System.err.println("Error during user cleanup via API: " + e.getMessage());
                throw new RuntimeException("User cleanup failed: " + e.getMessage(), e);
            }
        }
        super.tearDown();
    }
}
