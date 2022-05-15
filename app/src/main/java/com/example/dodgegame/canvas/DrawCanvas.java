package com.example.dodgegame.canvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.core.content.res.ResourcesCompat;

import com.example.dodgegame.R;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

enum GameScene {
    START, GAME, END
}

public class DrawCanvas {
    private static final String TAG = "Test";
    public static Player player;
    static LinkedList<Enemy> enemies = new LinkedList<>();
    static LinkedList<Bullet> bullets = new LinkedList<>();
    Context context;
    Canvas canvas;
    static int score, highScore = Integer.MIN_VALUE;
    static int time, total_time = 30;

    int fontSize = 100;
    static GameScene gameScene = GameScene.START;
//    static boolean endScreen = false, startScreen = false, gameScreen = true;
    static boolean grassAnimation = false;

    static boolean start = true, game_start = true, end_start = true;

    static boolean gameRunning = true;
    Paint paint = new Paint();
    String text;
    static MediaPlayer gameplay_music, endScreen_music, fire_music, hurt_music;
    float count = 1;
    static float grass_animation_count = 1.5f;

    static Timer timer_enemy = new Timer(),
                 timer_time = new Timer();

    public void update(Context context,Canvas canvas){
        this.context = context;
        this.canvas = canvas;

        if (start){
            start = false;
            Entity.loadAllImages(context);
            player = new Player(context, canvas);
            time = total_time;
            fontSize = 100;
            score = 0;
            paint.setColor(Color.RED);
            paint.setTextSize(fontSize);
            paint.setTypeface(ResourcesCompat.getFont(context, R.font.font_1));
            setUpMusic();
        }

        canvas.drawRGB(0,0,0);

        switch (gameScene){
            case START: showStartScreen(); break;
            case GAME: showGamePlayScreen(); break;
            case END: showEndScreen(); break;
        }
    }

    void setUpMusic(){
        gameplay_music = MediaPlayer.create(context,R.raw.gameplay_music);
        fire_music = MediaPlayer.create(context,R.raw.fire);
        endScreen_music = MediaPlayer.create(context,R.raw.endscreen_music);
        hurt_music = MediaPlayer.create(context,R.raw.no);
    }

    void updateMusic(){
        Log.d(TAG, "music: " + (total_time - time) + " == " + (total_time/2));
//        if (total_time/time > 2){
//            mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(2f));
//        }

        if (time > 10 && (total_time - time) == total_time*0.5){
            gameplay_music.setPlaybackParams(gameplay_music.getPlaybackParams().setSpeed(1f));
        }

        if (time == 10){
            gameplay_music.setPlaybackParams(gameplay_music.getPlaybackParams().setSpeed(1.25f));
        }
    }


    void showStartScreen(){
        updateGrass();
        toggle++;

        if (!grassAnimation){
            updateScreenText();
        }
    }

    void showGamePlayScreen(){
        if (game_start){ game_start = false;
            gameplay_music.start();
            gameplay_music.setLooping(true);
            gameplay_music.setPlaybackParams(gameplay_music.getPlaybackParams().setSpeed(0.75f));
            background();
        }

        updateEnemies();
        updateBullets();
        player.update();
        updateGrass();
        updateScore();
        updateTime();
        updateMusic();
    }

    int toggle = 0;
    void showEndScreen(){
        if (grassAnimation){
            updateEnemies();
            updateBullets();
            player.update();
        }
        updateGrass();
        toggle++;
//        Bitmap image = Entity.images.get("endScreen");
//        canvas.drawBitmap(image, new Matrix(),null);
        if (!grassAnimation){
            updateScreenText();
            if (end_start){
                endScreen_music.start();
                end_start = false;
            }
        }
    }


    public void updateScreenText(){
        showPlayerSymbol();
        text = "Score: " + (gameScene == GameScene.START ? "N/A" : score);
        canvas.drawText(text
                , player.x_per(0.975) - (fontSize * text.length())
                , player.y_per(0.1)
                , paint);

        paint.setTextSize(fontSize/=2);
        text = "High Score: " + (gameScene == GameScene.START ? "N/A" : highScore);
        canvas.drawText(text
                , player.x_per(0.975) - (fontSize * text.length())
                , player.y_per(0.1) + (fontSize * 2)
                , paint);
        paint.setTextSize(fontSize*=2);

        if (toggle % 100 < 50){
            paint.setTextSize(fontSize/=1.5);
            text = "Tap anywhere to";
            canvas.drawText(text
                    , player.x_per(0.975) - (fontSize * text.length())
                    , player.y_per(0.9)
                    , paint);

            text = " play" + (gameScene == GameScene.START ? "!" : " again!");
            canvas.drawText(text
                    , player.x_per(0.975) - (fontSize * text.length())
                    , player.y_per(0.9) + (fontSize * 2)
                    , paint);
            paint.setTextSize(fontSize*=1.5);
        }
    }

    void showPlayerSymbol(){
        int scalar = 6;
        canvas.save();
        canvas.translate(
               canvas.getWidth()/2f - (DrawCanvas.player.sprite_width * scalar)/2f
                , canvas.getHeight()/2f - (DrawCanvas.player.sprite_height * scalar)/2f
        );
        canvas.scale(scalar, scalar);
        canvas.drawBitmap(
                Bitmap.createBitmap(Entity.images.get("player")
                        , DrawCanvas.player.sprite_width * 0
                        , DrawCanvas.player.sprite_height * 2
                        , DrawCanvas.player.sprite_width
                        , DrawCanvas.player.sprite_height
                ), new Matrix(), null);
        canvas.restore();
    }

    public void updateBullets(){
        for (int i = 0; i < bullets.size(); i++)
            if (bullets.get(i).isToRemove())
                bullets.remove(i);


        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).update();
        }

    }

    public void updateEnemies(){
        for (int i = 0; i < enemies.size(); i++)
            if (enemies.get(i).isToRemove())
                enemies.remove(i);


        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).update();
        }
    }

    void updateGrass(){
        Bitmap image = Entity.images.get("grass_long");
        Log.d(TAG, "updateGrass: " + grass_animation_count);

         grassAnimate();
        canvas.drawBitmap(image, 0, -image.getHeight() + player.y_per(grass_animation_count), null);
    }

    void grassAnimate(){
        Log.d(TAG, "grass: scene: " + gameScene + " | count: " + grass_animation_count);
        switch (gameScene){
            case START: case END:
                grassShow(); break;
            case GAME: grassHide(); break;
        }
    }

    void grassHide(){
        if (grass_animation_count >= 0.5){
            grass_animation_count -= 0.01;
            Log.d(TAG, "scene: grass -> " + "hiding");
            grassAnimation = true;
            return;
        }
        Log.d(TAG, "scene: grass -> " + "noting");
        grassAnimation = false;
    }

    void grassShow(){
        if (grass_animation_count <= 1.5f){
            grass_animation_count += 0.01;
            Log.d(TAG, "scene: grass -> " + "showing");
            grassAnimation = true;
            return;
        }
        Log.d(TAG, "scene: grass -> " + "noting");
        grassAnimation = false;
    }

    void updateScore(){
        text = String.valueOf(score);
        canvas.drawText(text,
                player.x_per(0.95) - (text.length() * fontSize),
                player.y_per(0.075),
                paint);
    }

    void updateTime(){
        text = String.valueOf(time);

        bounceSize += bounceSpeed;
        paint.setTextSize(paint.getTextSize() + bounceSize);
        canvas.drawText(text,
                player.x_per(0.05),
                player.y_per(0.075) + bounceSize/2,
                paint);
        paint.setTextSize(paint.getTextSize() - bounceSize);

        Log.d(TAG, "updateTime: bounce: " + bounceSpeed);

        if (time < 10){
            bounceSpeed *= 1.02;
        }
    }

    public void background(){
        generateEnemies();
        timerCountDown();
    }

    void generateEnemies(){
        int width = player.entity_width/4;
        int min = -width;
        int max = canvas.getWidth() - (width * 3);
        int offset = player.entity_height;

        timer_enemy.cancel();
        timer_enemy = new Timer();
        timer_enemy.schedule(new TimerTask() {
            public void run() {
                if (player.isWalking) return;
                enemies.push(new Enemy(context, canvas, random(min, max),-offset * 2));
            }
        }, 0, 1000);
    }

    static float bounceSpeed = 0;
    float bounceSize = 0;
    void timerCountDown(){
        timer_time.cancel();
        timer_time = new Timer();
        timer_time.schedule(new TimerTask() {
            public void run() {
                if (--time < 10){
                    bounceSpeed = 1;
                    bounceSize = 0;
                }

                if (time > -1) return;
                gameScene = GameScene.END;
                gameplay_music.pause();
                if (score > highScore)
                    highScore = score;
                gameplay_music.seekTo(0);
            }
        }, 0, 1000);
    }


    int random(int min, int max){
        return (int) (Math. random()*(max-min+1))+min;
    }

    public static void resetGame(Context context, Canvas canvas){
        enemies.clear();
        bullets.clear();
        player = new Player(context, canvas);
        score = 0;
        time = total_time;
        gameScene = GameScene.GAME;
        bounceSpeed = 0;
        grass_animation_count = 1.5f;
        game_start = end_start = true;

        // sounds
        endScreen_music.pause();
        endScreen_music.seekTo(0);
        gameplay_music.start();
        gameplay_music.setPlaybackParams(gameplay_music.getPlaybackParams().setSpeed(0.75f));
    }



}

