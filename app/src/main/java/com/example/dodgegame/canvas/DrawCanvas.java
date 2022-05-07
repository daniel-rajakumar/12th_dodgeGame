package com.example.dodgegame.canvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;

import com.example.dodgegame.R;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DrawCanvas extends CustomCanvas {
    private static final String TAG = "Test";
    double i = 0;

    public void update(Context context,Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.RED);

        canvas.drawRect(new Rect(0, 0, 50, 50), paint);
//        setCanvas(canvas);
//        drawRGB(0,0,0);
//        updateGameWidth();
//        setInGame(true);
////        drawRect(Rect(0,0,1, 1), Paint(Color.RED, Paint.Style.FILL));
////        drawRect(Rect(0, 0, 0.5, 0.5), Paint(Color.GREEN, Paint.Style.FILL));
//
//        Bitmap image = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite);
//        i+=0.1;
//        int w = 48;
////        int w = (int) ((i * 50) % 9);
//        int x = ((int) i * w) % (w * 8) + 50;
//        image = Bitmap.createBitmap(image, x, 48 * 8, 50, 50);
////        Log.d(TAG, "update: " + x);
//        Matrix matrix1 = new Matrix();
//        Matrix matrix2 = new Matrix();
//        Matrix matrix = new Matrix();
//
//        matrix1.setScale(5f, 5f, 5f, 5f);
//        matrix2.setTranslate(getFullWidth()/2f, getFullHeight()/2f);
//
//        new Matrix(matrix1);
//        Log.d(TAG, "update: " + matrix);
//        drawBitmap(image, matrix, null);
//        setInGame(false);
//        drawRect(Rect(0, 0.5, 0.5, 0.5), Paint(Color.BLUE, Paint.Style.FILL));


//        Log.d(TAG, "update: " + getGameWidth() + ": " + getGameHeight());
    }
}

