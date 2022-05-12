package com.example.dodgegame.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.util.Log;

import com.example.dodgegame.R;

import java.sql.Struct;
import java.util.Timer;
import java.util.TimerTask;

public class Enemy extends Entity {
    private static final String TAG = "testing";
    private boolean toRemove;

    Enemy(Context context, Canvas canvas, float x){
        super(context, canvas);
        setImage(Direction.BOTTOM);
        setMoveSpeed(5);
        setLocation(x, 0);
        direction = Direction.BOTTOM;
    }

    @Override
    void move() {
        if (direction == Direction.BOTTOM) moveBottom();

        setCollision(new Rect(
                this.x + (this.sprite_width/2),
                this.y + this.sprite_height,
                (this.x + (this.sprite_width/2)) + this.sprite_width/2,
                this.y + this.sprite_height * 2));
    }

    @Override
    void checkIsAlive() {
//        Log.d(TAG, "checkIsAlive: " + "(" + x +  " > " + canvas.getHeight() + ")");
        if (this.y > canvas.getHeight()){
            setToRemove(true);
        }

        for (int i = 0; i < DrawCanvas.enemies.size(); i++){
            Rect temp2 = DrawCanvas.enemies.get(i).collision;
            Rect temp1 = DrawCanvas.player.collision;
            if (temp1 != null && temp2 != null)
                if (Rect.intersects(temp1, temp2)) {
                    removeEnemy(DrawCanvas.enemies.get(i));
//                    DrawCanvas.enemies.get(i).setToRemove(true);
//                    Log.d(TAG, "checkIsAlive: remove!");
                }
        }
    }

    void removeEnemy(Enemy enemy){
        if (enemy.isHasTouched()) return;

        enemy.setHasTouched(true);
        enemy.direction = Direction.NONE;
        enemy.sprite_x = 0;
        enemy.sprite_x_max = 5;
        enemy.sprite_y = 20;

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (++enemy.sprite_x <= enemy.sprite_x_max) return;
                enemy.setToRemove(true);
                cancel();
            }
        }, 500, 500);
    }




}
