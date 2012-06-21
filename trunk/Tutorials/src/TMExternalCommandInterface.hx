package ;

/**
 * ...
 * @author Theodore Norvell
 */


interface TMExternalCommandInterface {
     /** Loads the given string into the TM and compiles it.
      * <P>
      * The language is determined from the file name.
      * @param fileName The name the string will be known as within the TM.
      * @param programSource The program text.
      */
	 public function loadString( fileName : String, programSource: String ) : Void ;
    
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
    public function loadRemoteFile( root : String, fileName : String ) : Void ;

    /** Uses the Applet method documentBase() and the file name to construct a URL for
      *  the remote file:
      *       <kbd>new URL( this.getDocumentBase(), fileName )</kbd>
      *  Reads the configuration in that file. */
    public function readRemoteConfiguration( fileName : String ) : Void ;
    
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
    public function registerRemoteDataFile( fileName : String ) : Void  ;
    
    /** Clear the list of remote data files.
     * @see registerRemoteDataFile
     */
    public function clearRemoteDataFiles() : Void  ;

    /** Adds input to the input buffer for standard input (cin) 
     * <p>It must be called after the program is loaded.*/
    public function addInputString( input : String ) : Void  ;
    
    /** Add a program argument.  This add a new argument to main.
     * <p>It must be called after the program is loaded, but before
     * it has started executing main. */
    public function addProgramArgument( argument : String ) : Void ;
    
    /** Get output */
    public function getOutputString( ) : String ;
    //-------------------------------------------------------------------------

    /**  Restart the exection
      * Just like loadString, but recycling the previous string and language.
      * It is an error, if no previous load was done. */
    public function reStart() : Void ;

    /** Start an editor to edit the current file. */
    public function editCurrentFile() : Void ;
    
    /** Terminates the application. In the case of an applet,
     * it may only hide the applet's frame or it may do nothing at all. */
    public function quit() : Void ;
    
    /** Initialize the state of an interpreted program.
      * This should only be called after all files are loaded.
      */
    public function initializeTheState() : Void ;

    /** Undo the previous advance. */
    public function goBack() : Void ;
    
    /** Advance the state by one operation, such as a lookup, store, fetch,
     * or an arithmetic operation. */
    public function goForward() : Void ;
    
    /** Advance by the tiniest possible amount. */
    public function microStep() : Void ;
    
    /** Advance until the currently top subroutine returns. */
    public function overAll() : Void ;  // change to stepOut

    /** Advance up to the start of the next expression at the same or a lower
     * subroutine level.
     */
    public function intoExp() : Void ; 
    
    /** Advance up to the start of the next expression. */
    public function intoSub() : Void ;  // change to stepOver

    /** Execute until the given line number in the given file.
     * Line numbers are relative to the start of the
     * file <strong>and start with 1</strong>. Line numbers and
     * file names are prior to preprocessing.
     * 
     * @param fileName The file name as reported by cursorLineCoords.getFile().getFileName()
     * @param cursor The line number as reported by cursorLineCoords.getFile().getFileName()
     */
    public function toCursor(  fileName : String, cursor : Int ) : Void ;
    
    /** 
     * Execute the program from the current point until it ends, or a fault or a stop occurs.
     */
    public function autoRun() : Void ;
    
    /**
     *  Set the visibility of the TM. If the visibility is off there will be no visible
     *  rendering of the Teaching Machine at all. Note that the TM may run considerably
     *  faster this way as there is no longer any need to paint the view every time the
     *  model undergoes a state change.
     */
    
    public function showTM( visible : Bool) : Void ;
    
    /**
     * Is the Teaching Machine currently visible?
     * @return the current visibility of the TM
     */
    public function isTMShowing() : Bool ;

     /**
     * Start automatic stepping and step to the specified line number, unless a
     * stopAuto event or the end of the program occurs first.
     * @see #toCursor(String, int)
     * 
     * @param lineNo the line number counted from 1
     * @param fileName the file name as reported by
	 * 
	 * If one parameter is missing, both should be missing. In that case setpping continues until
	 * the end of the program or a stopAuto command is sent.
     */
	public function autoStep( ? fileName : String, ? lineNo : Int ) : Void ;

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
    public function stopAuto() : Void;
        
    /**
     * Set the rate at which autostepping occurs where the rate is defined on
     * a arbitrary scale of 0 to 100 where 0 denotes the slowest rate and 100
     * the fastest.
     * 
     * @param rateConstant 0 <= the rate constant <=100
     */
    public function setAutoStepRate( rateConstant : Int ) : Void;
         
    public function getSnap( plugIn : String, id : String) : Image;
    public function getLastSnapWidth() : Int ;
    public function getLastSnapHeight() : Int ;
    public function getComparison( plugIn : String, n : Int) : Bool;
    public function getLocalInt( datumName : String ) : Int ;
    public function isRunDone() : Bool;

    /** Set the current selection. It should be a boolean expression.
	 * For default use "!L & !S". To select all lines, use "true". */
    public function setSelectionString( str : String ) : Void ;
    
    /** Get the current selection. */
    public function getSelectionString( ) : String ;
	
}