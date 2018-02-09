import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author Brendan Juno and Jason Wallenfang.
 * @version 2/5/2018
 *          Project: ComputerGames
 *          Comments:
 */
public class DustGame extends Application{
    String appName = "Call of Dusty: Modern Roomba 2";
    Image carpet = new Image("Carpet.jpg");
    final int WIDTH = 500;
    final int HEIGHT = 600;
    final int FPS = 30;
    ArrayList<Dust> dustbunnies = new ArrayList<>();
    Roomba roomba = new Roomba();
    Random random = new Random();
    ROOMBA_STATE state = ROOMBA_STATE.IDLE;
    private enum ROOMBA_STATE {LEFT,RIGHT,IDLE}
    int killcount = 0;
    double frames = 0;
    int size;
    int wave = 1;
    Bomb bomb;
    int bombduration = 300;
    int explosionduration = 90;
    boolean bombhit = false;
    boolean nextwave = false;




    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {
        theStage.setTitle(appName);
        size = 10+random.nextInt(10);
        System.out.println(size);
        for (int i = 0;i<size;i++){
            dustbunnies.add(new Dust());
        }
        Group root = new Group();
        Scene theScene = new Scene(root);
        theStage.setScene(theScene);

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        theScene.setOnKeyPressed(
                new EventHandler<KeyEvent>()
                {
                    public void handle(KeyEvent e)
                    {
                        String code = e.getCode().toString();
                        if (code.equals("LEFT"))
                            state = ROOMBA_STATE.LEFT;
                        if (code.equals("RIGHT"))
                            state = ROOMBA_STATE.RIGHT;
                    }
                });

        theScene.setOnKeyReleased(
                new EventHandler<KeyEvent>()
                {
                    public void handle(KeyEvent e)
                    {
                        String code = e.getCode().toString();
                        System.out.println(code);
                        if (code.equals("LEFT")||code.equals("RIGHT"))
                            state = ROOMBA_STATE.IDLE;
                    }
                });
        GraphicsContext gc = canvas.getGraphicsContext2D();


        // Setup and start animation loop (Timeline)
        KeyFrame kf = new KeyFrame(Duration.millis(1000 / FPS),
                e -> {
                    // update position
                    update();
                    // draw frame
                    render(gc);
                }
        );
        Timeline mainLoop = new Timeline(kf);
        mainLoop.setCycleCount(Animation.INDEFINITE);
        mainLoop.play();

        theStage.show();
    }

    void render(GraphicsContext gc){
        gc.setFill(new ImagePattern(carpet));
        gc.fillRect(0, 0, WIDTH, HEIGHT);
        for (Dust e : dustbunnies){
            e.render(gc);
        }
        roomba.render(gc);

        //Scoreboard Rendering
        gc.setFill(Color.WHITE);
        gc.fillRect(0,500,500,200);
        gc.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        frames++;
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(1);
        nf.setGroupingUsed(false);
        gc.setFill(Color.BLACK);
        gc.fillText("Time: "+nf.format(frames/FPS),20,525);
        gc.fillText("Score: "+killcount,20,545);
        gc.fillText("Bunnies Remaining: "+dustbunnies.size(),20,565);
        gc.fillText("Wave: "+wave,20,585);

        //Bomb render
        if(bomb!=null){
            bomb.render(gc);
            if (bombhit == true){
                if (explosionduration>0){
                    gc.setFill(new ImagePattern(new Image("exp.gif")));
                    gc.fillRect(bomb.x-100,bomb.y-100,200,300);
                    explosionduration--;

                }else{
                    bomb = null;
                    bombduration = 300;
                    bombhit = false;
                    explosionduration = 90;
                    nextwave = false;
                }
            }
        }
    }

    void update(){

        //Dustbunny move
        for (int i = 0; i<dustbunnies.size();i++) {
            dustbunnies.get(i).move();
            if (((Math.pow(dustbunnies.get(i).cx-roomba.cx,2))+(Math.pow(roomba.cy-dustbunnies.get(i).cy,2)))<=(Math.pow((roomba.size/2)+(dustbunnies.get(i).size/2),2))){
                killcount+=100;
                dustbunnies.remove(i);

            }

        }

        //Roomba Controls
        switch (state){
            case LEFT:
                roomba.dir_angle = roomba.dir_angle-0.1;
                break;
            case RIGHT:
                roomba.dir_angle = roomba.dir_angle+0.1;
                break;
        }
        roomba.move();

        //New Wave
        if (dustbunnies.size()==0){
            size+=5;
            wave++;
            for (int i = 0;i<size;i++){
                dustbunnies.add(new Dust());
            }
        }

        //Bomb powerup
        if (bomb==null){ //No bomb alive roll the dice.
            if (5==(random.nextInt(250))){ //1/250 odds for a bomb each frame
                System.out.println("Bomb Spawned");
                bomb = new Bomb();
            }
        }else{
            if (bombduration<0){
                bomb = null;
                bombduration = 300;
            }else{
                bombduration--;
                int bx = bomb.x+(bomb.size/2);
                int by = bomb.y+(bomb.size/2);
                if (((Math.pow(bx-roomba.cx,2))+(Math.pow(roomba.cy-by,2)))<=(Math.pow((roomba.size/2)+(bomb.size/2),2))) {
                    if (nextwave==false){
                        killcount += 50;
                        dustbunnies.removeAll(dustbunnies);
                    }
                    nextwave = true;
                    System.out.println("BOOM");
                    bombhit = true;
                }
            }
        }


    }
}
