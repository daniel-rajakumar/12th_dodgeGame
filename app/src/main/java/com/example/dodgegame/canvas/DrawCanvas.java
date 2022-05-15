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

public class DrawCanvas {
    private static final String TAG = "Test";
    public static Player player;
    static LinkedList<Enemy> enemies = new LinkedList<>();
    static LinkedList<Bullet> bullets = new LinkedList<>();
    Context context;
    Canvas canvas;
    static int score, highScore = Integer.MIN_VALUE;
    static int time, total_time = 30;

    int fontSize;
    static boolean gameOver = false;

    static boolean start = true, end = false;
    static boolean gameRunning = true;
    Paint paint = new Paint();
    String text;
    static MediaPlayer gameplay_music, endScreen_music, fire_music, hurt_music;
    float count = 1;

    public void update(Context context,Canvas canvas){
        this.context = context;
        this.canvas = canvas;

        if (start){
            start = false;
            Entity.loadAllImages(context);
            player = new Player(context, canvas);
            background();
            time = total_time;
            fontSize = 100;
            score = 0;
            paint.setColor(Color.RED);
            paint.setTextSize(fontSize);
            paint.setTypeface(ResourcesCompat.getFont(context, R.font.font_1));
            setUpMusic();
        }

        canvas.drawRGB(0,0,0);

        if (!gameOver) showGamePlayScreen();
        else           showEndScreen();
    }

    void setUpMusic(){
        gameplay_music = MediaPlayer.create(context,R.raw.gameplay_music);
        fire_music = MediaPlayer.create(context,R.raw.fire);
        endScreen_music = MediaPlayer.create(context,R.raw.endscreen_music);
        hurt_music = MediaPlayer.create(context,R.raw.no);
        gameplay_music.start();
        gameplay_music.setLooping(true);
        gameplay_music.setPlaybackParams(gameplay_music.getPlaybackParams().setSpeed(0.75f));
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

    void showGamePlayScreen(){
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
        toggle++;
        Bitmap image = Entity.images.get("endScreen");
        canvas.drawBitmap(image, new Matrix(),null);


        text = "score: " + score;
        canvas.drawText(text
                , player.x_per(0.975) - (fontSize * text.length())
                , player.y_per(0.8)
                , paint);

        paint.setTextSize(fontSize/=2);
        text = "high Score: " + highScore;
        canvas.drawText(text
                , player.x_per(0.975) - (fontSize * text.length())
                , player.y_per(0.8) + (fontSize * 2)
                , paint);
        paint.setTextSize(fontSize*=2);

        if (toggle % 100 < 50){
            paint.setTextSize(fontSize/=2);
            text = "Tap anywhere to";
            canvas.drawText(text
                    , player.x_per(0.975) - (fontSize * text.length())
                    , player.y_per(0.9)
                    , paint);

            text = " play again!";
            canvas.drawText(text
                    , player.x_per(0.975) - (fontSize * text.length())
                    , player.y_per(0.9) + (fontSize * 2)
                    , paint);
            paint.setTextSize(fontSize*=2);
        }
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
        canvas.drawBitmap(Entity.images.get("grass"), 0, player.y_per(-0.1), null);
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

    static float bounceSpeed = 0;
    float bounceSize = 0;

    public void background(){
        int width = player.entity_width/4;
        int min = -width;
        int max = canvas.getWidth() - (width * 3);
        int offset = player.entity_height;

        new Timer().schedule(new TimerTask() {
            public void run() {
                    if (player.isWalking) return;
                    enemies.push(new Enemy(context, canvas, random(min, max),-offset * 2));
            }
        }, 0, 1000);

        new Timer().schedule(new TimerTask() {
            public void run() {
                if (--time < 10){
                    bounceSpeed = 1;
                    bounceSize = 0;
                }

                if (time > -1) return;
                gameOver = true;
                gameplay_music.pause();
                if (score > highScore)
                    highScore = score;
                gameplay_music.seekTo(0);
                endScreen_music.start();
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
        gameOver = false;
        endScreen_music.pause();
        endScreen_music.seekTo(0);
        gameplay_music.start();
        bounceSpeed = 0;

        gameplay_music.setPlaybackParams(gameplay_music.getPlaybackParams().setSpeed(0.75f));
    }



}

