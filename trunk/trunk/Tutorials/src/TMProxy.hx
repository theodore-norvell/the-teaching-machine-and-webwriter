package ;

/**
 * ...
 * @author Theodore Norvell
 */


class TMProxy implements TMExternalCommandInterface {
	
	var theTM : TMExternalCommandInterface ;
	public var replaying : Bool;
	public var count : Int;
	
	public function new( applet : TMExternalCommandInterface) {
		theTM = applet ;
		count = 0;
		replaying = false;
	}
	
	
    public function loadString( fileName : String, programSource: String ) : Void {
		 theTM.loadString( fileName, programSource ) ;
		 if (!replaying && count>=0)
			count = -2000;
	}
    
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
    public function loadRemoteFile( root : String, fileName : String ) : Void {
		Main.print( "proxied call to loadRemoteFile" );
		theTM.loadRemoteFile( root, fileName ) ;
		if (!replaying && count>=0)
			count = -2000;
		Main.print("back from loadRemoteFile");
	}

    /** Uses the Applet method documentBase() and the file name to construct a URL for
      *  the remote file:
      *       <kbd>new URL( this.getDocumentBase(), fileName )</kbd>
      *  Reads the configuration in that file. */
    public function readRemoteConfiguration( fileName : String ) : Void {
		theTM.readRemoteConfiguration( fileName ) ;
		if (!replaying && count>=0)
			count = -2000;
	}
    
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
    public function registerRemoteDataFile( fileName : String ) : Void  {
		theTM.registerRemoteDataFile( fileName ) ;
		if (!replaying && count>=0)
			count = -2000;
	}
    
    /** Clear the list of remote data files.
     * @see registerRemoteDataFile
     */
    public function clearRemoteDataFiles() : Void  {
		theTM.clearRemoteDataFiles() ;
		if (!replaying && count>=0)
			count = -2000;
	}

    /** Adds input to the input buffer for standard input (cin) 
     * <p>It must be called after the program is loaded.*/
    public function addInputString( input : String ) : Void  {
		theTM.addInputString( input ) ;
		if (!replaying && count>=0)
			count = -2000;
	}
    
    /** Add a program argument.  This add a new argument to main.
     * <p>It must be called after the program is loaded, but before
     * it has started executing main. */
    public function addProgramArgument( argument : String ) : Void {
		theTM.addProgramArgument( argument ) ;
		if (!replaying && count>=0)
			count = -2000;
	}
    
    /** Get output */
    public function getOutputString( ) : String {
		return theTM.getOutputString() ;
		if (!replaying && count>=0)
			count = -2000;
	}
    //-------------------------------------------------------------------------

    /**  Restart the exection
      * Just like loadString, but recycling the previous string and language.
      * It is an error, if no previous load was done. */
    public function reStart() : Void {
		theTM.reStart() ;
		if (!replaying && count>=0)
			count = -2000;
	}

    /** Start an editor to edit the current file. */
    public function editCurrentFile() : Void {
		theTM.editCurrentFile() ;
		if (!replaying && count>=0)
			count = -2000;
	}
    
    /** Terminates the application. In the case of an applet,
     * it may only hide the applet's frame or it may do nothing at all. */
    public function quit() : Void {
		theTM.quit() ;
		if (!replaying && count>=0)
			count = -2000;
	}
    
    /** Initialize the state of an interpreted program.
      * This should only be called after all files are loaded.
      */
    public function initializeTheState() : Void {
		theTM.initializeTheState() ;
		if (!replaying && count>=0)
			count = -2000;
	}
    
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
    public function go( commandString : String ) : Void {
		theTM.go( commandString ) ;
		if (!replaying && count>=0)
			count++;
	}
    /** Undo the previous advance. */
    public function goBack() : Void {
		theTM.goBack() ;
		if (!replaying && count>=0)
			count--;
	}
    
    /** Advance the state by one operation, such as a lookup, store, fetch,
     * or an arithmetic operation. */
    public function goForward() : Void {
		theTM.goForward() ;
		if (!replaying && count>=0)
			count++;
	}
    
    /** Advance by the tiniest possible amount. */
    public function microStep() : Void {
		theTM.microStep() ;
		if (!replaying && count>=0)
			count++;
	}
    
    /** Advance until the currently top subroutine returns. */
    public function overAll() : Void {
		theTM.overAll() ;
		if (!replaying && count>=0)
			count++;
	}

    /** Advance up to the start of the next expression at the same or a lower
     * subroutine level.
     */
    public function intoExp() : Void {
		theTM.intoExp() ;
		if (!replaying && count>=0)
			count++;
	}
    
    /** Advance up to the start of the next expression. */
    public function intoSub() : Void {
		theTM.intoSub() ;
		if (!replaying && count>=0)
			count++;
	}
    
    /** Step to the next break-point. */
    public function toBreakPoint() : Void {
		theTM.toBreakPoint() ;
		if (!replaying && count>=0)
			count++;
	}

    /** Execute until the given line number in the given file.
     * Line numbers are relative to the start of the
     * file <strong>and start with 1</strong>. Line numbers and
     * file names are prior to preprocessing.
     * 
     * @param fileName The file name as reported by cursorLineCoords.getFile().getFileName()
     * @param cursor The line number as reported by cursorLineCoords.getFile().getFileName()
     */
    public function toCursor(  fileName : String, cursor : Int ) : Void {
		theTM.toCursor( fileName, cursor ) ;
		if (!replaying && count>=0)
			count++;
	}
    
    /** 
     * Execute the program from the current point until it ends, or a fault or a stop occurs.
     */
    public function autoRun() : Void {
		theTM.autoRun() ;
		if (!replaying && count>=0)
			count = -2000;
	}
    
    /**
     *  Set the visibility of the TM. If the visibility is off there will be no visible
     *  rendering of the Teaching Machine at all. Note that the TM may run considerably
     *  faster this way as there is no longer any need to paint the view every time the
     *  model undergoes a state change.
     */
    
    public function showTM( visible : Bool) : Void {
		theTM.showTM( visible ) ;
		if (!replaying && count>=0)
			count = -2000;
	}
    
    /**
     * Is the Teaching Machine currently visible?
     * @return the current visibility of the TM
     */
    public function isTMShowing() : Bool {
		return theTM.isTMShowing() ;
	}

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
	public function autoStep( ? fileName : String, ? lineNo : Int ) : Void {
		if ( fileName == null ) {
			theTM.autoStep() ; }
		else {
			theTM.autoStep( fileName, lineNo ) ; }
		if (!replaying && count>=0)
			count = -2000;
	}

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
    public function stopAuto() : Void {
		theTM.stopAuto() ;
		if (!replaying && count>=0)
			count = -2000;
	}
        
    /**
     * Set the rate at which autostepping occurs where the rate is defined on
     * a arbitrary scale of 0 to 100 where 0 denotes the slowest rate and 100
     * the fastest.
     * 
     * @param rateConstant 0 <= the rate constant <=100
     */
    public function setAutoStepRate( rateConstant : Int ) : Void {
		theTM.setAutoStepRate( rateConstant ) ;
		if (!replaying && count>=0)
			count = -2000;
	}
         
    public function getSnap( plugIn : String, id : String) : Image {
		return theTM.getSnap( plugIn, id ) ;
	}
	
    public function getLastSnapWidth() : Int {
		return theTM.getLastSnapWidth() ;
	}
	
    public function getLastSnapHeight() : Int {
		return theTM.getLastSnapHeight() ;
	}
	
    public function getComparison( plugIn : String, n : Int) : Bool {
		return theTM.getComparison( plugIn, n ) ;
	}
	
    public function getLocalInt( datumName : String ) : Int {
		return theTM.getLocalInt( datumName ) ;
	}
	
    public function isRunDone() : Bool {
		return theTM.isRunDone() ; 
	}

    /** Set the current selection. It should be a boolean expression.
	 * For default use "!L & !S". To select all lines, use "true". */
    public function setSelectionString( str : String ) : Void {
		theTM.setSelectionString( str ) ;
		if (!replaying && count>=0)
			count = -2000;
	}
    
    /** Get the current selection. */
    public function getSelectionString( ) : String {
		return theTM.getSelectionString() ;
	}
	
}