package com.example.dodgegame.drawCanvas;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class DrawCanvas extends CustomCanvas {
    private static final String TAG = "Test";
    double i = 0;

    public void update(Canvas canvas){
        setCanvas(canvas);
        drawRGB(0,0,0);
        drawRect(Rect(0.5, (i += 0.001), 0.5, 0.1), paint(Color.BLUE));
    }
}

