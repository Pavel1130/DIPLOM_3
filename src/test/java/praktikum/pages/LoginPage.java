package praktikum.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {
    private By emailField = By.xpath("//label[text()='Email']/following-sibling::input");
    private By passwordField = By.xpath("//label[text()='Пароль']/following-sibling::input");
    private By loginButton = By.xpath("//button[text()='Войти']");
    private By registerLink = By.xpath("//a[@href='/register']");
    private By forgotPasswordLink = By.xpath("//a[@href='/forgot-password']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Step("Ввод email: {email}")
    public void setEmail(String email) {
        sendKeys(emailField, email);
    }

    @Step("Ввод пароля")
    public void setPassword(String password) {
        sendKeys(passwordField, password);
    }

    @Step("Клик по кнопке 'Войти'")
    public void clickLoginButton() {
        click(loginButton);
    }

    @Step("Клик по ссылке 'Зарегистрироваться'")
    public void clickRegisterLink() {
        click(registerLink);
    }

    @Step("Клик по ссылке 'Восстановить пароль'")
    public void clickForgotPasswordLink() {
        click(forgotPasswordLink);
    }

    @Step("Выполнение входа с email: {email}")
    public void login(String email, String password) {
        setEmail(email);
        setPassword(password);
        clickLoginButton();
    }

    @Step("Проверка отображения формы входа")
    public boolean isLoginFormDisplayed() {
        return isElementDisplayed(emailField) &&
                isElementDisplayed(passwordField) &&
                isElementDisplayed(loginButton);
    }

    @Step("Ожидание загрузки страницы входа")
    public void waitForPageToLoad() {
        waitForVisibility(emailField);
    }
}