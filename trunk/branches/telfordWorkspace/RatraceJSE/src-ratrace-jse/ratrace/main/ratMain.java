package ratrace.main;

import ratRace.RatRace;
import telford.common.Kit;
import telford.jse.KitJSE;

public class ratMain{
	
	public static void main(String[] args) {
		Kit.setKit( new KitJSE() ) ;
		javax.swing.SwingUtilities.invokeLater(
			new Runnable() {
				
				@Override public void run() {
					new RatRace() ;
				}
			} ) ;
}
}