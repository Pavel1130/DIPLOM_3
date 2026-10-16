package praktikum.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class MainPage extends BasePage {

    // Кнопки и основные элементы
    private By loginButton = By.xpath("//button[text()='Войти в аккаунт']");
    private By personalAccountButton = By.xpath("//p[text()='Личный Кабинет']");
    private By logoutButton = By.xpath("//button[text()='Выход']");
    public By constructorHeader = By.xpath("//h1[contains(text(),'Соберите бургер')]");
    public By orderInProgress = By.xpath("//p[text()='Ваш заказ начали готовить']");
    public By orderButton = By.xpath("//button[contains(text(),'Оформить заказ')]");
    public By orderIdLocator = By.xpath("//p[@class='text text_type_digits-large']");

    // --- Ингредиенты ---
    public By ingredientBun = By.xpath("//p[text()='Флюоресцентная булка R2-D3']/ancestor::a");
    public By ingredientSauce = By.xpath("//p[text()='Соус традиционный галактический']/ancestor::a");
    public By ingredientMain = By.xpath("//p[text()='Мясо бессмертных моллюсков Protostomia']/ancestor::a");

    // Локаторы для ВКЛАДОК КОНСТРУКТОРА (для клика)
    private By bunsSection = By.xpath("//span[text()='Булки']/parent::div");
    private By saucesSection = By.xpath("//span[text()='Соусы']/parent::div");
    private By fillingsSection = By.xpath("//span[text()='Начинки']/parent::div");

    // Локаторы для АКТИВНЫХ (выбранных) вкладок
    private By activeBunsTab = By.xpath(".//div[contains(@class, 'tab_tab_type_current__')]/span[text()='Булки']");
    private By activeSaucesTab = By.xpath(".//div[contains(@class, 'tab_tab_type_current__')]/span[text()='Соусы']");
    private By activeFillingsTab = By.xpath(".//div[contains(@class, 'tab_tab_type_current__')]/span[text()='Начинки']");

    // Локаторы для ЗАГОЛОВКОВ разделов (h2)
    private By bunsTitle = By.xpath("//h2[text()='Булки']");
    private By saucesTitle = By.xpath("//h2[text()='Соусы']");
    private By fillingsTitle = By.xpath("//h2[text()='Начинки']");

    public MainPage(WebDriver driver) {
        super(driver);
    }

    // Методы для работы с кнопками
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

    // Методы для работы с разделами конструктора
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

    // Методы для проверки АКТИВНОСТИ разделов
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

    // Методы для проверки видимости ЗАГОЛОВКОВ разделов
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

    // Методы для общих проверок страницы
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

    // Методы для работы с заказом
    @Step("Добавление ингредиента в конструктор")
    public void addIngredientToBurger(By ingredientLocator) {
        click(ingredientLocator);
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

    // Геттеры для ингредиентов
    public By getIngredientBun() {
        return ingredientBun;
    }

    public By getIngredientSauce() {
        return ingredientSauce;
    }

    public By getIngredientFilling() {
        return ingredientMain;
    }
}