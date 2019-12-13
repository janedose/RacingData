/*
 * TCSS 305 RACEDAY
 */

package controller;

import static model.PropertyChangeEnabledRaceControls.PROPERTY_ADVANCE;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_HEADER;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_INVALIDTIME;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_LOAD;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_TIME;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_TM;

import application.Main;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import model.RaceModel;
import view.LeaderBoardPanel;
import view.StatusBarPanel;
import view.TrackPanel;
import view.Utilities;

/**
 * Controller Panel.
 * @author Charles Bryan
 * @author Yolanda Xu
 * @version Autumn 2019
 */
public class ControllerPanel extends JPanel implements PropertyChangeListener {

    /** The serialization ID. */
    private static final long serialVersionUID = -6759410572845422202L;

    /** Amount of milliseconds between each call to the timer. */
    private static final int TIMER_FREQUENCY = 30; 
    
    /** The fast speed. */
    private static final int FAST = 4;
    
    /** 5. */
    private static final int FIVE = 5;
    
    /** 10. */
    private static final int TEN = 10;
    
    /** 25. */
    private static final int TWENTYFIVE = 25;
    
    /** 50. */
    private static final int FIFTY = 50;

    /** 60. */
    private static final int SIXTY = 60;
    
    /** Dimension X. */
    private static final int DIMX = 600;

    /** Dimension Y. */
    private static final int DIMY = 450;

    /** 1000. */
    private static final int THOUS = 1000;
    
    /** Play button. */
    private static final String PLAY = "Play";
    
    /** Play icon. */
    private static final String PLAY_ICON = "/ic_play.png";
    
    /** Normal speed button. */
    private static final String NORMAL = "Times One";
    
    /** Normal speed icon. */
    private static final String NORMAL_ICON = "/ic_one_times.png";

    /** No repeat button. */
    private static final String NOREPEAT = "Single Race";
    
    /** No repeat icon. */
    private static final String NOREPEAT_ICON = "/ic_repeat.png";
    
    /** The error message. */
    private static final String ERROR_MSG = "Error loading file.";
    
    /** The error. */
    private static final String ERROR = "Error";
    
    /** Race model. */
    private static RaceModel myRM = new RaceModel();

    /** Track panel. */
    private static TrackPanel myTP;
    
    /** Display of messages coming from the Race Model. */
    private JTextArea myOutputArea;

    /** Panel to display CheckBoxs for each race Participant. */
    private JPanel myParticipantPanel;

    /** A view on the race model  that displays the current race time. */
    private JLabel myTimeLabel;

    /** A controller and view of the Race Model. */
    private JSlider myTimeSlider;

    /** The list of javax.swing.Actions that make up the ToolBar (Controls) buttons. */
    private List<Action> myControlActions;

    /** The timer that advances the Race Model. */
    private Timer myTimer;

    /** Container to hold the different output areas. */
    private JTabbedPane myTabbedPane;
    
    /** Race Info menu item. */
    private final JMenuItem myInfoItem = new JMenuItem("Race Info...");
     
    /** File chooser. */
    private JFileChooser myChosenFile;
    
    /** The speed. */
    private int mySpeed;
    
    /** Single or loop race. */
    private boolean myNoRepeat;
    
    /** Participants map. */
    private final Map<JCheckBox, Integer> myParticipants = new HashMap<JCheckBox, Integer>();
    
    /** Track frame. */
    private final JFrame myTrackFrame = new JFrame("Race Track");
    
    /**
     * Construct a ControllerPanel.
     */
    public ControllerPanel() {
        super();
        helpConstruct();
        addListeners();
        setUpComponents();
    }
    
    /**
     * To help construct.
     */
    private void helpConstruct() {
        myOutputArea = new JTextArea(TEN, FIFTY);
        myTimeLabel = new JLabel(Utilities.formatTime(0));
        myTimeSlider = new JSlider(0, 0, 0);
        myTimeSlider.setEnabled(false);     //set initially disabled
        myControlActions = new ArrayList<>();
        myTabbedPane = new JTabbedPane();
        myParticipantPanel = new JPanel();
        myTimer = new Timer(TIMER_FREQUENCY, this::handleTimer);
        mySpeed = 1;
        myNoRepeat = true;
    }
    
    /**
     * Timer handler.
     * @param theEvent the event
     */
    private void handleTimer(final ActionEvent theEvent) { //NOPMD
        final int numAdvances = TIMER_FREQUENCY * mySpeed;
        for (int i = 0; i < numAdvances; i++) {
            myRM.advance();
        }
        
    }
    
    /**
     * Displays a simple JFrame.
     */
    private void setUpComponents() {
        setLayout(new BorderLayout());
        
        // JPanel is a useful container for organizing components inside a JFrame
        final JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(TEN, TEN, TEN, TEN));

        mainPanel.add(buildSliderPanel(), BorderLayout.NORTH);

        myOutputArea.setEditable(false);
        final JScrollPane scrollPane = new JScrollPane(myOutputArea);
        scrollPane.
            setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        
        final JScrollPane participantScrollPane = new JScrollPane(myParticipantPanel);
        participantScrollPane.setPreferredSize(scrollPane.getSize());
        

        myTabbedPane.addTab("Data Output Stream", scrollPane);
        myTabbedPane.addTab("Race Participants", participantScrollPane);
        myTabbedPane.setEnabledAt(1, false);    //set initially disabled

        mainPanel.add(myTabbedPane, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        add(buildToolBar(), BorderLayout.SOUTH);
        
        myTrackFrame.setMinimumSize(new Dimension(DIMX, DIMY));
        myTrackFrame.setResizable(false);
        myTrackFrame.setVisible(true);

    }

    /**
     * Builds the panel with the time slider and time label.
     * @return the panel
     */
    private JPanel buildSliderPanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(FIVE, FIVE, TWENTYFIVE, FIVE));
        
        myTimeSlider.setBorder(BorderFactory.createEmptyBorder(FIVE, FIVE, FIVE, FIVE));

        panel.add(myTimeSlider, BorderLayout.CENTER);

        myTimeLabel.setBorder(BorderFactory.
                              createCompoundBorder(BorderFactory.createEtchedBorder(),
                              BorderFactory.createEmptyBorder(FIVE, FIVE, FIVE, FIVE)));
        final JPanel padding = new JPanel();
        padding.add(myTimeLabel);
        panel.add(padding, BorderLayout.EAST);

        return panel;
    }
    
    /**
     * Constructs a JMenuBar for the Frame.
     * @return the Menu Bar
     */
    private JMenuBar buildMenuBar() {
        final JMenuBar bar = new JMenuBar();
        bar.add(buildFileMenu());
        bar.add(buildControlsMenu(myControlActions));
        bar.add(buildHelpMenu());
        return bar;
    }
    
    /**
     * Builds the file menu for the menu bar.
     * 
     * @return the File menu
     */
    private JMenu buildFileMenu() {

        final JMenu fileMenu = new JMenu("File");

        final JMenuItem load = new JMenuItem("Load Race...");
       
        fileMenu.add(load);
        load.addActionListener(new ChooseFile());

        fileMenu.addSeparator();

        final JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> { 
            System.exit(0); });

        fileMenu.add(exitItem);
        return fileMenu;
    }

    /**
     * Set up the view.
     */
    private void createView() {
        myTrackFrame.add(myTP);
        myTrackFrame.add(new StatusBarPanel(myRM), BorderLayout.SOUTH);
        myTrackFrame.add(new LeaderBoardPanel(myRM), BorderLayout.EAST);
        myTrackFrame.pack();
    }
    
    /**
     * Set up participant info.
     */
    private void createParticipantInfo() {
        myParticipantPanel.removeAll();
        myParticipants.clear();
        myRM.clearParticipants();
        for (int i = 0; i < myRM.getMyHeader().getMyNumParticipants(); i++) {
            final String name = myRM.getMyHeader().getMyParticipants().get(i).getMyRacerName();
            final int id = myRM.getMyHeader().getMyParticipants().get(i).getMyRacerID();
            final JCheckBox checkBox = new JCheckBox(new CheckboxAction(name));
            checkBox.setSelected(true);
            myParticipants.put(checkBox, id);
            myParticipantPanel.add(checkBox);
            myRM.toggleParticipant(myRM.getMyHeader().getMyParticipants().get(i).
                                   getMyRacerID(), true);
        }
    }
    
    /**
     * Create time slider.
     */
    private void createTimeSlider() {
        final int maxTime = myRM.getMyHeader().getMyTotalRaceTime();
        myTimeSlider.setMaximum(maxTime);
        myTimeSlider.setMajorTickSpacing(SIXTY * THOUS);
        myTimeSlider.setMinorTickSpacing(TEN * THOUS);
        myTimeSlider.setPaintTicks(true);
    }
  
    
    /**
     * Build the Controls JMenu.
     * 
     * @param theActions the Actions needed to add/create the items in this menu
     * @return the Controls JMenu
     */
    private JMenu buildControlsMenu(final List<Action> theActions) {
        final JMenu controlsMenu = new JMenu("Controls");

        for (final Action a : theActions) {
            controlsMenu.add(a);
            a.setEnabled(false);    //set initially disabled
        }
        

        return controlsMenu;
    }
    
    /**
     * Build the Help JMenu.
     * 
     * @return the Help JMenu
     */
    private JMenu buildHelpMenu() {
        final JMenu helpMenu = new JMenu("Help");
        myInfoItem.addActionListener(theEvent -> { 
            final String raceInfo = myRM.displayRaceInfo();
            JOptionPane.showMessageDialog(this, raceInfo);
        });
        helpMenu.add(myInfoItem);
        myInfoItem.setEnabled(false);  

        final JMenuItem aboutItem = new JMenuItem("About...");
        aboutItem.addActionListener(theEvent -> { 
            JOptionPane.showMessageDialog(this, "Yolanda Xu\n" + "Winter 2019\n"
                            + "TCSS 305\n");
        });
        helpMenu.add(aboutItem);
        
        return helpMenu;
    }

    /**
     * Build the toolbar from the Actions list.
     * 
     * @return the toolbar with buttons for all of the Actions in the list
     */
    private JToolBar buildToolBar() {
        final JToolBar toolBar = new JToolBar();
        for (final Action a : myControlActions) {
            final JButton b = new JButton(a);
            b.setHideActionText(true);
            b.setEnabled(false);    //set initially disabled
            toolBar.add(b);
        }
        return toolBar;
    }
    
    /**
     * Add actionListeners to the buttons. 
     */
    private void addListeners() {
        buildActions();
        myRM.addPropertyChangeListener(PROPERTY_TIME, this);
        myRM.addPropertyChangeListener(PROPERTY_LOAD, this);
        myRM.addPropertyChangeListener(PROPERTY_ADVANCE, this);
        myRM.addPropertyChangeListener(PROPERTY_INVALIDTIME, this);
        myRM.addPropertyChangeListener(PROPERTY_HEADER, this);
        myTimeSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(final ChangeEvent theEvent) {
                final JSlider source = (JSlider) theEvent.getSource();
                if (source.getValueIsAdjusting()) {
                    myRM.moveTo(source.getValue());
                }
            }
        });
    }
    
    /**
     * Instantiate and add the Actions.
     */
    private void buildActions() {
        myControlActions.add(new RestartAction("Restart", "/ic_restart.png"));
        myControlActions.add(new StartAction(PLAY, PLAY_ICON));
        myControlActions.add(new SpeedAction(NORMAL, NORMAL_ICON));
        myControlActions.add(new RepeatAction(NOREPEAT, NOREPEAT_ICON));
        myControlActions.add(new ClearAction("Clear", "/ic_clear.png"));
 
    }
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public static void createAndShowGUI() {
        //Create and set up the window.
        final JFrame frame = new JFrame("Astonishing Race!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        final ControllerPanel pane = new ControllerPanel();
        
        //Add the JMenuBar to the frame:
        frame.setJMenuBar(pane.buildMenuBar());
        
        pane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(pane);
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if (PROPERTY_LOAD.equals(theEvent.getPropertyName())) {
            myOutputArea.append("Loading Complete.\n\n");
        }
        if (PROPERTY_TIME.equals(theEvent.getPropertyName())) {
            final int totalRaceTime = (Integer) theEvent.getNewValue();
            if (!myNoRepeat && totalRaceTime >= myRM.getMyHeader().getMyTotalRaceTime()) {
                myRM.moveTo(0);
            }
            myTimeLabel.setText(Utilities.formatTime((Integer) theEvent.getNewValue()));
            myTimeSlider.setValue((Integer) theEvent.getNewValue());
        }
        if (PROPERTY_ADVANCE.equals(theEvent.getPropertyName())) {
            myOutputArea.append((String) theEvent.getNewValue() + "\n");
        }
        if (PROPERTY_INVALIDTIME.equals(theEvent.getPropertyName())) {
            if (myNoRepeat) {
                myRM.moveTo(myRM.getMyHeader().getMyTotalRaceTime());
            }
            if (!myNoRepeat) {
                myRM.moveTo(0);
            }
        }
    }
    

    /**
     * This is a simple implementation of an Action.
     * You will most likely not use this implementation in your final solution. Either
     * create your own Actions or alter this to suit the requirements for this assignment. 
     * 
     * @author Charles Bryan
     * @version Autumn 2019
     */
    private class SimpleAction extends AbstractAction {

        /** The serialization ID. */
        private static final long serialVersionUID = -3160383376683650991L;

        /**
         * Constructs a SimpleAction.
         * 
         * @param theText the text to display on this Action
         * @param theIcon the icon to display on this Action
         */
        SimpleAction(final String theText, final String theIcon) {
            super(theText);
            
            // small icons are usually assigned to the menu
            ImageIcon icon = (ImageIcon) new ImageIcon(getRes(theIcon));
            final Image smallImage = icon.getImage().
                            getScaledInstance(12, -1, java.awt.Image.SCALE_SMOOTH);
            final ImageIcon smallIcon = new ImageIcon(smallImage);
            putValue(Action.SMALL_ICON, smallIcon);

            // Here is how to assign a larger icon to the tool bar.
            icon = (ImageIcon) new ImageIcon(getRes(theIcon));
            final Image largeImage = icon.getImage().
                            getScaledInstance(24, -1, java.awt.Image.SCALE_SMOOTH);
            final ImageIcon largeIcon = new ImageIcon(largeImage);
            putValue(Action.LARGE_ICON_KEY, largeIcon);
        }
        
        /**
         * Wrapper method to get a system resource.
         * 
         * @param theResource the name of the resource to retrieve
         * @return the resource
         */
        private URL getRes(final String theResource) {
            return Main.class.getResource(theResource);
        }
        
        @Override
        public void actionPerformed(final ActionEvent theEvent) {
        
        }
        
        /**
         * Helper to set the Icon to both the Large and Small Icon values. 
         * @param theIcon the icon to set for this Action 
         */
        private void setIcon(final ImageIcon theIcon) {
            final ImageIcon icon = (ImageIcon) theIcon;
            final Image largeImage =
                icon.getImage().getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH);
            final ImageIcon largeIcon = new ImageIcon(largeImage);
            putValue(Action.LARGE_ICON_KEY, largeIcon);
            
            final Image smallImage =
                icon.getImage().getScaledInstance(12, -1, java.awt.Image.SCALE_SMOOTH);
            final ImageIcon smallIcon = new ImageIcon(smallImage);
            putValue(Action.SMALL_ICON, smallIcon);
        }
        
    }
    
    
    
    /**
     * Inner action class.
     * @author Yolanda Xu
     * @version 15 Nov 2019
     */
    class StartAction extends SimpleAction {

        private static final long serialVersionUID = 1L;

        /**
         * The constructor.
         * @param theText the text
         * @param theIcon the icon
         */
        StartAction(final String theText, final String theIcon) {
            super(theText, theIcon);
        }

        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            if (myTimer.isRunning()) {
                myTimer.stop();
                myTimeSlider.setEnabled(true);
                putValue(Action.NAME, PLAY);
                super.setIcon(new ImageIcon(super.getRes(PLAY_ICON)));
            } else {
                myTimer.start();
                myTimeSlider.setEnabled(false);
                putValue(Action.NAME, "Pause");
                super.setIcon(new ImageIcon(super.getRes("/ic_pause.png")));
            }
        }
        
    }
    
    /**
     * Inner action class.
     * @author Yolanda Xu
     * @version 15 Nov 2019
     */
    class RestartAction extends SimpleAction {

        private static final long serialVersionUID = 1L;

        /**
         * The constructor.
         * @param theText the text
         * @param theIcon the icon
         */
        RestartAction(final String theText, final String theIcon) {
            super(theText, theIcon);
        }
        
        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            myRM.moveTo(0);
            myTimeSlider.setEnabled(true);
        }
        
    }  
    
    /**
     * Inner action class.
     * @author Yolanda Xu
     * @version 15 Nov 2019
     */
    class ClearAction extends SimpleAction {

        private static final long serialVersionUID = 1L;

        /**
         * The constructor.
         * @param theText the text
         * @param theIcon the icon
         */
        ClearAction(final String theText, final String theIcon) {
            super(theText, theIcon);
        }

        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            myOutputArea.setText("");
        }
        
    }  
    
    /**
     * Inner action class.
     * @author Yolanda Xu
     * @version 15 Nov 2019
     */
    class SpeedAction extends SimpleAction {

        private static final long serialVersionUID = 1L;

        /**
         * The constructor.
         * @param theText the text
         * @param theIcon the icon
         */
        SpeedAction(final String theText, final String theIcon) {
            super(theText, theIcon);
        }

        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            if (mySpeed == 1) {
                mySpeed = FAST;
                putValue(Action.NAME, "Times Four");
                super.setIcon(new ImageIcon(super.getRes("/ic_four_times.png")));
            } else {
                mySpeed = 1;
                putValue(Action.NAME, NORMAL);
                super.setIcon(new ImageIcon(super.getRes(NORMAL_ICON)));
            }
        }
        
    }
    
    /**
     * Inner action class.
     * @author Yolanda Xu
     * @version 15 Nov 2019
     */
    class RepeatAction extends SimpleAction {

        private static final long serialVersionUID = 1L;

        /**
         * The constructor.
         * @param theText the text
         * @param theIcon the icon
         */
        RepeatAction(final String theText, final String theIcon) {
            super(theText, theIcon);
        }

        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            if (myNoRepeat) {
                myNoRepeat = false;
                putValue(Action.NAME, "Loop Race");
                super.setIcon(new ImageIcon(super.getRes("/ic_repeat_color.png")));
            } else {
                myNoRepeat = true;
                putValue(Action.NAME, NOREPEAT);
                super.setIcon(new ImageIcon(super.getRes(NOREPEAT_ICON)));
            }
        }
        
    }
    
    
    /**
     * Choose file class.
     * @author Yolanda Xu
     * @version 15 Nov 2019
     */
    private class ChooseFile implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            myChosenFile = new JFileChooser();
            myChosenFile.setCurrentDirectory(new File("."));
            myChosenFile.showOpenDialog(myChosenFile);
            final File f = myChosenFile.getSelectedFile();
            final JPanel errorPanel = new JPanel();
            try {
                if (f != null) {
                    myRM.loadRace(f);
                    if (myRM.validFile()) {
                        myTimeSlider.setEnabled(true);
                        myTabbedPane.setEnabledAt(1, true);
                        myInfoItem.setEnabled(true); 
                        for (final Action a : myControlActions) {
                            a.setEnabled(true);
                        }
                        createParticipantInfo();
                        createTimeSlider();
                        myTP = new TrackPanel(myRM);
                        myRM.addPropertyChangeListener(PROPERTY_TIME, myTP);
                        myRM.addPropertyChangeListener(PROPERTY_TM, myTP);

                        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                createView();
                            }
                        });
                    } else {
                        JOptionPane.showMessageDialog(errorPanel, ERROR_MSG,
                                                      ERROR, JOptionPane.ERROR_MESSAGE);
                    }
                    
                }
                
               
            } catch (final IOException e1) {
                JOptionPane.showMessageDialog(errorPanel, ERROR_MSG,
                                              ERROR, JOptionPane.ERROR_MESSAGE);
            }
            
        }
        
    }
    
    /**
     * Inner class.
     * @author Yolanda Xu
     * @version 15 Nov 2019
     */
    class CheckboxAction extends AbstractAction {
        /**
         * Serial id.
         */
        private static final long serialVersionUID = 1L;

        /**
         * The constructor.
         * @param theText The text
         */
        CheckboxAction(final String theText) {
            super(theText);
        }
     
        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            final JCheckBox cbLog = (JCheckBox) theEvent.getSource();
            final int id = myParticipants.get(cbLog);
            if (cbLog.isSelected()) {
                myRM.toggleParticipant(id, true);
            } else {
                myRM.toggleParticipant(id, false);
            }
        }
    }
    
}
