package praktikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.*;
import praktikum.models.User;
import praktikum.pages.LoginPage;
import praktikum.pages.MainPage;
import static org.junit.Assert.assertTrue;

@DisplayName("Тесты заказа")
public class OrderTests extends BaseTest {
    private String accessTokenForCleanup;
    private User testUser;

    @Before
    public void setUp() {
        super.setUp();

        // 1. Используем единый метод подготовки из Page Object (убирает дублирование кода)
        MainPage mainPage = new MainPage(driver);
        mainPage.prepareTestUserForLogin();

        this.accessTokenForCleanup = mainPage.getAccessTokenForCleanup();
        this.testUser = mainPage.getTestUser();

        // 2. Вход в браузере для UI-тестов
        driver.get(baseUrl + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForPageToLoad();
        loginPage.login(testUser.getEmail(), testUser.getPassword());

        MainPage currentPage = new MainPage(driver);
        currentPage.waitForPageToLoad();
        assertTrue("Не удалось войти в аккаунт для создания заказа", currentPage.isLoggedIn());
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Проверка создания заказа авторизованным пользователем")
    public void testCreateOrder() {
        MainPage mainPage = new MainPage(driver);

        // Переходим в раздел начинок (как в требованиях теста)
        mainPage.clickFillingsSection();
        assertTrue("Раздел 'Начинки' не стал активным", mainPage.isFillingsSectionActive());

        // Добавляем ингредиенты, используя новые геттеры
        mainPage.addIngredientToBurger(mainPage.getIngredientBun());
        mainPage.addIngredientToBurger(mainPage.getIngredientSauce());
        mainPage.addIngredientToBurger(mainPage.getIngredientFilling());

        // Оформляем заказ
        mainPage.clickOrderButton();

        // Проверяем сообщение об успешном создании заказа
        assertTrue("Сообщение 'Ваш заказ начали готовить' не отображается",
                mainPage.isOrderInProgressDisplayed());
    }

    @Test
    @DisplayName("Успешный вход и выход")
    @Description("Проверка входа и последующего выхода из аккаунта")
    public void testSuccessfulLoginAndLogout() {
        MainPage mainPage = new MainPage(driver);

        // Переход в личный кабинет и выход
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
                var deleteResponse = praktikum.utils.ApiHelper.deleteUser("Bearer " + accessTokenForCleanup);
                int status = deleteResponse.getStatusCode();
                System.out.println("Delete user API response status: " + status);

                // ВАЖНО: если удаление не удалось (не 200 и не 204), тест должен упасть,
                // чтобы пользователь не остался в базе данных
                if (status != 200 && status != 204) {
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
