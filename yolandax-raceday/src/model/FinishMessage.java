/*
 * TCSS 305 – Autumn 2019
 * Assignment 4 - Raceday
 */

package model;

/**
 * This is the class for a Finish message.
 * @author Yolanda Xu
 * @version 15 Nov 2019
 */
public class FinishMessage extends AbstractMessage {
    
    /** Participant ID. */
    private final int myRacerId;
    
    /** New lap number. */
    private final int myNewLapNum;
    
    /** Boolean value for if the participant is finished. */
    private final boolean myIsFinished;

    /**
     * Constructor for FinishMessage.
     * @param theMessage the message
     */
    public FinishMessage(final String theMessage) {
        super(theMessage);
        myRacerId = Integer.parseInt(myParts[2]);
        myNewLapNum = Integer.parseInt(myParts[THREE]);
        myIsFinished = Boolean.parseBoolean(myParts[FOUR]);
    }

    @Override
    public String toString() {
        return super.toString() + DELIMITER + myRacerId + DELIMITER + myNewLapNum
               + DELIMITER + myIsFinished;
    }
    
}
