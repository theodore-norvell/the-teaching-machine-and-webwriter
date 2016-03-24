package ratRace;

import telford.common.Kit;
import telford.jse.KitJSE;

public class Main {

	public static void main(String[] args) {
		Kit.setKit( new KitJSE() ) ;
		javax.swing.SwingUtilities.invokeLater(
			new Runnable() {

				@Override
				public void run() {
					RatRace ratRace = new RatRace() ;
				}} ) ;
	}

}
