//     Copyright 1998--2010 Michael Bruce-Lockhart and Theodore S. Norvell
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License. 
// You may obtain a copy of the License at 
//
//     http://www.apache.org/licenses/LICENSE-2.0 
//
// Unless required by applicable law or agreed to in writing, 
// software distributed under the License is distributed on an 
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
// either express or implied. See the License for the specific language 
// governing permissions and limitations under the License.

package tm.interfaces;

import java.awt.Image;
import java.io.File ;

public interface ExternalCommandInterface extends StatusProducer {
    

    /** Turn on or off test mode. In test mode, user interaction (such as closing
     * dialog boxes) should not be required. The testing program should be able
     * to entirely control the TM via the external command interface.
     */
    void setTestMode( boolean turnOn ) ;
    
     /** Loads the given string into the TM and compiles it.
      * <P>
      * The language is determined from the file name.
      * @param fileName The name the string will be known as within the TM.
      * @param programSource The program text.
      */
    public void loadString( String fileName, String programSource);
    
    /** Load a remote file into the TM and compile it.
     * <P> 
     * First constructs a URL for a root directory.
     *  The root is used as a basis for imports in Java and includes in C++.
     *  First, the root URL is constructed as
     *  <pre> <b>new</b> URL( getDocumentBase(), root ) </pre>
     *  Second, a file named <code>fileName</code> is loaded relative to this root. 
     *  <p>
     *  So for example, if the class is <code>C</code> and the package is <code>p</code> and directory
     *  <code>p</code> is located
     * in directory <code>../examples</code> relative to the document base, we would call
     * <pre> tm.loadRemoteFile( "../examples", "p/C.java" )</pre
     * @param root The root directory. This URL should be relative to the document base
     * @param fileName The file name relative to the root directory.
     */
    public void loadRemoteFile( String root, String fileName ) ;
    
    /** Loads a file using getDocumentBase() as the root.
     * 
     * @param fileName The fileName relative to the documentBase of the applet.
     * @deprecated
     */
    @Deprecated
    public void loadRemoteFile( String fileName);
    
    /** Load a file from a local directory.
     */
   public void loadLocalFile( File directory, String fileName);

    /** Uses the Applet method documentBase() and the file name to construct a URL for
      *  the remote file:
      *       <kbd>new URL( this.getDocumentBase(), fileName )</kbd>
      *  Reads the configuration in that file. */
    public void readRemoteConfiguration( String fileName ) ;
    
    /** Register that a remote file is to be accessible as as a data file.
     * <p> The point of this is that when the TM runs as an Applet, you can't
     * use a file chooser and in any case the files are likely back on the server.
     * You can register any number of data files.
     * <ul>
     * <li> If no remote data files are on the list, then an ordinary
     * file chooser will be used.
     * <li> If there is only one file on the list, that file will be used.
     * <li> If more than one file is on the list, the user will be prompted to choose.
     * </ul>
     * <p>The URL for the data file is constructed as
     *       <kbd>new URL( this.getDocumentBase(), fileName )</kbd>
     * @param fileName
     */
    public void registerRemoteDataFile( String fileName ) ;
    
    /** Clear the list of remote data files.
     * @see registerRemoteDataFile
     */
    public void clearRemoteDataFiles() ;

    /** Adds input to the input buffer for standard input (cin) 
     * <p>It must be called after the program is loaded.*/
    public void addInputString( String input );
    
    /** Add a program argument.  This add a new argument to main.
     * <p>It must be called after the program is loaded, but before
     * it has started executing main. */
    public void addProgramArgument( String argument ) ;
    
    /** Get output */
    public String getOutputString( );
    //-------------------------------------------------------------------------

    /**  Restart the exection
      * Just like loadString, but recycling the previous string and language.
      * It is an error, if no previous load was done. */
    public void reStart();

    /** Start an editor to edit the current file. */
    public void editCurrentFile() ;
    
    /** Terminates the application. In the case of an applet,
     * it may only hide the applet's frame or it may do nothing at all. */
    public void quit();
    
    /** Initialize the state of an interpreted program.
      * This should only be called after all files are loaded.
      */
    public void initializeTheState() ;

   
    /** Undo the previous advance. */
    public void goBack();
    
    
    /** Advance a number of steps as specified by the following grammar.
     * There will be only one refresh and the whole command can be undone with
     * 1 "goBack" command.
     * <pre>
     *     command &rarr; WS [ simpleCommand WS [ ";" command ] ]
     *     simpleCommand &rarr; [integer WS "*" WS] primaryCommand 
     *     primaryCommand &rarr; "s" | "e" | "o" | "f" | "b" | "m"
     * </pre>
     * where WS is any number (0 of more) of white space characters.
     * The letters are interpreted as follows.
     *      <table>
     *        <tr><td>s</td><td> means step to next expression, even if it is
     *                           in a deeper Subroutine.</td></tr>
     *        <tr><td>e</td><td> means step to next Expression, stepping over
     *        					subroutine calls.</td></tr>
     *        <tr><td>o</td><td> means step Out of this subroutine invocation.
     *                           </td></tr>
     *        <tr><td>f</td><td> means step Forward to the next operation.
     *                           </td></tr>
     *        <tr><td>b</td><td> means step until the next Break point.
     *                           </td></tr>
     *        <tr><td>m</td><td> means Microstep -- the smallest possible step..
     *                           </td></tr>
     *       </table>
     *                 
     */
    public void go( String commandString ) ;
    
    /** Advance the state by one operation, such as a lookup, store, fetch,
     * or an arithmetic operation. */
    public void goForward();
    
    /** Advance by the tiniest possible amount. */
    public void microStep();
    
    /** Advance until the currently top subroutine returns. */
    public void overAll();  // change to stepOut

    /** Advance up to the start of the next expression at the same or a lower
     * subroutine level.
     */
    public void intoExp(); 
    
    /** Advance up to the start of the next expression. */
    public void intoSub();  // change to stepOver
    
    /** Step to the next break-point. */
    public void toBreakPoint() ;

    /** Execute until the given line number in the given file.
     * Line numbers are relative to the start of the
     * file <strong>and start with 1</strong>. Line numbers and
     * file names are prior to preprocessing.
     * 
     * @param fileName The file name as reported by cursorLineCoords.getFile().getFileName()
     * @param cursor The line number as reported by cursorLineCoords.getFile().getFileName()
     */
    public void toCursor( String fileName, int cursor );
    
    /** 
     * Execute the program from the current point until it ends, or a fault or a stop occurs.
     */
    public void autoRun();
    
    /**
     *  Set the visibility of the TM. If the visibility is off there will be no visible
     *  rendering of the Teaching Machine at all. Note that the TM may run considerably
     *  faster this way as there is no longer any need to paint the view every time the
     *  model undergoes a state change.
     */
    
    public void showTM(boolean visible);
    
    /**
     * Is the Teaching Machine currently visible?
     * @return the current visibility of the TM
     */
    public boolean isTMShowing();

    /**
     * Start automatic stepping and step until the end of the program or until
     * a stop event occurs. The stepping rate is controlled by a rate control
     * 
     * @see setAutoStepRate()
     */
    public void autoStep();
        
    /**
     * Start automatic stepping and step to the specified line number, unless a
     * stopAuto event or the end of the program occurs first.
     * @see #toCursor(String, int)
     * 
     * @param lineNo the line number counted from 1
     * @param fileName the file name as reported by
     */
    public void autoStep( String fileName, int lineNo );

  
//    /**
//     * Start automatic stepping and step to the named checkPoint, unless a
//     * stopAuto event or the end of the program occurs first.
//     * 
//     * @param checkPoint
//     */
//    public void autoStep(String checkPoint);

//    /**
//     * Backup the TM to the named checkPoint. Note to Theo. We are probably going
//     *  
//     * @param checkPoint the name of the checkPoint to which to backup
//     * @throws a (????) exception if there is no such checkPoint
//     */
//    public void backup(String checkPoint);
    
//    /**
//     * Backup the TM to the named checkPoint. Note to Theo. We probably need
//     * to figure out some way to start the process in a new direction.
//     * e.g. backup to checkPoint "Charlie" and and tell the program the backup
//     * came from reference point 3.
//     * 
//     * @param checkPoint the name of the checkPoint to which to backup
//     * @param from an arbitrary, non-negative integer defining the point
//     *        from which the backup occurred
//     * @throws a (????) exception if there is no such checkPoint
//     */    
//    public void backup(String checkPoint, int from);
    
    /**
     * Request the Teaching Machine to stop. The TM will stop at the next point it
     * is reasonable to do so.
     * <p>
     * If the TM is processing hidden code when the request is raised, it will stop
     * at the first reasonable point once it emerges into visible code.
     * <p>
     * If a stop occurs inside a function it is stepping over, it will stop at the
     * at the first reasonable point within the function
     * <p>
     * If the TM is visible, the standard control buttons will be enabled after the
     * stop whatever their state before it.
     */
    public void stopAuto();
        
    /**
     * Set the rate at which autostepping occurs where the rate is defined on
     * a arbitrary scale of 0 to 100 where 0 denotes the slowest rate and 100
     * the fastest.
     * 
     * @param rateConstant 0 <= the rate constant <=100
     */
    public void setAutoStepRate(int rateConstant);
    
//    /**
//     * Set the number of tics generated each autoStep. Each autoStep interval is divided
//     * into n uniform tic intervals. A tic is generated at each tic interval and a call
//     * advising its occurrence is sent  to each registered tic listener.
//     * <p>
//     * Note to Theo. This means you will also need to define a TicListener and have
//     * a registration capability.
//     * <p>
//     * <strong>Note:</strong> For simplicity, times do not include code execution times.
//     * Thus animations that utilize this facility will have to make every effort to
//     * run quickly or risk skewing the whole animation process, particularly at high tic
//     * rates.
//     * @param n the number of tics to be generated each autoStep. 0<= n <= 100
//     */
//    public void setNumberOfTics(int n);
         
    public Image getSnap(String plugIn, String id);
    public int getLastSnapWidth();
    public int getLastSnapHeight();
    public boolean getComparison(String plugIn, int n);
    public long getLocalInt(String datumName);
    public boolean isRunDone();

    /** Set the current selection. It should be a boolean expression.*/
    public void setSelectionString( String str ) ;
    
    /** Get the current selection. */
    public String getSelectionString( ) ;
    
    public static final String DEFAULT_SELECTION = "!L & !S" ;

    public static final String COMPLETE_SELECTION = "true" ;
}