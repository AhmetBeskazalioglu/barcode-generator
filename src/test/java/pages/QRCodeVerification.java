package pages;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;

public class QRCodeVerification {
    @FindBy(id = "qrcode-field-text-text")
    public WebElement qrCodeTextBox;
    @FindBy(id = "qrcode-preview-image")
    public WebElement qrCodeImage;

    // Method to verify QR code with the given image URL and expected value
    public boolean verifyQRCode(String imageUrl, String expectedValue) {
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

    // Private method to read QR code from a BufferedImage
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