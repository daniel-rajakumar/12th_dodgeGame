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
    Timer damageCoolDown, blinkTimer;
    int coolDownTime = 5;

    Player(Context context, Canvas canvas){
        super(context, canvas);
        setImage(Direction.TOP, 3f);
        setMoveSpeed(15);
        do {
            setLocation_byPixel(canvas.getWidth()/2, (int)(canvas.getHeight()*0.8));
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



    public boolean blink = false;
    void damagePlayer(){
        blinkTimer.cancel();
        blinkTimer = new Timer();
        blink = false;
        setDamaged(true);
//        Log.d(TAG, "damagePlayer: " + "yes");

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
        Log.d(TAG, "startBlinking: " + "blinking");
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
        DrawCanvas.fire_music.start();

        int width = (this.entity_width/2);
        int height = (this.entity_height/2);

        DrawCanvas.bullets.push(new Bullet(context, canvas
                , this.x + width
                , this.y + height
        ));
    }

    @Override
    public void fire() {
        if (DrawCanvas.gameOver) {
            DrawCanvas.resetGame(context, canvas);
            return;
        }
        if (isPerformingAction() || isDamaged()) return;
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
//        setCollision(new Rect(
//                this.x + (this.sprite_width/2),
//                this.y + this.sprite_height,
//                (this.x + (this.sprite_width/2)) + this.sprite_width/2,
//                this.y + this.sprite_height * 2));


        int width = (this.entity_width/4);
        int height = (this.entity_height/4);

        setCollision(new Rect(
                this.x + (width),
                this.y + (height),
                this.x + (width * 3),
                this.y + (height * 4)));

//        for (int i = 0; i < DrawCanvas.enemies.size(); i++){
//            Rect temp = DrawCanvas.enemies.get(i).collision;
//            if (temp != null) {
//                if (Rect.intersects(collision, temp)){
//                    DrawCanvas.gameRunning = false;
//                }
//            }
//        }
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

        if (!isPerformingAction())
            setImage(direction, 3f);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) { }


}
