package praktikum.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MainPage extends BasePage {
    private By loginButton = By.xpath("//button[text()='Войти в аккаунт']");
    private By personalAccountButton = By.xpath("//p[text()='Личный Кабинет']");
    private By bunsSection = By.xpath("//span[text()='Булки']/parent::div");
    private By saucesSection = By.xpath("//span[text()='Соусы']/parent::div");
    private By fillingsSection = By.xpath("//span[text()='Начинки']/parent::div");
    public By constructorHeader = By.xpath("//h1[contains(text(),'Соберите бургер')]");
    public By orderInProgress = By.xpath("//p[text()='Ваш заказ начали готовить']");
    private By logoutButton = By.xpath("//button[text()='Выход']");
    public By ingredientBun = By.xpath("//p[text()='Флюоресцентная булка R2-D3']/ancestor::a");
    public By ingredientMain = By.xpath("//p[text()='Биотонный марсианский бургер']/ancestor::a");
    public By ingredientSauce = By.xpath("//p[text()='Соус традиционный галактический']/ancestor::a");
    public By orderButton = By.xpath("//button[contains(text(),'Оформить заказ')]");
    public By orderIdLocator = By.xpath("//p[@class='text text_type_digits-large']"); // Локатор для ID заказа

    public MainPage(WebDriver driver) {
        super(driver);
    }

    public void clickLoginButton() {
        click(loginButton);
    }

    public void clickPersonalAccountButton() {
        click(personalAccountButton);
    }

    public void clickBunsSection() {
        click(bunsSection);
        try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    public void clickSaucesSection() {
        click(saucesSection);
        try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    public void clickFillingsSection() {
        click(fillingsSection);
        try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    public boolean isConstructorDisplayed() {
        waitForVisibility(constructorHeader);
        return isElementDisplayed(constructorHeader);
    }

    public boolean isLoggedIn() {
        return isConstructorDisplayed();
    }

    public void waitForPageToLoad() {
        waitForVisibility(constructorHeader);
    }

    public void clickLogoutButton() {
        click(logoutButton);
    }

    public void addIngredientToBurger(By ingredientLocator) {
        click(ingredientLocator);
        // Пауза, чтобы элемент добавился в конструктор
        try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    public void clickOrderButton() {
        click(orderButton);
    }

    public String getOrderId() {
        waitForVisibility(orderIdLocator);
        return getText(orderIdLocator);
    }

    public boolean isOrderInProgressDisplayed() {
        waitForVisibility(orderInProgress); // Убедимся, что элемент проверяется на видимость
        return isElementDisplayed(orderInProgress);
    }
}