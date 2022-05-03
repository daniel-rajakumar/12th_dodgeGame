package com.example.dodgegame.canvas;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

class CustomCanvas extends Canvas {
    private static final String TAG = "test";
    Canvas canvas;
    Paint paint;
    int gameWidth = -1, gameHeight = -1;
    boolean inGame;

    CustomCanvas() {
        paint = new Paint();
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }


    public void setInGame(boolean inGame) {
        translate(0, (inGame ? 1 : -1) * (getFullHeight() - getGameHeight())/2f);
        this.inGame = inGame;
    }

    @Override
    public void drawRect(@NonNull Rect r, @NonNull Paint paint) {
        canvas.drawRect(r, paint);
    }

    public Rect Rect(double left, double top, double width, double height) {
        return new Rect( x_per(left),
                         y_per(top),
                        x_per(left) + x_per(width),
                        y_per(top) + y_per(height));
    }


    @Override
    public void drawRGB(int r, int g, int b) {
        canvas.drawRGB(r, g, b);
    }

    @Override
    public void drawBitmap(@NonNull Bitmap bitmap, @NonNull Matrix matrix, @Nullable Paint paint) {
        canvas.drawBitmap(bitmap, matrix, paint);
    }

    Paint Paint(int color, Paint.Style style) {
        paint.setStyle(style);
        paint.setColor(color);
        return paint;
    }

    int x_per(double percent){
        return (int) (inGame ? getGameWidth() * percent: getFullWidth() * percent);
    }

    int y_per(double percent){
        return (int)(inGame ? (getGameHeight() * percent): (getFullHeight() * percent));
    }


    /** @return the greatest common denominator */
    public long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public List<Integer> asFraction(long a, long b) {
        long gcd = gcd(a, b);
        return Arrays.asList((int)(a/gcd), (int) (b/gcd));
    }


    public int getFullHeight() {
        return canvas.getHeight();
    }

    public int getFullWidth() {
        return canvas.getWidth();
    }

    public void updateGameWidth(){
        if (gameWidth != -1 && gameHeight != -1) return;

        int w = getFullWidth(), h = getFullHeight();
        List<Integer> list = asFraction(w, h);
        while (list.get(0) != 2 && list.get(1) != 3){
            list = asFraction(w, --h);
        }
        gameWidth = w;
        gameHeight = h;
    }

    public int getGameWidth(){
        return gameWidth;
    }

    public int getGameHeight(){
        return gameHeight;
    }

    @Override
    public int save() {
        return canvas.save();
    }

    @Override
    public void restore() {
        canvas.restore();
    }






    @Override
    public void translate(float dx, float dy) {
        canvas.translate(dx, dy);
    }
}
