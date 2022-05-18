package com.example.dodgegame.canvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.example.dodgegame.R;

import java.util.HashMap;
import java.util.Map;

enum Direction {
    RIGHT, LEFT, TOP, BOTTOM, NONE
}

abstract public class Entity {
    Canvas canvas;
    Context context;
    Direction direction;
    int moveSpeed, moveSpeed_water, moveSpeed_land;
    boolean isWalkingFaster, inWater;
    int x, y;
    int sprite_width, sprite_height;
    int entity_width, entity_height;
    int sprite_x_max, sprite_x, sprite_y;
    int count;
    Rect collision;
    boolean toRemove, isDamaged, hasTouched, performingAction;
    static Map<String, Bitmap> images = new HashMap<>();
    Bitmap image;
    float scalar;

    public Entity(Context context, Canvas canvas) {
        this.canvas = canvas;
        this.context = context;
        toRemove = false;
        performingAction = false;
    }

    public void setImageBullet(float scalar){
        this.scalar = scalar;
        this.image = images.get("bullet");
        this.sprite_width  = image.getWidth()/10;
        this.sprite_height = image.getHeight()/10;
        this.entity_width = (int) (sprite_width * scalar);
        this.entity_height = (int) (sprite_height * scalar);
    }

    public void setImage(Direction direction, float scaler) {
        this.scalar = scaler;
        if (this instanceof Player) {
            if (isDamaged) {
                if (DrawCanvas.player.blink) {
                    this.image = images.get("player");
                } else {
                    this.image = images.get("enemy");
                }
            } else {
                this.image = images.get("player");
            }
        }
        if (this instanceof Enemy ) this.image = images.get("enemy");

        switch (direction){
            case LEFT:   this.sprite_x_max = 8; sprite_y = 9;  break;
            case RIGHT:  this.sprite_x_max = 8; sprite_y = 11;  break;
            case BOTTOM: this.sprite_x_max = 8; sprite_y = 10;  break;
            case TOP:    this.sprite_x_max = 1; sprite_y = 8; break;
        }

        this.sprite_width  = (image.getWidth()  / 13);
        this.sprite_height = (image.getHeight() / 21);
        this.entity_width = (int) (sprite_width * scaler);
        this.entity_height = (int) (sprite_height * scaler);

    }

    static void loadAllImages(Context context){
        images.put("player", BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite_player));
        images.put("enemy",  BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite_enemy));
        images.put("bullet",  BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet));
        images.put("cloud",  BitmapFactory.decodeResource(context.getResources(), R.drawable.cloud));
        images.put("water_bg",  BitmapFactory.decodeResource(context.getResources(), R.drawable.water_bg));
        images.put("land_bg",  BitmapFactory.decodeResource(context.getResources(), R.drawable.land_bg));
    }

    void setLocation_byPixel(int x, int y){
        this.x = x;
        this.y = y;
    }

    void draw(){
        inWater = y < y_per(DrawCanvas.waterSize - 0.1);

        canvas.save();
        canvas.translate(x, y);
        canvas.scale(scalar, scalar);

        if (this instanceof Bullet)  drawBullet();
        else                         drawEnemy_and_player();

        canvas.restore();
    }

    void drawBullet(){
        canvas.drawBitmap(image, new Matrix(), null);
        Log.d("TAG", "drawBullet: " + canvas.getWidth());
    }

    void drawEnemy_and_player(){
        int _x = sprite_width * (1 + sprite_x);
        int _y = sprite_height * sprite_y;
        int _w = sprite_width;
        int _h = sprite_height;
        if (inWater){
            _h/=2;
            moveSpeed = moveSpeed_water;
        } else {
            if (this instanceof Enemy) isWalkingFaster = true;
            moveSpeed = moveSpeed_land;
        }


        canvas.drawBitmap(Bitmap.createBitmap(this.image, _x, _y, _w, _h), new Matrix(), null);
    }

    abstract void move();
    abstract void checkIsAlive();

    void update(){
        if (!isPerformingAction()) {
            count++;
            move();
        }
        draw();
        checkIsAlive();

//        showCollision();

        if (!(this instanceof Bullet))
            if (!isPerformingAction()){
                if (!isHasTouched()){
                    if (count % (isWalkingFaster ? moveSpeed_water/2 : moveSpeed_land*2) == 0)
                        sprite_x++;
                    sprite_x %= sprite_x_max;
                }
            }
    }


    void showCollision(){
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawRect(collision, paint);
    }

    void moveLeft(){
        int width = (this.entity_width/4);
        if (x + width < x_per(0)) return;
        x -= moveSpeed;
    }

    void moveRight(){
        int width = (this.entity_width/4);
        if (x + (width * 3) > x_per(1)) return;
        x += moveSpeed;
    }


    void moveBottom(){
        y += moveSpeed;
    }

    int x_per(double percent){
        return (int) (canvas.getWidth() * percent);
    }

    int y_per(double percent){
        return (int) (canvas.getHeight() * percent);
    }


    public void setMoveSpeed(int moveSpeed) {
        this.moveSpeed = moveSpeed;
        this.moveSpeed_land = moveSpeed;
        this.moveSpeed_water = moveSpeed/3;
    }


    public void setCollision(Rect collision) {
        if (collision != null)
            this.collision = collision;
    }

    public boolean isToRemove() {
        return toRemove;
    }

    public void setToRemove(boolean toRemove) {
        this.toRemove = toRemove;
    }

    public void setHasTouched(boolean hasTouched) {
        this.hasTouched = hasTouched;
    }

    public boolean isHasTouched() {
        return hasTouched;
    }

    public abstract void fire();

    public boolean isPerformingAction() {
        return performingAction;
    }

    public void setPerformingAction(boolean performingAction) {
        this.performingAction = performingAction;
    }

    private boolean growing = true, growing_first = true;
    public void increaseScale(float increase, float max){
        if (scalar > max)   { growing = false; growing_first = false; }
        if (scalar *1.1 < max)     growing = true;


        if (!growing_first) increase/=2;

        if (growing) scalar +=increase;
        else         scalar -=increase;
    }

    public void setDamaged(boolean damaged) {
        isDamaged = damaged;
    }

    public boolean isDamaged() {
        return isDamaged;
    }
}
