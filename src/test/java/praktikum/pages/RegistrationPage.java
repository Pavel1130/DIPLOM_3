package praktikum.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RegistrationPage extends BasePage {
    private By nameField = By.xpath("//label[text()='Имя']/following-sibling::input");
    private By emailField = By.xpath("//label[text()='Email']/following-sibling::input");
    private By passwordField = By.xpath("//label[text()='Пароль']/following-sibling::input");
    private By registerButton = By.xpath("//button[text()='Зарегистрироваться']");
    private By errorText = By.xpath("//p[contains(@class, 'input__error') and contains(text(), 'Некорректный пароль')]");
    private By loginLink = By.xpath("//a[@href='/login']");

    public RegistrationPage(WebDriver driver) {
        super(driver);
    }

    @Step("Ввод имени: {name}")
    public void setName(String name) {
        sendKeys(nameField, name);
    }

    @Step("Ввод email: {email}")
    public void setEmail(String email) {
        sendKeys(emailField, email);
    }

    @Step("Ввод пароля")
    public void setPassword(String password) {
        sendKeys(passwordField, password);
    }

    @Step("Клик по кнопке 'Зарегистрироваться'")
    public void clickRegisterButton() {
        click(registerButton);
    }

    @Step("Клик по ссылке 'Войти'")
    public void clickLoginLink() {
        click(loginLink);
    }

    @Step("Проверка отображения ошибки валидации пароля")
    public boolean isErrorDisplayed() {
        try {
            waitForVisibility(errorText);
            return true;
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }

    @Step("Выполнение регистрации с именем: {name}, email: {email}")
    public void register(String name, String email, String password) {
        setName(name);
        setEmail(email);
        setPassword(password);
        clickRegisterButton();
    }

    @Step("Ожидание загрузки страницы регистрации")
    public void waitForPageToLoad() {
        waitForVisibility(nameField);
    }
}