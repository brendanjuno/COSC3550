import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

import java.util.Random;

public class Speed {

    int size = 50;
    int x, y;
    Random random = new Random();

    public Speed(){
        x = random.nextInt(500);
        y = random.nextInt(500);
    }

    void render(GraphicsContext gc){
        gc.setFill(new ImagePattern(new Image("speed.png")));
        gc.fillOval(x, y, size, size);
    }
}
