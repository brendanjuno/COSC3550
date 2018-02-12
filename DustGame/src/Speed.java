import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

import java.util.Random;
/**
 * @author Brendan Juno and Jason Wallenfang.
 * @version 2/5/2018
 *          Project: COSC3550 - Assignment 3
 *          Comments: Speed Powerup
 *          Handling is done within the DustGame class.
 *
 **/
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
