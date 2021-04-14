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


public class RepositoryPage extends PageBase {

	public RepositoryPage(WebDriver driver){
		super(driver);
	}

	public void createRepository(String name, String description){
		By creationLinkLocator = By.xpath("//a[@href='/repo/create']");
		this.waitAndReturnElement(creationLinkLocator).click();
		
		By projectSelectLocator = By.xpath("//div[@id='s2id_id_project']/a");
		By projectItemSelectLocator = By.xpath("//div[@id='select2-drop']/ul/li[@class='select2-results-dept-0 select2-result select2-result-selectable select2-highlighted'][1]");
		this.waitAndReturnElement(projectSelectLocator).click();
		this.waitAndReturnElement(projectItemSelectLocator).click();

		By repositoryNameLocator = By.xpath("//input[@id='id_name']");
		this.waitAndReturnElement(repositoryNameLocator).sendKeys(name);

		By advancedSettingLocator = By.xpath("//div[@class='repo-create--expandable']/button[@class='repo-create--expand-trigger aui-button aui-button-link']");
		this.waitAndReturnElement(advancedSettingLocator).click();

		By descriptionLocator = By.xpath("//textarea[@id='id_description']");
		this.waitAndReturnElement(descriptionLocator).sendKeys(description);

		By languageSelectLocator = By.xpath("//div[@id='s2id_id_language']/a");
		By languageItemSelectLocator = By.xpath("//div[@id='select2-drop']//ul/li[@class='select2-results-dept-1 select2-result select2-result-selectable'][2]");
		this.waitAndReturnElement(languageSelectLocator).click();
		this.waitAndReturnElement(languageItemSelectLocator).click();

		By submitLocator = By.xpath("//div[@class='buttons-container']//button[@type='submit']");
		this.waitAndReturnElement(submitLocator).click();

		returnToRepoIndex();
	}

	private void returnToRepoIndex(){
		driver.get("https://bitbucket.org/dashboard/repositories");
	}
}