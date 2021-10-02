package com.arbaz.ather_2048;

import android.graphics.Bitmap;

import com.arbaz.ather_2048.sprites.Tile;

public interface TileManagerCallback {

    Bitmap getBitmap(int count);
    void finishedMoving(Tile t);
}
