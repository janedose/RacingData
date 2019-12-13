/*
 * TCSS 305 RACEDAY
 */

package view;

import static model.PropertyChangeEnabledRaceControls.PROPERTY_TIME;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.RaceModel;

/**
 * Status Bar Panel to display time status.
 * @author Yolanda Xu
 * @version 7 Dec 2019
 */
public class StatusBarPanel extends JPanel implements PropertyChangeListener {
    
    /**  Serial ID. */
    private static final long serialVersionUID = 1L;
    
    /**  The time. */
    private static final String TIME = "Time: ";
    
    /**  The label. */
    private final JLabel myStatusLabel;
    
    /**  The time. */
    private String myTime = "";
    
    /**
     * The constructor.
     * @param theRM The Race model
     */
    public StatusBarPanel(final RaceModel theRM) {
        super();
        final RaceModel rm = theRM;
        myTime = TIME + Utilities.formatTime(0);
        myStatusLabel = new JLabel(myTime);
        add(myStatusLabel);
        rm.addPropertyChangeListener(PROPERTY_TIME, this);
    }
    
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if (PROPERTY_TIME.equals(theEvent.getPropertyName())) {
            myTime = TIME + Utilities.formatTime((Integer) theEvent.getNewValue());
            myStatusLabel.setText(myTime);
        }
        
    }

}
