package com.example.dodgegame.canvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Switch;

import com.example.dodgegame.R;

public class Player extends Entity implements SensorEventListener2 {
    private static final String TAG = "testing";

    Player(Context context, Canvas canvas){
        super(context, canvas);
        setImage(R.drawable.sprite_player, 8, 8);
        setMoveSpeed(5);
        setLocation(0.5f, 0.8f);
//        setDrawable(R.drawable.sprite_player);

        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometerSensor, sensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    void move() {
        if (direction == Direction.RIGHT) moveRight();
        if (direction == Direction.LEFT)  moveLeft();
    }

    @Override
    public void onFlushCompleted(Sensor sensor) { }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];

        direction = Direction.NONE;
        if (Math.abs(x) > 2f) {
            direction = (x < 0) ? Direction.RIGHT : Direction.LEFT;
        }

        switch (direction){
            case LEFT: setImage(R.drawable.sprite_player, 9, 8); break;
            case RIGHT: setImage(R.drawable.sprite_player, 11, 8);  break;
            default: setImage(R.drawable.sprite_player, 8, 8); break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) { }


}
