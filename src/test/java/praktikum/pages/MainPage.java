package praktikum.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import praktikum.models.LoginCredentials;
import praktikum.models.User;
import praktikum.utils.ApiHelper;
import praktikum.utils.TestUtils;

public class MainPage extends BasePage {

    // --- Поля для хранения данных тестового пользователя (нужны для очистки в tearDown) ---
    protected String accessTokenForCleanup;
    protected User testUser;
    // ------------------------------------------------------------------------------------

    // Кнопки и основные элементы страницы
    private final By loginButton = By.xpath("//button[text()='Войти в аккаунт']");
    private final By personalAccountButton = By.xpath("//p[text()='Личный Кабинет']");
    private final By logoutButton = By.xpath("//button[text()='Выход']");

    public final By constructorHeader = By.xpath("//h1[contains(text(),'Соберите бургер')]");
    public final By orderInProgress = By.xpath("//p[text()='Ваш заказ начали готовить']");
    public final By orderButton = By.xpath("//button[contains(text(),'Оформить заказ')]");
    public final By orderIdLocator = By.xpath("//p[@class='text text_type_digits-large']");

    // Локаторы ингредиентов
    private final By ingredientBun = By.xpath("//p[text()='Флюоресцентная булка R2-D3']/ancestor::a");
    private final By ingredientSauce = By.xpath("//p[text()='Соус традиционный галактический']/ancestor::a");
    // Логически это начинка (Filling), хотя поле названо ingredientMain
    private final By ingredientMain = By.xpath("//p[text()='Мясо бессмертных моллюсков Protostomia']/ancestor::a");

    // Вкладки конструктора (для клика)
    private final By bunsSection = By.xpath("//span[text()='Булки']/parent::div");
    private final By saucesSection = By.xpath("//span[text()='Соусы']/parent::div");
    private final By fillingsSection = By.xpath("//span[text()='Начинки']/parent::div");

    // Активные вкладки (проверка состояния)
    private final By activeBunsTab = By.xpath(".//div[contains(@class, 'tab_tab_type_current__')]/span[text()='Булки']");
    private final By activeSaucesTab = By.xpath(".//div[contains(@class, 'tab_tab_type_current__')]/span[text()='Соусы']");
    private final By activeFillingsTab = By.xpath(".//div[contains(@class, 'tab_tab_type_current__')]/span[text()='Начинки']");

    // Заголовки разделов
    private final By bunsTitle = By.xpath("//h2[text()='Булки']");
    private final By saucesTitle = By.xpath("//h2[text()='Соусы']");
    private final By fillingsTitle = By.xpath("//h2[text()='Начинки']");

    public MainPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Шаг подготовки тестового пользователя: регистрация через API, логин, сохранение токена.
     * Выносится сюда, чтобы убрать дублирование кода из тестов (LoginTests, ConstructorTests, OrderTests).
     */
    @Step("Подготовить тестового пользователя для входа: зарегистрировать через API и получить токен")
    public void prepareTestUserForLogin() {
        String email = TestUtils.generateRandomEmail();
        String password = TestUtils.generateRandomPassword();
        String name = TestUtils.generateRandomName();

        this.testUser = new User(email, password, name);
        ApiHelper.registerUser(this.testUser).assertThat().statusCode(200);

        var loginResponse = ApiHelper.loginUser(new LoginCredentials(email, password));
        loginResponse.assertThat().statusCode(200);
        this.accessTokenForCleanup = ApiHelper.extractAccessToken(loginResponse);

        System.out.println("Test user prepared: " + email);
    }

    // Геттеры для доступа к данным из теста (чтобы забрать токен и юзера для очистки)
    public String getAccessTokenForCleanup() {
        return accessTokenForCleanup;
    }

    public User getTestUser() {
        return testUser;
    }

    // ==========================================================
    // ГЕТТЕРЫ ДЛЯ ИНГРЕДИЕНТОВ (ИСПРАВЛЕНИЕ ОШИБКИ В ТЕСТАХ)
    // ==========================================================
    public By getIngredientBun() {
        return ingredientBun;
    }

    public By getIngredientSauce() {
        return ingredientSauce;
    }

    public By getIngredientFilling() {
        // Возвращаем локатор ingredientMain, так как это и есть начинка (мясо)
        return ingredientMain;
    }
    // ==========================================================

    // --- Методы для кнопок ---
    @Step("Клик по кнопке 'Войти в аккаунт'")
    public void clickLoginButton() {
        click(loginButton);
    }

    @Step("Клик по кнопке 'Личный Кабинет'")
    public void clickPersonalAccountButton() {
        click(personalAccountButton);
    }

    @Step("Клик по кнопке 'Выход'")
    public void clickLogoutButton() {
        click(logoutButton);
    }

    @Step("Клик по кнопке 'Оформить заказ'")
    public void clickOrderButton() {
        click(orderButton);
    }

    // --- Методы для вкладок конструктора ---
    @Step("Клик по разделу 'Булки'")
    public void clickBunsSection() {
        click(bunsSection);
    }

    @Step("Клик по разделу 'Соусы'")
    public void clickSaucesSection() {
        click(saucesSection);
    }

    @Step("Клик по разделу 'Начинки'")
    public void clickFillingsSection() {
        click(fillingsSection);
    }

    // --- Проверки активности вкладок ---
    @Step("Проверка, что раздел 'Булки' активен")
    public boolean isBunsSectionActive() {
        return isElementDisplayed(activeBunsTab);
    }

    @Step("Проверка, что раздел 'Соусы' активен")
    public boolean isSaucesSectionActive() {
        return isElementDisplayed(activeSaucesTab);
    }

    @Step("Проверка, что раздел 'Начинки' активен")
    public boolean isFillingsSectionActive() {
        return isElementDisplayed(activeFillingsTab);
    }

    // --- Проверки видимости заголовков ---
    @Step("Проверка видимости заголовка 'Булки'")
    public boolean isBunsTitleDisplayed() {
        return isElementDisplayed(bunsTitle);
    }

    @Step("Проверка видимости заголовка 'Соусы'")
    public boolean isSaucesTitleDisplayed() {
        return isElementDisplayed(saucesTitle);
    }

    @Step("Проверка видимости заголовка 'Начинки'")
    public boolean isFillingsTitleDisplayed() {
        return isElementDisplayed(fillingsTitle);
    }

    // --- Общие проверки страницы ---
    @Step("Проверка отображения конструктора")
    public boolean isConstructorDisplayed() {
        waitForVisibility(constructorHeader);
        return isElementDisplayed(constructorHeader);
    }

    @Step("Проверка успешного входа")
    public boolean isLoggedIn() {
        return isConstructorDisplayed();
    }

    @Step("Ожидание загрузки главной страницы")
    public void waitForPageToLoad() {
        waitForVisibility(constructorHeader);
    }

    // --- Работа с заказом (добавление ингредиентов) ---
    @Step("Добавление ингредиента в конструктор")
    public void addIngredientToBurger(By ingredientLocator) {
        click(ingredientLocator);
        // Ждем появления счетчика, чтобы убедиться, что клик сработал
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class, 'counter_counter__')]")));
    }

    @Step("Получение ID заказа")
    public String getOrderId() {
        waitForVisibility(orderIdLocator);
        return getText(orderIdLocator);
    }

    @Step("Проверка отображения сообщения 'Ваш заказ начали готовить'")
    public boolean isOrderInProgressDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(orderInProgress));
            return isElementDisplayed(orderInProgress);
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }
}


