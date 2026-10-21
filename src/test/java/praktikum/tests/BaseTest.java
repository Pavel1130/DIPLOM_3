package praktikum.tests;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import praktikum.utils.TestUtils;

public class BaseTest {
    protected WebDriver driver;
    protected String baseUrl;

    @Before
    public void setUp() {
        driver = TestUtils.createDriver();
        driver.manage().window().maximize();
        baseUrl = TestUtils.getBaseUrl();
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}