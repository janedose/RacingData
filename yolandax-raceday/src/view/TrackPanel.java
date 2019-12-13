/*
 * TCSS 305 RACEDAY
 */

package view;

import static model.PropertyChangeEnabledRaceControls.PROPERTY_TM;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import model.Header;
import model.Participant;
import model.RaceModel;
import model.TelemetryMessage;
import track.VisibleRaceTrack;

/**
 * Track Panel to display race track.
 * @author Yolanda Xu
 * @version 7 Dec 2019
 */
public class TrackPanel extends JPanel implements PropertyChangeListener {
    
    /**  
     * A generated serial version UID for object Serialization. 
     * http://docs.oracle.com/javase/7/docs/api/java/io/Serializable.html
     */
    private static final long serialVersionUID = 8385732728740430466L;
    
    /** The size of the Race Track Panel. */
    private static final Dimension TRACK_SIZE = new Dimension(500, 400);
    
    /** The x and y location of the Track. */
    private static final int OFF_SET = 40;

    /** The stroke width in pixels. */
    private static final int STROKE_WIDTH = 25;

    /** The size of participants moving around the track. */
    private static final int OVAL_SIZE = 20;
    
    /** The max color value. */
    private static final int MAX_COLOR = 255;
    
    /** The visible track. */
    private VisibleRaceTrack myTrack;
    
    /** The Header. */
    private final Header myHeader;
    
    /** The race model. */
    private final RaceModel myRM;
    
    /** The participants list. */
    private final List<Participant> myParticipants;
    
    /** The participant ID - shapes map. */
    private final Map<Integer, Ellipse2D> myMap = new HashMap<>();
    
    
    /**
     * The constructor.
     * @param theRM The Race model
     */
    public TrackPanel(final RaceModel theRM) {
        super();
        myRM = theRM;
        myHeader = theRM.getMyHeader();
        myParticipants = myHeader.getMyParticipants();
        setPreferredSize(TRACK_SIZE);
        setupComponents();
    }
    
    /**
     * Setup and layout components. 
     */
    private void setupComponents() {
        //perform calculations to decide how big to make the track.
        final int width = (int) myHeader.getWidth() * 100 - (OFF_SET * 2);
        final int height = (int) myHeader.getHeight() * 100 - (OFF_SET * 2);
        final int x = OFF_SET;
        final int y = (400 - height) / 2;

        myTrack = new VisibleRaceTrack(x, y, width, height, myHeader.getMyLapDistance());
        
        for (int i = 0; i < myParticipants.size(); i++) {
            final Participant p = myParticipants.get(i);
            final Point2D start = myTrack.
                            getPointAtDistance(p.getMyStartingDistance());
            final Ellipse2D circle = new Ellipse2D.Double(start.getX() - OVAL_SIZE / 2,
                                                          start.getY() - OVAL_SIZE / 2,
                                                          OVAL_SIZE,
                                                          OVAL_SIZE);
            
            
            myMap.put(p.getMyRacerID(), circle);
            final Color color = randomColor();
            p.setColor(color);
        }
        this.setBorder(BorderFactory.createTitledBorder("Race Track"));
        myRM.addPropertyChangeListener(PROPERTY_TM, this);
    }
    
    
    /**
     * Paints the VisibleTrack with a single ellipse moving around it.
     * 
     * @param theGraphics The graphics context to use for painting.
     */
    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;

        // for better graphics display
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);

        if (myTrack != null) {
            g2d.setPaint(Color.BLACK);
            g2d.setStroke(new BasicStroke(STROKE_WIDTH));
            g2d.draw(myTrack);
        }
        
        for (int i = 0; i < myParticipants.size(); i++) {
            g2d.setPaint(myParticipants.get(i).getColor());
            g2d.setStroke(new BasicStroke(1));
            g2d.fill(myMap.get(myParticipants.get(i).getMyRacerID()));
        }
    }
    
    /**
     * Create random color.
     * @return the color
     */
    private Color randomColor() {
        final Random rand = new Random();
        final int r = rand.nextInt(MAX_COLOR);
        final int g = rand.nextInt(MAX_COLOR);
        final int b = rand.nextInt(MAX_COLOR);
        return new Color(r, g, b);
    }

    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if (PROPERTY_TM.equals(theEvent.getPropertyName())) {
            final TelemetryMessage tm = (TelemetryMessage) theEvent.getNewValue();
            final Point2D current = 
                            myTrack.getPointAtDistance(tm.getMyDistance());
            myMap.get(tm.getMyRacerId()).
                setFrame(current.getX() - OVAL_SIZE / 2, 
                              current.getY() - OVAL_SIZE / 2, 
                              OVAL_SIZE, 
                              OVAL_SIZE);
            repaint();
        }
    }
}
