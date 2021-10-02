package com.arbaz.ather_2048;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.arbaz.ather_2048.sprites.Sprite;
import com.arbaz.ather_2048.sprites.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class TileManager implements TileManagerCallback, Sprite {

    private int screenWidth,screenHeight;
    private int standardSize;
    private Resources resources;
    private HashMap<Integer, Bitmap> tileBitmap = new HashMap<>();
    private ArrayList<Integer> drawablesList = new ArrayList<>();
    private Tile[][] matrix = new Tile[4][4];
    private boolean moving = false;
    private ArrayList<Tile> movingTiles ;
    private boolean toSpawn = false;
    private boolean endGame = false;
    private GameManagerCallback callback;

    public TileManager(Resources resources,int standardSize, int screenWidth, int screenHeight, GameManagerCallback callback){
        this.standardSize = standardSize;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.resources = resources;
        this.callback = callback;
        initBitmaps();
        initGame();
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

        for (int i = 1; i <= 16; i++) {

            Bitmap bmp = BitmapFactory.decodeResource(resources,drawablesList.get(i-1));
            Bitmap tileBmp = Bitmap.createScaledBitmap(bmp,standardSize,standardSize,false);
            tileBitmap.put(i,tileBmp);

        }
    }

    public void initGame(){
        matrix = new Tile[4][4];
        movingTiles = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            int x  =  new Random().nextInt(4);
            int y  =  new Random().nextInt(4);

            if (matrix[x][y] == null){
                Tile tile = new Tile(standardSize,screenWidth,screenHeight,this,x,y);
                matrix[x][y] = tile;
            } else {
                i--;
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (matrix[i][j] != null)   {
                    matrix[i][j].draw(canvas);
                }
            }
        }

        if (endGame){
            callback.gameOver();
        }
    }

    @Override
    public void update() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (matrix[i][j] != null){
                    matrix[i][j].update();
                }
            }
        }
    }

    @Override
    public Bitmap getBitmap(int count) {
        return tileBitmap.get(count);
    }

    @Override
    public void finishedMoving(Tile t) {
        movingTiles.remove(t);
        if (movingTiles.isEmpty()){
            moving = false;
            spawn();
            checkEndGame();
        }
    }

    @Override
    public void updateScore(int delta) {
        callback.updateScore(delta);
    }

    private void spawn() {
        if (toSpawn){
            toSpawn = false;
            Tile t = null;
            while (t == null){
                int x = new Random().nextInt(4);
                int y = new Random().nextInt(4);
                if (matrix[x][y] == null){
                    t = new Tile(standardSize,screenWidth,screenHeight,this,x,y);
                    matrix[x][y] = t;
                }
            }
        }
    }

    public void onSwipe(SwipeCallback.Direction direction){
        if (!moving){
            moving = true;
            Tile[][] newMatrix = new Tile[4][4];

            switch (direction){

                case UP:
                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 4; j++) {
                            if (matrix[i][j] != null){
                                newMatrix[i][j] = matrix[i][j];
                                for (int k = i-1; k >= 0; k--) {
                                    if (newMatrix[k][j] == null){
                                        newMatrix[k][j] = matrix[i][j];
                                        if (newMatrix[k+1][j] == matrix[i][j]){
                                            newMatrix[k+1][j] = null;
                                        }
                                    } else if (newMatrix[k][j].getValue() == matrix[i][j].getValue()
                                    && !newMatrix[k][j].toIncrement()){
                                        newMatrix[k][j] = matrix[i][j].increment();
                                        if (newMatrix[k+1][j] == matrix[i][j]){
                                            newMatrix[k+1][j] = null;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 4; j++) {
                            Tile t = matrix[i][j];
                            Tile newT = null;
                            int matrixX = 0;
                            int matrixY = 0;

                            for (int a = 0; a < 4; a++) {
                                for (int b = 0; b < 4; b++) {
                                    if (newMatrix[a][b] == t){
                                        newT = newMatrix[a][b];
                                        matrixX = a;
                                        matrixY = b;
                                        break;
                                    }
                                }
                            }
                            if (newT != null){
                                movingTiles.add(t);
                                t.move(matrixX,matrixY);
                            }
                        }
                    }
                    break;
                case DOWN:
                    for (int i = 3; i >=0 ; i--) {
                        for (int j = 0; j < 4; j++) {
                            if (matrix[i][j] != null){
                                newMatrix[i][j] = matrix[i][j];

                                for (int k = i + 1; k < 4; k++) {
                                    if (newMatrix[k][j] == null){
                                        newMatrix[k][j] = matrix[i][j];
                                        if (newMatrix[k-1][j] == matrix[i][j]){
                                            newMatrix[k-1][j] = null;
                                        }
                                    } else if (newMatrix[k][j].getValue() == matrix[i][j].getValue() &&
                                    !newMatrix[k][j].toIncrement()){
                                         newMatrix[k][j] = matrix[i][j].increment();
                                         if (newMatrix[k-1][j] == matrix[i][j]){
                                             newMatrix[k-1][j] = null;
                                         }
                                    } else {
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    for (int i = 3; i >=0 ; i--) {
                        for (int j = 0; j < 4; j++) {
                            Tile t = matrix[i][j];
                            Tile newT = null;
                            int matrixX = 0;
                            int matrixY = 0;
                            for (int a = 0; a < 4; a++) {
                                for (int b = 0; b < 4; b++) {
                                    if (newMatrix[a][b] == t){
                                        newT = newMatrix[a][b];
                                        matrixX = a;
                                        matrixY = b;
                                        break;
                                    }
                                }
                            }
                            if (newT != null){
                                movingTiles.add(t);
                                t.move(matrixX,matrixY);
                            }

                        }
                    }

                    break;
                case LEFT:
                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 4; j++) {
                            if (matrix[i][j] != null){
                                newMatrix[i][j] = matrix[i][j];
                                for (int k = j-1; k >=0 ; k--) {
                                    if (newMatrix[i][k] == null){
                                        newMatrix[i][k] = matrix[i][j];
                                        if (newMatrix[i][k+1] == matrix[i][j]){
                                            newMatrix[i][k+1] = null;
                                        }
                                    } else if (newMatrix[i][k].getValue() == matrix[i][j].getValue() &&
                                    !newMatrix[i][k].toIncrement()){
                                        newMatrix[i][k] = matrix[i][j].increment();
                                        if (newMatrix[i][k+1] == matrix[i][j]){
                                            newMatrix[i][k+1] = null;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 4; j++) {
                            Tile t = matrix[i][j];
                            Tile newT = null;
                            int matrixX = 0;
                            int matrixY = 0;
                            for (int a = 0; a < 4; a++) {
                                for (int b = 0; b < 4; b++) {
                                    if (newMatrix[a][b] == t){
                                        newT = newMatrix[a][b];
                                        matrixX = a;
                                        matrixY = b;
                                        break;
                                    }
                                }
                            }
                            if (newT != null){
                                movingTiles.add(t);
                                t.move(matrixX,matrixY);
                            }
                        }
                    }

                    break;
                case RIGHT:
                    for (int i = 0; i < 4; i++) {
                        for (int j = 3; j >=0; j--) {
                            if (matrix[i][j] != null){
                                newMatrix[i][j] = matrix[i][j];
                                for (int k = j+1; k < 4; k++) {
                                    if (newMatrix[i][k] == null){
                                        newMatrix[i][k] = matrix[i][j];
                                        if (newMatrix[i][k-1] == matrix[i][j]){
                                            newMatrix[i][k-1] = null;
                                        }
                                    } else if (newMatrix[i][k].getValue() == matrix[i][j].getValue() &&
                                    !newMatrix[i][k].toIncrement()) {
                                        newMatrix[i][k] = matrix[i][j].increment();
                                        if (newMatrix[i][k-1] == matrix[i][j]){
                                            newMatrix[i][k-1] = null;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    for (int i = 0; i < 4; i++) {
                        for (int j = 3; j >= 0; j--) {
                            Tile t = matrix[i][j];
                            Tile newT = null;
                            int matrixX = 0;
                            int matrixY = 0;
                            for (int a = 0; a < 4; a++) {
                                for (int b = 0; b < 4; b++) {
                                    if (newMatrix[a][b] == t){
                                        newT = newMatrix[a][b];
                                        matrixX = a;
                                        matrixY = b;
                                        break;
                                    }
                                }
                            }
                            if (newT != null){
                                movingTiles.add(t);
                                t.move(matrixX,matrixY);
                            }

                        }
                    }

                    break;
            }

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (newMatrix[i][j] != matrix[i][j]){
                        toSpawn = true;
                        //spawn();
                        break;
                    }
                }
            }
            matrix = newMatrix;
        }


    }

    private void checkEndGame(){
        endGame = true;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++){
                if (matrix[i][j] == null){
                    endGame = false;
                    break;
                }
            }
        }
        if (endGame){
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++){
                    if (i > 0 && matrix[i-1][j].getValue() == matrix[i][j].getValue() ||
                            (i < 3 && matrix[i+1][j].getValue() == matrix[i][j].getValue()) ||
                            (j > 0 && matrix[i][j-1].getValue() == matrix[i][j].getValue()) ||
                            (j < 3 && matrix[i][j+1].getValue() == matrix[i][j].getValue())) {

                        endGame = false;
                    }
                }
            }
        }
    }
}
