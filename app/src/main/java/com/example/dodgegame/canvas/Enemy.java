package com.example.dodgegame.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class Enemy extends Entity {
    private static final String TAG = "testing";

    Enemy(Context context, Canvas canvas, int x, int y){
        super(context, canvas);
        setImage(Direction.BOTTOM, 3f);
        setMoveSpeed(5);
        setLocation_byPixel(x, y);
        direction = Direction.BOTTOM;
    }

    @Override
    void move() {
        if (direction == Direction.BOTTOM) moveBottom();

//        setCollision(new Rect(
//                this.x + (this.sprite_width/2),
//                this.y + this.sprite_height,
//                (this.x + (this.sprite_width/2)) + this.sprite_width/2,
//                this.y + this.sprite_height * 2));
        int width = (this.entity_width/4);
        int height = (this.entity_height/4);

        setCollision(new Rect(
                this.x + (width),
                this.y + (height),
                this.x + (width * 3),
                this.y + (height * 8)/3
        ));
    }

    @Override
    void checkIsAlive() {
        if (this.y > canvas.getHeight()){
            setToRemove(true);
        }

        for (int i = 0; i < DrawCanvas.enemies.size(); i++){
            for (int j = 0; j < DrawCanvas.bullets.size(); j++) {
                Rect temp2 = DrawCanvas.enemies.get(i).collision;
                Rect temp1 = DrawCanvas.bullets.get(j).collision;
                if (temp1 != null && temp2 != null)
                    if (Rect.intersects(temp1, temp2)) {
                        removeEnemy(DrawCanvas.enemies.get(i), DrawCanvas.bullets.get(j));
                    }
            }
        }
    }

    @Override
    public void fire() {

    }

    void removeEnemy(Enemy enemy, Bullet bullet){
        if (enemy.isHasTouched()) return;
        bullet.setToRemove(true);
        enemy.setHasTouched(true);
        enemy.direction = Direction.NONE;
        enemy.sprite_x = 0;
        enemy.sprite_x_max = 5;
        enemy.sprite_y = 20;

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (++enemy.sprite_x <= enemy.sprite_x_max) return;
                enemy.setToRemove(true);
                cancel();
            }
        }, 500, 500);
    }




}
