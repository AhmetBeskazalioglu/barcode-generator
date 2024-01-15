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
        // Open the application URL
        driver.get(ConfigurationReader.get("url"));

        // Initialize JavascriptExecutor
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Find the text box element by ID
        WebElement textBox = driver.findElement(By.id("qrcode-field-text-text"));

        // Get the code generated from configuration
        String text = ConfigurationReader.get("codeGenerated");

        // Set the value of the text box using JavaScript
        js.executeScript("arguments[0].value = '" + text + "';", textBox);

        // Tab out of the text box to trigger QR code generation
        textBox.sendKeys("\t");

        // Wait for a short period to allow QR code generation
        BrowserUtils.waitFor(1);

        // Get the generated QR code image URL
        String imageUrl = driver.findElement(By.id("qrcode-preview-image")).getAttribute("src");

        // Get the expected value (code generated)
        String expectedValue = text;

        // Assert that the generated QR code matches the expected value
        Assert.assertTrue(qr.verifyQRCode(imageUrl, expectedValue));
    }
}
