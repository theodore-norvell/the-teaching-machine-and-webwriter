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

package tm.displayEngine.tmHigraph;

import java.awt.Graphics2D;

import tm.backtrack.BTVar;
import tm.displayEngine.DisplayAdapter;
import higraph.view.HigraphView;
import tm.interfaces.DisplayContextInterface;
import tm.interfaces.ImageSourceInterface;
import tm.subWindowPkg.SmallButton;
import tm.subWindowPkg.ToolBar;
import tm.utilities.Debug;

public class HigraphVisualizer	extends DisplayAdapter {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7382595374129821180L;
	
	private BTVar<HigraphView<NodePayloadTM,EdgePayloadTM,
			HigraphTM, WholeGraphTM,SubgraphTM,
			NodeTM,EdgeTM>> myViewVar;
	

	/**
	 * Constructor for the tm which uses plugins which requires standard
	 * constructor arguments so that view has to be set separately
	 * @param dc
	 * @param configId
	 */
	public HigraphVisualizer(DisplayContextInterface dc, String configId){
		super(dc, configId);
		myViewVar = new BTVar<HigraphView<NodePayloadTM,EdgePayloadTM,
		HigraphTM, WholeGraphTM,SubgraphTM,
		NodeTM,EdgeTM>>(dc.getTimeManager());
		SmallButton buttons[] = new SmallButton[2];
		ImageSourceInterface imageSource = context.getImageSource();
		buttons[0] = new SmallButton(SmallButton.BACKUP, imageSource);
		buttons[0].setToolTipText("Backup");
		buttons[1] = new SmallButton("AutoRun", imageSource);
		buttons[1].setToolTipText("Run from here");
		toolBar = new ToolBar(buttons);
		mySubWindow.addToolBar(toolBar);
	}
	
	/**
	 * Default implementation of refresh 
	 */
		public void refresh() {
//			System.out.println("refreshing " + this);
			HigraphView<NodePayloadTM,EdgePayloadTM,
			HigraphTM, WholeGraphTM,SubgraphTM,
			NodeTM,EdgeTM> view = myViewVar.get();
			if ( view != null) {
				this.mySubWindow.setTitle(view.getTitle());
				view.refresh();
			}
			
			super.refresh(); 
		}
		
		// Button handler
	    public void buttonPushed(int i) {
	        if (i < 0 || i > 1) return;
	        switch (i) {
	        case 0: commandProcessor.goBack();
	        break;
	        case 1: commandProcessor.autoRun();
	        break;
	        }
	    } 

		
	
	/**
	 * view should be passed in at construction but that doesn't fit
	 * with tm plugin model
	 * @param view
	 */
	public void setView(HigraphView<NodePayloadTM,EdgePayloadTM,
			HigraphTM, WholeGraphTM,SubgraphTM,
			NodeTM,EdgeTM> view){
		if (myViewVar.get() == null)	myViewVar.set(view);
		tm.utilities.Debug.getInstance().msg(Debug.DISPLAY, "SubgraphVisualizer set view. ");
//		System.out.println("SubgraphVisualizer " + this + " set view to " + myView);

	}
	
	public void dispose(){
//		System.out.println("disposing " + myView );
		if (myViewVar.get() != null){
			tm.utilities.Debug.getInstance().msg(Debug.DISPLAY, "disposing HigraphView.");
			myViewVar.get().dispose();
		}

		super.dispose();
	}


	@Override
	public void drawArea(Graphics2D screen) {
		if (myViewVar.get() != null)
			myViewVar.get().drawArea(screen);
	}
	
	public String toString() {return "Subgraph Visualizer " + configId + " @" + hashCode();}

}
