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
    int image_index_max, image_index;
    Bitmap image;
    int sprite_width, sprite_height;
    Direction direction;
    int moveSpeed = 25;

    public Entity(Context context, Canvas canvas) {
        this.canvas = canvas;
        this.context = context;
        this.x = x_per(0.5);
        this.y = y_per(0.8);
    }


    void draw(){
        this.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite_player);
        this.sprite_width  = image.getWidth()  / 13;
        this.sprite_height = image.getHeight() / 21;

        this.image = Bitmap.createBitmap(this.image
                , (sprite_width/4) + (sprite_width *  (1 + image_index))
                , (sprite_height/4) + (sprite_height * 8)
                , sprite_width -  (sprite_width/4)
                , sprite_height - (sprite_height/4));

        Matrix matrix = new Matrix();
        canvas.save();
        canvas.translate(x, y);
        canvas.scale(3f, 3f);
        canvas.drawBitmap(image, matrix, null);
        canvas.restore();
    }

     abstract void move();

    void update(){
        move();
        draw();
        image_index++;
        image_index %= image_index_max;
    }

    int x_per(double percent){
        return (int) (canvas.getWidth() * percent);
    }

    int y_per(double percent){
        return (int) (canvas.getHeight() * percent);
    }

    public void setImage_index_max(int image_index_max) {
        this.image_index_max = image_index_max;
    }
}
