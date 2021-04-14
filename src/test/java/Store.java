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
import java.io.File;
import java.io.FileNotFoundException;


public class Store {
    
	private static Store instance = null;

	public static Store getInstance(){
		if(instance == null){
			instance = new Store();
		}
		return instance;
	}

	public JSONObject configContent;
	private String chars; 

	private Store(){
		this.configContent = new JSONObject(readConfigFile(System.getProperty("user.dir") + "\\src\\config.json"));
		this.chars = "ABCDEFGHIJKLMNOPQRSTVWXYZabcdefghijklmnopqrstvwxyz";
	}

	private String readConfigFile(String path){
		try {
			File file = new File(path);
			Scanner reader = new Scanner(file);
			String content = "";
			while (reader.hasNextLine()) {
				content += reader.nextLine();
			}
			reader.close();
			return content;
		} catch (FileNotFoundException e) {
			System.out.println("Error occurred while reading config file.");
			e.printStackTrace();
			return "";
		}
	}

	public String getRandomString(int length){
		Random random = new Random();
		String text = "";
		for(int i = 0; i < length; i++){
			text += chars.charAt(random.nextInt(chars.length()));
		}
		return text;
	}
}