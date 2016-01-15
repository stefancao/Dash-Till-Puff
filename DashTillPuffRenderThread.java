package team.dash_till_puff;
/**
 * Created by stefancao on 4/22/15.
 */
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class DashTillPuffRenderThread extends Thread {
    private final DashTillPuffSurfaceView view;
    private static final int FRAME_Period = 5; //In ms

    public DashTillPuffRenderThread(DashTillPuffSurfaceView View){
        this.view = View;
    }

    public void run(){
        SurfaceHolder sh = view.getHolder();

        //Main game loop
        while(!Thread.interrupted()){
            Canvas c = sh.lockCanvas(null);
            try{
                synchronized (sh){
                    view.tick(c);
                }
            } catch(Exception e){

            } finally{
                if (c != null){
                    sh.unlockCanvasAndPost(c);
                }
            }
            try{
                Thread.sleep(FRAME_Period);
            } catch(InterruptedException e){
                return;
            }
        }

    }
}