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

public class Enemy extends Entity {
    private static final String TAG = "testing";
    private boolean toRemove;

    Enemy(Context context, Canvas canvas, float x){
        super(context, canvas);
        setImage(R.drawable.sprite_enemy, 10, 8);
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
    }

}
