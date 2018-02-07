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
    ROOMBA_STATE state = ROOMBA_STATE.LEFT;
    private enum ROOMBA_STATE {LEFT,RIGHT,IDLE}
    double killcount = 0;
    double frames = 0;




    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {
        theStage.setTitle(appName);
        int size = 10+random.nextInt(10);
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
        gc.setFill(Color.WHITE);
        gc.fillRect(0,500,500,200);
        gc.setLineWidth(10);

        gc.setFill(Color.WHITE);
        gc.fillRect(0,500,500,200);
        gc.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        frames++;
        killcount = frames/30;
        gc.fillText("Kill Count: "+killcount,30,30);
        for (Dust e : dustbunnies){
            e.render(gc);
        }
        roomba.render(gc);
    }

    void update(){
        for (Dust e: dustbunnies) {
            e.x+=e.vx;
            e.y+=e.vy;
        }
        roomba.move();
    }
}
