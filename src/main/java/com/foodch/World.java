package com.foodch;

import javafx.scene.Camera;
import javafx.scene.Scene;

import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.paint.Color;


public class World extends Scene{
    ArrayList<Creature> creatures = new ArrayList<Creature>();
    ArrayList<Food> foods = new ArrayList<Food>();
    double x,y;
    long ticks, foodPerTick;
    boolean isRun,splitLimit;
    Group group;

    public World(Group group, Camera cam, double x, double y){
        super(group,x,y);
        this.setCamera(cam);
        this.group = group;
        this.x=x;
        this.y=y;
        this.ticks=0;
        this.isRun = false;
        this.foodPerTick =1;
        createEntities();
        this.setFill(Color.LIGHTGRAY);
    }
    void createEntities(){
        int num=100;
        double r,a,tx,ty;
        Creature p;
        for(int i=0;i<num;i++){
            r=(Math.random()*this.y)/2;
            a=Math.random() * Math.PI * 2;
            tx = Math.cos(a) * r;
            ty = Math.sin(a) * r;
            p=new Creature(this,tx,ty);
            creatures.add(p);
            group.getChildren().add(p);
            addRandomFood();
        }
    }
    void addRandomFood(){
        double r  = (((Math.random() + 0.05) / 1.05) * this.y) / 2;
        double a = Math.random() * Math.PI * 2;
        double tx = Math.cos(a) * r;
        double ty = Math.sin(a) * r;
        Food p = new Food(this,tx,ty);
        this.foods.add(p);
        group.getChildren().add(p);
    }
    void RemoveFood(Food f){
        this.foods.remove(f);
        group.getChildren().remove(f);
    }
    void addCreature(Creature c){
        this.creatures.add(c);
        group.getChildren().add(c);

    }
    void RemoveCreature(Creature c){
        this.creatures.remove(c);
        group.getChildren().remove(c);
    }
    void tick(){
        this.ticks += 1;
        //System.out.println(ticks);
        ArrayList<Creature> predDel = new ArrayList<Creature>();
        Creature p;
        for (int i = 0; i<this.creatures.size(); i++){
            p = this.creatures.get(i);
            if(p.alive) p.Do();
            if(!p.alive) predDel.add(this.creatures.get(i));
        }
        for(int i=0;i<predDel.size();i++){
            RemoveCreature(predDel.get(i));
        }
        ArrayList<Food> foodDel = new ArrayList<Food>();
        for(int i = 0; i<this.foods.size(); i++){
            if(!this.foods.get(i).alive) foodDel.add(this.foods.get(i));
        }
        for(int i = 0; i< foodDel.size(); i++){
            RemoveFood(foodDel.get(i));
        }
        for(int i = 0; i<this.foodPerTick && this.foods.size() < 2000; i++){
            this.addRandomFood();
        }
    }
}
