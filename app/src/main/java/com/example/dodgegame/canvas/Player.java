package com.example.dodgegame.canvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.example.dodgegame.R;

public class Player {
    private static final String TAG = "testing";
    Bitmap image;
    int i = 0;
    Canvas canvas;
    Context context;
    int image_index = 0;
    int sprite_width, sprite_height;

    Player(Context context, Canvas canvas){
        this.canvas = canvas;
        this.context = context;
    }

    void draw(){
        this.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite);
        this.sprite_width = image.getWidth() / 13;
        this.sprite_height = image.getHeight() / 21;

        this.image = Bitmap.createBitmap(this.image
                , (sprite_width/4) + (sprite_width *  (1 + image_index))
                , (sprite_height/4) + (sprite_height * 8)
                , sprite_width -  sprite_width/4
                , sprite_height - sprite_height/4);

        Paint paint = new Paint();
        paint.setColor(Color.RED);

//        Log.d(TAG, "update: [w: " + w + " | h: " + h + "]");

        Matrix matrix = new Matrix();
        canvas.save();
//        canvas.translate(i, 50);
        canvas.scale(5f, 5f);
        canvas.drawBitmap(image, matrix, null);
        canvas.restore();
    }

    void update(){
        draw();
        i++;
        image_index++; image_index %= 8;
    }
}
