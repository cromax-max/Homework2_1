package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardOrderFormTest {

    private WebDriver driver;
    private ChromeOptions options;
    private String nameField = "[name='name']";
    private String phoneField = "[name='phone']";
    private String checkbox = ".checkbox__box";
    private String submitButton = ".button__text";

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setupTest() {
        options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.navigate().to("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldBeValidData() {
        driver.findElement(By.cssSelector(nameField)).sendKeys("Петров Роман");
        driver.findElement(By.cssSelector(phoneField)).sendKeys("+79012345678");
        driver.findElement(By.cssSelector(checkbox)).click();
        driver.findElement(By.cssSelector(submitButton)).click();
        String text = driver.findElement(By.cssSelector("p[data-test-id='order-success']")).getText();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        assertEquals(expected, text.trim());
    }

    @Test
    void shouldBeInvalidNameFields() {
        driver.findElement(By.cssSelector(nameField)).sendKeys("Rhona Cox");
        driver.findElement(By.cssSelector(phoneField)).sendKeys("+79012345678");
        driver.findElement(By.cssSelector(checkbox)).click();
        driver.findElement(By.cssSelector(submitButton)).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'] .input__sub")).getText();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        assertEquals(expected, text.trim());
    }

    @Test
    void shouldBeInvalidPhoneFields() {
        driver.findElement(By.cssSelector(nameField)).sendKeys("Петров Роман");
        driver.findElement(By.cssSelector(phoneField)).sendKeys("-7901234567");
        driver.findElement(By.cssSelector(checkbox)).click();
        driver.findElement(By.cssSelector(submitButton)).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'] .input__sub")).getText();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        assertEquals(expected, text.trim());
    }

    @Test
    void shouldBeUncheckedCheckbox() {
        driver.findElement(By.cssSelector(nameField)).sendKeys("Петров Роман");
        driver.findElement(By.cssSelector(phoneField)).sendKeys("+79012345678");
        driver.findElement(By.cssSelector(submitButton)).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='agreement']")).getAttribute("className");
        assertTrue(text.contains("input_invalid"));
    }

    @Test
    void shouldBeEmptyFields() {
        driver.findElement(By.cssSelector(submitButton)).click();
        String text = driver.findElement(By.cssSelector("[data-test-id] .input__sub")).getText();
        String expected = "Поле обязательно для заполнения";
        assertEquals(expected, text.trim());

    }
}
