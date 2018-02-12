import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

import java.util.Random;

public class Cat {
    int x, y;
    int size = 50;
    Random random = new Random();

    public Cat(){
        x = random.nextInt(500);
        y = random.nextInt(500);
    }

    void render(GraphicsContext gc){
        gc.setFill(new ImagePattern(new Image("cat.png")));
        gc.fillOval(x,y,size,size);
    }
}
