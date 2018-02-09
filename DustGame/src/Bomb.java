import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

import java.util.Random;

public class Bomb {

    int size = 50;
    int x;
    int y;
    Random random = new Random();

    public Bomb() {
        x = random.nextInt(500);
        y = random.nextInt(500);
    }
    void render(GraphicsContext gc){
        gc.setFill(new ImagePattern(new Image("bomb.png")));
        gc.fillOval(x,y,size,size);
    }
}
