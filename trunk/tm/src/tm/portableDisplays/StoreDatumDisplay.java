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

package tm.portableDisplays;

import java.util.Vector;

import telford.common.Graphics;
import telford.common.Point;
import tm.displayEngine.StoreSwingDisplay;
import tm.interfaces.DataDisplayView;
import tm.interfaces.Datum;

public class StoreDatumDisplay extends DatumDisplay {
	protected Vector valueBoxes = new Vector();
	public StoreDatumDisplay(Datum datum, DataDisplayView top, boolean expand) {
		super(datum, top, expand);
		addressBox = new StringBox(Integer.toString(myDatum.getAddress()), false, StringBox.LEFT, StringBox.TOP);
		addressBox.nudge(STRING_OFFSET, 0);

		for (int i = 0; i < datum.getNumChildren(); i++) {
			Datum kid = myDatum.getChildAt(i);
			DatumDisplay dd = getAssociated(kid, top);
			if (dd == null)
				dd = new StoreDatumDisplay(kid, top, false);
		}
	}

	public void resize(int nWidth, int vGap, int vWidth, int aWidth) {
		super.resize(nWidth, vGap, vWidth, aWidth);
		if (myDatum.getNumChildren() > 0)
			return; // Compound datums handle this by accretion

		if (myDataView.getDatumView() != StoreSwingDisplay.LOGICAL) {
			extent.height = myDatum.getNumBytes() * baseHeight;
		}
		valueBox.resize(valueBox.getExtent().width,
				myDataView.getDatumView() == StoreSwingDisplay.SCALED ? extent.height : baseHeight);
		setValueBoxes();
	}

	protected void drawScalar(Graphics screen) {

		// We need local co-ordinates for accumulating
		Point position = new Point(extent.x, extent.y);

		screen.setFont(nameFont);
		nameBox.draw(screen, position, -1);

		screen.setFont(addressFont);
		addressBox.draw(screen, position, -1);
		//Color.yellow #FFFF00; Color.lightgrey #d3d3d3; Color.pink
		
		int color = (myDatum.getHighlight() == Datum.HIGHLIGHTED) ? (0XFFFF00)
				: (isGrayedOut() ? 0Xd3d3d3 : -1);
		if (isSelected())
			color = 0Xffc0cb;
		screen.setFont(valueFont);
		// Added 2001.12.15: puts a box around area where an expander would go
		// so scalar datums have same width as compound ones
		int width = valueBox.getExtent().x + valueBox.getExtent().width - nameBox.getExtent().width;
		screen.drawRect((int)position.getX() + nameBox.getExtent().width, (int)position.getY(), width, extent.height);

		// setValueBoxes();
		valueBox.draw(screen, position, color); // primary value box

		// In binary view we have to draw all the extra internal byte boxes
		if (myDataView.getDatumView() == StoreSwingDisplay.BINARY)
			for (int i = 0, d = baseHeight; i < valueBoxes.size(); i++) {
				StringBox vBox = (StringBox) valueBoxes.elementAt(i);
				vBox.move(valueBox.getExtent().x, d);
				vBox.resize(valueBox.getExtent().width, valueBox.getExtent().height);
				vBox.draw(screen, position, color);
				d += baseHeight;
			}

	} // end draw method

	/*
	 * valueBoxes are complicated by BINARY view since multiple boxes need to be
	 * created in that case (one for each byte) Mar 13, 2001: Since
	 * DatumDisplays are no longer recreated every time there is a change. The
	 * code has been rewritten to allow value boxes to update the value. Major
	 * change is to add boolean useOld and stay in if non-binary values,
	 * allowing strings to be reset.
	 */
	private void setValueBoxes() {
		boolean binary = (myDataView.getDatumView() == StoreSwingDisplay.BINARY);
		boolean useOld = false; // Using existing value boxes
		// 3/13/2001: useOld added, no longer exit routine if we have the
		// correct no. of boxes already
		if (binary && valueBoxes.size() == (myDatum.getNumBytes() - 1))
			useOld = true;
		if (!binary && valueBoxes.size() == 0)
			useOld = true;
		/*
		 * If we get here we've got some building to do All of this hair is to
		 * support binary view which requires multiple boxes I did some changes
		 * to support primary valueBox + extras (1 -> 0 in next line) but this
		 * won't work, needs farther ammendment.
		 */
		if (!useOld)
			valueBoxes.setSize(binary ? myDatum.getNumBytes() - 1 : 0);
		if (!binary)
			valueBox.setString(myDatum.getValueString()); // Otherwise we still
															// need an empty box
		else {
			valueBox.setString(binaryByte(myDatum.getByte(0)));
			for (int i = 0; i < valueBoxes.size(); i++) {
				StringBox sb = (StringBox) valueBoxes.elementAt(i);
				if (sb == null) {
					sb = new StringBox(binaryByte(myDatum.getByte(i + 1)), true, StringBox.LEFT, StringBox.MIDDLE);
					valueBoxes.setElementAt(sb, i);
					sb.nudge(STRING_OFFSET, 0);
				} else
					sb.setString(binaryByte(myDatum.getByte(i + 1)));
			}
		}

	}

	// Returns hex byte for binary mode
	private static String binaryByte(int i) {
		char fullRep[] = { '0', '0', '0', '0', '0', '0', '0', '0' }; // full
																		// size
																		// representation
		String binary = Integer.toBinaryString(i & 0xff);
		// copy in the hex string, right justified, so extra zeroes on left
		binary.getChars(0, binary.length(), fullRep, 8 - binary.length());
		return new String(fullRep);
	}

	public String toString() {
		return "DatumDisplayStore for " + myDatum.toString();
	}

}