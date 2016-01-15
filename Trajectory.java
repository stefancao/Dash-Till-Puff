package team.dash_till_puff;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by junli on 4/29/2015.
 */
public class Trajectory implements TimeConscious {

    //declaring instances of trajectory
    private ArrayList<Point2D> lines = new ArrayList<>();
    public final int parts = 4;
    private int x_width;
    private int height;
    private int spaceship_size;
    private int x = 0;
    private int y;
    private int move;
    private Random ran = new Random();
    public Trajectory(DashTillPuffSurfaceView view) {
        x_width = view.getWidth() / parts;
        height = view.getHeight();
        ;
        this.spaceship_size = view.spaceship_size;
        y = view.getHeight() / 2;
        move = view.move;
    }

    //creating the trajectory
    public void setList() {
        for (int i = 0; i < 6 * parts; i++) {
            lines.add(new Point2D(x, y));
            x += x_width;
            y = spaceship_size  + ran.nextInt(height - 2 * spaceship_size);
        }
    }

    public ArrayList<Point2D> getLines() {
        return lines;
    }

    public int getX_width() {
        return x_width;
    }

    public int getY(int x) {
        int index;
        check:
        for (index = 0; index < lines.size(); index++) {
            if (x >= lines.get(index).x && x <= lines.get(index + 1).x) {
                break check;
            }
        }
        return lines.get(index).y + (x - lines.get(index).x) * (lines.get(index + 1).y - lines.get(index).y) / x_width;
    }

    //drawing the trajectory
    public void drawTrajectory(Canvas c) {
        Path path = new Path();
        path.moveTo(lines.get(0).x, lines.get(0).y);
        for (int index = 1; index < lines.size(); index++) {
            path.lineTo(lines.get(index).x, lines.get(index).y);

        }

        //using type dashed line and color cyan for the trajectory
        Paint paint = new Paint();
        paint.setStrokeWidth(10);
        paint.setColor(Color.CYAN);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setPathEffect(new DashPathEffect(new float[]{10, 4}, 0));
        c.drawPath(path, paint);

    }
    @Override
    public void tick(Canvas c) {


        for (int index = 0; index < lines.size(); index++) {
            if (lines.get(1).x <= 0) {  //if points of trajectory is out of the screen remove them and add another set
                Random ran = new Random();
                x = lines.get(lines.size() - 1).x + x_width;
                y = spaceship_size / 2 + ran.nextInt(height - spaceship_size);
                lines.add(new Point2D(x, y));       //adding a new set of trajectory
                lines.remove(0);            //removing the set of trajectory that is out of the screen
            }
            lines.get(index).moveX(move);   //moving the trajectory
        }

        drawTrajectory(c);      //drawing the trajectory
    }
}

