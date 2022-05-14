package com.example.dodgegame.canvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.dodgegame.R;

import java.util.HashMap;
import java.util.Map;

enum Direction {
    RIGHT, LEFT, TOP, BOTTOM, NONE
}

abstract public class Entity {
    private static final String TAG = "test";
    Canvas canvas;
    Context context;
    Direction direction;
    int moveSpeed;
    int drawable;
    int x, y;
    int sprite_width, sprite_height;
    int entity_width, entity_height;
    int sprite_x_max, sprite_x, sprite_y;
    int count;
    Rect collision;
    boolean toRemove, hasTouched, performingAction;
    static Map<String, Bitmap> images = new HashMap<>();
    Bitmap image;
    float scaler;


    public Entity(Context context, Canvas canvas) {
        this.canvas = canvas;
        this.context = context;
        toRemove = false;
        performingAction = false;
    }

    public void setImageBullet(float scaler){
        this.scaler = scaler;
        this.image = images.get("bullet");
        this.sprite_width  = image.getWidth()/10;
        this.sprite_height = image.getHeight()/10;
        this.entity_width = (int) (sprite_width * scaler);
        this.entity_height = (int) (sprite_height * scaler);
    }

    public void setImage(Direction direction, float scaler) {
        this.scaler = scaler;
        if (this instanceof Player) this.image = images.get("player");
        if (this instanceof Enemy ) this.image = images.get("enemy");

        switch (direction){
            case LEFT:   this.sprite_x_max = 8; sprite_y = 9;  break;
            case RIGHT:  this.sprite_x_max = 8; sprite_y = 11;  break;
            case BOTTOM: this.sprite_x_max = 8; sprite_y = 10;  break;
            case TOP:    this.sprite_x_max = 8; sprite_y = 8; break;
        }

        assert image != null;
        this.sprite_width  = (image.getWidth()  / 13);
        this.sprite_height = (image.getHeight() / 21);
        this.entity_width = (int) (sprite_width * scaler);
        this.entity_height = (int) (sprite_height * scaler);
    }

    static void loadAllImages(Context context){
        images.put("player", BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite_player));
        images.put("enemy",  BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite_enemy));
        images.put("bullet",  BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet));
        images.put("grass",  BitmapFactory.decodeResource(context.getResources(), R.drawable.grass));
    }


    void setLocation_byPercentage(float x, float y){
        this.x = x_per(x);
        this.y = y_per(y);
    }

    void setLocation_byPixel(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setDrawable(int drawable) {
        this.image = BitmapFactory.decodeResource(context.getResources(), drawable);
    }

    void draw(){
        canvas.save();
        canvas.translate(x, y);
        canvas.scale(scaler, scaler);

        if (this instanceof Bullet) {
            canvas.drawBitmap(image, new Matrix(), null);
        } else {
//            canvas.drawBitmap(
//                    Bitmap.createBitmap(this.image
//                            , (sprite_width/4) + (sprite_width *  (1 + sprite_x))
//                            , (sprite_height/4) + (sprite_height * sprite_y)
//                            , sprite_width -  (sprite_width/4)
//                            , sprite_height - (sprite_height/4))
//                    , new Matrix(), null);
//

            canvas.drawBitmap(
                    Bitmap.createBitmap(this.image
                            , sprite_width * (1 + sprite_x)
                            , sprite_height * sprite_y
                            , sprite_width
                            , sprite_height
                    ), new Matrix(), null);
        }
        canvas.restore();
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
                    if (count % 10 == 0)
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
        if (x - moveSpeed < 0) {
            return;
        }
        x -= moveSpeed;
    }

    void moveRight(){
        if ((x + (sprite_width - sprite_width/4) * 2) + moveSpeed > canvas.getWidth()) {
            return;
        }
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

    public void setScaler(float scaler) {
        this.scaler = scaler;
    }


    private boolean growing = true, growing_first = true;
    public void increaseScale(float increase, float max){
        if (scaler     > max)   { growing = false; growing_first = false; }
        if (scaler*1.1 < max)     growing = true;


        if (!growing_first) increase/=2;

        if (growing) scaler+=increase;
        else         scaler-=increase;
    }
}
