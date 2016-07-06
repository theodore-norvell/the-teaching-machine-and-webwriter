package tm.portableDisplays;

public interface SuperSourceCoords {
	public SuperTMFile getFile();

    /** Line number counting from 1. */
    public int getLineNumber();

    public boolean equals( Object other );
}
