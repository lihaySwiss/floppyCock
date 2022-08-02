package com.example.floppycock;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.ArrayList;

public class Cock extends BaseObject{

    private ArrayList<Bitmap> animation = new ArrayList<>();
    private float gravity;
    private int bm;

    //ctor
    public Cock(double x, double y, int width, int height) {
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
    }

    //draw the correct version of birb (flap\no flap) by bitmap
    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.animation.get(bm), (int)this.getX(), (int)this.getY(), null );
    }

    //to make birb auto fall
    public void gravity() {
        this.gravity += 1.5; //speed of falling
        setY(getY() + this.gravity);
    }


    //getters and setters

    public float getGravity() { return gravity;}

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }

    public ArrayList<Bitmap> getAnimation() {
        return animation;
    }

    public void setAnimation(ArrayList<Bitmap> animation) {
        this.animation = animation;
        for(int i = 0; i < animation.size(); i++)
        {
            this.animation.set(i, Bitmap.createScaledBitmap(this.animation.get(i), this.getWidth(), this.getHeight(), true));
        }
    }

    public void setBm(int frame) {
        this.bm = frame;
    }

    public RectF getHitbox() {
        return new RectF((int)this.getX() + 50,
                (int)this.getY() + 80,
                this.getWidth()  + (int)this.getX() - 70,
                this.getHeight() + (int)this.getY() - 70);
    }

    public RectF getSmallHitbox() {
        return new RectF((int)this.getX() + 130,
                (int)this.getY() + 40,
                this.getWidth()  + (int)this.getX() - 40,
                this.getHeight() + (int)this.getY() - 140);
    }
}
