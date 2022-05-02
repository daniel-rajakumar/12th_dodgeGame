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
import java.util.List;

public class DrawCanvas extends CustomCanvas {
    private static final String TAG = "Test";
    double i = 0;

    public void update(Canvas canvas){
        setCanvas(canvas);
        updateGameWidth();
        setInGame(true);
        drawRect(Rect(0,0,1, 1), Paint(Color.RED, Paint.Style.FILL));
        drawRect(Rect(0, 0, 0.5, 0.5), Paint(Color.GREEN, Paint.Style.FILL));
        setInGame(false);
        drawRect(Rect(0, 0.5, 0.5, 0.5), Paint(Color.BLUE, Paint.Style.FILL));

        Log.d(TAG, "update: " + getGameWidth() + ": " + getGameHeight());


//
//        drawRect(Rect(0.5, (i += 0.001), 0.5, 0.1),
//                 paint(Color.GREEN, Paint.Style.STROKE));
    }
}

