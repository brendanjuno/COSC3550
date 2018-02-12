import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.transform.Affine;

/**
 * @author Brendan Juno and Jason Wallenfang.
 * @version 2/5/2018
 *          Project: COSC3550 - Assignment 3
 *          Comments: This is the Roomba class which contains all information releated to the Roomba (player).
 *          Control is done in the DustGame class.
 *
 **/

public class Roomba {
    int x = 200;
    int y = 200;
    int size = 100;
    int battery = 3;
    double speed = 3;
    double dir_angle = 0;
    double vx = speed * Math.cos(dir_angle);
    double vy = speed * Math.sin(dir_angle);
    Image roomba = new Image("room.png");
    int cx = x+(size/2);
    int cy = y+(size/2);

    void render(GraphicsContext gc){
        gc.setFill(new ImagePattern(roomba));
        gc.fillOval(x,y,size,size);
    }

    void move(){
        if (x>400 || x<0){
            System.out.println("Bounce!");
            speed = -speed;
        }
        if (y>400 || y<0){
            System.out.println("Bounce!");
            speed = -speed;
        }
        vx = speed * Math.cos(dir_angle);
        vy = speed * Math.sin(dir_angle);
        x+=vx;
        y+=vy;
        cx = x+(size/2);
        cy = y+(size/2);


        if (y>=0 && y<=400){


        }

    }




}
