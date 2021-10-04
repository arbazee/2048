package com.arbaz.ather_2048;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.arbaz.ather_2048.sprites.EndGame;
import com.arbaz.ather_2048.sprites.Grid;
import com.arbaz.ather_2048.sprites.Score;

public class GameManager extends SurfaceView implements SurfaceHolder.Callback, SwipeCallback, GameManagerCallback{

    private MainThread mainThread;
    private Grid grid;
    private int screenWidth, screenHeight, standardSize;
    private TileManager tileManager;
    private SwipeListener swipeListener;
    private boolean endGame = false;
    private EndGame endGameSprite;
    private Score score;
    private static final String APP_NAME  = "2048-game";
    private Bitmap restartButton;
    private int restartButtonX, restartButtonY, restartButtonSize;

    public GameManager(Context context, AttributeSet attrs) {
        super(context, attrs);

        setLongClickable(true);
        getHolder().addCallback(this);
        swipeListener = new SwipeListener(getContext(),this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        standardSize = (int) (screenWidth * .88) / 4;

        grid = new Grid(getResources(),screenWidth,screenHeight,standardSize);
        tileManager = new TileManager(getResources(),standardSize,screenWidth,screenHeight,this);
        endGameSprite = new EndGame(getResources(),screenWidth,screenHeight);

        score = new Score(getResources(),screenWidth,screenHeight,standardSize,
                getContext().getSharedPreferences(APP_NAME,Context.MODE_PRIVATE));

        restartButtonSize = (int) getResources().getDimension(R.dimen.restart_button_size);
        restartButtonX = screenWidth / 2 + 2 * standardSize - restartButtonSize;
        restartButtonY = screenHeight / 2 - 2 * standardSize - 3 * restartButtonSize / 2;

        Bitmap restartBmp = BitmapFactory.decodeResource(getResources(),R.drawable.restart);
        restartButton = Bitmap.createScaledBitmap(restartBmp,restartButtonSize,restartButtonSize,false);
    }

    public void initGame(){
        endGame = false;
        tileManager.initGame();

        score = new Score(getResources(),screenWidth,screenHeight,standardSize,
                getContext().getSharedPreferences(APP_NAME,Context.MODE_PRIVATE));


    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {

        mainThread = new MainThread(holder,this);
        mainThread.setRunning(true);
        mainThread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

        mainThread.setSurfaceHolder(holder);

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

        boolean retry = true;
        while (retry){
            try {
                mainThread.setRunning(false);
                mainThread.join();
                retry = false;
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }

    }

    public void update() {
        if (!endGame){
            tileManager.update();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawRGB(255,255,255);
        grid.draw(canvas);
        tileManager.draw(canvas);
        canvas.drawBitmap(restartButton,restartButtonX,restartButtonY,null);

        score.draw(canvas);
        if (endGame){
            endGameSprite.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (endGame){
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                initGame();
            }
        } else {
            float eventX = event.getAxisValue(MotionEvent.AXIS_X);
            float eventY = event.getAxisValue(MotionEvent.AXIS_Y);
            if (event.getAction() == MotionEvent.ACTION_DOWN &&
                eventX > restartButtonX && eventX < restartButtonX + restartButtonSize &&
                eventY > restartButtonY && eventY < restartButtonY + restartButtonSize){
                initGame();
            } else {
                swipeListener.onTouchEvent(event);
            }
        }


        return super.onTouchEvent(event);
    }

    @Override
    public void onSwipe(Direction direction) {
        tileManager.onSwipe(direction);
    }

    @Override
    public void gameOver() {
        endGame = true;
    }

    @Override
    public void updateScore(int delta) {
        score.updateScore(delta);
    }
}
