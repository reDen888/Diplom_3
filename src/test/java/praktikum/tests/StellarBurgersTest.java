package praktikum.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import praktikum.pages.*;
import praktikum.utils.ApiHelper; // Импортируем ApiHelper

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class StellarBurgersTest {
    private WebDriver driver;
    private final String baseUrl = "https://stellarburgers.nomoreparties.site";
    // Фиксированные данные для входа (уже существующий пользователь)
    private final String fixedTestEmail = "test2025@ya.ru";
    private final String fixedTestPassword = "password";
    private final String fixedTestName = "test";
    // Уникальные данные для регистрации
    private String registrationTestName;
    private String registrationTestEmail;
    private String registrationTestPassword = "password123";
    // Для нового теста
    private String accessTokenForCleanup;
    private final String browserType;

    public StellarBurgersTest(String browser) {
        this.browserType = browser;
    }

    @Parameterized.Parameters(name = "Browser: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"chrome"},
                {"yandex"}
        });
    }

    @Before
    public void setUp() {
        // Генерация уникальных данных для каждого теста регистрации
        long timestamp = System.currentTimeMillis();
        registrationTestName = "TestUser" + timestamp;
        registrationTestEmail = "test" + timestamp + "@yandex.ru";
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        // options.addArguments("--headless"); // Раскомментируйте для headless режима
        if ("yandex".equalsIgnoreCase(browserType)) {
            options.setBinary("C:\\Users\\Den\\AppData\\Local\\Yandex\\YandexBrowser\\Application\\browser.exe"); // Windows
        }
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }

    // Тесты регистрации
    @Test
    @DisplayName("Успешная регистрация")
    @Description("Проверка успешной регистрации пользователя с уникальными данными")
    public void testSuccessfulRegistration() {
        driver.get(baseUrl + "/register");
        RegistrationPage registrationPage = new RegistrationPage(driver);
        registrationPage.waitForPageToLoad();
        registrationPage.register(registrationTestName, registrationTestEmail, registrationTestPassword);
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForPageToLoad();
        assertTrue("Не произошел переход на страницу входа после успешной регистрации",
                loginPage.isLoginFormDisplayed());
    }

    @Test
    @DisplayName("Ошибка для некорректного пароля")
    @Description("Проверка ошибки при регистрации с паролем менее 6 символов")
    public void testRegistrationWithShortPassword() {
        driver.get(baseUrl + "/register");
        RegistrationPage registrationPage = new RegistrationPage(driver);
        registrationPage.waitForPageToLoad();
        registrationPage.register(registrationTestName, registrationTestEmail, "123");
        assertTrue("Не отображается ошибка валидации пароля 'Некорректный пароль'",
                registrationPage.isErrorDisplayed());
    }

    // Тесты входа
    @Test
    @DisplayName("Вход по кнопке «Войти в аккаунт» на главной")
    @Description("Проверка входа через кнопку на главной странице")
    public void testLoginViaMainPageButton() {
        driver.get(baseUrl);
        MainPage mainPage = new MainPage(driver);
        mainPage.waitForPageToLoad();
        mainPage.clickLoginButton();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForPageToLoad();
        loginPage.login(fixedTestEmail, fixedTestPassword);
        mainPage.waitForPageToLoad(); // Ждем загрузки главной после входа
        assertTrue("Конструктор не отображается после входа",
                mainPage.isLoggedIn());
    }

    @Test
    @DisplayName("Вход через кнопку «Личный кабинет»")
    @Description("Проверка входа через личный кабинет")
    public void testLoginViaPersonalAccount() {
        driver.get(baseUrl);
        MainPage mainPage = new MainPage(driver);
        mainPage.waitForPageToLoad();
        mainPage.clickPersonalAccountButton();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForPageToLoad();
        loginPage.login(fixedTestEmail, fixedTestPassword);
        mainPage.waitForPageToLoad();
        assertTrue("Конструктор не отображается после входа",
                mainPage.isLoggedIn());
    }

    @Test
    @DisplayName("Вход через кнопку в форме регистрации")
    @Description("Проверка входа через ссылку в форме регистрации")
    public void testLoginViaRegistrationForm() {
        driver.get(baseUrl + "/register");
        RegistrationPage registrationPage = new RegistrationPage(driver);
        registrationPage.waitForPageToLoad();
        registrationPage.clickLoginLink();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForPageToLoad();
        loginPage.login(fixedTestEmail, fixedTestPassword);
        MainPage mainPage = new MainPage(driver);
        mainPage.waitForPageToLoad();
        assertTrue("Конструктор не отображается после входа",
                mainPage.isLoggedIn());
    }

    @Test
    @DisplayName("Вход через кнопку в форме восстановления пароля")
    @Description("Проверка входа через ссылку в форме восстановления пароля")
    public void testLoginViaForgotPasswordForm() {
        driver.get(baseUrl + "/forgot-password");
        ForgotPasswordPage forgotPasswordPage = new ForgotPasswordPage(driver);
        forgotPasswordPage.clickLoginLink();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForPageToLoad();
        loginPage.login(fixedTestEmail, fixedTestPassword);
        MainPage mainPage = new MainPage(driver);
        mainPage.waitForPageToLoad();
        assertTrue("Конструктор не отображается после входа",
                mainPage.isLoggedIn());
    }

    // Тесты конструктора
    @Test
    @DisplayName("Переход к разделу «Булки»")
    @Description("Проверка перехода к разделу булок в конструкторе")
    public void testNavigateToBuns() {
        driver.get(baseUrl);
        MainPage mainPage = new MainPage(driver);
        mainPage.waitForPageToLoad();
        mainPage.clickBunsSection();
        assertTrue("Конструктор не отображается после перехода к разделу 'Булки'",
                mainPage.isConstructorDisplayed());
    }

    @Test
    @DisplayName("Переход к разделу «Соусы»")
    @Description("Проверка перехода к разделу соусов в конструкторе")
    public void testNavigateToSauces() {
        driver.get(baseUrl);
        MainPage mainPage = new MainPage(driver);
        mainPage.waitForPageToLoad();
        mainPage.clickSaucesSection();
        assertTrue("Конструктор не отображается после перехода к разделу 'Соусы'",
                mainPage.isConstructorDisplayed());
    }

    @Test
    @DisplayName("Переход к разделу «Начинки»")
    @Description("Проверка перехода к разделу начинок в конструкторе")
    public void testNavigateToFilling() {
        driver.get(baseUrl);
        MainPage mainPage = new MainPage(driver);
        mainPage.waitForPageToLoad();
        mainPage.clickFillingsSection();
        assertTrue("Конструктор не отображается после перехода к разделу 'Начинки'",
                mainPage.isConstructorDisplayed());
    }

    @Test
    @DisplayName("Успешный вход и выход")
    @Description("Проверка входа и последующего выхода из аккаунта")
    public void testSuccessfulLoginAndLogout() {
        driver.get(baseUrl);
        MainPage mainPage = new MainPage(driver);
        mainPage.waitForPageToLoad();
        mainPage.clickPersonalAccountButton();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForPageToLoad();
        loginPage.login(fixedTestEmail, fixedTestPassword);
        mainPage.waitForPageToLoad();
        assertTrue("Не удалось войти в аккаунт", mainPage.isLoggedIn());
        mainPage.clickPersonalAccountButton(); // Переход в ЛК после входа
        mainPage.clickLogoutButton();
        // Ожидаем переход на страницу логина
        loginPage.waitForPageToLoad();
        assertTrue("Не произошел переход на страницу входа после выхода",
                loginPage.isLoginFormDisplayed());
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Проверка создания заказа авторизованным пользователем")
    public void testCreateOrder() {
        // 1. Регистрация нового пользователя через API для получения токена
        ApiHelper.registerUser(registrationTestEmail, registrationTestPassword, registrationTestName)
                .assertThat().statusCode(200);
        // 2. Вход под новым пользователем через API для получения токена
        var loginResponse = ApiHelper.loginUser(registrationTestEmail, registrationTestPassword);
        loginResponse.assertThat().statusCode(200);
        accessTokenForCleanup = ApiHelper.extractAccessToken(loginResponse);
        String authHeader = "Bearer " + accessTokenForCleanup;
        // 3. Вход в браузере с новыми данными
        driver.get(baseUrl + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForPageToLoad();
        loginPage.login(registrationTestEmail, registrationTestPassword);
        MainPage mainPage = new MainPage(driver);
        mainPage.waitForPageToLoad(); // Используем публичный метод
        assertTrue("Не удалось войти в аккаунт для создания заказа", mainPage.isLoggedIn());
        // 4. Создание заказа
        // Добавляем ингредиенты
        mainPage.addIngredientToBurger(mainPage.ingredientBun); // Используем публичное поле
        mainPage.addIngredientToBurger(mainPage.ingredientMain); // Используем публичное поле
        mainPage.addIngredientToBurger(mainPage.ingredientSauce); // Используем публичное поле
        // Кликаем кнопку "Оформить заказ"
        mainPage.clickOrderButton(); // Используем публичный метод
        // 5. Проверка результата
        // Ждем сообщения о готовке и проверяем его
        assertTrue("Сообщение 'Ваш заказ начали готовить' не отображается",
                mainPage.isOrderInProgressDisplayed());
    }

    @After
    public void tearDown() {
        // Попытка удалить пользователя через API после теста, если токен был получен
        if (accessTokenForCleanup != null && !accessTokenForCleanup.isEmpty()) {
            System.out.println("Attempting to delete user via API...");
            try {
                var deleteResponse = ApiHelper.deleteUser("Bearer " + accessTokenForCleanup);
                System.out.println("Delete user API response status: " + deleteResponse.getStatusCode());
                // Ожидаем 204 No Content или 202 Accepted
                assertTrue("Failed to delete user via API. Status: " + deleteResponse.getStatusCode(),
                        deleteResponse.getStatusCode() == 204 || deleteResponse.getStatusCode() == 202);
            } catch (Exception e) {
                System.err.println("Error during user cleanup via API: " + e.getMessage());
                // Не фейлим тест из-за ошибки очистки
            }
        }
        if (driver != null) {
            driver.quit();
        }
    }
}