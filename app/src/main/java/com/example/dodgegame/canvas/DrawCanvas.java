package com.example.dodgegame.canvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener2;
import android.util.Log;
import android.view.Display;

import com.example.dodgegame.R;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.IntStream;

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

    static boolean start = true, end = false;
    static boolean gameRunning = true;

    public void update(Context context,Canvas canvas){
        this.context = context;
        this.canvas = canvas;

        if (start){
            start = false;
            Entity.loadAllSpites(context);
            player = new Player(context, canvas);
            background();
        }

        canvas.drawRGB(0,0,0);
        updateEnemies();
        updateBullets();
        player.update();
        updateScore();
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

    void updateScore(){
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(100);
        paint.setTypeface(Typeface.createFromAsset(getAssets(), "ChopinScript.ttf"));
        canvas.drawText("hello", 500, 500,paint);

    }

    public void background(){
        new Timer().schedule(new TimerTask() {
            public void run() {
                    float rand = new Random().nextFloat();
                    enemies.push(new Enemy(context, canvas, player.x_per(rand), player.y_per(-0.2)));
            }
        }, 0, 1000);
    }

}

