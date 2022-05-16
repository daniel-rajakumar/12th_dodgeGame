package com.example.dodgegame.canvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Vibrator;

import androidx.core.content.res.ResourcesCompat;

import com.example.dodgegame.R;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

enum GameScene {
    START, GAME, END
}

public class DrawCanvas {
    public static Player player;
    static LinkedList<Enemy> enemies = new LinkedList<>();
    static LinkedList<Bullet> bullets = new LinkedList<>();
    Context context;
    Canvas canvas;
    Paint paint;
    String text;
    static int score, highScore = Integer.MIN_VALUE;
    static GameScene gameScene;
    static Vibrator vibrator;
    static boolean grassAnimation = false;
    static boolean startScreen_start = true,
                   gameScreen_start = true,
                   endScreen_start = true;
    static MediaPlayer gameplay_music,
                       endScreen_music,
                       fire_music,
                       hurt_music;
    static Timer timer_enemy = new Timer(),
                 timer_time = new Timer();

    static float cloud_hideLength = 4f;
    static float cloud_scalar = 0.3f;
    static float waterSize = 0.6f;
    int fontSize, original_fontSize = 100;
    static int time, total_time = 45;

    public void update(Context context,Canvas canvas){
        this.context = context;
        this.canvas = canvas;

        if (startScreen_start){
            startingGameSetUp();
        }
        drawBackground();
        switch (gameScene){
            case START: showStartScreen(); break;
            case GAME: showGamePlayScreen(); break;
            case END: showEndScreen(); break;
        }
    }

    void startingGameSetUp(){
        paint = new Paint();
        startScreen_start = false;
        gameScene = GameScene.START;
        Entity.loadAllImages(context);
        player = new Player(context, canvas);
        time = total_time;
        fontSize = original_fontSize;
        score = 0;
        paint.setColor(Color.RED);
        paint.setTextSize(fontSize);
        paint.setTypeface(ResourcesCompat.getFont(context, R.font.font_1));
        setUpMusic();
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    void drawBackground(){
        draw_background_water();
        draw_background_land();
    }

    void draw_background_water(){
        canvas.save();
        Bitmap _image = Entity.images.get("water_bg");
        int _x = 0;
        int _y = 0;
        int _w = player.x_per(1);
        int _h = player.y_per(waterSize + 0.1);
        canvas.drawBitmap(Bitmap.createBitmap(_image, _x, _y, _w, _h), new Matrix(), null);
        canvas.restore();
    }


    void draw_background_land(){
        canvas.save();
        canvas.translate(player.x_per(0), player.y_per(waterSize));
        canvas.drawBitmap(Entity.images.get("land_bg"),
                new Matrix(),
                null);
        canvas.restore();
    }
    void setUpMusic(){
        gameplay_music = MediaPlayer.create(context,R.raw.gameplay_music);
        fire_music = MediaPlayer.create(context,R.raw.fire);
        endScreen_music = MediaPlayer.create(context,R.raw.endscreen_music);
        hurt_music = MediaPlayer.create(context,R.raw.no);
    }

    void updateMusic(){
        if (time > 10 && (total_time - time) == total_time*0.5){
            gameplay_music.setPlaybackParams(gameplay_music.getPlaybackParams().setSpeed(1f));
        }

        if (time == 10){
            gameplay_music.setPlaybackParams(gameplay_music.getPlaybackParams().setSpeed(1.25f));
        }
    }


    void showStartScreen(){
        updateGrass();
        toggle_tapAnywhereText++;

        if (!grassAnimation){
            updateScreenText();
        }
    }

    void showGamePlayScreen(){
        if (gameScreen_start){ gameScreen_start = false;
            gameplay_music.start();
            gameplay_music.setLooping(true);
            gameplay_music.setPlaybackParams(gameplay_music.getPlaybackParams().setSpeed(0.75f));
            background_tasks();
        }

        updateEnemies();
        updateBullets();
        player.update();
        updateGrass();
        updateScore();
        updateTime();
        updateMusic();
    }

    int toggle_tapAnywhereText = 0;
    void showEndScreen(){
        if (grassAnimation){
            updateEnemies();
            updateBullets();
            player.update();
        }
        updateGrass();

        if (!grassAnimation){
            updateScreenText();
            if (endScreen_start){
                if (gameScene == GameScene.END){
                    endScreen_music.start();
                    endScreen_start = false;
                }
            }
        }
    }


    public void updateScreenText(){
        showPlayerSymbol();
        showText_score();
        showText_highScore();
        if (++toggle_tapAnywhereText % 20 < 10) showText_TapAnywhere();
    }

    void showText_score(){
        paint.setTextSize(fontSize = original_fontSize);
        text = "Score: " + (gameScene == GameScene.END ? score :  "N/A");
        canvas.drawText(text
                , player.x_per(0.975) - (fontSize * text.length())
                , player.y_per(0.1)
                , paint);
    }

    void showText_highScore(){
        paint.setTextSize(fontSize/=2);
        text = "High Score: " + (gameScene == GameScene.END ? highScore : "N/A");
        canvas.drawText(text
                , player.x_per(0.975) - (fontSize * text.length())
                , player.y_per(0.1) + (fontSize * 2)
                , paint);
        paint.setTextSize(fontSize*=2);
    }

    void showText_TapAnywhere(){
        paint.setTextSize(fontSize/=1.5);
        text = "Tap anywhere to";
        canvas.drawText(text
                , player.x_per(0.975) - (fontSize * text.length())
                , player.y_per(0.9)
                , paint);

        text = " play " + (gameScene == GameScene.END ? "again!" : "!");
        canvas.drawText(text
                , player.x_per(0.975) - (fontSize * text.length())
                , player.y_per(0.9) + (fontSize * 2)
                , paint);
        paint.setTextSize(fontSize*=1.5);
    }

    void showPlayerSymbol(){
        int scalar = 6;
        canvas.save();
        canvas.translate(
                 canvas.getWidth() /2f - (DrawCanvas.player.sprite_width  * scalar)/2f
                ,canvas.getHeight()/2f - (DrawCanvas.player.sprite_height * scalar)/2f
        );
        canvas.scale(scalar, scalar);
        canvas.drawBitmap(
                Bitmap.createBitmap(Entity.images.get("player")
                        , 0
                        , DrawCanvas.player.sprite_height * 2
                        , DrawCanvas.player.sprite_width
                        , DrawCanvas.player.sprite_height
                ), new Matrix(), null);
        canvas.restore();
    }

    public void updateBullets(){
        for (int i = 0; i < bullets.size(); i++)
            if (bullets.get(i).isToRemove())
                bullets.remove(i);

        for (int i = 0; i < bullets.size(); i++)
            bullets.get(i).update();
    }

    public void updateEnemies(){
        for (int i = 0; i < enemies.size(); i++)
            if (enemies.get(i).isToRemove())
                enemies.remove(i);

        for (int i = 0; i < enemies.size(); i++)
            enemies.get(i).update();
    }

    void updateGrass(){

        Bitmap _image = Entity.images.get("cloud");
        grassAnimate();
        canvas.save();
        canvas.scale(cloud_scalar, cloud_scalar);
        canvas.drawBitmap(
                _image
                , 0
                , -_image.getHeight() + player.y_per(cloud_hideLength)
                , null);
        canvas.restore();



    }

    void grassAnimate(){
        switch (gameScene){
            case START: case END:
                grassShow(); break;
            case GAME: grassHide(); break;
        }
    }

    void grassHide(){
        if (cloud_hideLength >= 0.75){
            cloud_hideLength -= 0.025;
            grassAnimation = true;
            return;
        }
        grassAnimation = false;
    }

    void grassShow(){
        if (cloud_hideLength <= 4){
            cloud_hideLength += 0.025;
            grassAnimation = true;
            return;
        }
        grassAnimation = false;
    }

    void updateScore(){
        text = String.valueOf(score);
        paint.setTextSize(original_fontSize);
        canvas.drawText(text
                , player.x_per(0.95) - (text.length() * fontSize)
                , player.y_per(0.075)
                , paint);
    }

    void updateTime(){
        text = String.valueOf(time);

        paint.setTextSize(original_fontSize + (bounceSize += bounceSpeed));
        canvas.drawText(text
                , player.x_per(0.05)
                , player.y_per(0.075) + bounceSize/2
                , paint);
        paint.setTextSize(paint.getTextSize() - bounceSize);

        if (time < 10) bounceSpeed *= 1.02;
        else           bounceSpeed  = 0;
    }

    public void background_tasks(){
        generateEnemies();
        timerCountDown();
    }

    void generateEnemies(){
        int width = player.entity_width/4;
        int min = -width;
        int max = canvas.getWidth() - (width * 3);
        int offset = player.entity_height;

        timer_enemy.cancel();
        timer_enemy = new Timer();
        timer_enemy.schedule(new TimerTask() {
            public void run() {
                if (player.isWalking) return;
                enemies.push(new Enemy(context, canvas, random(min, max),0));
            }
        }, 0, 2000);
    }

    static float bounceSpeed = 0;
    float bounceSize = 0;
    void timerCountDown(){
        timer_time.cancel();
        timer_time = new Timer();
        timer_time.schedule(new TimerTask() {
            public void run() {
                if (--time < 10){
                    bounceSpeed = 1;
                    bounceSize = 0;
                }

                if (time > -1) return;
                gameScene = GameScene.END;
                gameplay_music.pause();
                if (score > highScore)
                    highScore = score;
                gameplay_music.seekTo(0);
            }
        }, 0, 1000);
    }


    int random(int min, int max){
        return (int) (Math. random()*(max-min+1))+min;
    }

    public static void resetGame(Context context, Canvas canvas){
        enemies.clear();
        bullets.clear();
        player = new Player(context, canvas);
        score = 0;
        time = total_time;
        gameScene = GameScene.GAME;
        bounceSpeed = 0;
        cloud_hideLength = 4f;
        gameScreen_start = true;
        endScreen_start = true;

        // sounds
        endScreen_music.pause();
        endScreen_music.seekTo(0);
        gameplay_music.start();
        gameplay_music.setPlaybackParams(gameplay_music.getPlaybackParams().setSpeed(0.75f));
    }



}

