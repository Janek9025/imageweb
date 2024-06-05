package pl.umcs.oop.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageProcessor {
    BufferedImage image;

    public void read(String scr) throws IOException {
            image = ImageIO.read(new File(scr));
    }

    public void write(String src) throws IOException {
        String format = src.substring(src.lastIndexOf('.') + 1);

        ImageIO.write(image, format,  new File(src));
    }

    public void brighten(int value){
        for(int y = 0; y < image.getHeight(); y++){
            for (int x = 0; x < image.getWidth(); x++){
                int rgb = image.getRGB(x, y);
//                System.out.println("RGB signed 32 bit = "+rgb+" hex: "+Integer.toHexString(rgb));
                int b = rgb&0xFF;
                int g = (rgb>>8)&0xFF;
                int r = (rgb>>16)&0xFF;
                b = Clamp.clamp(b + value, 0, 255);
                g = Clamp.clamp(g + value, 0, 255);
                r = Clamp.clamp(r + value, 0, 255);
                int newRGB = (r<<16) | (g<<8) | b;
                image.setRGB(x, y, newRGB);
            }
        }
    }

    public void brightenMulti(int value) throws InterruptedException {
        int numThreads = Runtime.getRuntime().availableProcessors();
        int height = image.getHeight();
        Thread[] threads = new Thread[numThreads];
        int chunkSize = height / numThreads;

        for (int i = 0; i < numThreads; i++) {
            int startIndex = i * chunkSize;
            int endIndex = (i == numThreads - 1) ? height : (i + 1) * chunkSize;

            threads[i] = new Thread(new BrightenRunner(image, startIndex, endIndex, value));
            threads[i].start();
        }

        for (Thread thread : threads) thread.join();
    }
}
