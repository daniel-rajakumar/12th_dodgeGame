package com.example.dodgegame.canvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import androidx.core.content.res.ResourcesCompat;

import com.example.dodgegame.R;

import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class DrawCanvas {
    private static final String TAG = "Test";
    int i = 0;
    public static Player player;
    static LinkedList<Enemy> enemies = new LinkedList<>();
    static LinkedList<Bullet> bullets = new LinkedList<>();
    Context context;
    Canvas canvas;
    static int score;
    static int time;
    int fontSize;
    static boolean gameOver = false;

    static boolean start = true, end = false;
    static boolean gameRunning = true;
    Paint paint = new Paint();
    String text;

    public void update(Context context,Canvas canvas){
        this.context = context;
        this.canvas = canvas;

        if (start){
            start = false;
            Entity.loadAllImages(context);
            player = new Player(context, canvas);
            background();
            time = 90;
            fontSize = 100;
            score = 0;

            paint.setColor(Color.RED);
            paint.setTextSize(fontSize);
            paint.setTypeface(ResourcesCompat.getFont(context, R.font.font_1));
        }

        canvas.drawRGB(0,0,0);

        if (!gameOver) showGamePlayScreen();
        else           showEndScreen();
    }

    void showGamePlayScreen(){
        updateEnemies();
        updateBullets();
        player.update();
        updateGrass();
        updateScore();
    }

    int toggle = 0;
    void showEndScreen(){
        toggle++;
        Bitmap image = Entity.images.get("endScreen");
        canvas.drawBitmap(image, new Matrix(),null);

        text = "score: " + score;
        canvas.drawText(text
                , player.x_per(0.975) - (fontSize * text.length())
                , player.y_per(0.7)
                , paint);

        if (toggle % 100 < 50){
            paint.setTextSize(fontSize/=2);
            text = "Tap anywhere to";
            canvas.drawText(text
                    , player.x_per(0.975) - (fontSize * text.length())
                    , player.y_per(0.8)
                    , paint);

            text = " play again!";
            canvas.drawText(text
                    , player.x_per(0.975) - (fontSize * text.length())
                    , player.y_per(0.8) + (fontSize * 2)
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
        String text = String.valueOf(score);
        canvas.drawText(text,
                player.x_per(0.95) - (text.length() * fontSize),
                player.y_per(0.075),
                paint);

    }

    public void background(){
        int width = (player.entity_width/4);
        int min = -width;
        int max = canvas.getWidth() - (width * 3);
        int offset = player.entity_height;

        new Timer().schedule(new TimerTask() {
            public void run() {
                    enemies.push(new Enemy(context, canvas, random(min, max),-offset * 2));
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
        time = 90;
    }

}

