/*
 * TCSS 305 – Autumn 2019
 * Assignment 4 - Raceday
 */

package model;

/**
 * This is the class for an Abstract message.
 * @author Yolanda Xu
 * @version 15 Nov 2019
 */
public abstract class AbstractMessage implements Message {
    
    /** Delimiter. */
    protected static final String DELIMITER = ":";
    
    /** The 3rd index. */
    protected static final int THREE = 3;
    
    /** The 4th index. */
    protected static final int FOUR = 4;

    /** The code for the type of message. */
    protected String myCode;
    
    /** The timestamp. */
    protected int myTimestamp;
    
    /** To separate the string. */
    protected String[] myParts;
    
    /**
     * Constructor for AbstractMessage.
     * @param theMessage the message
     */
    public AbstractMessage(final String theMessage) {
        myParts = theMessage.split(DELIMITER);
        myCode = myParts[0];
        myTimestamp = Integer.parseInt(myParts[1]);
    }

    @Override
    public String toString() {
        return myCode + DELIMITER + myTimestamp;
    }
    
    /**
     * Accessor for the timestamp.
     * @return the timestamp
     */
    public int getMyTimestamp() {
        return myTimestamp;
    }
    
    /**
     * Accessor for the code.
     * @return the code
     */
    public String getMyCode() {
        return myCode;
    }
}
