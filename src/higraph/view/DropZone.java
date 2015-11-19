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

package higraph.view;

import tm.backtrack.BTTimeManager;
import tm.backtrack.BTVar;

import higraph.model.interfaces.*;

/**
 * DropZone represents a {@link ZoneView} that detects mouse
 * operations in a region where a new {@link ComponentView}
 * can be dropped.
 *
 * @param NP The Node Payload type
 * @param EP The Edge Payload type
 * @param HG The Higraph type 
 * @param WG The Whole Graph type
 * @param SG The Subgraph type
 * @param N  The Node type
 * @param E  The Edge type
 * 
 * @author mpbl
*/
public class DropZone
    < NP extends Payload<NP>,
      EP extends Payload<EP>,
      HG extends Higraph<NP,EP,HG,WG,SG,N,E>,
      WG extends WholeGraph<NP,EP,HG,WG,SG,N,E>,
      SG extends Subgraph<NP,EP,HG,WG,SG,N,E>,
      N extends Node<NP,EP,HG,WG,SG,N,E>, 
      E extends Edge<NP,EP,HG,WG,SG,N,E>
    >
extends ZoneView<NP, EP, HG, WG, SG, N, E> {
    
    public static final String TYPE_STRING = "DropZone" ;
    

	protected DropZone(NodeView <NP,EP,HG,WG,SG,N,E> nv, BTTimeManager timeMan ){
		super(nv, timeMan);
		colorVar.set(nv.getHigraphView().getDefaultZoneColor());
		fillColorVar.set(nv.getHigraphView().getDefaultZoneFillColor());
		strokeVar.set(nv.getHigraphView().getDefaultZoneStroke());
	}
	
	
/*	public void draw(Graphics2D screen){
		if (screen == null) return;
		Shape s = getShape();
		if (s == null) return;
		Rectangle r = s.getBounds();
		Color currentColor = screen.getColor();
		screen.setColor(isOver ? color : fillColor);
		int delta = 5;
		int xl = r.x + delta;
		int xr = r.x + r.width - delta;
		int top = r.y + delta;
		int midX = r.x + r.width/2;
		int bottom = r.y + r.height - delta;
		screen.drawLine(xl, top,xr, top);
		screen.drawLine(midX, top, midX, bottom);
		screen.drawLine(xl, bottom, xr, bottom);
		screen.setColor(currentColor);	
	}*/
	
	public String toString(){
		return TYPE_STRING + associatedComponentVar.get();
	}
	
	@Override
	public String getViewType() {
		return TYPE_STRING;
	}


}
