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


public class MainPage extends PageBase {
	private By profileAndSettingsLocator = By.xpath("//button[@id='profileGlobalItem']");
	private By repositoriesLocator = By.xpath("//a[@href='/dashboard/repositories']");
	private By logoutLocator = By.xpath("//div[@role='menu']/a[@href='https://id.atlassian.com/logout?continue=https%3A%2F%2Fbitbucket.org%2Faccount%2Fsignout%2F']");
    
	public MainPage(WebDriver driver){
		super(driver);
	}

	public RepositoryPage openRepositoriesPage(){
		this.waitAndReturnElement(repositoriesLocator).click();
		return new RepositoryPage(driver);
	}

	public LogoutPage logout(){
		this.waitAndReturnElement(profileAndSettingsLocator).click();
		this.waitAndReturnElement(logoutLocator).click();
		return new LogoutPage(driver);
	}
}