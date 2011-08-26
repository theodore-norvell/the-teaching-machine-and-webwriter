package mmgood;

import java.awt.Cursor;

public interface StatusI {
	
	public void setStatusMessage( final String pMessage ) ;
	
	public void popupWarning( final String pTitle, final String pMessage ) ;
	
	public void setCursor( Cursor pCursor ) ;
}
