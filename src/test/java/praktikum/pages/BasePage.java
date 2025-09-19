package praktikum.pages;

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

    public void waitForVisibility(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void waitForClickable(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void waitForInvisibility(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public void waitForUrlToContain(String urlPart) {
        wait.until(ExpectedConditions.urlContains(urlPart));
    }

    public void click(By locator) {
        int attempts = 0;
        while (attempts < 2) {
            try {
                waitForClickable(locator);
                driver.findElement(locator).click();
                return;
            } catch (ElementClickInterceptedException e) {
                System.out.println("ElementClickInterceptedException on attempt " + (attempts + 1) + ". Retrying with JS click...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(ie);
                }
                WebElement element = driver.findElement(locator);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                return; // Считаем, что JS клик сработал
            } catch (StaleElementReferenceException e) {
                System.out.println("StaleElementReferenceException on attempt " + (attempts + 1) + ". Retrying...");
                attempts++;
                if (attempts >= 2) {
                    throw e;
                }
                try { Thread.sleep(500); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
            }
        }
    }

    public void sendKeys(By locator, String text) {
        waitForVisibility(locator);
        WebElement element = driver.findElement(locator);
        element.clear();
        element.sendKeys(text);
    }

    public String getText(By locator) {
        waitForVisibility(locator);
        return driver.findElement(locator).getText();
    }

    public boolean isElementDisplayed(By locator) {
        try {
            waitForVisibility(locator);
            return driver.findElement(locator).isDisplayed();
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    // Метод для ожидания загрузки страницы
    public void waitForPageToLoad() {
    }
}