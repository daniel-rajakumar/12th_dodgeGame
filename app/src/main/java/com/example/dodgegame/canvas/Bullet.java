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
        setImageBullet(0f);
        setMoveSpeed(5);
        direction = Direction.TOP;
        setLocation_byPixel(x, y);
    }

    @Override
    void move() {
        if (direction == Direction.TOP) moveTop();
        increaseScale(0.002f, 0.1f);
//        setCollision(new Rect(
//                this.x,
//                this.y,
//                this.x + (int)(this.sprite_width),
//               this.y + (int)(this.sprite_height)));

        setCollision(new Rect(
                this.x ,
                this.y,
                this.x  + this.sprite_width,
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

}
