package com.example.floppycock;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;

public class BaseObject {
    private double x,y;
    private int width, height;
    private Bitmap texture; //the skin of the object
    private Rect hitbox; //this represents the shape of the object


    public BaseObject() {

    }

    //ctor
    public BaseObject(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }


    //getters and setters
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() { return height; }

    public void setHeight(int height) {
        this.height = height;
    }

    public Bitmap getTexture() {
        return texture;
    }

    public void setTexture(Bitmap texture) {
        this.texture = texture;
    }

    public RectF getHitbox() {
        return new RectF((int)this.x,
                (int)this.y,
                (int)this.width  + (int)this.x - 15,
                (int)this.height + (int)this.y);
    }

    public void setHitbox(Rect hitbox) {
        this.hitbox = hitbox;
    }
}
