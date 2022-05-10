package com.example.dodgegame.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;

import com.example.dodgegame.R;

public class Enemy extends Entity {
    private static final String TAG = "testing";

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
    }
}
