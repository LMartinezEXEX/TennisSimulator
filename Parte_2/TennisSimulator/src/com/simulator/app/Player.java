package com.simulator.app;

import java.util.Random;

import com.simulator.app.Court.Section;

public class Player {
    private String name;
    private double speed;
    private double winnable;
    private Court.Section sectionIn;
    private Random rand = new Random();

    public Player(String name, double winnable, Section sectionIn){
        this.name = name;
        this.winnable = winnable;
        this.speed = Math.log(10 + .2 * winnable);
        this.sectionIn = sectionIn;
    }

    public boolean firstHit(Ball ball){
        double probabilityOfSlam = rand.nextDouble() * (this.winnable / 100);
        if (probabilityOfSlam > 0.65) {
            ball.setSlam(true);
            //System.out.println("Is SLAM!");
        }

        double probabilityOfHit = rand.nextDouble() * (this.winnable / 100);

        return hit(ball, probabilityOfHit, 0.1);
    }

    public boolean hit(Ball ball){
        double timeToMove = Court.moveTime(this.sectionIn, ball.getGoingToSection());
        if (this.speed < timeToMove){
            //System.out.println("No llega a zona");
            return false;
        }
        
        double probabilityOfHit = rand.nextDouble() * (this.winnable / 100);

        if (ball.isSlam()) {
            return hit(ball, probabilityOfHit, 0.4);
        }

        return hit(ball, probabilityOfHit, 0.1);
    }

    private boolean hit(Ball ball, double probabilityOfHit, double probabilityNeeded) {
        if (probabilityNeeded < probabilityOfHit) {
            ball.setGoingToSection(Section.randomSection());
            return true;
        }

        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String nombre) {
        this.name = nombre;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getWinnable() {
        return winnable;
    }

    public void setWinnable(double winnable) {
        this.winnable = winnable;
    }

    public Section getSectionIn() {
        return sectionIn;
    }

    public void setSectionIn(Section sectionIn) {
        this.sectionIn = sectionIn;
    }

}
