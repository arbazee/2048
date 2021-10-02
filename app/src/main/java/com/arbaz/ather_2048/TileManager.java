package com.arbaz.ather_2048;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.arbaz.ather_2048.sprites.Sprite;
import com.arbaz.ather_2048.sprites.Tile;

import java.util.ArrayList;
import java.util.HashMap;

public class TileManager implements TileManagerCallback, Sprite {

    private int screenWidth,screenHeight;
    private int standardSize;
    private Resources resources;
    private Tile tile;
    private HashMap<Integer, Bitmap> tileBitmap = new HashMap<>();
    private ArrayList<Integer> drawablesList = new ArrayList<>();
    private Tile[][] matrix = new Tile[4][4];

    public TileManager(Resources resources,int standardSize, int screenWidth, int screenHeight){
        this.standardSize = standardSize;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.resources = resources;
        initBitmaps();
        tile = new Tile(standardSize,screenWidth,screenHeight,this,1,1);
        matrix[1][1] = tile;
    }

    private void initBitmaps(){
        drawablesList.add(R.drawable.one);
        drawablesList.add(R.drawable.two);
        drawablesList.add(R.drawable.three);
        drawablesList.add(R.drawable.four);
        drawablesList.add(R.drawable.five);
        drawablesList.add(R.drawable.six);
        drawablesList.add(R.drawable.seven);
        drawablesList.add(R.drawable.eight);
        drawablesList.add(R.drawable.nine);
        drawablesList.add(R.drawable.ten);
        drawablesList.add(R.drawable.eleven);
        drawablesList.add(R.drawable.twelve);
        drawablesList.add(R.drawable.thirteen);
        drawablesList.add(R.drawable.fourteen);
        drawablesList.add(R.drawable.fifteen);
        drawablesList.add(R.drawable.sixteen);

        for (int i = 0; i < 16; i++) {
            Bitmap bmp = BitmapFactory.decodeResource(resources,drawablesList.get(i));
            Bitmap tileBmp = Bitmap.createScaledBitmap(bmp,standardSize,standardSize,false);
            tileBitmap.put(i,tileBmp);

        }
    }

    @Override
    public void draw(Canvas canvas) {
        tile.draw(canvas);
    }

    @Override
    public void update() {
        tile.update();
    }

    @Override
    public Bitmap getBitmap(int count) {
        return tileBitmap.get(count);
    }

    public void onSwipe(SwipeCallback.Direction direction){
        switch (direction){

            case UP:
                tile.move(0,1);
                break;
            case DOWN:
                tile.move(3,1);
                break;
            case LEFT:
                tile.move(1,0);
                break;
            case RIGHT:
                tile.move(1,3);
                break;
        }
    }
}
