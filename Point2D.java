package team.dash_till_puff;

/**
 * Created by junli on 4/29/15.
 */
//2d point that draws the trajectory
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
