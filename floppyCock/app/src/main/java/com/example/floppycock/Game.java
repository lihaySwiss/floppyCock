package com.example.floppycock;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;

import java.util.ArrayList;

public class Game extends Activity {
    private ParallexView parallaxView;
    private Cock cock;

    private final double DISTANCE =  4;
    private final int SUM_PIPES = 4;
    private final int JUMP = 22;

    private ArrayList<Pipe> highPipes = new ArrayList<>();
    private ArrayList<Pipe> lowPipes = new ArrayList<>();
    DisplayMetrics displayMetrics = new DisplayMetrics();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        defineCock();
        definePipes();

        // Get a Display object to access screen details
        Display display = getWindowManager().getDefaultDisplay();

        // Load the resolution into a Point object
        Point resolution = new Point();
        display.getSize(resolution);

        // And finally set the view for our game
        parallaxView = new ParallexView(this, resolution.x, resolution.y, this.cock, this.highPipes, this.lowPipes);

        setContentView(parallaxView);

    }

    public void defineCock() {
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        ArrayList<Bitmap> cockAnimation = new ArrayList<>();
        cockAnimation.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.cock));
        cockAnimation.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.cock2));

        cock = new Cock(
                (float)200*width/1080,
                height/2f,
                230*width/1080,
                220*height /1920);

        cock.setAnimation(cockAnimation);

    }

    public void definePipes() {
        // tube down =  from up to down, tube up = from bottom up
        highPipes = generatePipes(BitmapFactory.decodeResource(getResources(), R.drawable.pipe_down));
        lowPipes = generatePipes(BitmapFactory.decodeResource(getResources(), R.drawable.pipe_up));
    }

    public ArrayList<Pipe> generatePipes(Bitmap texture)
    {
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        ArrayList<Pipe> pipes = new ArrayList<>();

        for (int i = 0; i < SUM_PIPES; i++) {
            Pipe pipe = new Pipe(width,
                    0,
                    (int) (width/5.5),
                    (int) (height/1.8));
            pipe.setBm(texture);
            pipe.randomizeY(height);
            if(i > 0) {
                //here we set the distance between each pipe from another
                // this is carefully calculated according to the screen, to change mess with distance value
                pipe.setX( pipes.get(i - 1).getX() + pipes.get(0).getWidth() * DISTANCE);
            }
            pipes.add(pipe);
        }
        return pipes;
    }

    protected void onPause() {
        super.onPause();
        parallaxView.pause();
    }

    // If the Activity is resumed make sure to resume our thread
    @Override
    protected void onResume() {
        super.onResume();
        parallaxView.resume();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            cock.setGravity(-JUMP);
        }
        return true;
    }
}