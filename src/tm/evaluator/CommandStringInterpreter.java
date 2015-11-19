package tm.evaluator;

import tm.utilities.Assert;

public class CommandStringInterpreter {

	private InputString input;
	private CommandsI commands;

	public CommandStringInterpreter( String commandString, CommandsI commands ) {
		this.input = new InputString( commandString ) ;
		this.commands = commands ;
	}
	
	public void interpretGoCommand( ) {
		skipWhiteSpace() ;
		if( input.empty() ) return ;
		boolean ok = interpretCommand(  ) ; 
		if( !ok ) {
			Assert.runTimeError( "Could not interpret argument to 'go' command <"
				+input.str+ ">. Failed at position " +input.cursor+ "." ) ; 
			return ;}
		skipWhiteSpace() ;
		if( input.empty() ) return ;
		if(input.next() == ';'  ) {
			input.advance() ; 
			interpretGoCommand() ; }
		else {
			Assert.runTimeError( "Could not interpret argument to 'go' command <"
					+input.str+ ">. Failed at position " +input.cursor+ "." ) ;  }
	} 
    
    private boolean interpretCommand( ) {
    	// Precondition input is not empty.
    	int reps = 0 ;
    	boolean hasReps = false ;
    	while( ! input.empty() ) {
    		char digit = input.next() ;
    		if( !( '0' <= digit && digit <= '9' ) ) break ;
    		hasReps = true ;
    		reps = reps*10 + (digit-'0') ;
    		input.advance() ; }
    	if( !hasReps ) reps = 1 ;
    	skipWhiteSpace() ;
    	if( input.empty() ) return false ;
    	if( hasReps ) {
    		if( input.next() == '*' ) {
    			input.advance();
    			skipWhiteSpace() ;
    			if( input.empty() ) return false ; }
    		else return false ;
    	}
    	// assert !input.isEmpty() ;
    	switch( Character.toLowerCase( input.next() ) ) {
    	case 's' :
    		input.advance() ;
    		for( int i=0 ; i<reps ; ++i)
    			commands.intoSubCommand() ;
    		return true ;
    	case 'e' : 
    		input.advance() ;
    		for( int i=0 ; i<reps ; ++i)
    			commands.intoExpCommand()  ;
    		return true ;
    	case 'o' : 
    		input.advance() ;
    		for( int i=0 ; i<reps ; ++i)
    			commands.overAllCommand()  ;
    		return true ;
    	case 'f' : 
    		input.advance() ;
    		for( int i=0 ; i<reps ; ++i)
    			commands.goForwardCommand()  ;
    		return true ;
    	case 'b' : 
    		input.advance() ;
    		for( int i=0 ; i<reps ; ++i)
    			commands.toBreakPointCommand()  ;
    		return true ;
    	case 'm' : 
    		input.advance() ;
    		for( int i=0 ; i<reps ; ++i)
    			commands.microStepCommand() ;
    		return true ;
    	default: 
    		return false ; }
    }
    
    private void skipWhiteSpace() {
    	while(!input.empty() && Character.isWhitespace( input.next() ) )
    		input.advance() ;
    }
    
	public interface CommandsI {
		void intoSubCommand();
		void intoExpCommand();
		void overAllCommand();
		void goForwardCommand();
		void toBreakPointCommand();
		void microStepCommand();
	}
    
    private class InputString {
    	String str ;
    	int cursor = 0 ;
    	InputString( String str ) { this.str = str ; }
    	boolean empty() { return cursor == str.length() ; }
    	char next() { Assert.check( cursor < str.length()) ; return str.charAt(cursor) ; }
    	void advance() { Assert.check( cursor < str.length()) ; ++cursor ; }
    }
}
