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


public class LogoutPage extends PageBase {

	private By logoutSubmitLoator = By.xpath("//button[@id='logout-submit']");
    
	public LogoutPage(WebDriver driver){
		super(driver);
	}

	public void submitLogout(){
		this.waitAndReturnElement(logoutSubmitLoator).click();
	}
}