/*
 * TCSS 305 – Autumn 2019
 * Assignment 4 - Raceday
 */

package model;

/**
 * This is the class for a Telemetry message.
 * @author Yolanda Xu
 * @version 15 Nov 2019
 */
public class TelemetryMessage extends AbstractMessage {

    /** Participant ID. */
    private final int myRacerId;
    
    /** The distance. */
    private final Double myDistance;
    
    /** Number of laps. */
    private final int myLap;
    
    /**
     * Constructor for TelemetryMessage.
     * @param theMessage the message
     */
    public TelemetryMessage(final String theMessage) {
        super(theMessage);
        myRacerId = Integer.parseInt(myParts[2]);
        myDistance = Double.parseDouble(myParts[THREE]);
        myLap = Integer.parseInt(myParts[FOUR]);
    }
    
    /**
     * Accessor for racerID.
     * @return the racerID
     */
    public int getMyRacerId() {
        return myRacerId;
    }
    
    /**
     * Accessor for distance.
     * @return the distance
     */
    public Double getMyDistance() {
        return myDistance;
    }

    @Override
    public String toString() {
        return super.toString() + DELIMITER + myRacerId + DELIMITER + myDistance
               + DELIMITER + myLap;
    }
    
}
