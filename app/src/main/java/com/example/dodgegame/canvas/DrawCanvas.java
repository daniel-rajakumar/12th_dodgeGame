package com.example.dodgegame.canvas;

import android.graphics.Canvas;
import android.graphics.Color;

public class DrawCanvas extends CustomCanvas {
    private static final String TAG = "Test";
    double i = 0;

    public void update(Canvas canvas){
        setCanvas(canvas);
        drawRGB(0,220,0);
        drawRect(Rect(0.5, i, 0.5, 0.1), paint(Color.BLUE));
    }
}

