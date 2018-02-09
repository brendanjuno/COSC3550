import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

/**
 * @author Brendan Juno
 * @version 2/5/2018
 *          Project: ComputerGames
 *          Comments: Testing Git
 */
public class Dust {
    int x;
    int y;
    int size = 20;
    int vx;
    int vy;
    int cx = x+(size/2);
    int cy = y+(size/2);
    Random random = new Random();
    public Dust(){
        x = random.nextInt(500);
        y = random.nextInt(500);
        vx = -5+random.nextInt(10);
        vy = -5+random.nextInt(10);
    }

    void render(GraphicsContext gc){
        tranfer();
        gc.setFill(Color.GREY);
        gc.fillOval(x,y,size,size);
    }

    void tranfer(){
        if (y<=0){
            y = 495;
            //x = 500-x;
        }
        if (y>=500){
            y = 5;
            //x = 500-x;
        }
        if (x<=-20){
            x = 495;
        }
        if (x>=500){
            x = 5;
        }
    }

    void move(){
        x+=vx;
        y+=vy;
        cx = x+(size/2);
        cy = y+(size/2);
    }

}
