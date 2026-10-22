package praktikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.*;
import praktikum.models.LoginCredentials;
import praktikum.models.User;
import praktikum.pages.MainPage;
import praktikum.utils.ApiHelper;
import praktikum.utils.TestUtils;
import static org.junit.Assert.assertTrue;

@DisplayName("Тесты конструктора")
public class ConstructorTests extends BaseTest {
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
        praktikum.pages.LoginPage loginPage = new praktikum.pages.LoginPage(driver);
        loginPage.waitForPageToLoad();
        loginPage.login(testUser.getEmail(), testUser.getPassword());
        MainPage mainPage = new MainPage(driver);
        mainPage.waitForPageToLoad();
        assertTrue("Не удалось войти в аккаунт для теста конструктора", mainPage.isLoggedIn());
    }

    @Test
    @DisplayName("Переход к разделу «Булки»")
    @Description("Проверка перехода к разделу булок в конструкторе")
    public void testNavigateToBuns() {
        MainPage mainPage = new MainPage(driver);
        // По умолчанию активен таб 'Булки'
        assertTrue("Раздел 'Булки' не активен по умолчанию", mainPage.isBunsSectionActive());

        // Нажать на таб 'Начинки'
        mainPage.clickFillingsSection();
        assertTrue("Раздел 'Начинки' не стал активным", mainPage.isFillingsSectionActive());

        // Нажать на таб 'Булки'
        mainPage.clickBunsSection();

        // Проверить, что таб 'Булки' активен
        assertTrue("Раздел 'Булки' не стал активным", mainPage.isBunsSectionActive());
    }

    @Test
    @DisplayName("Переход к разделу «Соусы»")
    @Description("Проверка перехода к разделу соусов в конструкторе")
    public void testNavigateToSauces() {
        MainPage mainPage = new MainPage(driver);
        // По умолчанию активен таб 'Булки'
        assertTrue("Раздел 'Булки' не активен по умолчанию", mainPage.isBunsSectionActive());

        // Нажать на таб 'Начинки'
        mainPage.clickFillingsSection();
        assertTrue("Раздел 'Начинки' не стал активным", mainPage.isFillingsSectionActive());
        assertTrue("Заголовок 'Начинки' не виден", mainPage.isFillingsTitleDisplayed());

        // Нажать на таб 'Соусы'
        mainPage.clickSaucesSection();

        // Проверить, что таб 'Соусы' активен и заголовок 'Соусы' виден
        assertTrue("Раздел 'Соусы' не стал активным", mainPage.isSaucesSectionActive());
        assertTrue("Заголовок 'Соусы' не виден", mainPage.isSaucesTitleDisplayed());
    }

    @Test
    @DisplayName("Переход к разделу «Начинки»")
    @Description("Проверка перехода к разделу начинок в конструкторе")
    public void testNavigateToFilling() {
        MainPage mainPage = new MainPage(driver);
        // По умолчанию активен таб 'Булки'
        assertTrue("Раздел 'Булки' не активен по умолчанию", mainPage.isBunsSectionActive());

        // Нажать на таб 'Начинки'
        mainPage.clickFillingsSection();

        // Проверить, что таб 'Начинки' активен и заголовок 'Начинки' виден
        assertTrue("Раздел 'Начинки' не стал активным", mainPage.isFillingsSectionActive());
        assertTrue("Заголовок 'Начинки' не виден", mainPage.isFillingsTitleDisplayed());
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