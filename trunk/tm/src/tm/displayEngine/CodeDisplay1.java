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
import tm.interfaces.CodeLine;
import tm.interfaces.ImageSourceInterface;
import tm.interfaces.SelectionInterface;
import tm.interfaces.SourceCoords;
import tm.portableDisplays.CodeDisplayer;
import tm.subWindowPkg.SmallButton;
import tm.subWindowPkg.ToolBar;
import tm.utilities.Debug;
import tm.utilities.TMFile;

public class CodeDisplay1 extends SwingDisplay {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2180490760572023332L;
	private final static int LINE_PADDING = 1; // Space between lines

	// Printing modes

	private int cursorLine; // The line which contains the user-settable cursor
	private int cursorChar; // The char which the cursor is on
	private SourceCoords cursorLineCoords;
	private TMFile theFile = null; // The file currently being displayed.
	private SelectionInterface theSelection;
	private int rate = 50; // Middle of arbitrary 0-100 scale
	private JSlider slider;
	private final JCheckBoxMenuItem lineNumbersCheckBox = new JCheckBoxMenuItem("Line Numbers", true);

	{
		lineNumbersCheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				refresh();
			}
		});
	}

	public CodeDisplay1(DisplayManager dm, String configId) {
		// super(dm, configId);
		super(dm, configId, new CodeDisplayer(dm.getCommandProcessor(), dm.getPortableContext()));
		cursorLine = 0;
		cursorChar = 0;
		cursorLineCoords = null;

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
		SourceCoords focus = commandProcessor.getCodeFocus();
		TMFile file = focus.getFile();
		/*
		 * DBG System.out.println("Current file is " + file.getFileName());/*DBG
		 */
		Graphics screen = getGraphics();
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
			cursorLine = 0;
			cursorLineCoords = null;
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
			CodeLine codeLine = commandProcessor.getSelectedCodeLine(theFile, allowGaps, focusLine);
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
				paintImmediately(getBounds());
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

	protected void mouseJustClicked(MouseEvent evt) {
		moveCursor(evt);
	}

	/*
	 * align focus with the line inside which the mouse clicked. The mouse event
	 * will be correctly located even when part of the display is scrolled
	 * offscreen
	 */
	public void moveCursor(MouseEvent evt) {
		cursorLine = (evt.getY() /*- TOP_MARGIN*/) / (getFontMetrics(context.getCodeFont()).getHeight() + LINE_PADDING)
				- 1;
		cursorChar = 0; // Just for now
		refresh();
	}

	public void setLineNumbering(boolean on) {
		lineNumbersCheckBox.setState(on);
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
		config.setValue("lineNumbers", String.valueOf(lineNumbersCheckBox.getState()));
		
	}

	public void notifyOfLoad(Configuration config) {
		super.notifyOfLoad(config);
		String temp = config.getValue("fontMapper[NORMAL]");
		Debug.getInstance().msg(Debug.DISPLAY, "temp is " + temp);
		temp = config.getValue("lineNumbers");
		if (temp != null) {
			setLineNumbering(temp.compareTo("true") == 0);
			// (ViewStateManager.getViewStateManager()).setLineNumbering(lineNumbers);
		}

	}
}
