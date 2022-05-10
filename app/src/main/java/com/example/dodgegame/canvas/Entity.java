package com.example.dodgegame.canvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.util.Log;

import com.example.dodgegame.R;

enum Direction {
    RIGHT, LEFT, TOP, BOTTOM,NONE
}

abstract public class Entity {
    private static final String TAG = "test";
    Canvas canvas;
    Context context;
    int x, y;
    Bitmap image;
    int sprite_width, sprite_height;
    Direction direction;
    int moveSpeed;
    int drawable;
    int sprite_x_max, sprite_x, sprite_y;
    int count;

    public Entity(Context context, Canvas canvas) {
        this.canvas = canvas;
        this.context = context;
    }

    public void setImage(int drawable, int sprite_y, int sprite_x_max) {
        this.sprite_x_max = sprite_x_max;
        this.sprite_y = sprite_y;

        this.image = BitmapFactory.decodeResource(context.getResources(), drawable);
        this.sprite_width  = image.getWidth()  / 13;
        this.sprite_height = image.getHeight() / 21;
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

    void update(){
        count++;
        move();
        draw();
        if (count % 10 == 0)
            sprite_x++;
        sprite_x %= sprite_x_max;
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

}
