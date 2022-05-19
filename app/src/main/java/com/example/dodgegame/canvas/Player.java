package com.example.dodgegame.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.os.VibrationEffect;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Timer;
import java.util.TimerTask;

public class Player extends Entity implements SensorEventListener2 {
    Timer damageCoolDown, blinkTimer;
    int coolDownTime = 5;
    boolean isWalking = true;

    public Player(Context context, Canvas canvas){
        super(context, canvas);
        setImage(Direction.TOP, 3f);
        setMoveSpeed(15);
        do {
            setLocation_byPixel(canvas.getWidth()/2, canvas.getHeight());
        } while (x == 0);
        setDamaged(false);

        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometerSensor, sensorManager.SENSOR_DELAY_NORMAL);

        damageCoolDown = new Timer();
        blinkTimer = new Timer();
    }

    @Override
    void move() {
        if (direction == Direction.RIGHT) moveRight();
        if (direction == Direction.LEFT)  moveLeft();
        if (isWalking)                    moveTop();

//        updateCollision();

        int width = (this.entity_width/4);
        int height = (this.entity_height/4);

        setCollision(new Rect(
                this.x + (width),
                this.y + (height),
                this.x + (width * 3),
                this.y + (height * 8)/3
        ));
    }

    private void moveTop() {
        if (y_per(0.8) <= y)
            y -= 3;
        else
            isWalking = false;
    }


    public boolean blink = false;
    void damagePlayer(){
        DrawCanvas.vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        blinkTimer.cancel();
        blinkTimer = new Timer();
        blink = false;
        setDamaged(true);

        damageCoolDown.cancel();
        damageCoolDown = new Timer();
        damageCoolDown.schedule(new TimerTask() {
            int count = 0;
            public void run() {
                count++;
                if (count == coolDownTime - 1) startBlinking();
                if (count <= coolDownTime) return;
                setDamaged(false);
                damageCoolDown.cancel();
            }
        }, 0, 1000);
    }

    void startBlinking(){
        blinkTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                blink = !blink;
            }
        }, 0, 250);
    }

    @Override
    void checkIsAlive() {

    }

    void fireBullet(){
        this.direction = Direction.NONE;
        this.sprite_x = 0;
        this.sprite_x_max = 4;
        this.sprite_y = 12;

        DrawCanvas.fire_music.start();
        DrawCanvas.vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));

        int width = (this.entity_width/2);
        int height = (this.entity_height/2);

        DrawCanvas.bullets.push(new Bullet(context, canvas
                , this.x + width
                , this.y + height
        ));
    }

    @Override
    public void fire() {
        if (isPerformingAction() || isDamaged()) return;
        setPerformingAction(true);
        fireBullet();
        animateShootingAction();
    }

    void animateShootingAction(){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (++sprite_x < sprite_x_max) return;
                setPerformingAction(false);
                cancel();
            }
        }, 0, 200);
    }

    void updateCollision(){
        int width = (this.entity_width/4);
        int height = (this.entity_height/4);

        setCollision(new Rect(
                this.x + (width),
                this.y + (height),
                this.x + (width * 3),
                this.y + (height * 4)));
    }

    @Override
    public void onFlushCompleted(Sensor sensor) { }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];

        direction = Direction.TOP;
        if (Math.abs(x) > 5f) {
            setMoveSpeed(15);
            isWalkingFaster = true;
            direction = (x < 0) ? Direction.RIGHT : Direction.LEFT;
        } else if (Math.abs(x) > 2f){
            isWalkingFaster = false;
            setMoveSpeed(5);
            direction = (x < 0) ? Direction.RIGHT : Direction.LEFT;
        }

        if (!isPerformingAction())
            setImage(direction, 3f);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) { }


}
