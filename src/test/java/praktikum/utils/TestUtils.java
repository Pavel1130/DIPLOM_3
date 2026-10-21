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
                System.out.println("[TestUtils] WARNING: Unable to find application.properties");
            } else {
                properties.load(input);
                System.out.println("[TestUtils] application.properties loaded successfully");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getBaseUrl() {
        String value = properties.getProperty("base.url");
        if (value != null) {
            System.out.println("[TestUtils] base.url from application.properties: " + value);
        } else {
            System.out.println("[TestUtils] base.url using default: https://qa-stellarburgers.education-services.ru");
        }
        // Важно: не добавляем слэш в конце здесь — он добавляется в ApiHelper
        return value != null ? value : "https://qa-stellarburgers.education-services.ru";
    }

    public static String getBrowserType() {
        return System.getProperty("browser.type", properties.getProperty("browser.type", "chrome"));
    }

    public static WebDriver createDriver() {
        String browserType = getBrowserType();
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        // Если понадобится поддержка Yandex Browser, можно раскомментировать логику
        return new ChromeDriver(options);
    }

    public static String generateRandomEmail() {
        return faker.internet().emailAddress();
    }

    public static String generateRandomName() {
        return faker.name().firstName();
    }

    public static String generateRandomPassword() {
        return faker.internet().password(8, 15, true, true);
    }
}
