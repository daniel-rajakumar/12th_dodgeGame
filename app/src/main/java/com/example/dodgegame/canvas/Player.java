package com.example.dodgegame.canvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import com.example.dodgegame.R;

public class Player {
    private static final String TAG = "testing";
    Bitmap image;
    Canvas canvas;
    Context context;
    int image_index = 0;
    int sprite_width, sprite_height;
    int x, y;
    int moveSpeed = 15;

    Player(Context context, Canvas canvas){
        this.canvas = canvas;
        this.context = context;
        this.x = x_per(0.5);
        this.y = y_per(0.8);
    }

    void draw(){
        this.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite);
        this.sprite_width = image.getWidth() / 13;
        this.sprite_height = image.getHeight() / 21;


        this.image = Bitmap.createBitmap(this.image
                , (sprite_width/4) + (sprite_width *  (1 + image_index))
                , (sprite_height/4) + (sprite_height * 8)
                , sprite_width -  (sprite_width/4)
                , sprite_height - (sprite_height/4));


        Paint paint = new Paint();
        paint.setColor(Color.RED);

//        Log.d(TAG, "update: [w: " + w + " | h: " + h + "]");

        Matrix matrix = new Matrix();
        canvas.save();
        canvas.translate(x, y);
        canvas.scale(3f, 3f);
        canvas.drawBitmap(image, matrix, null);
        canvas.restore();
    }

    void update(){
        draw();
        image_index++;

        image_index %= 8;
    }

    void moveLeft(){
        if (x - moveSpeed < 0) {
            this.x=0;
            return;
        }
        this.x-=moveSpeed;
    }

    void moveRight(){
        if ((x + (sprite_width - sprite_width/4) * 2) + moveSpeed > canvas.getWidth()) {
            this.x = canvas.getWidth() - (sprite_width - (sprite_width/4)) * 2;
            return;
        }
        this.x+=moveSpeed;
    }



    int x_per(double percent){
        return (int) (canvas.getWidth() * percent);
    }

    int y_per(double percent){
        return (int) (canvas.getHeight() * percent);
    }

}
