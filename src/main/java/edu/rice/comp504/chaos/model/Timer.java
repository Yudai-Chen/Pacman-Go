package edu.rice.comp504.chaos.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * For recording the scatter and chase period lasting.
 */
public class Timer implements PropertyChangeListener {
    private int timer = 0;

    /**
     * When receive a clock signal, the inner timer increase.
     * @param evt the property change event.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("clock")) {
            timer ++;
        }
    }

    /**
     * Get the value of the inner timer.
     * @return the value of the inner timer.
     */
    int getTimer() {
        return timer;
    }

    /**
     * Reset the timer.
     */
    void reset() {
        timer = 0;
    }

    /**
     * Determine which period the game is in.
     * @return the period.
     */
    int getPeriod() {
        if (timer < Settings.readyTime) {
            return 0;
        } else if (timer < Settings.scatterTime1) {
            return 1;
        } else if (timer < Settings.chaseTime1) {
            return 2;
        } else if (timer < Settings.scatterTime2) {
            return 1;
        } else if (timer < Settings.chaseTime2) {
            return 2;
        } else if (timer < Settings.scatterTime3) {
            return 1;
        } else if (timer < Settings.chaseTime3) {
            return 2;
        } else if (timer < Settings.scatterTime4) {
            return 1;
        } else {
            return 2;
        }
    }
}
