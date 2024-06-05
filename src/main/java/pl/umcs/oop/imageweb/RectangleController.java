package pl.umcs.oop.imageweb;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("restapi")
public class RectangleController {

    private List<Rectangle> rectangles = new ArrayList<>();
    @GetMapping("rectangle")
    Rectangle rectangle() {
        return new Rectangle(1, 2, 10, 15, "Red");
    }

    @PostMapping("rectangle")
    void rectanglePost(@RequestBody Rectangle rectangle) {
        rectangles.add(rectangle);
    }

    @GetMapping("rectangle/list")
    List<Rectangle> rectangleList() {
        return rectangles;
    }

    @GetMapping("rectangle/index")
    Rectangle rectangleIndex(@RequestParam int index) {
        return rectangles.get(index);
    }

    @PutMapping("rectangle/index")
    void rectangleIndexPut(@RequestParam int index, @RequestBody Rectangle rectangle) {
        Rectangle rectangle1 = rectangles.get(index);
        rectangle1.setColor(rectangle.getColor());
        rectangle1.setHeight(rectangle.getHeight());
        rectangle1.setWidth(rectangle.getWidth());
        rectangle1.setX(rectangle().getX());
        rectangle1.setY(rectangle().getY());
    }

    @DeleteMapping("rectangle/index")
    void rectangleIndexDelete(@RequestParam int index) {
        rectangles.remove(index);
    }

    @GetMapping("rectangle/svg")
    String rectangleSVG() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<svg>");
        for (Rectangle rectangle : rectangles
        ) {
            stringBuilder.append(String.format(
                    "<rect x=\"%d\" y=\"%d\" width=\"%d\" height=\"%d\" fill=\"%s\" />\n",
                    rectangle.getX(),
                    rectangle.getY(),
                    rectangle.getWidth(),
                    rectangle.getHeight(),
                    rectangle.getColor()
            ));
        }
        stringBuilder.append("</svg>");
        return stringBuilder.toString();
    }

}
