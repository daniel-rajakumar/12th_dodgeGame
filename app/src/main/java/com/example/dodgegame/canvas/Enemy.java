package com.example.dodgegame.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.VibrationEffect;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Timer;
import java.util.TimerTask;

public class Enemy extends Entity {

    Enemy(Context context, Canvas canvas, int x, int y){
        super(context, canvas);
        setImage(Direction.BOTTOM, 3f);
        setMoveSpeed(15);
        setLocation_byPixel(x, y);
        direction = Direction.BOTTOM;
    }

    @Override
    void move() {
        if (direction == Direction.BOTTOM) moveBottom();

        int width = (this.entity_width/4);
        int height = (this.entity_height/4);

        setCollision(new Rect(
                this.x + (width),
                this.y + (height),
                this.x + (width * 3),
                this.y + (height * 8)/ (inWater ? 5 : 3)
        ));
    }

    @Override
    void checkIsAlive() {
        if (this.y > canvas.getHeight()){
            setToRemove(true);
        }

        for (int i = 0; i < DrawCanvas.enemies.size(); i++){
            Rect temp1 = DrawCanvas.enemies.get(i).collision;

            // check enemy vs. bullet collision
            for (int j = 0; j < DrawCanvas.bullets.size(); j++) {
                Rect temp2 = DrawCanvas.bullets.get(j).collision;

                if (temp1 != null && temp2 != null)
                    if (Rect.intersects(temp1, temp2)) {
                        removeEnemy(DrawCanvas.enemies.get(i), DrawCanvas.bullets.get(j));
                    }
            }

            // check enemy vs. player collision
            Rect temp3 = DrawCanvas.player.collision;
            if (temp1 != null && temp3 != null)
                if (Rect.intersects(temp1, temp3)) {
                    removeEnemy(DrawCanvas.enemies.get(i), DrawCanvas.player);
                }
        }
    }

    @Override
    public void fire() { }

    void removeEnemy(Enemy enemy, Entity entity){
        if (enemy.isHasTouched()) return;

        if (!DrawCanvas.grassAnimation){
            if (entity instanceof Player){
                DrawCanvas.score--;
                DrawCanvas.hurt_music.start();
                ((Player) entity).damagePlayer();
            } else if (entity instanceof Bullet){
                DrawCanvas.score++;
                entity.setToRemove(true);
            }
        }

        enemy.setHasTouched(true);
        dieAnimation(enemy);
    }

    void dieAnimation(Enemy enemy){
        enemy.direction = Direction.NONE;
        enemy.sprite_x = 0;
        enemy.sprite_x_max = enemy.inWater ? 2 : 4;
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
