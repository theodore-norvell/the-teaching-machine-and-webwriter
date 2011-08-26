package jsnoopy.swingui;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * <p>Title: JSnoopy</p>
 * <p>Description: Regression testing based on event sequences.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Memorial University of Newfoundland</p>
 * @author Theodore S. Norvell
 * @version 1.0
 */

class OptionsDialog extends JDialog {
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JPanel replayOptionsPanel = new JPanel();
    private JLabel speedLabel = new JLabel();
    private JSlider speedSlider = new JSlider();
    private GridBagLayout gridBagLayout2 = new GridBagLayout();
    private JRadioButton guiThreadButton = new JRadioButton();
    private JRadioButton workerThreadButton = new JRadioButton();
    private JLabel replayOptionsLabel = new JLabel();
    private ButtonGroup threadOptionsButtonGroup = new ButtonGroup();

    public OptionsDialog(JFrame owner) {
        super(owner, "JSnoopy Options", true ) ;
        try {
            jbInit();
            finishInit() ;
            this.pack();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void jbInit() throws Exception {
        this.getContentPane().setLayout(gridBagLayout1);
        speedSlider.setMajorTickSpacing(1);
        speedSlider.setMinimum(0);
        speedSlider.setMaximum(3);
        speedSlider.setPaintTicks(true);
        speedSlider.setToolTipText("Change replay speed");
        speedSlider.setSnapToTicks(true);
        speedLabel.setText("speedLabel");
        replayOptionsPanel.setBorder(BorderFactory.createEtchedBorder());
        replayOptionsPanel.setLayout(gridBagLayout2);
        guiThreadButton.setText("Run in event dispatch thread");
        workerThreadButton.setText("Run in worker thread -- not implemented");
        replayOptionsLabel.setText("Replay Options");
        this.getContentPane().add(replayOptionsPanel,    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        replayOptionsPanel.add(speedSlider,      new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        replayOptionsPanel.add(speedLabel,      new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        replayOptionsPanel.add(guiThreadButton,    new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        replayOptionsPanel.add(workerThreadButton,    new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.getContentPane().add(replayOptionsLabel,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        threadOptionsButtonGroup.add(guiThreadButton);
        threadOptionsButtonGroup.add(workerThreadButton);
    }

    private void finishInit() {
        speedSlider.getModel().addChangeListener(
            new ChangeListener() {
                public void stateChanged( ChangeEvent e ) {
                    switch( speedSlider.getValue() ) {
                        case 0: speedLabel.setText( "No delay");
                        break ;
                        case 1: speedLabel.setText( "Delay: 0.5 seconds");
                        break ;
                        case 2: speedLabel.setText( "Delay: 2 seconds");
                        break ;
                        case 3: speedLabel.setText( "Single step -- not implemented");
                                workerThreadButton.setSelected(true);
                        break ; } }	} ) ;

        guiThreadButton.getModel().addChangeListener(
            new ChangeListener() {
                public void stateChanged( ChangeEvent e ) {
                    if( guiThreadButton.isSelected() ) {
                        if( speedSlider.getValue() == 3 ) {
                            speedSlider.setValue( 0 ) ; } } } } ) ;

        speedSlider.setValue(0);
        guiThreadButton.setSelected(true);
    }

    int getSpeed() {
        switch( speedSlider.getValue() ) {
            case 0: return 0 ;
            case 1: return 500 ;
            case 2: return 2000 ;
            default: return 0 ; }
    }
}