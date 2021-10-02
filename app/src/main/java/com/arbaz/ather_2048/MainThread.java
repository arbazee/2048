package com.arbaz.ather_2048;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread{

    private GameManager gameManager;
    private SurfaceHolder surfaceHolder;
    private int targetFps = 60;
    private Canvas canvas;
    private boolean running;

    public MainThread(SurfaceHolder surfaceHolder,GameManager gameManager){

        super();
        this.gameManager = gameManager;
        this.surfaceHolder = surfaceHolder;

    }

    public void setRunning(boolean isRunning){
        running = isRunning;
    }

    public void setSurfaceHolder(SurfaceHolder holder) {
        this.surfaceHolder = holder;
    }

    /**
     * This method handles the calling of the game manager for a new thread
     */
    @Override
    public void run() {
        long startTime, timeMillis, waitTime;
        long totalTime = 0;
        long frameTime = 0;
        long targetTime = 1000 / targetFps;

        while (running){
            startTime = System.nanoTime();
            canvas = null;

            try{
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    gameManager.update();
                    gameManager.draw(canvas);

                }
            } catch (Exception e){
                e.printStackTrace();
            } finally {

                if (canvas!=null){
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime =  targetTime - timeMillis;

            try{
                if (waitTime >0){
                    sleep(waitTime);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
