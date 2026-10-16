package praktikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.*;
import praktikum.models.LoginCredentials;
import praktikum.models.User;
import praktikum.pages.LoginPage;
import praktikum.pages.MainPage;
import praktikum.utils.ApiHelper;
import praktikum.utils.TestUtils;

import static org.junit.Assert.assertTrue;

@DisplayName("Тесты заказа")
public class OrderTests extends BaseTest {
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

        // Вход в браузере
        driver.get(baseUrl + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForPageToLoad();
        loginPage.login(testUser.getEmail(), testUser.getPassword());
        MainPage mainPage = new MainPage(driver);
        mainPage.waitForPageToLoad();
        assertTrue("Не удалось войти в аккаунт для создания заказа", mainPage.isLoggedIn());
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Проверка создания заказа авторизованным пользователем")
    public void testCreateOrder() {
        MainPage mainPage = new MainPage(driver);
        mainPage.clickFillingsSection();
        assertTrue("Раздел 'Начинки' не стал активным", mainPage.isFillingsSectionActive());
        mainPage.addIngredientToBurger(mainPage.getIngredientBun());
        mainPage.addIngredientToBurger(mainPage.getIngredientSauce());
        mainPage.addIngredientToBurger(mainPage.getIngredientFilling());
        mainPage.clickOrderButton();
        assertTrue("Сообщение 'Ваш заказ начали готовить' не отображается",
                mainPage.isOrderInProgressDisplayed());
    }

    @Test
    @DisplayName("Успешный вход и выход")
    @Description("Проверка входа и последующего выхода из аккаунта")
    public void testSuccessfulLoginAndLogout() {
        MainPage mainPage = new MainPage(driver);
        mainPage.clickPersonalAccountButton();
        mainPage.clickLogoutButton();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForPageToLoad();
        assertTrue("Не произошел переход на страницу входа после выхода",
                loginPage.isLoginFormDisplayed());
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