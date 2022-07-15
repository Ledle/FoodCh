package com.foodch;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;


public class Creature extends Circle{
    Genom genom;
    World world;
    int sleep;
    double size,energyToSplit,energy,movingCost,speed, diet,mutationChance;
    double[] weights;
    boolean alive;
    public Creature(World w, double x, double y, Genom g, double energy){
        super(x,y,1.0);
        this.world = w;
        this.genom = (g!=null)?g:new Genom(new Genom(),36);
        this.sleep = 100;
        this.alive = true;
        this.setProperties(this.genom);
        this.energyToSplit = this.size*this.size;
        this.energy = (energy!=0)?Math.min(energy,this.energyToSplit) : this.energyToSplit;
        this.movingCost = (Math.pow(this.speed, 3) * Math.pow(this.size, 2)) / 15000000;
        if(diet < 0.5) this.setFill(Color.YELLOW);
        else if(diet < 0.75) this.setFill(Color.ORANGE);
        else this.setFill(Color.RED);
    }
    public Creature(World w, double x, double y){
        this(w,x,y,null,0);
    }
    void setProperties(Genom g){
        this.mutationChance=g.mutationChance;
        this.speed=g.speed;
        this.size=g.size;
        this.setRadius(size);
        this.diet =g.diet;
        this.weights = g.weights;
    }
    Creature findNearestCreature(){
        Creature resCreat=null;
        double dist,r = Double.POSITIVE_INFINITY;
        for(Creature p : world.creatures){
            if(p.alive){
                dist = dist(p);
                if(r>dist && p.size < this.size){
                    r=dist;
                    resCreat = p;
                }
            }
        }
        return resCreat;
    }
    Food findNearestFood(){
        Food resFood =null;
        double dist,r = Double.POSITIVE_INFINITY;
        for(Food p : world.foods){
            if(p.alive){
                dist = dist(p);
                if(r>dist && p.size < this.size){
                    r=dist;
                    resFood = p;
                }
            }
        }
        return resFood;

    }
    void Die(){
        this.alive = false;
    }
    void split(){
        this.energy /=2;
        Creature newCreat = new Creature(this.world,(int)this.getCenterX(),(int)this.getCenterY(),this.genom.getMutated(),this.energy);
        this.world.addCreature(newCreat);
    }
    void eatCreature(Creature c){
        if(c.alive){
            this.energy+=c.energy * this.diet;
            c.Die();
        }
        this.sleep += 300;
    }
    void eatFood(Food f){
        if (this.size > f.size) {
            this.energy += (f.energy * (1-this.diet))/4;
            f.Die();
            this.sleep += 25;
        }
    }
    boolean getDecision(Creature Creat, Food food){
        if(food ==null) return true;
        double[] input = {
                this.diet,
                this.dist(Creat),
                this.dist(food),
                Creat.speed,
                Creat.energy,
                Creat.sleep
        };
        for(int i=0;i<input.length;i++){
            input[i] = 1/(1+Math.exp(0-input[i]));
        }
        double res = 0;
        for(int i=0;i<input.length*input.length;i++){
            res += input[i%input.length] * this.weights[i];
        }
        return res>0;
    }
    double dist(Rectangle rect){
        return Math.sqrt(Math.pow(this.getCenterX()-rect.getX(),2) + Math.pow(this.getCenterY()-rect.getY(),2));
    }
    double dist(Circle rect){
        return Math.sqrt(Math.pow(this.getCenterX()-rect.getCenterX(),2) + Math.pow(this.getCenterY()-rect.getCenterY(),2));
    }
    void moveTo(Rectangle rect){
        double d = this.dist(rect);
        if(d!=0){
            double mov = Math.min(speed,d);
            double cost = this.movingCost * mov;
            double k = mov/d;
            this.setCenterX(this.getCenterX()+(rect.getX() - this.getCenterX()) * k);
            this.setCenterY(this.getCenterY()+(rect.getY() - this.getCenterY()) * k);
            this.energy -= cost;
        }
    }
    void moveTo(Circle rect){
        double d = this.dist(rect);
        if(d!=0){
            double mov = Math.min(speed,d);
            double cost = this.movingCost * mov;
            double k = mov/d;
            this.setCenterX(this.getCenterX()+(rect.getCenterX() - this.getCenterX()) * k);
            this.setCenterY(this.getCenterY()+(rect.getCenterY() - this.getCenterY()) * k);
            this.energy -= cost;
        }
    }
    void Do(){
        if(this.energy < 0 ){
            this.Die();
            return;
        }
        if(this.sleep!=0){
            this.sleep--;
            return;
        }
        if(this.energy > this.energyToSplit && this.world.creatures.size()<2000){
            this.split();
        }
        else{
            Creature Creat = findNearestCreature();
            Food food = findNearestFood();
            if(Creat!=null && getDecision(Creat, food)){
                moveTo(Creat);
                if(dist(Creat)==0) eatCreature(Creat);
            }
            else if(food !=null){
                if(dist(food)==0) eatFood(food);
                moveTo(food);
            }
        }
        this.energy--;
    }
}
