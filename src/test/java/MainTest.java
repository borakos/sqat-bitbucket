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

import org.json.JSONArray;
import org.json.JSONObject;


public class MainTest{
    private WebDriver driver;
    
    @Before
    public void setup(){
        WebDriverManager.chromedriver().setup();

        this.driver = new ChromeDriver();
        this.driver.manage().window().maximize();
    }
    
	/*@Test
    public void UnAuthenticatedTest(){
		JSONArray staticPages = Store.getInstance().configContent.getJSONArray("static_page_tests");
		for(int i = 0; i < staticPages.length(); i++){
			StaticPage page = new StaticPage(this.driver, staticPages.getJSONObject(i).getString("url"));
			Assert.assertTrue(page.getTitleText().contains(staticPages.getJSONObject(i).getString("title_check")));
			Assert.assertTrue(page.getBodyText().contains(staticPages.getJSONObject(i).getString("body_check")));
		}
    }*/

	@Test
    public void AuthenticantedTest(){
		LoginPage loginPage = new LoginPage(this.driver);
		JSONObject credentials = Store.getInstance().configContent.getJSONObject("credentials");
		MainPage mainPage = loginPage.login(credentials.getString("username"), credentials.getString("password"));

		RepositoryPage repositoryPage = mainPage.openRepositoriesPage();

		/*List<String> repoNames = new ArrayList<>();
		List<String> repoDescriptions = new ArrayList<>();
		JSONObject repos = Store.getInstance().configContent.getJSONObject("repos");
		for(int i = 0; i < repos.getInt("random_repos_per_iteration"); i++){
			String repoName = Store.getInstance().getRandomString(10);
			repoNames.add(repoName);
			String repoDescription = Store.getInstance().getRandomString(50);
			repoDescriptions.add(repoDescription);
			repositoryPage.createRepository(repoName, repoDescription);
		}*/

		System.out.print(repositoryPage.getBodyText());

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
