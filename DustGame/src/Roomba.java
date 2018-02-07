import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.transform.Affine;

/**
 * @author Brendan Juno
 * @version 2/7/2018
 *          Project: COSC3550
 *          Comments:
 */
public class Roomba {
    int x = 200;
    int y = 200;
    int size = 100;
    int vx = 2;
    int vy = 2;
    Image roomba = new Image("room.png");

    void render(GraphicsContext gc){
        gc.setFill(Color.BLACK);
        gc.fillOval(x,y,size,size);
        gc.setFill(new ImagePattern(roomba));
        gc.fillOval(x,y,size,size);
    }

    void move(){

        if (x>=0 && x<=400){
            x+=vx;
        }
        if (y>=0 && y<=400){
            y+=vy;
        }
    }




}
