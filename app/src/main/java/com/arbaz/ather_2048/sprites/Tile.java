package com.arbaz.ather_2048.sprites;

import android.graphics.Canvas;

import com.arbaz.ather_2048.TileManagerCallback;

public class Tile implements Sprite {

    private int screenWidth,screenHeight;
    private int standardSize;
    private TileManagerCallback callback;
    private int count = 3;
    private int currentX, currentY;
    private int destX,destY;
    private boolean moving = false;
    private int speed = 10;

    public Tile(int standardSize, int screenWidth, int screenHeight, TileManagerCallback callback,
                int matrixX,int matrixY){

        this.standardSize = standardSize;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.callback = callback;
        this.currentX = destX = screenWidth / 2 - 2 * standardSize + matrixY * standardSize;
        this.currentY = destY = screenHeight / 2 - 2 * standardSize + matrixX * standardSize;

    }

    public void move(int matrixX, int matrixY){
        moving = true;
        destX = screenWidth / 2 - 2 * standardSize + matrixY * standardSize;
        destY = screenHeight / 2 - 2 * standardSize + matrixX * standardSize;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(callback.getBitmap(count), currentX, currentY, null);
        if (moving && currentX == destX && currentY == destY){
            moving = false;
        }
    }

    @Override
    public void update() {
        if (currentX < destX){

            if (currentX + speed > destX){
                currentX = destX;
            } else {
                currentX +=speed;
            }
        } else if (currentX > destX){
            if (currentX - speed < destX){
                currentX = destX;
            } else {
                currentX -= speed;
            }
        }

        if (currentY < destY){
            if (currentY + speed > destY){
                currentY = destY;
            } else {
                currentY += speed;
            }
        }else if (currentY > destY){
            if (currentY - speed < destY){
                currentY = destY;
            } else{
                currentY -= speed;
            }
        }
    }
}
