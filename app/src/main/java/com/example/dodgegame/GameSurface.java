package com.example.dodgegame;

import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener2;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.dodgegame.drawCanvas.DrawCanvas;

public class GameSurface extends SurfaceView implements Runnable, SensorEventListener2 {

    private static final String TAG = "test";
    SurfaceHolder surfaceHolder;
    Thread gameThread;
    volatile boolean running = false;
    DrawCanvas drawCanvas;

    public GameSurface(Context context) {
        super(context);
        this.surfaceHolder = getHolder();
        this.drawCanvas = new DrawCanvas();
    }

    @Override
    public void onFlushCompleted(Sensor sensor) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void run() {
        while (running) {
            if (!surfaceHolder.getSurface().isValid()) continue;
            Canvas canvas = surfaceHolder.lockCanvas();
            drawCanvas.update(canvas);
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
