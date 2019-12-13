/*
 * TCSS 305 – Autumn 2019
 * Assignment 4 - Raceday
 */

package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Header class.
 * @author Yolanda Xu
 * @version 15 Nov 2019
 */
public class Header {

    /** 3. */
    protected static final int THREE = 3;
    
    /** 4. */
    protected static final int FOUR = 4;
    
    /** 5. */
    protected static final int FIVE = 5;
    
    /** 6. */
    protected static final int SIX = 6;
    
    /** 7. */
    protected static final int SEVEN = 7;
    
    /** The race name. */
    private String myRaceName;
    
    /** The type of track. */
    private String myTrack;
    
    /** The track width. */
    private int myWidth;
    
    /** The track height. */
    private int myHeight;
    
    /** The track distance. */
    private int myLapDistance;
    
    /** The total race time. */
    private int myTotalRaceTime;
    
    /** The number of participants. */
    private int myNumParticipants;
    
    /** The parsed header. */
    private final List<String[]> myParsedHeader = new ArrayList<>();
    
    /** The (String) participant list. */
    private final List<String[]> myParticipantsList = new ArrayList<>();
    
    /** The participant list. */
    private final List<Participant> myParticipants = new ArrayList<>();
    
    /**
     * The constructor.
     * @param theHeaderList the header
     */
    public Header(final List<String> theHeaderList) {
        for (final String h : theHeaderList) {
            final String[] parts = h.split(":");
            if (parts.length == 2) {
                myParsedHeader.add(parts);
            }
            if (parts.length == THREE) {
                parts[0] = parts[0].replaceAll("#", "");
                myParticipantsList.add(parts);
            }
            
        }
        helpConstruct();
        createParticipants(myParticipantsList);
    }
    
    /**
     * Helper to construct.
     */
    private void helpConstruct() {
        
        myRaceName = myParsedHeader.get(0)[1];
        myTrack = myParsedHeader.get(1)[1];
        myWidth = Integer.parseInt(myParsedHeader.get(2)[1]);
        if (myWidth != FIVE) {
            throw new IllegalArgumentException("Width must be 5");
        }
        myHeight = Integer.parseInt(myParsedHeader.get(THREE)[1]);
        if (myHeight < 2 || myHeight > FOUR) {
            throw new IllegalArgumentException("Height must be integer from 2 to 4");
        }
        myLapDistance = Integer.parseInt(myParsedHeader.get(FOUR)[1]);
        myTotalRaceTime = Integer.parseInt(myParsedHeader.get(FIVE)[1]);
        myNumParticipants = Integer.parseInt(myParsedHeader.get(SIX)[1]);
    }

    /**
     * Accessor for the track width.
     * @return the track width
     */
    public int getWidth() {
        return myWidth;
    }

    /**
     * Accessor for the track height.
     * @return the track height
     */
    public int getHeight() {
        return myHeight;
    }

    /**
     * Accessor for the race name.
     * @return the race name
     */
    public String getMyRaceName() {
        return myRaceName;
    }

    /**
     * Accessor for the track type.
     * @return the track type
     */
    public String getMyTrack() {
        return myTrack;
    }

    /**
     * Accessor for the the lap distance.
     * @return the lap distance
     */
    public int getMyLapDistance() {
        return myLapDistance;
    }

    /**
     * Accessor for the total race time.
     * @return the total race time
     */
    public int getMyTotalRaceTime() {
        return myTotalRaceTime;
    }

    /**
     * Accessor for the number of participants.
     * @return the number of participants
     */
    public int getMyNumParticipants() {
        return myNumParticipants;
    }
    
    /**
     * Accessor for the participants list.
     * @return the participants list
     */
    public List<Participant> getMyParticipants() {
        return myParticipants;
    }
    
    /**
     * Create participants.
     * @param theParticipantsList the participant list
     */
    private void createParticipants(final List<String[]> theParticipantsList) {
        for (final String[] p : theParticipantsList) {
            final Participant part = new Participant(p);
            myParticipants.add(part);
        }
    }

}
