package com.example.dodgegame.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class Player extends Entity implements SensorEventListener2 {
    private static final String TAG = "testing";

    Player(Context context, Canvas canvas){
        super(context, canvas);
        setImage(Direction.TOP);
        setMoveSpeed(5);
        setLocation_byPercentage(0.5f, 0.8f);

        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometerSensor, sensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    void move() {
        if (direction == Direction.RIGHT) moveRight();
        if (direction == Direction.LEFT)  moveLeft();

        updateCollision();
    }

    @Override
    void checkIsAlive() {

    }

    void fireBullet(){
        DrawCanvas.bullets.push(new Bullet(context, canvas, x + (sprite_width/2), y));
    }

    @Override
    public void fire() {
        if (isPerformingAction()) return;
        setPerformingAction(true);

        this.direction = Direction.NONE;
        this.sprite_x = 0;
        this.sprite_x_max = 4;
        this.sprite_y = 12;

        fireBullet();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "fire: sprite_x: " + sprite_x + " | x_max: " + sprite_x_max);
                if (++sprite_x < sprite_x_max) return;
                Log.d(TAG, "fire: cancel");
                setPerformingAction(false);
                cancel();
            }
        }, 0, 200);
        Log.d(TAG, "fire: Hello world");

    }

    void updateCollision(){
        setCollision(new Rect(
                this.x + (this.sprite_width/2),
                this.y + this.sprite_height,
                (this.x + (this.sprite_width/2)) + this.sprite_width/2,
                this.y + this.sprite_height * 2));

        for (int i = 0; i < DrawCanvas.enemies.size(); i++){
            Rect temp = DrawCanvas.enemies.get(i).collision;
            if (temp != null) {
                if (Rect.intersects(collision, temp)){
                    DrawCanvas.gameRunning = false;
                }
            }
        }

//        Log.d(TAG, "updateCollision: " + DrawCanvas.gameRunning);
    }

    @Override
    public void onFlushCompleted(Sensor sensor) { }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];

        direction = Direction.TOP;
        if (Math.abs(x) > 2f) {
            direction = (x < 0) ? Direction.RIGHT : Direction.LEFT;
        }

//        Log.d(TAG, "onSensorChanged: " + direction);
        if (!isPerformingAction())
            setImage(direction);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) { }


}
