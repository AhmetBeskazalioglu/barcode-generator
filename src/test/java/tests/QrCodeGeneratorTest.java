package tests;


import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import pages.QRCodeVerification;
import utilities.BrowserUtils;
import utilities.ConfigurationReader;


public class QrCodeGeneratorTest extends TestBase {
    QRCodeVerification qr = new QRCodeVerification();

    @Test
    public void testName() {
        driver.get(ConfigurationReader.get("url"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement textBox = driver.findElement(By.id("qrcode-field-text-text"));
        String text = ConfigurationReader.get("codeGenerated");
        js.executeScript("arguments[0].value = '" + text + "';", textBox);
        //js.executeScript("arguments[0].value = '" + text + "';", qr.qrCodeTextBox);
        textBox.sendKeys("\t");
        BrowserUtils.waitFor(1);
        String imageUrl = driver.findElement(By.id("qrcode-preview-image")).getAttribute("src");
        //String imageUrl = qr.qrCodeImage.getAttribute("src");
        String expectedValue = text;
        Assert.assertTrue(qr.verifyQRCode(imageUrl, expectedValue));

    }
}



