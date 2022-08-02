package com.example.floppycock;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Pipe extends BaseObject{
    private float speed = 8;

    public Pipe(double x, double y, int width, int height) {
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);


    }

    public void randomizeY(int screenHeight) {

        Random r = new Random();
        if (screenHeight != 0)
        {
            //multiply by -1.2 for a lil trick to keep pipe in frame but also change height very randomly
            //divide by 5 to limit pipe generation height to a 1/5 of the screen
            this.setY(r.nextInt((int) (screenHeight / 6)) *-1.2);
        }
    }

    public void draw(Canvas canvas) {

        setX(getX() - speed);
        canvas.drawBitmap(this.getTexture(), (int)this.getX(), (int)this.getY(), null );
    }

    public void setBm(Bitmap bm) {
        this.setTexture(Bitmap.createScaledBitmap(bm, this.getWidth(), this.getHeight(), true));
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
