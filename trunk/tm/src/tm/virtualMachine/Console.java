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

package tm.virtualMachine;

import tm.backtrack.BTTimeManager;
import tm.backtrack.BTVar;
import tm.backtrack.BTVector;
import tm.interfaces.StateInterface ;

/** The Console class represents standard input and output.

*/
public class Console {

	BTVector outputLines ;  // Vector of strings -- Output only
    BTVector consoleLines ;  // Vector of strings -- Output + echoed input 
	BTVar inputString ; // A String
	BTVar inputPosition ; // An Integer
	
	BTVar inputState ;      // An Integer
	public static final int INIT_STATE = 0 ;
	public static final int EOFBIT = 1, FAILBIT = 2, BADBIT = 4 ;

	Console(BTTimeManager timeMan) {
		outputLines = new BTVector( timeMan ) ; 
        outputLines.addElement("") ;
        
        consoleLines = new BTVector( timeMan ) ; 
        consoleLines.addElement("") ;
		
        inputString = new BTVar( timeMan ) ;
		inputString.set("") ; 
		
        inputState = new BTVar( timeMan ) ;
		inputState.set( new Integer( INIT_STATE ) ) ;
		
        inputPosition = new BTVar( timeMan ) ;
		inputPosition.set( new Integer(0) ) ;
    }

	/** Size in lines */
	public int getConsoleSize() {
		return getSize( consoleLines ) ; }

	/** A line (counting from 0 ) */
	public String getConsoleLine(int i) {
		return getLine( i, consoleLines ) ; }

    /** Size in lines */
    public int getOutputSize() {
        return getSize( outputLines ) ; }

    /** A line (counting from 0 ) */
    public String getOutputLine(int i) {
        return getLine( i, outputLines ) ; }
    
    public void putString( String message ) {
        for( char c : message.toCharArray() ) { putchar(c) ;  }
    }
    
    public void putchar( char ch ) {
        putchar( ch, outputLines ) ;
        putchar( ch, consoleLines ) ; }
    
    /** Add input to the input stream.
    If there is an EOF entered, it should be marked by a 0 and be the
    last character in the input string.
    */
    public void addInputString( String input ) {
        String oldInput = (String) inputString.get() ;
        String newInput = oldInput + input ;
        inputString.set( newInput ) ;
        
        // Echo
        putchar( StateInterface.INPUT_MARK, consoleLines );
        for( int i=0, sz=input.length() ; i < sz ; ++i ) {
            if( input.charAt(i) == '\0' ) {
                // Char 0 represents the end of file.
                putchar( 'E', consoleLines );
                putchar( '0', consoleLines ) ;
                putchar( 'F', consoleLines ) ; }
            else {
                putchar( input.charAt(i), consoleLines ) ; } }
         putchar(StateInterface.NORMAL_MARK, consoleLines);}
    
    public void setFailBit( ) {
        Integer i = (Integer) inputState.get() ;
        inputState.set( new Integer(i.intValue() | FAILBIT)) ; }
        
    public boolean fail() {
        Integer i = (Integer) inputState.get() ;
        return 0 != (i.intValue() & (FAILBIT | BADBIT)); }
    
    public void setEOFBit( ) {
        Integer i = (Integer) inputState.get() ;
        inputState.set( new Integer(i.intValue() | EOFBIT)) ; }
        
    public boolean eof() {
        Integer i = (Integer) inputState.get() ;
        return 0 != (i.intValue() & EOFBIT); }
    
    public char peekChar(int i) {
        /* peekChar(0) returns the next character of input.
           peekChar(1) returns the one after that, etc.
           If a character is the end-of-file, then '\0' is returned.
           If the peek could not succeed because more input is required from the
           user, then '\uffff' is returned.
           Precondition: i must be small enough that it does not pass over the end of file.
           This means that you should not peek at i+1 before peeking at i, since character i
           may be the end of file.
           Peeking at the eof char causes the EOFBIT to be set.
        */
        String inputStringValue = (String) inputString.get() ;
        int currentPosition = ((Integer) inputPosition.get()).intValue() ;
        int peekPosition = currentPosition + i ;
        if( inputStringValue.length() <= peekPosition ) {
            // Use the biggest char to indicate a failure.
            return '\uffff' ; }
        else {
            char result = inputStringValue.charAt( peekPosition ) ;
            if( result == '\0' ) setEOFBit() ;
            return result ; } }
        
    public void consumeChars(int count) {
        /* Advance the position in the input by count characters.
           Or until the current character is the end of file marker.
        */
        for(int i = 0 ; i < count ; ++ i ) {
            if( peekChar(0) == '\0' ) {
                // Attempt to read past the end of file.
                setFailBit() ;
                break ; }
            else {
                int currentPosition = ((Integer) inputPosition.get()).intValue() ;
                inputPosition.set( new Integer( currentPosition + 1 ) ) ; } } }
    

    
    private int getSize( BTVector where ) {
        return where.size() ; }
    
    private String getLine( int i, BTVector where ) {
        return (String) where.elementAt( i ) ; }

    /** Output a character to a Vector of strings */
    private void putchar(char c, BTVector where) {
        if( c == '\n' ) {
            where.addElement("") ; }
        else if( (int)c==9 || 32 <= (int)c && (int)c <= 126 || c >=  StateInterface.INPUT_MARK) {
            // Tabs are taken care of at a lower level.
            // Char 7 ought to cause a BEEP, but how?
            int lastLine = getSize(where) - 1 ;
            String newString = getLine( lastLine, where ) + c ;
            where.removeElementAt( lastLine ) ;
            where.addElement( newString ) ; } }

}