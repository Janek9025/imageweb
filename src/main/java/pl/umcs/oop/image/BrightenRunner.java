package pl.umcs.oop.image;

import java.awt.image.BufferedImage;

public class BrightenRunner implements Runnable {
    BufferedImage image;
    int start, end, value;

    public BrightenRunner(BufferedImage image, int start, int end, int value) {
        this.image = image;
        this.start = start;
        this.end = end;
        this.value = value;
    }

    @Override
    public void run() {
        for (int y = start; y < end; y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                int b = rgb & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int r = (rgb >> 16) & 0xFF;
                b = Clamp.clamp(b + value, 0, 255);
                g = Clamp.clamp(g + value, 0, 255);
                r = Clamp.clamp(r + value, 0, 255);
                int newRGB = (r << 16) | (g << 8) | b;
                image.setRGB(x, y, newRGB);
            }
        }
    }
}
