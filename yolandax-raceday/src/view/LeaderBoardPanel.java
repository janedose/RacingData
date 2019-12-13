/*
 * TCSS 305 RACEDAY
 */

package view;

import static model.PropertyChangeEnabledRaceControls.PROPERTY_LBM;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import model.Participant;
import model.RaceModel;

/**
 * Leader Board Panel to display ranking.
 * @author Yolanda Xu
 * @version 7 Dec 2019
 */
public class LeaderBoardPanel extends JPanel implements PropertyChangeListener {
    
    /** Serial ID. */
    private static final long serialVersionUID = 1L;
    
    /** Participant list. */
    private final List<Participant> myParticipants;
    
    /** Leaderboard list. */
    private List<Integer> myLB;
    
    /** Map of JPanel for participants. */
    private final Map<Integer, JPanel> myMap = new HashMap<>();
    
    /**
     * The constructor.
     * @param theRM The Race model
     */
    public LeaderBoardPanel(final RaceModel theRM) {
        super();
        final RaceModel rm = theRM;
        rm.addPropertyChangeListener(PROPERTY_LBM, this);
        myParticipants = rm.getMyHeader().getMyParticipants();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        makeLabels();
    }
    
    /**
     * Make the labels.
     */
    private void makeLabels() {
        for (final Participant p : myParticipants) {
            final JPanel pPanel = new JPanel();
            final int id = p.getMyRacerID();
            final JLabel pLabel = new JLabel(id + ": " + p.getMyRacerName());
            pPanel.add(pLabel);
            pPanel.setBorder(new LineBorder(Color.black));
            pPanel.setBackground(p.getColor());
            myMap.put(id, pPanel);
            add(pPanel);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if (PROPERTY_LBM.equals(theEvent.getPropertyName())) {
            this.removeAll();
            myLB = (List<Integer>) theEvent.getNewValue();
            for (final int id : myLB) {
                final JPanel pPanel = myMap.get(id);
                add(pPanel);
            }
        }
        
    }

}
