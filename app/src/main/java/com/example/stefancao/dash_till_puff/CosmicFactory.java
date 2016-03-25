package com.example.stefancao.dash_till_puff;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by stefancao on 3/25/16.
 */
public class CosmicFactory implements TimeConscious {
    private DashTillPuffSurfaceView view;
    private Trajectory trajectory;
    public List<List<CosmicObject>> Clusters = new ArrayList<List<CosmicObject>>();
    private ArrayList<CosmicObject> Cluster;

    //declaring variables
    private int check_flag = 1;
    private Bitmap cosmic;
    private Bitmap draw_cosmic;
    private Rect dst;
    private Rect draw_dst;
    public int cell_width;
    private int x1, y1, x2, y2;
    private int move;
    public int up_down = -1;
    private int length;
    public int counter = 0;
    private int safe_space;
    private Random ran = new Random();

    public CosmicFactory(DashTillPuffSurfaceView view, Trajectory trajectory) {
        this.view = view;
        this.trajectory = trajectory;
        move = view.move;
        safe_space = 3 * view.spaceship_size / 2;   //a certain distance that is used for safe space
        cell_width = trajectory.getX_width() / 3;   //the width of the cell (object is a rectangle cell)
        x1 = view.getWidth() / 2;       //the start x coordinate of object
        x2 = x1 + cell_width;           //the end x coordingate of object
    }

    private void randomCosmic() {
        up_down = -up_down;
        cosmic = view.cosmicArray[ran.nextInt(view.cosmicArray.length)];    //get a random cosmic object
    }


    public void setClusterList() {
        while (counter <= 3) {      //set 3 clusters
            addCluster();
            counter++;
        }

    }

    public void addCluster() {
        randomCosmic();
        Cluster =  new ArrayList<>();

        for (int i = 0; i < 10; i++) {  //10 cosmic object in one cluster
            if (check_flag == 1) {
                if (up_down > 0 ) { //draw cluster above for alternation
                    length = trajectory.getY(x1 + cell_width / 2) - safe_space;
                    y2 = length;
                    y1 = y2 - cell_width;
                }
                else {  //draw cluster below for alternation
                    length = view.getHeight() - trajectory.getY(x1 + cell_width / 2) - safe_space;
                    y1 = trajectory.getY(x1 + cell_width / 2) + safe_space;
                    y2 = y1 + cell_width;
                }
            }


            if (length >= cell_width) {  //if can draw in that cell draw it
                check_flag = 0;
                Cluster.add(new CosmicObject(x1, y1, x2, y2, cosmic));
                length -= cell_width;
                if (up_down > 0){
                    y2 = y1;
                    y1 -= cell_width;
                }
                else {
                    y1 = y2;
                    y2 += cell_width;
                }
            }
            else {  //if cannot draw in that cell move to the right and try there
                i--;
                x1 += cell_width;
                x2 = x1 + cell_width;
                check_flag = 1;
            }
        }
        check_flag = 1;
        x1 += cell_width;
        Clusters.add(Cluster);
    }

    public void drawCluster(Canvas c) { //draw the cluster by going through the cluster list
        for (int i = 0; i < Clusters.size(); i++) {
            for (int j = 0; j < 10; j++) {
                Clusters.get(i).get(j).drawObject(c);
            }
        }
    }
    @Override
    public void tick(Canvas c) {
        // Create new ‘‘clusters’’ of cosmic objects. Alternately place // clusters of cosmic objects above and below the guiding
        // trajectory.
        // Randomly select the type of cluster objects from a list // (e.g., stars, clouds, planets, etc.). So all objects in // a cluster are of the same type.
        // Remove cosmic objects (stars, planets, etc.) that moved out of the scene.
       /* for (int index = 0; index < Cluster.size(); index++) {
            if (Cluster.get(index).x2 <= 0) {

                Cluster.remove(index);

            }
            Cluster.get(index).setMove(move);
        }*/

        //moving the clusters
        for (int i = 0; i < Clusters.size(); i++) {
            for (int j = 0; j < 10; j++) {
                Clusters.get(i).get(j).setMove(move);   //moving the clusters

                if (Clusters.get(0).get(9).x2 <= 0) {   //if cluster of 0's have gone out of display remove it and add another one
                    x1 = Clusters.get(Clusters.size() - 1).get(9).x2 + cell_width;
                    x2 = x1 + cell_width;
                    addCluster();       //adding another cluster
                    Clusters.remove(0); //removing the cluster that is out of the screen
                }

            }
        }
        drawCluster(c); //call draw cluster to draw the clusters
    }
}