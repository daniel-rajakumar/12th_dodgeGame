package com.example.dodgegame.canvas;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.annotation.NonNull;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

class CustomCanvas extends Canvas {
    Canvas canvas;
    Paint paint;

    CustomCanvas() {
        paint = new Paint();
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void drawRect(@NonNull Rect r, @NonNull Paint paint) {
        canvas.drawRect(r, paint);
    }

    public Rect Rect(double left, double top, double width, double height) {
        return new Rect(x_per(left), y_per(top), x_per(left) + x_per(width), y_per(top) + y_per(height));
    }


    @Override
    public void drawRGB(int r, int g, int b) {
        canvas.drawRGB(r, g, b);
    }

    Paint paint(int color, Paint.Style style) {
        paint.setStyle(style);
        paint.setColor(color);
        return paint;
    }

    int x_per(double percent){
        return (int) (canvas.getWidth() * percent);
    }

    int y_per(double percent){
        return (int) (canvas.getHeight() * percent);
    }

    /** @return the greatest common denominator */
    public long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public List<Integer> asFraction(long a, long b) {
        long gcd = gcd(a, b);
        return Arrays.asList((int)(a/gcd), (int) (b/gcd));
    }


    @Override
    public int getHeight() {
        return canvas.getHeight();
    }

    @Override
    public int getWidth() {
        return canvas.getWidth();
    }
}
