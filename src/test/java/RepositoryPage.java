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
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;


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

	public void waitForReposToLoad(){
		By tableLocator = By.xpath("//table[@class='css-1yq88hf edylmxf0']");
		this.waitAndReturnElement(tableLocator);
	}

	public void downloadRepos(JSONArray repoNames, int randomReposCount){
		Map<String, String> repos = reposToDownload(getRepoLinks(), repoNames, randomReposCount);
		JSONObject repoConfig = Store.getInstance().configContent.getJSONObject("repos");

		for(Map.Entry<String, String> entry : repos.entrySet()){
			driver.get(entry.getValue());
			WebElement downloadPageLink = getDownloadPageLinkElement();
			if(downloadPageLink != null){
				downloadPageLink.click();
				String destinationPath = repoConfig.getString("download_destination");
				File directory = new File(destinationPath);
				if (!directory.exists()){
					directory.mkdir();
				}
				destinationPath += "\\" + entry.getKey() + ".zip";
				downloadFromLink(destinationPath, getDownloadLink());
			}
    	}
	}

	private String getAuthorizationToken(){
		JSONObject credentials = Store.getInstance().configContent.getJSONObject("credentials");
		String userPass = credentials.getString("username") + ":" + credentials.getString("password");
		return "Basic " + new String(Base64.getEncoder().encode(userPass.getBytes()));
	}

	private void downloadFromLink(String path, String urlPath){
		try {
			URL url = new URL(urlPath);
			URLConnection connection = url.openConnection();
			connection.setRequestProperty("Authorization", getAuthorizationToken());
			
			BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
			FileOutputStream fileOutputStream = new FileOutputStream(path);
			byte dataBuffer[] = new byte[1024];
			int bytesRead;
			while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
				fileOutputStream.write(dataBuffer, 0, bytesRead);
			}
			fileOutputStream.close();
		} catch (IOException exc) {
			System.out.println(exc.getStackTrace());
		}
	}

	private String getDownloadLink(){
		By locator = By.xpath("//tr[@class='download-repo']/td/a[@class='lfs-warn-link']");
		WebElement item = this.waitAndReturnElement(locator);
		return item.getAttribute("href");
	}

	private WebElement getDownloadPageLinkElement(){
		By locator = By.xpath("//div[@role='presentation']/a[@data-testid='NavigationItem']");
		List<WebElement> items = this.waitAndReturnElements(locator);
		int i = 0;
		while((i < items.size()) && !items.get(i).getAttribute("href").contains("downloads")){
			i++;
		}
		return i < items.size() ? items.get(i) : null;
	}

	private Map<String, String> getRepoLinks(){
		Map<String, String> map = new HashMap<String, String>();
		By locator = By.xpath("//span[@class='css-1ezm1k2 e1b5og4v0']/a[@target='_self']");
		List<WebElement> items = this.waitAndReturnElements(locator);
		for(int i = 0; i < items.size(); i++){
			if(!items.get(i).getText().trim().isEmpty()){
				map.put(items.get(i).getText(), items.get(i).getAttribute("href"));
			}
		}
		return map;
	}

	private Map<String, String> reposToDownload(Map<String, String> repos, JSONArray repoNames, int randomReposCount){
		List<String> names = new ArrayList<>();
		Map<String, String> download = new HashMap<String, String>();
		Random random = new Random();

		for(int i = 0; i < repoNames.length(); i++){
			String key = (String) repoNames.get(i);
			if(repos.containsKey(key)){
				download.put(key, repos.get(key));
				repos.remove(key);
			}
		}

		for(Map.Entry<String,String> entry : repos.entrySet()){
			names.add(entry.getKey());
    	}

		int lenght = randomReposCount > names.size() ? names.size() : randomReposCount;
		for(int i = 0; i < lenght; i++){
			int index  = random.nextInt(names.size());
			String key = names.get(index);
			names.remove(index);
			download.put(key, repos.get(key));
		}
		return download;
	}

	public Map<String, String> generateRandomRepos(){
		Map<String, String> repoDescriptions = new HashMap<String, String>();
		JSONObject repos = Store.getInstance().configContent.getJSONObject("repos");
		for(int i = 0; i < repos.getInt("random_repos_per_iteration"); i++){
			String repoName = Store.getInstance().getRandomString(10);
			String repoDescription = Store.getInstance().getRandomString(50);
			repoDescriptions.put(repoName, repoDescription);
			createRepository(repoName, repoDescription);
		}
		return repoDescriptions;
	}
}