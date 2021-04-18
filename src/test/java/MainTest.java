import org.junit.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;

import org.openqa.selenium.support.ui.WebDriverWait;

import org.openqa.selenium.support.ui.ExpectedConditions;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

import java.lang.reflect.InvocationTargetException;
import java.util.*;  

import org.json.JSONArray;
import org.json.JSONObject;


public class MainTest{
    private WebDriver driver;

	@Before
    public void setup() 
		throws NoSuchMethodException,
		InstantiationException,
		IllegalAccessException,
		InvocationTargetException 
	{
		switch(DriverSetup.getDriverConfig().getString("driver").toLowerCase()){
			case "chrome": 			  this.driver = DriverSetup.driverSetup(ChromeDriver.class);break;
			case "firefox": 		  this.driver = DriverSetup.driverSetup(FirefoxDriver.class);break;
			case "edge": 			  this.driver = DriverSetup.driverSetup(EdgeDriver.class);break;
			case "opera": 			  this.driver = DriverSetup.driverSetup(OperaDriver.class);break;
			case "internet explorer": this.driver = DriverSetup.driverSetup(InternetExplorerDriver.class);break;
		}
        this.driver.manage().window().maximize();
    }
    
	@Test
    public void UnAuthenticatedTest(){
		JSONArray staticPages = Store.getInstance().configContent.getJSONArray("static_page_tests");
		for(int i = 0; i < staticPages.length(); i++){
			StaticPage page = new StaticPage(this.driver, staticPages.getJSONObject(i).getString("url"));
			Assert.assertTrue(page.getTitleText().contains(staticPages.getJSONObject(i).getString("title_check")));
			Assert.assertTrue(page.getBodyText().contains(staticPages.getJSONObject(i).getString("body_check")));
		}

		for(int i = staticPages.length() - 1; i >= 0; i--){
			PageBase page = new PageBase(this.driver);
			Assert.assertTrue(page.getTitleText().contains(staticPages.getJSONObject(i).getString("title_check")));
			Assert.assertTrue(page.getBodyText().contains(staticPages.getJSONObject(i).getString("body_check")));
			page.back();
		}
    }


	@Test
    public void AuthenticantedTest(){
		LoginPage loginPage = new LoginPage(this.driver);
		JSONObject credentials = Store.getInstance().configContent.getJSONObject("credentials");
		MainPage mainPage = loginPage.login(credentials.getString("username"), credentials.getString("password"));

		RepositoryPage repositoryPage = mainPage.openRepositoriesPage();

		Map<String, String> repoDescriptions = repositoryPage.generateRandomRepos();

		repositoryPage.waitForReposToLoad();

		String repoList = repositoryPage.getBodyText();
		for(Map.Entry<String,String> entry : repoDescriptions.entrySet()){
			Assert.assertTrue(repoList.contains(entry.getKey()));
			Assert.assertTrue(repoList.contains(entry.getValue()));
    	}

		JSONObject repos = Store.getInstance().configContent.getJSONObject("repos");
		repositoryPage.downloadRepos(repos.getJSONArray("repos_to_download"), repos.getInt("random_repo_download_limit"));
	
		LogoutPage logoutPage = mainPage.logout();
		logoutPage.submitLogout();
    }
    
    @After
    public void close(){
        if(driver != null){
            driver.quit();
        }
    }
}
