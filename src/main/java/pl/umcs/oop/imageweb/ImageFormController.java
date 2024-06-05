package pl.umcs.oop.imageweb;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import java.util.Scanner;

@RestController
@RequestMapping("restapi")
public class ImageFormController {
    @GetMapping("index")
    ResponseEntity<byte[]> index() throws IOException {
        Resource resource = new ClassPathResource("static/index.html");
        Path path = resource.getFile().toPath();
        byte[] fileBytes = Files.readAllBytes(path);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);

        return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
    }

    @PostMapping("index")
    public ModelAndView uploadImage(@RequestParam("image") MultipartFile file, @RequestParam("brightness") int brightness) {
        ModelAndView modelAndView = new ModelAndView("image");
        try {
            byte[] bytes = file.getBytes();

            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
            BufferedImage bufferedImage1 = changeBrightness(bufferedImage, brightness);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            String imageType = file.getContentType().substring(6);
            ImageIO.write(bufferedImage1, imageType, byteArrayOutputStream);
            System.out.println(Arrays.toString(new String[]{file.getContentType()}));
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            modelAndView.addObject("image", base64Image);

            System.out.println(base64Image);

            return modelAndView;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return modelAndView;
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
