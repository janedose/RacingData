/*
 * TCSS 305 – Autumn 2019
 * Assignment 4 - Raceday
 */

package model;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the class for a Leaderboard message.
 * @author Yolanda Xu
 * @version 15 Nov 2019
 */
public class LeaderboardMessage extends AbstractMessage {
    
    /** Leaderboard List. */
    private final List<Integer> myLBList = new ArrayList<Integer>();

    /**
     * Constructor for LeaderboardMessage.
     * @param theMessage the message
     */
    public LeaderboardMessage(final String theMessage) {
        super(theMessage);
        for (int i = 2; i < myParts.length; i++) {
            myLBList.add(Integer.parseInt(myParts[i]));
        }
    }

    /**
     * Acessor for the leaderboard list.
     * @return the leaderboard list
     */
    public List<Integer> getMyLBList() {
        return myLBList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 2; i < myParts.length; i++) {
            sb.append(DELIMITER + myParts[i]);
        }
        
        return super.toString() + sb.toString();
    }

}
