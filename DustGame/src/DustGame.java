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
import javafx.stage.Stage;
import javafx.util.Duration;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author Brendan Juno and Jason Wallenfang.
 * @version 2/5/2018
 *          Project: COSC3550 - Assignment 3
 *          Comments: Take control of the Roomba and attempt to defeat multiple waves of dustbunnies while avoiding the cats who will drain your battery!
 *          Pick up powerups along the way to make your Roomba stronger and try to get the highscore! But be careful, if your battery runs out it is GAME OVER!
 *          Suck up the dust by driving the Roomba over them, once sucked a marker will indicate that they are destroyed!
 *
 *          Powerups are:
 *              Bomb - Destroy all of the visible dust bunnies and start the next wave.
 *              Speed Boost - Temporarily increase the speed of the Roomba by increasing its power! Be careful, the machine may act strange as it is not designed to operate at these speeds.
 *          Controls are:
 *              LEFT and RIGHT - Control the direction of the Roomba!
 *
 *
 *         This is the main game code.
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
    int score = 0;
    double frames = 0;
    int size;
    int wave = 1;
    Bomb bomb;
    Speed boost;
    Cat cat;
    int bombduration = 300;
    int speedduration = 300;
    int boostduration = 200;
    int explosionduration = 90;
    int catduration = 500;
    int endduration = 150;
    int finalScore;
    boolean bombhit = false;
    boolean nextwave = false;
    boolean speedhit = false;
    boolean cathit = false;
    boolean gameend = false;
    boolean showhit = false;

    String[] lives = {"dead.png", "low.png", "medium.png", "full.png"};

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

        //Bomb render
        if(bomb!=null){
            bomb.render(gc);
            if (bombhit == true){
                if (explosionduration>0){
                    gc.setFill(new ImagePattern(new Image("boom.png")));
                    gc.fillRect(bomb.x-100,bomb.y-100,300,150);
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

        //Speed render
        if(boost!=null){
            boost.render(gc);
            if(speedhit == true){
                if(boostduration > 0){
                    gc.setFill(new ImagePattern(new Image("boost.png")));
                    gc.fillRect(boost.x-25, boost.y-25, 100, 100);
                    boostduration--;
                }
                else{
                    boost = null;
                    speedduration = 300;
                    speedhit = false;
                    boostduration = 200;
                }
            }
        }

        //Cat(enemy) render
        if(cat!=null){
            cat.render(gc);
            if(cathit == true){
                if(catduration > 0){
                    catduration--;
                }
                else{
                    cat = null;
                    catduration = 500;
                    cathit = false;
                }
            }
        }

        //Hit Marker Rendering
        if (showhit){
            showhit = false;
            gc.setFill(new ImagePattern(new Image("hitmarker.png")));
            gc.fillRect(roomba.x+25,roomba.y+25,50,50);
        }

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
        gc.fillText("Score: "+ score,20,545);
        gc.fillText("Bunnies Remaining: "+dustbunnies.size(),20,565);
        gc.fillText("Wave: "+wave,20,585);
        gc.fillText("Battery: " + roomba.battery, 375, 517);
        gc.setFill(new ImagePattern(new Image(lives[roomba.battery])));
        gc.fillRect(400, 520, 25, 75);


        //Game over render
        if (roomba.battery == 0){
            if (endduration<0){
                System.exit(2);//Exit Game
            }
            if (!gameend){
                finalScore = score;
                gameend = true;
            }
            //Draw End Scene
            gc.setFill(Color.WHITE);
            gc.fillRect(0,0,WIDTH,HEIGHT);
            gc.setFill(Color.BLACK);
            gc.fillText("Game Over!",150,300);
            gc.fillText("Final Score: "+finalScore,150,350);
            endduration--;
        }
    }

    void update(){

        //Dustbunny move
        for (int i = 0; i<dustbunnies.size();i++) {
            dustbunnies.get(i).move();
            if (((Math.pow(dustbunnies.get(i).cx-roomba.cx,2))+(Math.pow(roomba.cy-dustbunnies.get(i).cy,2)))<=(Math.pow((roomba.size/2)+(dustbunnies.get(i).size/2),2))){
                score +=100;
                showhit = true;
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
                        score += 50;
                        dustbunnies.removeAll(dustbunnies);
                    }
                    nextwave = true;
                    System.out.println("BOOM");
                    bombhit = true;
                }
            }
        }

        //Speed powerup
        if(boost==null) {
            if (3 == (random.nextInt(300))) {
                System.out.println("Boost Spawned");
                boost = new Speed();
            }
        }
        else {
            if (speedduration < 0) {
                boost = null;
                speedduration = 300;
            } else {
                speedduration--;
                int sx = boost.x + (boost.size/2);
                int sy = boost.y + (boost.size/2);
                if(((Math.pow(sx-roomba.cx,2)) + (Math.pow(roomba.cy - sy,2))) <= (Math.pow((roomba.size/2) + (boost.size/2),2))) {
                    boostduration = 200;
                    roomba.speed = 5;
                    System.out.println("SPEED BOOST!" + roomba.speed);
                    speedhit = true;
                }
                boostduration--;
                if(boostduration<=0){
                    roomba.speed = 3;
                }
            }
        }

        //Cat enemy
        if(cat == null){
            if(1 == (random.nextInt(200))){
                System.out.println("Cat Spawned");
                cat = new Cat();
            }
        }
        else{
            if(catduration < 0){
                cat = null;
                catduration = 500;
            }
            else{
                catduration--;
                int ex = cat.x + (cat.size/2);
                int ey = cat.y + (cat.size/2);
                if(((Math.pow(ex-roomba.cx, 2)) + (Math.pow(roomba.cy-ey, 2))) <= (Math.pow((roomba.size/2) + (cat.size/2),2))){
                    System.out.println("A cat drained your battery!");
                    cathit = true;
                    cat = null;
                    roomba.battery--;
                }
            }
        }
    }
}
