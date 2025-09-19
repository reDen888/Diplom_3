package praktikum.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ForgotPasswordPage extends BasePage {
    private By loginLink = By.xpath("//a[@href='/login']");

    public ForgotPasswordPage(WebDriver driver) {
        super(driver);
    }

    public void clickLoginLink() {
        click(loginLink);
    }
}