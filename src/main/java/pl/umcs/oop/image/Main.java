package pl.umcs.oop.image;

import pl.umcs.oop.image.ImageProcessor;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        ImageProcessor imageProcessor = new ImageProcessor();

        Long startTime;
        Long endTime;

        try {
            System.out.println(Runtime.getRuntime().availableProcessors());
            imageProcessor.read("thailand.jpg");

            startTime = System.currentTimeMillis();
            imageProcessor.brighten(20);
            endTime = System.currentTimeMillis();

            imageProcessor.write("thailand2.jpg");

            System.out.println(endTime - startTime);
//---
            imageProcessor.read("thailand.jpg");

            startTime = System.currentTimeMillis();
            imageProcessor.brightenMulti(20);
            endTime = System.currentTimeMillis();

            imageProcessor.write("thailand2.jpg");

            System.out.println(endTime - startTime);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}