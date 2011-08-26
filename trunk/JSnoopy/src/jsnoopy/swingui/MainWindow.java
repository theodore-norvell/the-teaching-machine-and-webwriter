package jsnoopy.swingui;

import java.awt.Color;
import java.awt.CardLayout;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.* ;
import javax.swing.*;

import jsnoopy.JSnoopyException;
import jsnoopy.Manager;
import jsnoopy.Trace ;
import jsnoopy.parser.Parser;
import jsnoopy.parser.ParseException;
import jsnoopy.parser.TokenMgrError;


/**
 * Title:        JSnoopy
 * Description:  Regression testing based on event sequences.
 * Copyright:    Copyright (c) 2002
 * Company:      Memorial University of Newfoundland
 * @author Theodore S. Norvell
 * @version 1.0
 */

public class MainWindow extends JFrame {
    private String versionString = "JSnoopy 0.0 alpha" ;
    private DoneReplayDialog doneReplayDialog = new DoneReplayDialog(this) ;
    private CardLayout cardLayout1 = new CardLayout();
    private JPanel jPanel1 = new JPanel();
    private JButton recordButton = new JButton();
    private JButton stopButton = new JButton();
    private JButton replayButton = new JButton();

    private final int UNINIT=0, READY=1, RECORDING=2 ;
    private int state = UNINIT ;
    private Manager traceMan ;
    private Trace oldTrace ;
    private File currentFile ; // The file from which oldTrace was obtained.
    // Invariant (oldTrace==null)==(currentFile==null)
    private Trace newTrace ;
    JFileChooser fileChooser = new JFileChooser(".") ;
    private JPanel jPanel2 = new JPanel();
    private JButton optionsButton = new JButton();
    private OptionsDialog optionsDialog ;
    private TextEditor commentEditor ;

    public MainWindow(Manager traceMan) {
        this.traceMan = traceMan ;
        try {
            jbInit();
            finishInit() ;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void jbInit() throws Exception {
        this.getContentPane().setLayout(cardLayout1);
        recordButton.setToolTipText("Start recording events now.");
        recordButton.setActionCommand("record");
        recordButton.setText("Record");
        recordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                record();
            }
        });
        stopButton.setToolTipText("Stop recording events now.");
        stopButton.setText("Stop");
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stop();
            }
        });
        replayButton.setToolTipText("Compare current behaviour to past");
        replayButton.setText("Replay");
        replayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                replay();
            }
        });
        optionsButton.setToolTipText("Change options");
        optionsButton.setText("Options");
        optionsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayOptionsDialog(e);
            }
        });
        this.getContentPane().add(jPanel1, "jPanel1");
        jPanel1.add(recordButton, null);
        jPanel1.add(stopButton, null);
        jPanel1.add(replayButton, null);
        jPanel1.add(optionsButton, null);
        this.getContentPane().add(jPanel2, "jPanel2");
    }

    void enterState( int newState ) {
        switch( newState ) {
        case READY:
            newTrace = null ;
            oldTrace = null ;
            currentFile=null ;
            cardLayout1.show(this.getContentPane(), "jPanel1" );
            recordButton.setEnabled( true );
            stopButton.setEnabled( false );
            replayButton.setEnabled( true );
            setTitle( versionString ) ;
        break;
        case RECORDING:
            cardLayout1.show(this.getContentPane(), "jPanel1" );
            recordButton.setEnabled( false );
            stopButton.setEnabled( true );
            replayButton.setEnabled( false );
            setTitle( "RECORDING" ) ;
        break ;
        }
        state = newState ;
    }

    void finishInit() {
        setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
        this.optionsDialog = new OptionsDialog(this) ;
        this.commentEditor = new TextEditor(this) ;

        enterState( READY ) ;

        traceMan.addErrorListener(
            new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    recordingError( (Throwable) evt.getNewValue() ) ; } } ) ;

        pack() ;
        setLocation(0,0) ;
        setVisible( true ) ;
    }

    void record() {
        if( state==READY) {
            traceMan.startRecording();
            enterState( RECORDING ) ;
        }
    }

    void stop() {
        if( state==RECORDING ) {
            newTrace = traceMan.stopRecording();
            saveDialog( new File("./untitiled.jst") ) ;
            enterState( READY ) ;
        }
    }

    void replay() {
        if( state==READY ) {

            File file = openFileDialog( new File("") ) ;
            if( file != null ) {
                InputStream is ;
                try {
                    is = new FileInputStream(file) ; }
                catch( FileNotFoundException e ) {
                    JOptionPane.showMessageDialog(this,
                                "File not found: " + file,
                                "Error",
                                JOptionPane.ERROR_MESSAGE );
                    is = null ; }
                if( is != null ) {
                    boolean ok = true ;
                    try {
                        oldTrace = 	Parser.parseTrace( is );
                        currentFile = file ; }
                    catch( ParseException e ) {
                        JOptionPane.showMessageDialog(this,
                                    "Parse error: " + e.getMessage(),
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE );
                        ok = false ; }
                    catch( TokenMgrError e ) {
                        JOptionPane.showMessageDialog(this,
                                    "Lexer error: " + e.getMessage(),
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE );
                        ok = false ; }
                    if( ok ) {
                        int delay = optionsDialog.getSpeed() ;
                        traceMan.startRecording();
                        Trace tempTrace ;
                        try {
                            oldTrace.run( traceMan, delay ) ; }
                        catch( JSnoopyException e ) {
                            /** @todo Report the exception
                             *  Should send it to the manager.
                             *  Manager will alert listeners */
                             JOptionPane.showMessageDialog(this,
                                    "Injection error: " + e.getMessage(),
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE );
                             ok = false ; }
                        finally {
                            tempTrace = traceMan.stopRecording() ; }
                        if( ok ) {
                            newTrace = tempTrace ;
                            int firstDiff = newTrace.compare( oldTrace ) ;
                            boolean equal =  firstDiff == -1 ;
                            doneReplayDialog.setData(equal, newTrace, oldTrace );
                            doneReplayDialog.pack();
                            doneReplayDialog.setVisible( true );
                            enterState( READY ) ; } } } } }
    }

    boolean replaceOldTrace() {
        if( ! oldTrace.getComment().equals( newTrace.getComment() ) ) {

            String[] options = {"New Comment", "Old Comment", "Cancel"} ;
            int choice = JOptionPane.showOptionDialog(
                                this,
                                "Comment is different. Use new comment?",
                                "Comment changed",
                                JOptionPane.YES_NO_CANCEL_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                options,
                                options[0] ) ;
            if( choice == JOptionPane.CANCEL_OPTION ) {
                return false ; }
            else if( choice == JOptionPane.NO_OPTION ) {
                newTrace.setComment( oldTrace.getComment() ) ; } }

        try{
            saveTo( currentFile ) ; }
        catch( FileNotFoundException e ) {
            JOptionPane.showMessageDialog(this,
                                "Can not write file " + currentFile,
                                "Error",
                                JOptionPane.ERROR_MESSAGE );
            return false ; }
        catch( IOException e ) {
            JOptionPane.showMessageDialog(this,
                                "Error while writing file " + currentFile,
                                "Error",
                                JOptionPane.ERROR_MESSAGE );
            return false ; }
        return true ;
    }

    void saveDialog(File defaultFile)  {

        String[] options0 = {"Yes Edit Comment", "No Don't Edit Comment"} ;
        int choice = JOptionPane.showOptionDialog(
                                    this,
                                    "Would you like to edit the comment?",
                                    "Before you save it!",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE,
                                    null,
                                    options0,
                                    options0[1] ) ;
        if( choice == JOptionPane.YES_OPTION ) {
            displayCommentEditor() ; }

        /** @todo Add filtering */
        fileChooser.setSelectedFile( defaultFile );
        while( true ) {
            int returnVal = fileChooser.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                /** @todo Add a ".jst" suffix, if user supplies none. */
                boolean ok = true ;
                if( file.exists() ) {
                    String[] options1 = {"Yes", "No", "Discard Trace"} ;
                    choice = JOptionPane.showOptionDialog(
                                    this,
                                    "File exists. Overwrite file?",
                                    "Whoa Nelly!",
                                    JOptionPane.YES_NO_CANCEL_OPTION,
                                    JOptionPane.QUESTION_MESSAGE,
                                    null,
                                    options1,
                                    options1[0] ) ;
                    if( choice == JOptionPane.CANCEL_OPTION ) {
                        break ; }
                    else {
                        ok = choice == JOptionPane.YES_OPTION ; } }
                if( ok ) {
                    try{
                        saveTo( file ) ;
                        break ; }
                    catch( FileNotFoundException e ) {
                        JOptionPane.showMessageDialog(this,
                                "Can not write file " + file,
                                "Error",
                                JOptionPane.ERROR_MESSAGE ); }
                    catch( IOException e ) {
                        JOptionPane.showMessageDialog(this,
                                "Error while writing file " + file,
                                "Error",
                                JOptionPane.ERROR_MESSAGE ); } } }
            else {
                break ; } }
    }

    File openFileDialog(File defaultFile)  {
        fileChooser.setSelectedFile( defaultFile );
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return null ; }
        else {
            File file = fileChooser.getSelectedFile();
            /** @todo Add a ".jst" suffix, if user supplies none. */
            boolean ok = true ;
            if( ! file.exists() ) {
                JOptionPane.showMessageDialog(this,
                            "File does not exist: " + file,
                            "Error",
                            JOptionPane.ERROR_MESSAGE );
                return null ; }
            else {
                return file ; } }
    }

    void saveTo( File file )
    throws FileNotFoundException, IOException
    {
        PrintStream ps = new PrintStream( new FileOutputStream( file ) ) ;
        newTrace.printSelf( ps ) ;
    }

    private void recordingError( final Throwable t ) {
        Runnable popUpMessage = new Runnable() {
            public void run() {
                JOptionPane.showMessageDialog(MainWindow.this,
                                "Error while recording: " + t.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE ); } } ;

        SwingUtilities.invokeLater(popUpMessage);
    }

    void displayOptionsDialog(ActionEvent e) {
        optionsDialog.show() ;
    }

    void displayCommentEditor() {
        commentEditor.setText( newTrace.getComment() ) ;
        commentEditor.show() ;
        newTrace.setComment( commentEditor.getText() ) ;
    }
}