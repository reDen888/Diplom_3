package praktikum.pages;

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

    public void setEmail(String email) {
        sendKeys(emailField, email);
    }

    public void setPassword(String password) {
        sendKeys(passwordField, password);
    }

    public void clickLoginButton() {
        click(loginButton);
    }

    public void clickRegisterLink() {
        click(registerLink);
    }

    public void clickForgotPasswordLink() {
        click(forgotPasswordLink);
    }

    public void login(String email, String password) {
        setEmail(email);
        setPassword(password);
        clickLoginButton();
    }

    public boolean isLoginFormDisplayed() {
        return isElementDisplayed(emailField) &&
                isElementDisplayed(passwordField) &&
                isElementDisplayed(loginButton);
    }

    public void waitForPageToLoad() {
        waitForVisibility(emailField);
    }
}