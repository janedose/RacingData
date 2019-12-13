/*
 * TCSS 305 – Autumn 2019
 * Assignment 4 - Raceday
 */

package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import view.Utilities;

/**
 * Race Model class.
 * @author Yolanda Xu
 * @version 15 Nov 2019
 */
public class RaceModel implements PropertyChangeEnabledRaceControls {


    /** The 2D map for messages. */
    private static Map<Integer, List<AbstractMessage>> myTwoDMap 
        = new TreeMap<Integer, List<AbstractMessage>>();
    
    /** The PropertyChangeSupport. */
    private final PropertyChangeSupport myPcs;
    
    /** Boolean value for valid messages. */
    private boolean myValidMessages = true;
    
    /** The header list. */
    private final List<String> myHeaderList = new ArrayList<String>();
    
    /** The message list. */
    private final List<AbstractMessage> myMessageList = new ArrayList<AbstractMessage>();
    
    /** The header. */
    private Header myHeader;
    
    /** The time. */
    private int myTime;
    
    /** The active participants. */
    private final Set<Integer> myActiveParticipants = new HashSet<>();
    
    /** The old value. */
    private int myOld;
    
    /**
     * The constructor.
     */
    public RaceModel() {
        myPcs = new PropertyChangeSupport(this);
    }
    
    @Override
    public void advance() {
        advance(1);
        
    }
    
    @Override
    public void advance(final int theMillisecond) {
        if ((myTime + theMillisecond < 0) 
                        || (myTime + theMillisecond > myHeader.getMyTotalRaceTime())) {
            myPcs.firePropertyChange(PROPERTY_INVALIDTIME, -1, myTime + theMillisecond);
        } else { 
            changeTime(myTime + theMillisecond);
            if (myTwoDMap.containsKey(myOld)) {
                helpSendMessages(myOld);
                myOld = -1;
            }
            helpSendMessages(myTime + theMillisecond);
        }
    }
    
    /**
     * Help send messages.
     * @param theTime the time
     */
    private void helpSendMessages(final int theTime) {
        if (myTwoDMap.containsKey(theTime)) {
            final List<AbstractMessage> msgs = myTwoDMap.get(theTime);
            for (final AbstractMessage m : msgs) {
                if (m instanceof TelemetryMessage) {
                    if (myActiveParticipants.contains(((TelemetryMessage) m).getMyRacerId())) {
                        myPcs.firePropertyChange(PROPERTY_ADVANCE, -1, m.toString());
                        myPcs.firePropertyChange(PROPERTY_TM, -1, m);
                    }
                } else {
                    myPcs.firePropertyChange(PROPERTY_ADVANCE, -1, m.toString());
                    if (m instanceof LeaderboardMessage) {
                        myPcs.firePropertyChange(PROPERTY_LBM, -1, 
                                                 ((LeaderboardMessage) m).getMyLBList());
                    }
                }
            }
        }
    }

    @Override
    public void moveTo(final int theMillisecond) {
        if (theMillisecond < 0 || theMillisecond > myHeader.getMyTotalRaceTime()) {
            myPcs.firePropertyChange(PROPERTY_INVALIDTIME, -1, theMillisecond);
        }
        if (theMillisecond == 0) {
            myOld = 0;
            helpSendMessages(myOld);
            myOld = -1;
        } else {
            helpSendMessages(theMillisecond);
        }
        changeTime(theMillisecond);
        
    }
    
    /**
     * Helper method to change the value of time and notify observers. 
     * Functional decomposition. 
     * @param theMillisecond the time to change to
     */
    private void changeTime(final int theMillisecond) {
        final int old = myTime;
        myTime = theMillisecond;
        myPcs.firePropertyChange(PROPERTY_TIME, old, myTime);
        
    }
    
    /**
     * Displays race info.
     * @return the race info
     */
    public String displayRaceInfo() {
        String raceInfo = "";
        raceInfo = myHeader.getMyRaceName() + '\n' 
                        + "Track type: " + myHeader.getMyTrack() + '\n' 
                        + "Total time: " + Utilities.formatTime(myHeader.getMyTotalRaceTime())
                        + '\n'
                        + "Lap distance: " + myHeader.getMyLapDistance();
        return raceInfo;
    }

    @Override
    public void toggleParticipant(final int theParticpantID, final boolean theToggle) {
        if (theToggle) {
            myActiveParticipants.add(theParticpantID);
        } else {
            myActiveParticipants.remove(theParticpantID);
        }
        
    }
    
    /**
     * Clears the active participants for loading a new file.
     */
    public void clearParticipants() {
        myActiveParticipants.clear();
        myTime = 0;
    }

    @Override
    public void loadRace(final File theRaceFile) throws IOException {
        
        myHeaderList.clear();
        myMessageList.clear();
        myOld = 0;
        try (Scanner input = new Scanner(theRaceFile)) { 
            while (input.hasNextLine()) {
                final String lineAsString = input.nextLine();
                if (lineAsString.charAt(0) == '#') {
                    myHeaderList.add(lineAsString);
                } else if (lineAsString.startsWith("$T:")) {
                    myMessageList.add(new TelemetryMessage(lineAsString));
                } else if (lineAsString.startsWith("$L:")) {
                    myMessageList.add(new LeaderboardMessage(lineAsString));
                } else if (lineAsString.startsWith("$C:")) {
                    myMessageList.add(new FinishMessage(lineAsString));
                } else {
                    myValidMessages = false;
                }
            }
            final boolean notValidFile = !validFile();
            if (wrongHeaderSize() || notValidFile) {
                throw new IOException("Incorrect file format.");
            } else {
                myHeader = new Header(myHeaderList);
                myPcs.firePropertyChange(PROPERTY_LOAD, null, true);
            }
            create2DMap();
            input.close();
        } catch (final IOException e) {
            e.printStackTrace();
        } // no file
        
    }
    
    /**
     * Accessor for the header.
     * @return the header
     */
    public Header getMyHeader() {
        return myHeader;
    }
    
    /**
     * Determine validity of the file.
     * @return the boolean value
     */
    public boolean validFile() {
        return !wrongHeaderSize() && validHeader() && myValidMessages;
    }
    
    /**
     * Wrong header size.
     * @return the boolean value
     */
    private boolean wrongHeaderSize() {
        return myHeaderList.size() < Header.SEVEN;
    }
    
    /**
     * Determine validity of header.
     * @return the boolean value
     */
    private boolean validHeader() {
        final boolean flag = myHeaderList.get(0).startsWith("#RACE:") 
                        && myHeaderList.get(1).startsWith("#TRACK:")
                        && myHeaderList.get(2).startsWith("#WIDTH:")
                        && myHeaderList.get(Header.THREE).startsWith("#HEIGHT:")
                        && myHeaderList.get(Header.FOUR).startsWith("#DISTANCE:")
                        && myHeaderList.get(Header.FIVE).startsWith("#TIME:")
                        && myHeaderList.get(Header.SIX).startsWith("#PARTICIPANTS:");
        boolean numParticipantsFlag = false;
        if (flag) {
            final String[] parts = myHeaderList.get(Header.SIX).split(":");
            final int num = Integer.parseInt(parts[1]);
            numParticipantsFlag = myHeaderList.size() == (Header.SEVEN + num);
        }
        return flag && numParticipantsFlag;
    }
    
    /**
     * Creates the 2D Map of messages.
     */
    private void create2DMap() {
        
        List<AbstractMessage> someMessages = new ArrayList<AbstractMessage>();
        for (int i = 0; i < myMessageList.size(); i++) {
            if (!myTwoDMap.containsKey(myMessageList.get(i).getMyTimestamp())) {
                someMessages = new ArrayList<AbstractMessage>();
            } 
            someMessages.add(myMessageList.get(i));
            myTwoDMap.put(myMessageList.get(i).getMyTimestamp(), someMessages);
            
        }
    }
    
    
    @Override
    public void addPropertyChangeListener(final PropertyChangeListener theListener) {
        myPcs.addPropertyChangeListener(theListener);
    }

    @Override
    public void addPropertyChangeListener(final String thePropertyName,
                                          final PropertyChangeListener theListener) {
        myPcs.addPropertyChangeListener(thePropertyName, theListener);
    }

    @Override
    public void removePropertyChangeListener(final PropertyChangeListener theListener) {
        myPcs.removePropertyChangeListener(theListener);
    }

    @Override
    public void removePropertyChangeListener(final String thePropertyName,
                                             final PropertyChangeListener theListener) {
        myPcs.removePropertyChangeListener(thePropertyName, theListener);
    }

}
