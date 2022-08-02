package com.example.floppycock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import androidx.compose.ui.geometry.RoundRect;

import java.util.ArrayList;

@SuppressLint("ViewConstructor")
public class ParallexView extends SurfaceView implements Runnable {

    private Cock cock;
    private death_screen death_screen;

    private int currScore;
    private int scorePipe;
    private boolean startGame;

    private ArrayList<Pipe> highPipes;
    private ArrayList<Pipe> lowPipes;

    ArrayList<Background> backgrounds;
    private volatile boolean running;
    private Thread gameThread = null;

    private Paint textPaint;
    private Paint numPaint;
    private Paint gameOverPaint;
    private Paint pipePaint;

    private final int GAP = 1570;
    private final int SCREEN_GAP = 0;

    // For drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;

    // Holds a reference to the Activity
    Context context;

    // Control the fps
    long fps = 60;

    // Screen resolution
    int screenWidth;
    int screenHeight;

    ParallexView(Context context, int screenWidth, int screenHeight, Cock cock, ArrayList<Pipe> highPipes, ArrayList<Pipe> lowPipes) {
        super(context);
        this.cock = cock;
        this.highPipes = highPipes;
        this.lowPipes = lowPipes;
        this.context = context;
        this.currScore = 0;
        this.scorePipe = 0;
        this.startGame = false;

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        textPaint = new Paint();
        textPaint.setColor(Color.YELLOW);
        textPaint.setTypeface(Typeface.createFromAsset(this.context.getAssets(), "font/flopfont.ttf"));
        textPaint.setTextSize(280);

        numPaint = new Paint();
        numPaint.setColor(Color.YELLOW);
        numPaint.setTypeface(Typeface.createFromAsset(this.context.getAssets(), "font/flopfont.ttc"));
        numPaint.setTextSize(110);

        gameOverPaint = new Paint();
        gameOverPaint.setColor(Color.RED);
        gameOverPaint.setTypeface(Typeface.createFromAsset(this.context.getAssets(), "font/flopfont.ttf"));
        gameOverPaint.setTextSize(300);

        pipePaint = new Paint();
        pipePaint.setColor(Color.RED);

        // Initialize our drawing objects
        ourHolder = getHolder();
        paint = new Paint();

        // Initialize our array list
        backgrounds = new ArrayList<>();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        //load the background data into the Background objects and
        // place them in our GameObject arraylist

        backgrounds.add(new Background(
                this.context,
                screenWidth,
                screenHeight,
                "background", 0, 110, 50));

        backgrounds.add(new Background(
                this.context,
                screenWidth,
                screenHeight,
                "clouds", 10, 80, 10));
    }

    private void drawBackground(int position) {

        // Make a copy of the relevant background
        Background bg = backgrounds.get(position);

        // define what portion of images to capture and
        // what coordinates of screen to draw them at

        // For the regular bitmap
        Rect fromRect1 = new Rect(0, 0, bg.width - bg.xClip, bg.height);
        Rect toRect1 = new Rect(bg.xClip, bg.startY, bg.width, bg.endY);

        // For the reversed background
        Rect fromRect2 = new Rect(bg.width - bg.xClip, 0, bg.width, bg.height);
        Rect toRect2 = new Rect(0, bg.startY, bg.xClip, bg.endY);

        //draw the two background bitmaps
        if (!bg.reversedFirst) {
            canvas.drawBitmap(bg.bitmap, fromRect1, toRect1, paint);
            canvas.drawBitmap(bg.bitmapReversed, fromRect2, toRect2, paint);
        } else {
            canvas.drawBitmap(bg.bitmap, fromRect2, toRect2, paint);
            canvas.drawBitmap(bg.bitmapReversed, fromRect1, toRect1, paint);
        }

    }

    @Override
    public void run() {

        while (running) {
            long startFrameTime = System.currentTimeMillis();

            update();

            draw();

            // Calculate the fps this frame
            long timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
        }
    }

    //update background by frame rate
    private void update() {
        // Update all the background positions
        for (Background bg : backgrounds) {
            bg.update(fps);
        }
    }

    //method to draw each frame (background and objects)
    private void draw() {

        if (ourHolder.getSurface().isValid()) {
            //First we lock the area of memory we will be drawing to
            canvas = ourHolder.lockCanvas();

            //draw a background color
            canvas.drawColor(Color.argb(255, 0, 3, 70));

            // Draw the background parallax
            drawBackground(0);
            // Draw the foreground parallax
            drawBackground(1);

            // Draw the rest of the game

            for (int i = 0; i < highPipes.size(); i++) {
                lowPipes.get(i).setY(highPipes.get(i).getY() + GAP);
            }

            cock.draw(canvas);

            if(startGame) {
                drawPipes(lowPipes);
                drawPipes(highPipes);
                cock.setBm(0);
                cock.gravity();
            }

            else
            {
                canvas.drawText("Tap to start",
                        (float) cock.getX() - 100,
                        (float) cock.getY() + 400,
                        textPaint);

                cock.setY(screenHeight/2.0);
            }

            if (gameOver()) {
                deathScreen();
            }

            incrementScore();

            // Unlock and draw the scene
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void deathScreen() {
        canvas.drawText("Game Over!",
                (float) (screenWidth / 9),
                (float) (screenHeight / 3),
                gameOverPaint);

        Intent intent = new Intent();
        intent.setClass(context, death_screen.class);
        intent.putExtra("score", currScore);
        context.startActivity(intent);

    }

    //method to increase score when passing pipe
    private void incrementScore() {
        //noinspection IntegerDivisionInFloatingPointContext

        canvas.drawText("Score",
                screenWidth / 5,
                screenHeight / 10,
                textPaint);

        canvas.drawText(String.valueOf(currScore), // draws the current score
                (float) (screenWidth / 1.6),
                screenHeight / 10,
                numPaint);

        if(lowPipes.get(scorePipe).getX() < -1*SCREEN_GAP) {

            currScore++;

            if(scorePipe < lowPipes.size() -1) { scorePipe++; } // check if we pass the closest pipe

            else {scorePipe = 0;} // resets when we pass all 4 pipes
        }

    }

    //method to draw the pipes
    public void drawPipes(ArrayList<Pipe> pipes) {
        for(Pipe pipe : pipes)
        {
            //here we set the timing for pipes to respawn to change mess with screen_gap
            if(pipe.getX() <= -1*(screenWidth) + SCREEN_GAP)
            {
                pipe.setX(screenWidth*2);
                pipe.randomizeY(screenHeight);
            }
            pipe.draw(canvas);

            if(currScore % 10 == 0 && currScore > 0)
            {
                pipe.setSpeed((float) (pipe.getSpeed() + 0.1));
            }
        }
    }

    //method to determine if cock is dead
    public boolean gameOver() {
        if(cock.getY() >= screenHeight || cock.getY() <= 0) {
            return true;
        }

        for(int pipe = 0; pipe < highPipes.size(); pipe++) {
            if (cock.getHitbox().intersect(highPipes.get(pipe).getHitbox())
                    || cock.getSmallHitbox().intersect(highPipes.get(pipe).getHitbox())
                    || cock.getHitbox().intersect(lowPipes.get(pipe).getHitbox())
                    || cock.getSmallHitbox().intersect(lowPipes.get(pipe).getHitbox())) {
                return true;
            }

        }
        return false;
    }

    // Clean up our thread if the game is stopped
    public void pause() {
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }

    // Make a new thread and start it
    // Execution moves to our run method
    public void resume() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            cock.setBm(1);
            startGame = true;
        }
        return false;
    }
}
