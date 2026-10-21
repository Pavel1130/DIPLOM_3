package praktikum.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Step("Ожидание видимости элемента {locator}")
    public void waitForVisibility(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    @Step("Ожидание кликабельности элемента {locator}")
    public void waitForClickable(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    @Step("Клик по элементу {locator}")
    public void click(By locator) {
        try {
            waitForClickable(locator);
            driver.findElement(locator).click();
        } catch (ElementClickInterceptedException e) {
            // Элемент перекрыт: скроллим и кликаем через JS
            WebElement element = driver.findElement(locator);
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block: 'center'});", element
            );
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        } catch (StaleElementReferenceException e) {
            // Элемент устарел — делаем повторную попытку ровно один раз
            waitForClickable(locator);
            driver.findElement(locator).click();
        }
    }

    @Step("Ввод текста '{text}' в элемент {locator}")
    public void sendKeys(By locator, String text) {
        waitForVisibility(locator);
        WebElement element = driver.findElement(locator);
        element.clear();
        element.sendKeys(text);
    }

    @Step("Получение текста элемента {locator}")
    public String getText(By locator) {
        waitForVisibility(locator);
        return driver.findElement(locator).getText();
    }

    @Step("Проверка отображения элемента {locator}")
    public boolean isElementDisplayed(By locator) {
        try {
            waitForVisibility(locator);
            return driver.findElement(locator).isDisplayed();
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }
}
