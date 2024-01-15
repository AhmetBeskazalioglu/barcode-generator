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

    public boolean verifyQRCode(String imageUrl, String expectedValue) {
    try {
        // QR kodu al
        BufferedImage image = ImageIO.read(new URL(imageUrl));

        // QR kodunu okuma işlemi
        Result result = readQRCode(image);

        // Okunan QR kodu ile beklenen değeri karşılaştır
        if (result != null && result.getText().equals(expectedValue)) {
            return true;
        } else {
            return false;
        }
    } catch (IOException e) {
        e.printStackTrace();
        return false;
    }
}

    private static Result readQRCode(BufferedImage image) {
        try {
            if (image != null) {
                BinaryBitmap binaryBitmap = new BinaryBitmap(
                        new HybridBinarizer(new BufferedImageLuminanceSource(image)));
                Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
                hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);

                return new MultiFormatReader().decode(binaryBitmap, hints);
            }
        } catch (NotFoundException  e) {
            e.printStackTrace();
        }
        return null;
    }

}