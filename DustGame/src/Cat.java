import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

import java.util.Random;
/**
 * @author Brendan Juno and Jason Wallenfang.
 * @version 2/5/2018
 *          Project: COSC3550 - Assignment 3
 *          Comments: Cat Enemy
 *          Handling is done within the DustGame class.
 *
 **/
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
