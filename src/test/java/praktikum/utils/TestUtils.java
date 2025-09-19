package praktikum.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class TestUtils {
    public static WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        // options.addArguments("--headless"); // Для CI/CD
        return new ChromeDriver(options);
    }

    public static WebDriver createYandexDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.setBinary("C:\\yandexdriver-25.8.0.1872-win64\\yandexdriver.exe"); // Windows
        // options.setBinary("C:\\Users\\Den\\AppData\\Local\\Yandex\\YandexBrowser\\Application\\browser.exe"); // Windows
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        // options.addArguments("--headless"); // Для CI/CD
        return new ChromeDriver(options);
    }

    public static String generateRandomEmail() {
        return "test" + System.currentTimeMillis() + "@ya.ru";
    }

    public static String generateRandomName() {
        return "User" + System.currentTimeMillis();
    }
}