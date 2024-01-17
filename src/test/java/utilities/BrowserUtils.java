package utilities;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;

public class BrowserUtils {
    /*
     * takes screenshot
     * @param name
     * take a name of a test and returns a path to screenshot takes
     */
    public static String getScreenshot(String name) throws IOException {
        // name the screenshot with the current date time to avoid duplicate name
        String date = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        // TakesScreenshot ---> interface from selenium which takes screenshots
        TakesScreenshot ts = (TakesScreenshot) Driver.get();

        File source = ts.getScreenshotAs(OutputType.FILE);
        //   ((TakesScreenshot)Driver.get()).getScreenshotAs(OutputType.FILE);
        // full path to the screenshot location
        String target = System.getProperty("user.dir") + "/test-output/Screenshots/" + name + date + ".png";
        File finalDestination = new File(target);
        // save the screenshot to the path given
        FileUtils.copyFile(source, finalDestination);
        return target;
    }

    /**
     * Performs a pause
     *
     * @param seconds
     */
    public static void waitFor(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to verify QR code with the given image URL and expected value
     * @param imageUrl
     * @param expectedValue
     * @return
     */
    public static boolean verifyQRCode(String imageUrl, String expectedValue) {
        try {
            // Retrieve the QR code image
            BufferedImage image = ImageIO.read(new URL(imageUrl));

            // Perform QR code reading
            Result result = readQRCode(image);

            // Compare the read QR code with the expected value
            if (result != null && result.getText().equals(expectedValue)) {
                return true; // Return true if they match
            } else {
                return false; // Return false if they don't match
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Return false in case of any IO exception
        }
    }

    /**
     * Private method to read QR code from a BufferedImage
     * @param image
     * @return
     */
    private static Result readQRCode(BufferedImage image) {
        try {
            if (image != null) {
                // Create a BinaryBitmap from the image using HybridBinarizer
                BinaryBitmap binaryBitmap = new BinaryBitmap(
                        new HybridBinarizer(new BufferedImageLuminanceSource(image)));

                // Set decoding hints, attempting a harder decode
                Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
                hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);

                // Decode the QR code using MultiFormatReader
                return new MultiFormatReader().decode(binaryBitmap, hints);
            }
        } catch (NotFoundException e) {
            e.printStackTrace(); // Print the exception if QR code is not found
        }
        return null; // Return null if there's an issue during decoding
    }
}
