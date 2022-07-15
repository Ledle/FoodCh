package com.foodch;

public class Genom {
    double mutationChance=Double.NaN,speed=Double.NaN,size=Double.NaN, diet =Double.NaN;
    double[] weights=null;
    public Genom(){}
    public Genom(Genom g, int weightsNumber){
        this.mutationChance = (Double.isNaN(g.mutationChance))?random(0.1,0.9):g.mutationChance;
        this.speed = (Double.isNaN(g.speed))?random(5,20):g.speed;
        this.size = (Double.isNaN(g.size))?random(75,150):g.size;
        this.diet = (Double.isNaN(g.diet))?random(0.05,0.95):g.diet;
        if(g.weights!=null) this.weights = g.weights.clone();
        else{
            this.weights = new double[weightsNumber];
            for(int i=0;i<weightsNumber;i++){
                this.weights[i]=random(-1,0.7);
            }
        }
    }
    public Genom(Genom g){
        this(g,36);
    }
    Genom getMutated(){
        Genom newGen = new Genom(this);
        if(this.mutationChance>Math.random()) newGen.speed = this.randomNormal(this.speed,this.speed/4);
        if(this.mutationChance>Math.random()) newGen.size = this.randomNormal(this.size,this.size/4);
        if(this.mutationChance>Math.random()) newGen.mutationChance = this.randomNormal(this.mutationChance,this.mutationChance/4);
        newGen.diet = this.randomNormal(this.diet,Math.min(this.diet,1-this.diet)+0.4);
        if(newGen.diet <0) newGen.diet = 0.05;
        else if (newGen.diet >1) newGen.diet = 0.95;
        for(int i=0;i<newGen.weights.length;i++){
            if(this.mutationChance > Math.random()){
                newGen.weights[i] = this.randomNormal(newGen.weights[i],Math.abs(newGen.weights[i]*2)+0.2);
            }
        }
        return newGen;
    }
    double randomNormal(double x,double d){
        double u=0,v=0;
        while(u==0) u = Math.random();
        while(v==0) v = Math.random();
        double num = Math.sqrt(-2.0*Math.log(u)) * Math.cos(2.0 * Math.PI * v);
        num/=5;
        if(num>1 || num <-1) return this.randomNormal(x,d);
        return x+num*d;
    }
    double random(double min,double max){
        return Math.random() * (max-min) + min;
    }

}

