import org.junit.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import java.util.*;  


public class LoginPage extends PageBase {
	//wawopi8538@whipjoy.com
	//sqat21_late

	private By usernameLocator = By.xpath("//input[@id='username']");
	private By passwordLocator = By.xpath("//input[@id='password']");
    
	public LoginPage(WebDriver driver){
		super(driver);
		this.driver.get("https://id.atlassian.com/login?application=bitbucket&continue=https%3A%2F%2Fbitbucket.org%2Faccount%2Fsignin%2F%3FredirectCount%3D1%26next%3D%252F");
	}

	public MainPage login(String username, String password){
		this.waitAndReturnElement(usernameLocator).sendKeys(username + "\n");
		this.waitAndReturnElement(passwordLocator).sendKeys(password + "\n");
		return new MainPage(driver);
	}
}