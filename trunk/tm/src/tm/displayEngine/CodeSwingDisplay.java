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

package tm.displayEngine;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import tm.configuration.Configuration;
import tm.interfaces.CodeLineI ;
import tm.interfaces.ImageSourceInterface;
import tm.interfaces.SelectionInterface ;
import tm.interfaces.SourceCoords;
import tm.portableDisplays.CodeDisplayer;
import tm.subWindowPkg.SmallButton;
import tm.subWindowPkg.ToolBar;
import tm.utilities.Debug;
import tm.utilities.TMFile;


/**
 * 
 */
public class CodeSwingDisplay extends SwingDisplay {
	private static final long serialVersionUID = 2180490760572023332L;
	private final static int LINE_PADDING = 1; // Space between lines
	
	// Printing modes
	private final static int NORMAL = 0;
	private final static int KEYWORD = 1;
	private final static int COMMENT = 2;
	private final static int PREPROCESSOR = 3;
	private final static int CONSTANT = 4;
	private final static int LINE_NUMBER = 5;
	
	private final static int PLAIN = 0;
	private final static int BOLD = 1;
	private final static int ITALIC = 2;
//	private final static int BOLD_ITALIC = 3;
	// Printing modes
//	private Font myFonts[] = { null, null, null, null }; // Indexed by font

	// Configurables
	private int fontMapper[] = { PLAIN, PLAIN, ITALIC, PLAIN, BOLD, PLAIN };
	private int fontColor[] = { 0x000000, 0x0000FF, 0x000000, 0xFF0000, 0x000000, 0xFF0000 };
	private static int tabSpaces = 4;
	
	private int cursorColor = 0x008000;
//	private int cursorChar; // The char which the cursor is on
	private SourceCoords cursorLineCoords;
	private TMFile theFile = null; // The file currently being displayed.
	private SelectionInterface theSelection;
	private int rate = 50; // Middle of arbitrary 0-100 scale
	private JSlider slider;
	private CodeDisplayer codeDisplayer;
	private final JCheckBoxMenuItem lineNumbersCheckBox = new JCheckBoxMenuItem("Line Numbers", true);

	{
		lineNumbersCheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				refresh();
			}
		});
	}

	public CodeSwingDisplay(DisplayManager dm, String configId) {
		// super(dm, configId);
		
		super(dm, configId, new CodeDisplayer(dm.getCommandProcessor(), dm.getPortableContext()));
		cursorLineCoords = null;
		
		dm.getPortableContext().getAsserter().check( this.displayer instanceof CodeDisplayer ) ;
		this.codeDisplayer = (CodeDisplayer)this.displayer;
		
		/** dbg */
		Debug.getInstance().msg(Debug.CURRENT,
				"CodeDisplay " + hashCode() + " adding " + lineNumbersCheckBox.hashCode()); /**/
		context.getViewMenu().add(lineNumbersCheckBox);

		SmallButton buttons[] = new SmallButton[8];
		ImageSourceInterface imageSource = context.getImageSource();
		buttons[0] = new SmallButton(SmallButton.BACKUP, imageSource);
		buttons[0].setToolTipText("Backup");
		buttons[1] = new SmallButton("stepOver", imageSource);
		buttons[1].setToolTipText("Single step, stepping OVER functions");
		buttons[2] = new SmallButton("stepInto", imageSource);
		buttons[2].setToolTipText("Single step, stepping INTO functions");
		buttons[3] = new SmallButton("stepOut", imageSource);
		buttons[3].setToolTipText("Step OUT of current function");
		buttons[4] = new SmallButton("ToCursor", imageSource);
		buttons[4].setToolTipText("Run DOWN to cursor");
		buttons[5] = new SmallButton("ReStart", imageSource);
		buttons[5].setToolTipText("Restart the program");
		buttons[6] = new SmallButton("AutoRun", imageSource);
		buttons[6].setToolTipText("Run from here");
		buttons[7] = new SmallButton("AutoStep", imageSource);
		buttons[7].setToolTipText("Continuously step from here");
		toolBar = new ToolBar(buttons);
		mySubWindow.addToolBar(toolBar);
	}

	@Override
	public void dispose() {
		/** dbg */
		Debug.getInstance().msg(Debug.CURRENT,
				"CodeDisplay " + hashCode() + " removing " + lineNumbersCheckBox.hashCode()); /**/
		context.getViewMenu().remove(lineNumbersCheckBox);
		super.dispose();
	}

	// =================================================================
	// Resource Interface Methods
	// =================================================================

	// 98.06.15: Replaces setNumLines(n) method - now pulls # of lines
	/*
	 * 2001.11.19: added check for change in line height (caused by changing
	 * font sizes) to do full refresh.
	 */
	public void refresh() {
		refreshTheButtons();
		boolean allowGaps = lineNumbersCheckBox.getState();
		codeDisplayer.getDisplayInfo().setLineNumbersCheckStatus(allowGaps);
		SourceCoords focus = (SourceCoords)commandProcessor.getCodeFocus();
		TMFile file = focus.getFile();
		codeDisplayer.getDisplayInfo().setTmFile(file);
		codeDisplayer.getDisplayInfo().setFocusLineNumber(focus.getLineNumber());
		/*
		 * DBG System.out.println("Current file is " + file.getFileName());/*DBG
		 */
		Graphics screen = myComponent.getGraphics();
		if (screen == null)
			return;
		screen.setFont(context.getCodeFont());
		// lines are used for the vertical scale
		FontMetrics fm = screen.getFontMetrics();
		int lineHeight = fm.getHeight() + LINE_PADDING;
		if (file != theFile || lineHeight != getVScale() || commandProcessor.getSelection() != theSelection) {
			setScale(1, lineHeight);
			int portWidth;
			int n = commandProcessor.getNumSelectedCodeLines(file, allowGaps);
			portWidth = 1000;
			int portHeight = (n + 2) * lineHeight; // blank line at top and
													// bottom
			codeDisplayer.getDisplayInfo().setCursorLine(0);
			theFile = file;
			theSelection = commandProcessor.getSelection();
			setPreferredSize(new Dimension(portWidth, portHeight));
		}
		// Update the title on each refresh just to make sure.
		mySubWindow.setTitle(theFile.getFileName());
		// The focus line might be off the screen.
		// So we search through all the selected lines of the
		// file and, if we need to, we adjust the vertical
		// focus to include the highlighted line.
		int focusLine = 0;
		boolean found = false;
		for (int sz = commandProcessor.getNumSelectedCodeLines(theFile, allowGaps); focusLine < sz; ++focusLine) {
			CodeLineI codeLine = commandProcessor.getSelectedCodeLine(theFile, allowGaps, focusLine);
			if (codeLine != null && codeLine.getCoords().equals(focus)) {
				found = true;
				break;
			}
		}
		if (found) {
			int topLine = (1 + focusLine) * getVScale();
			int bottomLine = topLine + getVScale();
			int vertValue = myWorkPane.getVerticalScrollBar().getValue();
			if (topLine < vertValue || bottomLine > vertValue + myWorkPane.getViewport().getHeight()) {
				// paintImmediately(myComponent.getBounds());
				myWorkPane.getVerticalScrollBar().setValue(topLine - 3 * getVScale());
			}
		}
		super.refresh();
	}

	private void refreshTheButtons() {
		boolean isAutoStepping = commandProcessor.isInAuto();
		boolean haveSlider = slider != null;
		if (isAutoStepping) {
			if (!haveSlider) {
				slider = new JSlider(1, 100, 10);
				slider.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent evt) {
						// Note. I removed the check that
						// !slider.getValueIsAdjusting()
						// since without it speed changes are
						// more immediate. TSN
						rate = slider.getValue();
						commandProcessor.setAutoStepRate(rate);
					}
				});

				toolBar.add(slider);
				toolBar.doLayout();
			}
			slider.setValue(commandProcessor.getAutoStepRate());
			// TODO Change the appearance of the autostep button
			// to a red square or octagon or some such.
			// TODO Ensure all go forward and undo buttons are disabled.
		} else { // Not autostepping
			if (haveSlider) {
				// TODO There is an exception thrown on the doLayout
				// below and then later during repaint.
				toolBar.remove(slider);
				slider = null;
				toolBar.doLayout();
			}
			// TODO Change the appearance of the autostep button
			// to a rabbit or a runner or a VCR fast forward button
			// or some such
			// TODO Ensure all go forward and undo buttons are enabled.
		}
	}

	@Override
    protected void mouseJustClicked(MouseEvent evt) {
		moveCursor(evt);
	}

	/*
	 * align focus with the line inside which the mouse clicked. The mouse event
	 * will be correctly located even when part of the display is scrolled
	 * offscreen
	 */
	public void moveCursor(MouseEvent evt) {
	    FontMetrics fm = myComponent.getFontMetrics( context.getCodeFont() ) ;
		int cursorLine = (evt.getY() /*- TOP_MARGIN*/) / (fm.getHeight() + LINE_PADDING) - 1;
		codeDisplayer.getDisplayInfo().setCursorLine(cursorLine);
//		cursorChar = 0; // Just for now
		refresh();
	}

	public void setLineNumbering(boolean on) {
		lineNumbersCheckBox.setState(on);
		codeDisplayer.getDisplayInfo().setLineNumbersCheckStatus(on);
	}

	// Button handler
	public void buttonPushed(int i) {
		if (i < 0 || i > 7)
			return;
		switch (i) {
		case 0:
			commandProcessor.goBack();
			break;
		case 1:
			commandProcessor.intoExp();
			break;
		case 2:
			commandProcessor.intoSub();
			break;
		case 3:
			commandProcessor.overAll();
			break;
		case 4: {
			String fileName = cursorLineCoords.getFile().getFileName();
			commandProcessor.toCursor(fileName, cursorLineCoords.getLineNumber());
		}
			break;
		case 5:
			commandProcessor.reStart();
			break;
		case 6:
			commandProcessor.autoRun();
			break;
		case 7:
			if (commandProcessor.isInAuto())
				commandProcessor.stopAuto();
			else
				commandProcessor.autoStep();
			break;
		}
	}

	/*
	 * Parameter over-rides to save/restore view settings
	 */
	public void notifyOfSave(Configuration config) {
		super.notifyOfSave(config);
		config.setValue("fontMapper[NORMAL]", Integer.toString(fontMapper[NORMAL]));
		config.setValue("fontMapper[KEYWORD]", Integer.toString(fontMapper[KEYWORD]));
		config.setValue("fontMapper[COMMENT]", Integer.toString(fontMapper[COMMENT]));
		config.setValue("fontMapper[PREPROCESSOR]", Integer.toString(fontMapper[PREPROCESSOR]));
		config.setValue("fontMapper[CONSTANT]", Integer.toString(fontMapper[CONSTANT]));
		config.setValue("fontMapper[LINE_NUMBER]", Integer.toString(fontMapper[LINE_NUMBER]));
		config.setComment("fontMapper[NORMAL]", "0 = PLAIN, 1 = BOLD, 2 = ITALICS, 3 = BOLDITALICS");
		config.setValue("fontColor[NORMAL]", Integer.toString(0x00ffffff & fontColor[NORMAL]));
		config.setValue("fontColor[KEYWORD]", Integer.toString(0x00ffffff & fontColor[KEYWORD]));
		config.setValue("fontColor[COMMENT]", Integer.toString(0x00ffffff & fontColor[COMMENT]));
		config.setValue("fontColor[PREPROCESSOR]", Integer.toString(0x00ffffff & fontColor[PREPROCESSOR]));
		config.setValue("fontColor[CONSTANT]", Integer.toString(0x00ffffff & fontColor[CONSTANT]));
		config.setValue("fontColor[LINE_NUMBER]", Integer.toString(0x00ffffff & fontColor[LINE_NUMBER]));
		config.setValue("cursorColor", Integer.toString(0x00ffffff & cursorColor));
		String comment = "Using 24 bit RGB colour model. Take care when hand editing as colour support can be quite limited\n";
		comment += "See appendix C of \"Using HTML 4, 4th edition\" by Lee Anne Phillips, Que Books, ISBN 0-7897-1562-7\n";
		comment += "for a useful discussion of this issue.\n";
		config.setComment("fontColor[NORMAL]", comment);
		config.setValue("tabSpaces", Integer.toString(tabSpaces));
		config.setValue("lineNumbers", String.valueOf(lineNumbersCheckBox.getState()));
		
		
	}

	public void notifyOfLoad(Configuration config) {
		super.notifyOfLoad(config);
		String temp = config.getValue("fontMapper[NORMAL]");
		Debug.getInstance().msg(Debug.DISPLAY, "temp is " + temp);
		if (temp != null)
			fontMapper[NORMAL] = new Integer(temp).intValue();
		temp = config.getValue("fontMapper[KEYWORD]");
		if (temp != null)
			fontMapper[KEYWORD] = new Integer(temp).intValue();
		temp = config.getValue("fontMapper[COMMENT]");
		if (temp != null)
			fontMapper[COMMENT] = new Integer(temp).intValue();
		temp = config.getValue("fontMapper[PREPROCESSOR]");
		if (temp != null)
			fontMapper[PREPROCESSOR] = new Integer(temp).intValue();
		temp = config.getValue("fontMapper[CONSTANT]");
		if (temp != null)
			fontMapper[CONSTANT] = new Integer(temp).intValue();
		temp = config.getValue("fontMapper[LINE_NUMBER]");
		if (temp != null)
			fontMapper[LINE_NUMBER] = new Integer(temp).intValue();
		temp = config.getValue("fontColor[NORMAL]");
		if (temp != null)
			fontColor[NORMAL] = Integer.valueOf(temp);
		temp = config.getValue("fontColor[KEYWORD]");
		if (temp != null)
			fontColor[KEYWORD] = Integer.valueOf(temp);
		temp = config.getValue("fontColor[COMMENT]");
		if (temp != null)
			fontColor[COMMENT] = Integer.valueOf(temp);
		temp = config.getValue("fontColor[PREPROCESSOR]");
		if (temp != null)
			fontColor[PREPROCESSOR] = Integer.valueOf(temp);
		temp = config.getValue("fontColor[CONSTANT]");
		if (temp != null)
			fontColor[CONSTANT] = Integer.valueOf(temp);
		temp = config.getValue("fontColor[LINE_NUMBER]");
		if (temp != null)
			fontColor[LINE_NUMBER] = Integer.valueOf(temp);
		temp = config.getValue("cursorColor");
		if (temp != null)
			cursorColor = Integer.valueOf(temp);
		temp = config.getValue("tabSpaces");
		if (temp != null)
			tabSpaces = new Integer(temp).intValue();
		temp = config.getValue("lineNumbers");
		if (temp != null) {
			setLineNumbering(temp.compareTo("true") == 0);
			codeDisplayer.getDisplayInfo().setLineNumbersCheckStatus(temp.compareTo("true") == 0);
		}
		codeDisplayer.getDisplayInfo().setFontMapper(fontMapper);
		codeDisplayer.getDisplayInfo().setFontColor(fontColor);
		codeDisplayer.getDisplayInfo().setCursorColor(cursorColor);
		codeDisplayer.getDisplayInfo().setTabSpaces(tabSpaces);
	}
}
