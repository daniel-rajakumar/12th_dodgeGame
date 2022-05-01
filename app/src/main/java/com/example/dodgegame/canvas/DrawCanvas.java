package com.example.dodgegame.canvas;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;

public class DrawCanvas extends CustomCanvas {
    private static final String TAG = "Test";
    double i = 0;

    public void update(Canvas canvas){
        setCanvas(canvas);
        drawRGB(0,255,0);

        drawRect(Rect(0.1, 0.1, 0.8, 0.8), paint(Color.RED, Paint.Style.FILL_AND_STROKE));

        Log.d(TAG, "update: " + getWidth() + ": " + getHeight());
        Log.d(TAG, "update: " + asFraction(getWidth(), getHeight()));
//
//        drawRect(Rect(0.5, (i += 0.001), 0.5, 0.1),
//                 paint(Color.GREEN, Paint.Style.STROKE));
    }
}

