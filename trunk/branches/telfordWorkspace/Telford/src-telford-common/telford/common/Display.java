package telford.common;

public interface Display {
		
	public void setRooot ( Root root ) ;
	
	public Root getRoot () ; 
	
	public void setPreferredSize (int width, int height) ;

	public void add(Container toolBar, Object north);
	
}
