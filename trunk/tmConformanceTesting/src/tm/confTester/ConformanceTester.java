/*
 * Created on Mar 28, 2005
 *
 */
package tm.confTester;

import java.awt.Color;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.* ;

import javax.swing.* ;
import javax.swing.text.* ;

/** Simple GUI for conformance tester.
 * @author theo
 *
 */
public class ConformanceTester extends JFrame {
    
    String versionString = "TM Conformance Tester v 0.0" ;
    JScrollPane logScrollPane = new JScrollPane( ) ;
    JTextPane logTextPane = new JTextPane();
    JToolBar statusBar = new JToolBar();
    JLabel rootLabel = new JLabel() ;
    JButton clearRootDirButton ;
    JButton setRootDirButton ;
    JButton loadButton  ;
    JButton clearButton ;
    JButton stopButton  ;
    JLabel statusPassesLabel = new JLabel() ;
    JLabel statusFailsLabel = new JLabel() ;
    JLabel statusMismatchLabel = new JLabel() ;
    JLabel statusErrorsLabel = new JLabel() ;
    JLabel statusNonteststLabel = new JLabel() ;
    Log log = new Log( logTextPane ) ;
    
    JFileChooser folderChooser = null ;
    JFileChooser fileChooser = null ;
    Thread workerThread = null ;
    
    CompilerAdapter currentCompilerAdapter = new TMCompilerAdapter() ;
    TestSupervisor testSupervisor = new TestSupervisor() ;
    TestHistory testHistory = new TestHistory(this) ;
    File rootDirectory = null ;
    private boolean[] poisonPill = new boolean[1] ;
    
    public static final Color DARKGREEN = new Color(0, 128, 0 ) ;
    public static final Color DARKRED = new Color(196, 0, 0 ) ;
    public static final Color DARKBLUE = new Color(0, 0, 196 ) ;
    public static final Color DARKORANGE = new Color( 128, 48, 0 ) ;
    
    
    ConformanceTester( ) {
        JPanel contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        this.setSize(new Dimension(550, 550));
        this.setTitle(versionString);
        
        JToolBar toolBar = new JToolBar();
        contentPane.add( toolBar, BorderLayout.NORTH ) ;
        
        loadButton = new JButton("Load File") ;
        ActionListener loadAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadFile() ; }} ;
        loadButton.addActionListener( loadAction ) ;
        toolBar.add(loadButton) ;
        
        setRootDirButton = new JButton( "Set Root" ) ;
        setRootDirButton.addActionListener( new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                setRootDirectory() ; } } ) ;
        toolBar.add( setRootDirButton ) ; 
        
        clearRootDirButton = new JButton( "Clear Root" ) ;
        clearRootDirButton.addActionListener( new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                clearRootDirectory() ; } } ) ;
        clearRootDirButton.setEnabled( false ) ;
        toolBar.add( clearRootDirButton ) ; 
        
        clearButton = new JButton( "Clear" ) ;
        clearButton.addActionListener( new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                clear() ; } } ) ;
        toolBar.add( clearButton ) ; 
        
        stopButton = new JButton( "Stop" ) ;
        stopButton.addActionListener( new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                stopWorker() ; } } ) ;
        stopButton.setEnabled( false ) ;
        toolBar.add( stopButton ) ; 
        
        
        contentPane.add( statusBar, BorderLayout.SOUTH ) ;
        statusBar.add( statusPassesLabel ) ;
        statusBar.add( statusMismatchLabel ) ;
        statusBar.add( statusFailsLabel ) ;
        statusBar.add( statusErrorsLabel ) ;
        statusBar.add( statusNonteststLabel ) ;
        statusBar.add( rootLabel ) ;
        statusPassesLabel.setForeground( DARKGREEN ) ;
        statusMismatchLabel.setForeground( DARKORANGE ) ;
        statusFailsLabel.setForeground( DARKRED ) ;
        statusErrorsLabel.setForeground( DARKBLUE ) ;
        statusNonteststLabel.setForeground( Color.BLACK ) ;
        updateStatus() ;
        
        
        contentPane.add( logScrollPane, BorderLayout.CENTER ) ;
        logScrollPane.getViewport().add(logTextPane, null);
        
        this.setDefaultCloseOperation( EXIT_ON_CLOSE ) ;
        
    }
    
    synchronized private void clear() {
        log.clearScreen() ;
        testHistory.reset() ;
    }

    /**
     * 
     */
    synchronized private void clearRootDirectory() {
        rootDirectory = null ;
        clearRootDirButton.setEnabled( false ) ;
        updateStatus() ;
    }

    /**
     * 
     */
    synchronized private void setRootDirectory() {
        if( folderChooser == null ) {
            folderChooser = new JFileChooser() {
                private static final long serialVersionUID = 1541813407103968848L;

                // Work around slowness on Windows XP. See http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6372808
                @Override
                public void updateUI() {
                    putClientProperty("FileChooser.useShellFolder", Boolean.FALSE);
                    super.updateUI();
                }
            };
            folderChooser.setDialogTitle( "Set root Directory" ) ;
            folderChooser.setCurrentDirectory( new File(".") ) ;
            folderChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY) ;
        }
        int status = folderChooser.showDialog(this, "Select as root" ) ;
        if( status == JFileChooser.APPROVE_OPTION ) {
            rootDirectory = folderChooser.getSelectedFile() ;
            clearRootDirButton.setEnabled( true ) ;
            prepareFileChooser() ;
            fileChooser.setCurrentDirectory( rootDirectory ) ;
            updateStatus() ; }
    }
    
    synchronized void updateStatus( ) {
        statusPassesLabel.setText( "Passes: "+ testHistory.getCount(StatusCodes.PASS) + "  " ) ;
        statusMismatchLabel.setText( "Mismatches: "+testHistory.getCount(StatusCodes.MISMATCH) + "  "  ) ;
        statusFailsLabel.setText( "Failures: "+testHistory.getCount(StatusCodes.FAIL) + "  "  ) ;
        statusErrorsLabel.setText( "Errors: "+ testHistory.getCount(StatusCodes.ERROR) + "  "  ) ;
        statusNonteststLabel.setText( "Nontests: "+ testHistory.getCount(StatusCodes.NONTEST) + "  "  ) ;
        if( rootDirectory == null ) 
            rootLabel.setText("") ;
        else
            rootLabel.setText("Root is "+rootDirectory.getAbsolutePath() ) ;
        statusBar.validate() ;
    }
    
    private void prepareFileChooser() {

        if( fileChooser == null ) {
            fileChooser = new JFileChooser() {
                private static final long serialVersionUID = 1541813407103968848L;

                // Work around slowness on Windows XP. See http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6372808
                @Override
                public void updateUI() {
                    putClientProperty("FileChooser.useShellFolder", Boolean.FALSE);
                    super.updateUI();
                }
            };
            fileChooser.setDialogTitle( "Select File or Directory" ) ;
            fileChooser.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES) ;
            fileChooser.setMultiSelectionEnabled( false ) ;
            fileChooser.setCurrentDirectory( new File(".") ) ;
        }
    }
    
    synchronized private void loadFile() {
        try {
            prepareFileChooser() ;
            int status = fileChooser.showDialog(this, "Test" ) ;
            if( status == JFileChooser.APPROVE_OPTION ) {
                File selection = fileChooser.getSelectedFile() ;
                if( selection != null ) {
                    startWorker( rootDirectory, selection ) ; } } }
        catch( Throwable e) {
            putln( "Could not open file", Color.BLACK ) ;
            e.printStackTrace( System.err ) ;  }
    }
    
    private void startWorker( File root, File selection ) {
        enterWorkingMode() ;
        poisonPill[0] = false ;
        workerThread = new WorkerThread( this, root, selection, testSupervisor,
                testHistory, currentCompilerAdapter, poisonPill ) ;
        workerThread.start() ;
    }    
    
    synchronized void putln( String message, Color color ) {
        log.println( message, color ) ;
    }

    private void stopWorker() {
        putln( "Attempting to stop.", DARKBLUE ) ;
        poisonPill[0] = true ; }

    synchronized void enterWorkingMode() {
        stopButton.setEnabled( true ) ;
        loadButton.setEnabled( false ) ; }

    synchronized void exitWorkingMode() {
        stopButton.setEnabled( false ) ;
        loadButton.setEnabled( true ) ; }
        
    
    public static void main(String[] args) {
        // Create the frame

        ConformanceTester frame = new ConformanceTester() ;
        frame.validate();
        
        //      Center the window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) {
          frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
          frameSize.width = screenSize.width;
        }
        frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        frame.setVisible(true);
        
        // Deal with the args if any.
        
        File root ; // Absolute path of directory
        File selection ; // Relative path of file.
        if( args.length == 2 ) {
            root = (new File( args[0] )).getAbsoluteFile() ;
            String fileName = args[1] ;
            selection = new File( root, fileName ) ;
            if( !root.isDirectory() ) {
                System.err.println( root + " is not a directory" ) ;
                System.exit(1) ; } }
        else if( args.length == 1 ) {
            File fullPath = new File( args[0] ).getAbsoluteFile() ;
            if( fullPath.isDirectory() ) {
                root = null ;
                selection = fullPath ; }
            else {
                String directoryString = fullPath.getParent() ;
                if( directoryString == null ) {
                    System.err.println( directoryString + " is not a directory" ) ;
                    System.exit(1) ; }
                root = null ;
                selection = fullPath ; } }
        else {
            root = null ;
            selection = null ; }
        
        if( root != null && selection != null ) {
            frame.startWorker( root, selection ) ; }
    }
}
