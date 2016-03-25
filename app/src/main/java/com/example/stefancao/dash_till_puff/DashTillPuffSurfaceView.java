package com.example.stefancao.dash_till_puff;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by stefancao on 3/25/16.
 */
public class DashTillPuffSurfaceView extends SurfaceView
        implements SurfaceHolder.Callback , TimeConscious {

    //declaring variables

    private DashTillPuffRenderThread renderThread;
    private Trajectory trajectory;
    private CosmicFactory cosmicfactory;
    public final int spaceship_size = 150;
    public final int move = 15;
    private final int YDown = 20;
    private int YVel;
    private int pointX = 275;
    private int pointY;
    private int safe_distance;
    private int counter = 0;
    private int score = 0;
    private int lose = 0;
    //coordinates for two slicing wall paper.
    private int slicing1_x1 = 0;
    private int slicing1_x2;
    private int slicing1_y1 = 0;
    private int slicing1_y2;

    private int slicing2_x1;
    private int slicing2_x2;
    private int slicing2_y1 = 0;
    private int slicing2_y2;

    private int spaceship_y1;
    private int spaceship_y2;

    private Bitmap background;
    private Bitmap spaceship;
    private Bitmap blackhole;
    private Bitmap blueplanet;
    private Bitmap cloud;
    private Bitmap earth;
    private Bitmap glossyplanet;
    private Bitmap goldenstar;
    private Bitmap neutronstar;
    private Bitmap shinystar;
    private Bitmap star1;
    private Bitmap star2;
    public Bitmap[] cosmicArray;

    public DashTillPuffSurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }
    public void surfaceCreated(SurfaceHolder holder) {
        renderThread = new DashTillPuffRenderThread(this);
        slicing1_x2 = getWidth();
        slicing1_y2 = getHeight();
        slicing2_x1 = slicing1_x2;
        slicing2_x2 = 2 * slicing2_x1;
        slicing2_y2 = slicing1_y2;

        spaceship_y1 = getHeight() / 2 - spaceship_size / 2;
        spaceship_y2 = spaceship_y1 + spaceship_size;
        // Create the sliding background , cosmic factory , and the space ship
        BitmapFactory.Options options = new BitmapFactory.Options();
        background = BitmapFactory.decodeResource(this.getResources(), R.drawable.dashtillpuffwallpaper, options);
        spaceship = BitmapFactory.decodeResource(this.getResources(), R.drawable.dashtillpuffspaceship, options);
        blackhole = BitmapFactory.decodeResource(this.getResources(), R.drawable.dashtillpuffblackhole, options);
        blueplanet = BitmapFactory.decodeResource(this.getResources(), R.drawable.dashtillpuffblueplanet, options);
        cloud = BitmapFactory.decodeResource(this.getResources(), R.drawable.dashtillpuffcloud, options);
        earth = BitmapFactory.decodeResource(this.getResources(), R.drawable.dashtillpuffearth, options);
        glossyplanet = BitmapFactory.decodeResource(this.getResources(), R.drawable.dashtillpuffglossyplanet, options);
        goldenstar = BitmapFactory.decodeResource(this.getResources(), R.drawable.dashtillpuffgoldenstar, options);
        neutronstar = BitmapFactory.decodeResource(this.getResources(), R.drawable.dashtillpuffneutronstar, options);
        shinystar = BitmapFactory.decodeResource(this.getResources(), R.drawable.dashtillpuffshinystar, options);
        star1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.dashtillpuffstar1, options);
        star2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.dashtillpuffstar2, options);

        //putting the cosmic project in an array to easily randomly choose them
        cosmicArray = new Bitmap[10];
        cosmicArray[0] = blackhole;
        cosmicArray[1] = blueplanet;
        cosmicArray[2] = cloud;
        cosmicArray[3] = earth;
        cosmicArray[4] = glossyplanet;
        cosmicArray[5] = goldenstar;
        cosmicArray[6] = neutronstar;
        cosmicArray[7] = shinystar;
        cosmicArray[8] = star1;
        cosmicArray[9] = star2;

        trajectory = new Trajectory(this);
        trajectory.setList();

        cosmicfactory = new CosmicFactory(this, trajectory);
        cosmicfactory.setClusterList();

        safe_distance = spaceship_size / 2 + cosmicfactory.cell_width / 2 - 5;
        renderThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Respond to surface changes, e.g., aspect ratio changes.
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // The cleanest way to stop a thread is by interrupting it.
        // BubbleShooterThread regularly checks its interrupt flag.
        renderThread.interrupt();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch ( e.getAction() ) {
            case MotionEvent.ACTION_DOWN: // Thrust the space ship up.
                YVel = - 2 * YDown;
                break;
            case MotionEvent.ACTION_UP : // Let space ship fall freely.
                YVel = 0;
                break;
        }
        return true;
    }


    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        // Draw everything (restricted to the displayed rectangle).
    }

    private void drawWallPaper(Canvas c) {  //drawing the sliding wallpapers
        Paint paint = new Paint();
        Rect slicing1 = new Rect(slicing1_x1, slicing1_y1, slicing1_x2, slicing1_y2);
        Rect slicing2 = new Rect(slicing2_x1, slicing2_y1, slicing2_x2, slicing2_y2);
        c.drawBitmap(background, null, slicing1, paint);
        c.drawBitmap(background, null, slicing2, paint);
    }

    private void drawSpaceShip(Canvas c) {  //drawing the spaceship
        Paint paint = new Paint();
        Rect spaceship_position = new Rect(200, spaceship_y1, 350, spaceship_y2);
        c.drawBitmap(spaceship, null, spaceship_position, paint);
    }

    @Override
    public void tick(Canvas c) {

        //moving the sliding background
        slicing1_x1 -= move;
        slicing1_x2 -= move;
        slicing2_x1 -= move;
        slicing2_x2 -= move;

        //if sliding background is out of the screen, chane the x coordinates to move it back again
        if (slicing1_x2 <= 0) {
            slicing1_x1 = slicing2_x2;
            slicing1_x2 = slicing1_x1 + getWidth();
        }
        if (slicing2_x2 <= 0) {
            slicing2_x1 = slicing1_x2;
            slicing2_x2 = slicing2_x1 + getWidth();
        }
        drawWallPaper(c);   //calling to draw the sliding background

        spaceship_y1 += YDown + YVel;
        spaceship_y2 += YDown + YVel;

        if (spaceship_y1 <= 0) {
            spaceship_y1 = 0;
            spaceship_y2 = spaceship_size;
        }

        if (spaceship_y2 >= getHeight()) {
            spaceship_y2 = getHeight();
            spaceship_y1 = spaceship_y2 - spaceship_size;
        }
        pointY = spaceship_y1 + spaceship_size / 2;
        trajectory.tick(c);     //calling trajectory tick for the trajectory
        cosmicfactory.tick(c);  //calling cosmicfactory tick for the cosmic factory objects
        drawSpaceShip(c);       //drawing the spaceship

        //counter for the score
        counter++;
        if (counter % 10 == 0) {    //to slow down the counter
            score++;    //the real score
        }

        //printing the score on the top left of the screen
        Paint paint2 = new Paint();
        paint2.setColor(Color.WHITE);
        paint2.setTextSize(100);
        c.drawText(Integer.toString(score), 50, 100, paint2);


        //cheking if spaceship is hitting any objects
        int distance;
        check:
        for (int i = 0; i < 10; i++) {
            CosmicObject obj1 = cosmicfactory.Clusters.get(0).get(i);
            CosmicObject obj2 = cosmicfactory.Clusters.get(1).get(i);
            if (obj1.centerX <= pointX + safe_distance && obj1.centerX >= pointX - safe_distance) {
                distance = (int) Math.sqrt(Math.pow(obj1.centerX - pointX, 2) + Math.pow(obj1.centerY - pointY, 2));
                if (distance < safe_distance) {
                    lose = 1;
                    break check;
                }
            }

            if (obj2.centerX <= pointX + safe_distance && obj2.centerX >= pointX - safe_distance) {
                distance = (int) Math.sqrt(Math.pow(obj2.centerX - pointX, 2) + Math.pow(obj2.centerY - pointY, 2));
                if (distance < safe_distance) {
                    lose = 1;
                    break check;
                }
            }
/*
            if (obj2.x1 <= 350 && obj2.x2 >= 200) {
                if ((spaceship_y1 >= obj2.y1 && spaceship_y1 <= obj2.y2) ||
                        (spaceship_y2 >= obj2.y1 && spaceship_y2 <= obj2.y2))
                {
                    lose = 1;
                    break check;
                }
            }*/

        }


        //if lost print out gameover and the score and stop the program
        if (lose == 1) {
            Paint paint4 = new Paint();
            paint4.setColor(Color.WHITE);
            paint4.setTextSize(250);
            c.drawText("Game Over", getWidth() / 2 - 600, getHeight() / 2 - 50, paint4);

            Paint paint5 = new Paint();
            paint5.setColor(Color.WHITE);
            paint5.setTextSize(150);
            c.drawText("Your Score  " + Integer.toString(score), getWidth() / 2 - 600, getHeight() / 2 + 100, paint5);
            renderThread.interrupt();

        }
    }
}
