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


public final class DriverSetup {
    
	public static JSONObject getDriverConfig(){
		return Store.getInstance().configContent.getJSONObject("config");
	}
    
	public static WebDriverManager setDriverVersion(WebDriverManager driverManager){
		String driverVersion = DriverSetup.getDriverConfig().getString("driver_version").trim();
		if(!driverVersion.isEmpty()){
			return driverManager.driverVersion(driverVersion);
		}
		return driverManager;
	}

	public static WebDriverManager setDriverOPSystem(WebDriverManager driverManager){
		String opSystem = DriverSetup.getDriverConfig().getString("op_system").trim();
		switch(opSystem.toLowerCase()){
			case "win" : return driverManager.win();
			case "linux" : return driverManager.linux();
			case "mac" : return driverManager.mac();
		}
		return driverManager;
	}

	public static WebDriverManager setDriverArch(WebDriverManager driverManager){
		String arch = DriverSetup.getDriverConfig().getString("op_system").trim();
		switch(arch.toLowerCase()){
			case "32" : return driverManager.arch32();
			case "64" : return driverManager.arch64();
		}
		return driverManager;
	}

	public static WebDriver driverSetup(Class<? extends WebDriver> driverClass)
		throws NoSuchMethodException,
		InstantiationException,
		IllegalAccessException,
		InvocationTargetException 
	{
		WebDriverManager driverManager = WebDriverManager.getInstance(driverClass);
		driverManager = DriverSetup.setDriverVersion(driverManager);
		driverManager = DriverSetup.setDriverOPSystem(driverManager);
		driverManager = DriverSetup.setDriverArch(driverManager);
		driverManager.setup();
		return driverClass.getDeclaredConstructor().newInstance();
	}
}