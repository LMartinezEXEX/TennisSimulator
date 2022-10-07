package com.simulator.app;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Court {
    private static final double ADJACENT_MOVING_TIME = 1.8;
    private static final double DIAGONAL_MOVING_TIME = 2.1;
    private static final Random RANDOM = new Random();
    
    public static enum Section {
        LeftAlley, 
        RightAlley, 
        DeuceCourt, 
        AdCourt, 
        LeftBackcourt, 
        RightBackcourt;

        private static final List<Section> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
        private static final int SIZE = VALUES.size();
        private static final Random RANDOM = new Random();

        public static Section randomSection() {
            return VALUES.get(RANDOM.nextInt(SIZE));
        }
    }

    public static double moveTime(Section from, Section to) {
        double time;

        switch (from) {
            case LeftBackcourt:
            case DeuceCourt:
                if (to == Section.RightBackcourt || to == Section.AdCourt)
                    time = ADJACENT_MOVING_TIME;
                else time = DIAGONAL_MOVING_TIME;
                break;

            case RightBackcourt:
            case AdCourt:
                if (to == Section.LeftBackcourt || to == Section.DeuceCourt)
                    time = ADJACENT_MOVING_TIME;
                else time = DIAGONAL_MOVING_TIME;
                break;
            
            default:
                return -1;
        }

        double unexpectedTimeNeeded = RANDOM.nextDouble();
        return time + unexpectedTimeNeeded;
    }
}
