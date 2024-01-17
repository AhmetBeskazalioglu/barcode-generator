package tests;

import com.aventstack.extentreports.ExtentTest;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utilities.BrowserUtils;
import utilities.Driver;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TestBase {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Actions action;
    protected ExtentTest extentLogger;

    @BeforeMethod
    public void setUp() {
        driver = Driver.get();
        //driver.get(ConfigurationReader.get("url"));
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        wait=new WebDriverWait(driver,10);
        action=new Actions(driver);
        //driver.manage().window().maximize();
        //driver.manage().window().fullscreen();
    }

    @AfterMethod
    public void tearDown(ITestResult result) throws InterruptedException, IOException {
        //if Test fails
        if(result.getStatus()==ITestResult.FAILURE){
            //Record the name of the failed test
            extentLogger.fail(result.getName());
            //Take the screenshot and return the location of screenshot
            String screenShotPath= BrowserUtils.getScreenshot(result.getName());
            //Add the screenshot to the report
            extentLogger.addScreenCaptureFromPath(screenShotPath);
            //Capture the exception and put inside the report
            extentLogger.fail(result.getThrowable());
;        }

        Thread.sleep(2000);
        Driver.closeDriver();
    }

}
