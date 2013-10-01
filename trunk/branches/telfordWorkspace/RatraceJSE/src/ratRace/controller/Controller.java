package ratRace.controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ratRace.model.Model;
import ratRace.view.ViewI;
import ratRace.view.ViewListenerI;

/** The controller controls the the model (and possibly the view). */
public class Controller implements ViewListenerI {
	
	//private ViewI view ;
	private Model model ;	
	
	
	public Controller( Model model, ViewI view ) {
		this.model = model ;
		//this.view = view ;
		view.addListener(this) ;
	}

	@Override
	public void mouseMovedTo(double i, double j) {
		model.pointRatAt(i, j) ;
	}
	
	@Override
	public void pulse() {
		model.pulse() ;
	}
}
