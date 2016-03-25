package com.example.stefancao.dash_till_puff;

/**
 * Created by stefancao on 3/25/16.
 */
public class Point2D {
    public int x, y;
    public Point2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void moveX(int x) {
        this.x -= x;
    }   //move the trajectory by subtracting x coordinate
}