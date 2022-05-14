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

//        setCollision(new Rect(
//                this.x + (this.sprite_width/2),
//                this.y + this.sprite_height,
//                (this.x + (this.sprite_width/2)) + this.sprite_width/2,
//                this.y + this.sprite_height * 2));

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
////        Log.d(TAG, "checkIsAlive: " + "(" + x +  " > " + canvas.getHeight() + ")");
        if (this.y < 0){
            setToRemove(true);
        }
//
//        for (int i = 0; i < DrawCanvas.enemies.size(); i++){
//            Rect temp2 = DrawCanvas.enemies.get(i).collision;
//            Rect temp1 = DrawCanvas.player.collision;
//            if (temp1 != null && temp2 != null)
//                if (Rect.intersects(temp1, temp2)) {
//                    DrawCanvas.bullets.remove();
//                    removeBullet(DrawCanvas.bullets.get(i));
////                    DrawCanvas.enemies.get(i).setToRemove(true);
////                    Log.d(TAG, "checkIsAlive: remove!");
//                }
//        }
    }

    @Override
    public void fire() {

    }

    void removeBullet(Bullet bullet){
        bullet.setToRemove(true);
//        if (enemy.isHasTouched()) return;
//
//        enemy.setHasTouched(true);
//        enemy.direction = Direction.NONE;
//        enemy.sprite_x = 0;
//        enemy.sprite_x_max = 5;
//        enemy.sprite_y = 20;
//
//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if (++enemy.sprite_x <= enemy.sprite_x_max) return;
//                enemy.setToRemove(true);
//                cancel();
//            }
//        }, 500, 500);
    }




}
