package com.arbaz.ather_2048;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class GameManager extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread mainThread;

    public GameManager(Context context, AttributeSet attrs) {
        super(context, attrs);

        getHolder().addCallback(this);

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

        System.out.println("Test 2048");

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        System.out.println("Draw 2048");
    }
}
