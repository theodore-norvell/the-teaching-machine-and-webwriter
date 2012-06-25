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


package higraph.swing;

import higraph.view.HigraphView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/** Animate one or more HigraphViews */
public class Animator {
	
	final int FRAME_TIME_ms = 40 ;
	double degree ;
	double delta ;
	
	final HigraphView<?,?,?,?,?,?,?> view ;
	
	ActionListener listener = new ActionListener() {
		@Override public void actionPerformed(ActionEvent ev) {
			degree += delta ;
			if( degree < 1.0 ) {
				view.advanceTransition(degree) ;
			} else {
				view.finishTransition() ; 
				timer.stop() ; } } } ;
	
	Timer timer = new Timer(FRAME_TIME_ms, listener) ;
	
	public Animator(HigraphView<?,?,?,?,?,?,?> view) { this.view = view ; }
	
	public void start(int lengthIn_ms) {
		delta = (double)FRAME_TIME_ms / lengthIn_ms  ;
		degree = 0 ;
		// Finish the last transition, if any
		view.finishTransition() ;
		
		view.startTransition() ;

		timer.start(); }
}
