package tm.interfaces;


public interface SourceCoordsI {
    
	public TMFileI getFile();

    /** Line number counting from 1. */
    public int getLineNumber();

    public boolean equals( Object other );
}
