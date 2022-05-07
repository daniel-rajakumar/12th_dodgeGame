package com.example.dodgegame.canvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.example.dodgegame.R;

public class Player {
    Bitmap image;
    int i = 0;
    Canvas canvas;

    Player(Context context, Canvas canvas){
        this.canvas = canvas;
        this.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite);
        this.image = Bitmap.createBitmap(this.image, 50, 50, 50 * 3, 50 * 3);
    }

    void draw(){
        Matrix matrix = new Matrix();
        canvas.save();
        canvas.translate(i, 50);
        canvas.scale(2, 2);
        canvas.drawBitmap(image, matrix, null);
        canvas.restore();
    }

    void update(){
        draw();
        i++;
    }
}
