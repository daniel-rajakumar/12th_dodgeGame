package com.example.dodgegame;

import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener2;
import android.os.VibrationEffect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.example.dodgegame.canvas.DrawCanvas;
import com.example.dodgegame.canvas.Entity;
import com.example.dodgegame.canvas.GameScene;
import com.example.dodgegame.canvas.Player;

public class GameSurface extends SurfaceView implements Runnable {

    private static final String TAG = "test";
    SurfaceHolder surfaceHolder;
    Thread gameThread;
    volatile boolean running = false;
    DrawCanvas drawCanvas;
    MotionEvent motionEvent;

    public GameSurface(Context context) {
        super(context);
        this.surfaceHolder = getHolder();
        setUpTouch();
    }

    void setUpTouch() {
        setOnTouchListener((view, motionEvent) -> {
            this.motionEvent = motionEvent;
            performClick();

            if (from_endScreen_to_gameScreen()) return false;
            if (from_startScreen_to_gameScreen()) return false;

            DrawCanvas.player.fire();

//            Log.d(TAG, "setUpTouch: (" + motionEvent.getX() + "," + motionEvent.getY() + ")");

            return false;
        });
    }

    boolean from_endScreen_to_gameScreen(){
        if (DrawCanvas.gameScene == GameScene.END
                && !DrawCanvas.grassAnimation) {
            DrawCanvas.resetGame(this.getContext(), DrawCanvas.canvas);
            DrawCanvas.vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
            return true;
        }
        return false;
    }

    boolean from_startScreen_to_gameScreen(){
        if (DrawCanvas.gameScene == GameScene.START){
            DrawCanvas.gameScene = GameScene.GAME;
            boolean isTop = true;
            while (DrawCanvas.canvas.getHeight() == 0){
            }
            DrawCanvas.team_one = motionEvent.getY() < DrawCanvas.canvas.getHeight()/2f;
            Entity.loadDynamicImage(this.getContext());
            DrawCanvas.player = new Player(this.getContext(), DrawCanvas.canvas);
            DrawCanvas.vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
            return true;
        }
        return false;
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
