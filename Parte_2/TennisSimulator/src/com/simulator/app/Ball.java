package com.simulator.app;

import com.simulator.app.Court.Section;

public class Ball {
    private boolean isSlam = false;
    private Section goingToSection;
    
    public void setSlam(boolean isSlam) {
        this.isSlam = isSlam;
    }
    
    public boolean isSlam() {
        return isSlam;
    }

    public void setGoingToSection(Section goingToSection) {
        this.goingToSection = goingToSection;
    }

    public Section getGoingToSection() {
        return goingToSection;
    }
}
