package praktikum.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import com.github.javafaker.Faker;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

public class TestUtils {
    private static final Properties properties = new Properties();
    private static final Faker faker = new Faker(Locale.ENGLISH);

    static {
        try (InputStream input = TestUtils.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find application.properties");
            } else {
                properties.load(input);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getBaseUrl() {
        return properties.getProperty("base.url", "https://stellarburgers.nomoreparties.site");
    }

    // Метод для получения типа браузера из системного свойства или properties файла
    public static String getBrowserType() {
        // Приоритет: системное свойство -> application.properties -> значение по умолчанию
        return System.getProperty("browser.type", properties.getProperty("browser.type", "chrome"));
    }

    public static WebDriver createDriver() {
        String browserType = getBrowserType();
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        if ("yandex".equalsIgnoreCase(browserType)) {

        }
        return new ChromeDriver(options);
    }

    // Методы для генерации данных с использованием JavaFaker
    public static String generateRandomEmail() {
        return faker.internet().emailAddress();
    }
    public static String generateRandomName() {
        return faker.name().firstName();
    }
    public static String generateRandomPassword() {
        return faker.internet().password(8, 15, true, true); // мин. 8, макс. 15, с цифрами, со спец. символами
    }
}