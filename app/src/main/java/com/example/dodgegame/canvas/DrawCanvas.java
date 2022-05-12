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
    static Player player;
    static LinkedList<Enemy> enemies = new LinkedList<>();
    static LinkedList<Enemy> bullets = new LinkedList<>();
    Context context;
    Canvas canvas;
    Timer timer;

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
        player.update();
    }


    public void updateEnemies(){
//        enemies.removeIf(enemy -> enemy.y == canvas.getHeight());
        enemies.removeIf(Enemy::isToRemove);

        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).update();
        }

//        Log.d(TAG, "updateEnemies: " + enemies.size());
    }

    public void background(){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                    enemies.push(new Enemy(context, canvas, new Random().nextFloat()));
            }
        }, 0, 1000);
    }

}

