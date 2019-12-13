/*
 * TCSS 305 – Autumn 2019
 * Assignment 4 - Raceday
 */

package model;

/**
 * This is the interface Message.
 * @author Yolanda Xu
 * @version 15 Nov 2019
 */
public interface Message {

    /**
     * Accessor for the timestamp.
     * @return the timestamp
     */
    int getMyTimestamp();
    
    /**
     * Accessor for the code.
     * @return the code
     */
    String getMyCode();
    
}
