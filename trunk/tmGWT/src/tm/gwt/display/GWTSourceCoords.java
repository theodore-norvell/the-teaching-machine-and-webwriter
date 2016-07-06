package tm.gwt.display;

import tm.portableDisplays.SuperSourceCoords;
import tm.portableDisplays.SuperTMFile;

public class GWTSourceCoords implements SuperSourceCoords {

	private int lineNumber;

	// public static final SourceCoords UNKNOWN ;
	// static {
	// StringFileSource fs = new StringFileSource() ;
	// fs.addString( "unknown", "\n" ) ;
	// TMFile file = new TMFile( fs, "unknown" ) ;
	// UNKNOWN = new SourceCoords( file, 0 ) ; }

	public GWTSourceCoords(int ln) {
		this.lineNumber = ln;
	}

	/** The TMFile. May be hashed or tested for equality. */
	public SuperTMFile getFile() {
		return null;
	}

	/** Line number counting from 1. */
	public int getLineNumber() {
		return lineNumber;
	}

	public String toString() {
		return "lineNumber is: " + lineNumber;
	}

	public boolean equals(Object other) {
		if (!(other instanceof GWTSourceCoords))
			return false;
		GWTSourceCoords otherSourceCoords = (GWTSourceCoords) other;
		return lineNumber == otherSourceCoords.lineNumber;
	}
}