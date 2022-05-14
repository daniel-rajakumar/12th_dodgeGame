package com.example.dodgegame.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Timer;
import java.util.TimerTask;

public class Bullet extends Entity {
    private static final String TAG = "testing";

    Bullet(Context context, Canvas canvas, int x, int y){
        super(context, canvas);
        setImageBullet();
        setMoveSpeed(1);
        direction = Direction.TOP;
        setLocation_byPixel(x, y);
    }

    @Override
    void move() {
        if (direction == Direction.TOP) moveTop();

        setCollision(new Rect(
                this.x,
                this.y,
                this.x + this.sprite_width,
                this.y + this.sprite_height));

    }

    private void moveTop() {
        this.y -= moveSpeed;
    }

    @Override
    void checkIsAlive() {
        if (this.y < 0){
            setToRemove(true);
        }
    }

    @Override
    public void fire() {

    }

    void removeBullet(Bullet bullet){
        bullet.setToRemove(true);
    }




}
