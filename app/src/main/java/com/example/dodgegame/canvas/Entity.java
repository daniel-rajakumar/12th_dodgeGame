package com.example.dodgegame.canvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Switch;

import com.example.dodgegame.R;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

enum Direction {
    RIGHT, LEFT, TOP, BOTTOM,NONE
}

abstract public class Entity {
    private static final String TAG = "test";
    Canvas canvas;
    Context context;
    int x, y;
    int sprite_width, sprite_height;
    Direction direction;
    int moveSpeed;
    int drawable;
    int sprite_x_max, sprite_x, sprite_y;
    int count;
    Rect collision;
    boolean toRemove;
    static Map<String, Bitmap> sprites = new HashMap<>();
    Bitmap image;

    public Entity(Context context, Canvas canvas) {
        this.canvas = canvas;
        this.context = context;
        toRemove = false;
    }

//    public void setImage(int drawable, int sprite_y, int sprite_x_max) {
    public void setImage(Direction direction) {
        if (this instanceof Player) this.image = sprites.get("player");
        if (this instanceof Enemy ) this.image = sprites.get("enemy");
//        Log.d(TAG, "setImage: " + (this instanceof Player)+ this );
        Log.d(TAG, "setImage: " + (sprites));

        switch (direction){
            case LEFT:   this.sprite_x_max = 8; sprite_y = 9;  break;
            case RIGHT:  this.sprite_x_max = 8; sprite_y = 11;  break;
            case BOTTOM: this.sprite_x_max = 8; sprite_y = 10;  break;
            case TOP:    this.sprite_x_max = 8; sprite_y = 8; break;
        }

        this.sprite_width  = image.getWidth()  / 13;
        this.sprite_height = image.getHeight() / 21;
    }

    static void loadAllSpites(Context context){
        sprites.put("player", BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite_player));
        sprites.put("enemy", BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite_enemy));
    }


    void setLocation(float x, float y){
        this.x = x_per(x);
        this.y = y_per(y);
    }

    public void setDrawable(int drawable) {
        this.image = BitmapFactory.decodeResource(context.getResources(), drawable);
    }

    void draw(){
        canvas.save();
        canvas.translate(x, y);
        canvas.scale(3f, 3f);
        canvas.drawBitmap(
                Bitmap.createBitmap(this.image
                        , (sprite_width/4) + (sprite_width *  (1 + sprite_x))
                        , (sprite_height/4) + (sprite_height * sprite_y)
                        , sprite_width -  (sprite_width/4)
                        , sprite_height - (sprite_height/4))
                , new Matrix(), null);
        canvas.restore();
    }

     abstract void move();
    abstract void checkIsAlive();

    void update(){
        count++;
        move();
        draw();
        showCollision();
        checkIsAlive();





        if (count % 10 == 0)
            sprite_x++;
        sprite_x %= sprite_x_max;
    }


    void showCollision(){
        Paint paint = new Paint();
        paint.setColor(Color.RED);

        canvas.drawRect(collision, paint);
    }

    void moveLeft(){
        if (x - moveSpeed < 0) {
//            this.x=0;
            return;
        }
        x -= moveSpeed;
    }

    void moveRight(){
        if ((x + (sprite_width - sprite_width/4) * 2) + moveSpeed > canvas.getWidth()) {
//            this.x = canvas.getWidth() - (sprite_width - (sprite_width/4)) * 2;
            return;
        }
        x += moveSpeed;
    }


    void moveBottom(){
        y += moveSpeed;
    }

    int x_per(double percent){
        return (int) (canvas.getWidth() * percent);
    }

    int y_per(double percent){
        return (int) (canvas.getHeight() * percent);
    }


    public void setMoveSpeed(int moveSpeed) {
        this.moveSpeed = moveSpeed;
    }


    public void setCollision(Rect collision) {
        if (collision != null)
            this.collision = collision;
    }

    void fallDown(){
        sprite_y = 20;
        sprite_x_max = 6;
//        final int[] count = {0};
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            public void run() {
//                count[0]++;
//            }
//        }, 0, 1000);

    }

    public boolean isToRemove() {
        return toRemove;
    }

    public void setToRemove(boolean toRemove) {
        this.toRemove = toRemove;
    }
}
