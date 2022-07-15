package com.foodch;

import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
public class Food extends Circle{
    boolean alive;
    World world;
    double size,energy;
    public Food(World w, double x, double y){
        super(x,y,1);
        this.alive = true;
        this.size = Math.random()*50+50;
        this.setRadius(size);
        this.energy = this.size*this.size;
        this.world=w;
        this.setFill(Color.GREEN);
    }
    public void Die(){
        this.alive = false;
    }
}
