package pl.umcs.oop.imageweb;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

@RestController
public class ImageController {
    @GetMapping("/restapi/image/adjustbrightness")
    public ResponseEntity<String> adjustBrightness(@RequestBody ImageDTO imageDTO) throws IOException {
        String image64 = imageDTO.getBase64Image();
        String[] image = image64.split(",");;
        String imageType = image[0].substring(image[0].indexOf("/") + 1,image[0].indexOf(";"));
        byte[] imageByte = Base64.getDecoder().decode(image[1]);
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageByte));
        BufferedImage bufferedImage1 = changeBrightness(bufferedImage, imageDTO.getBrightness());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage1, imageType, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        System.out.println(Arrays.toString(imageBytes));
        System.out.println(Arrays.toString(imageByte));
        String encoded = Base64.getEncoder().encodeToString(imageBytes);
        String base64 = "data:image/" + imageType + ";base64," + encoded;
        System.out.println(base64);
        System.out.println(encoded);
        System.out.println(image[1]);
        return new ResponseEntity<>(base64, HttpStatus.OK);
    }

    @GetMapping("/restapi/image/adjustbrightness/noncoded")
    public ResponseEntity<byte[]> adjustBrightnessNonCoded(@RequestBody ImageDTO imageDTO) throws IOException {
        String image64 = imageDTO.getBase64Image();
        String[] image = image64.split(",");;
        String imageType = image[0].substring(image[0].indexOf("/") + 1,image[0].indexOf(";"));
        byte[] imageByte = Base64.getDecoder().decode(image[1]);
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageByte));
        BufferedImage bufferedImage1 = changeBrightness(bufferedImage, imageDTO.getBrightness());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage1, imageType, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return new ResponseEntity<>(imageBytes, HttpStatus.OK);
    }

    private BufferedImage changeBrightness(BufferedImage originalImage, int brightness) {
        BufferedImage result = new BufferedImage(
                originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < originalImage.getHeight(); y++) {
            for (int x = 0; x < originalImage.getWidth(); x++) {
                Color color = new Color(originalImage.getRGB(x, y));
                int r = Clamp.clamp(color.getRed() + brightness, 0, 255);
                int g = Clamp.clamp(color.getGreen() + brightness, 0, 255);
                int b = Clamp.clamp(color.getBlue() + brightness, 0, 255);
                Color newColor = new Color(r, g, b);
                result.setRGB(x, y, newColor.getRGB());
            }
        }
        return result;
    }
}
