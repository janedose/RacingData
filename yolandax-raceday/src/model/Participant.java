/*
 * TCSS 305 – Autumn 2019
 * Assignment 4 - Raceday
 */

package model;

import java.awt.Color;

/**
 * Participant class.
 * @author Yolanda Xu
 * @version 15 Nov 2019
 */
public class Participant {

    /** Participant ID. */
    private final int myRacerID;
    
    /** Participant name. */
    private final String myRacerName;
    
    /** Starting distance. */
    private final double myStartingDistance;
    
    /** Participant color. */
    private Color myColor;
    
    /**
     * The constructor.
     * @param theParticipants the participant
     */
    public Participant(final String[] theParticipants) {
        myRacerID = Integer.parseInt(theParticipants[0]);
        myRacerName = theParticipants[1];
        myStartingDistance = Double.parseDouble(theParticipants[2]);
        
    }

    /**
     * Accessor for racerID.
     * @return the racerID
     */
    public int getMyRacerID() {
        return myRacerID;
    }

    /**
     * Accessor for racer name.
     * @return the racer name
     */
    public String getMyRacerName() {
        return myRacerName;
    }

    /**
     * Accessor for starting distance.
     * @return the distance
     */
    public double getMyStartingDistance() {
        return myStartingDistance;
    }
    
    /**
     * Sets the color.
     * @param theColor color
     */
    public void setColor(final Color theColor) {
        myColor = theColor;
    }
    
    /**
     * Accessor for racer color.
     * @return the racer color
     */
    public Color getColor() {
        return myColor;
    }
    
}
