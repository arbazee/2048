package com.arbaz.ather_2048;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.arbaz.ather_2048.sprites.EndGame;
import com.arbaz.ather_2048.sprites.Grid;

public class GameManager extends SurfaceView implements SurfaceHolder.Callback, SwipeCallback, GameManagerCallback{

    private MainThread mainThread;
    private Grid grid;
    private int screenWidth, screenHeight, standardSize;
    private TileManager tileManager;
    private SwipeListener swipeListener;
    private boolean endGame = false;
    private EndGame endGameSprite;

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
    }

    public void initGame(){
        endGame = false;
        tileManager.initGame();
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
            swipeListener.onTouchEvent(event);
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
}
