package com.example.stefancao.dash_till_puff;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by stefancao on 3/25/16.
 */
public class CosmicObject {

    //declaring instances of cosmic objects
    public int x1, x2;
    public int y1, y2;
    private Rect dst;
    private Bitmap cosmic;
    private Paint paint;
    public int centerX;
    public int centerY;
    public CosmicObject(int x1, int y1, int x2, int y2, Bitmap cosmic){

        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.centerX = x1 + (x2 - x1) / 2;      //used for checking if collision of spaceship and objects
        this.centerY = y1 + (y2 - y1) / 2;      //used for checking if collision of spaceship and objects
        dst = new Rect(x1, y1, x2, y2);
        this.cosmic = cosmic;
    }

    public void setMove(int move) {         //move he objects by subtracting the x cooridinates
        this.x1 -= move;
        this.x2 -= move;
        this.centerX -= move;
        dst = new Rect(x1, y1, x2, y2);
    }

    public void drawObject(Canvas c) {      //drawing the cosmic objects
        paint = new Paint();
        c.drawBitmap(cosmic, null, dst, paint);
    }
}
