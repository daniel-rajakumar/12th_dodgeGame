package com.example.dodgegame;

import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener2;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.example.dodgegame.canvas.DrawCanvas;

public class GameSurface extends SurfaceView implements Runnable {

    private static final String TAG = "test";
    SurfaceHolder surfaceHolder;
    Thread gameThread;
    volatile boolean running = false;
    DrawCanvas drawCanvas;

    public GameSurface(Context context) {
        super(context);
        this.surfaceHolder = getHolder();
        setUpTouch();
    }

    void setUpTouch() {
        setOnTouchListener((view, motionEvent) -> {
            performClick();
            DrawCanvas.player.fire();
            return false;
        });
    }


    @Override
    public void run() {
        this.drawCanvas = new DrawCanvas();
        while (running) {
            if (!surfaceHolder.getSurface().isValid()) continue;
            Canvas canvas = surfaceHolder.lockCanvas();
            drawCanvas.update(this.getContext(), canvas);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }


    public void resume(){
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void pause(){
        running = false;
        while(true){
            try {
                gameThread.join();
            } catch (InterruptedException ignored){}
        }
    }
}
