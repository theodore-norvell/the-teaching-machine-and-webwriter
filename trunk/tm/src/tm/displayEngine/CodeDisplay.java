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

import java.awt.Color ;
import java.awt.Dimension ;
import java.awt.Font ;
import java.awt.FontMetrics ;
import java.awt.Graphics ;
import java.awt.Graphics2D ;
import java.awt.Paint ;
import java.awt.Point ;
import java.awt.Rectangle ;
import java.awt.event.ItemEvent ;
import java.awt.event.ItemListener ;
import java.awt.event.MouseEvent ;

import javax.swing.JCheckBoxMenuItem ;
import javax.swing.JSlider ;
import javax.swing.event.ChangeEvent ;
import javax.swing.event.ChangeListener ;

import tm.configuration.Configuration ;
import tm.interfaces.CodeLineI ;
import tm.interfaces.ImageSourceInterface ;
import tm.interfaces.MarkUpI ;
import tm.interfaces.SelectionInterface ;
import tm.interfaces.SourceCoords ;
import tm.interfaces.SourceCoordsI ;
import tm.interfaces.TMFileI ;
import tm.subWindowPkg.SmallButton ;
import tm.subWindowPkg.ToolBar ;
import tm.utilities.Assert ;
import tm.utilities.Debug ;

public class CodeDisplay extends DisplayAdapter {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2180490760572023332L;
	private final static int LEFT_MARGIN = 10;      // These units are in pixels
    private final static int TOP_MARGIN = 0;
    private final static int LINE_PADDING = 1;      // Space between lines

//Printing modes
    private final static int NORMAL = 0;
    private final static int KEYWORD = 1;
    private final static int COMMENT = 2;
    private final static int PREPROCESSOR = 3;
    private final static int CONSTANT = 4;
    private final static int LINE_NUMBER = 5;
// Font styles
    private final static int PLAIN = 0;
    private final static int BOLD = 1;
    private final static int ITALIC = 2;
    private final static int BOLD_ITALIC = 3;

    private Font myFonts[] = {null,null,null,null}; // Indexed by font style

// Configurables
    private int fontMapper[] = {PLAIN,PLAIN,ITALIC,PLAIN,BOLD,PLAIN};
    private Color fontColor[] =
        {Color.black, Color.blue, Color.black, Color.red, Color.black, Color.red};
    private static int tabSpaces = 4;

    private Color cursorColor = Color.green;

// Critical references

//    private CommandInterface commandProcessor;

    private int cursorLine;		// The line which contains the user-settable cursor
    private int cursorChar;     // The char which the cursor is on
    private SourceCoordsI cursorLineCoords ;
    private TMFileI theFile = null ; // The file currently being displayed.
    private SelectionInterface theSelection ;
    private int rate = 50; // Middle of arbitrary 0-100 scale
    private JSlider slider;
    private final JCheckBoxMenuItem lineNumbersCheckBox = new JCheckBoxMenuItem("Line Numbers", true);
    {
        lineNumbersCheckBox.addItemListener( new ItemListener() {
            @Override  public void itemStateChanged(ItemEvent arg0) {
                refresh() ;
            }} ) ;
    }

    public CodeDisplay(DisplayManager dm, String configId){
        super(dm, configId);
        cursorLine = 0;
        cursorChar = 0;
        cursorLineCoords = null ;
        
        /**dbg*/ Debug.getInstance().msg(Debug.CURRENT, "CodeDisplay "+hashCode() +" adding "+lineNumbersCheckBox.hashCode()); /**/
        context.getViewMenu().add(lineNumbersCheckBox) ;

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
    
    

    @Override public void dispose() {
        /**dbg*/ Debug.getInstance().msg(Debug.CURRENT, "CodeDisplay "+hashCode() +" removing "+lineNumbersCheckBox.hashCode()); /**/
        context.getViewMenu().remove( lineNumbersCheckBox ) ;
        super.dispose() ;
    }

// =================================================================
// Resource Interface Methods
// =================================================================

// 98.06.15:    Replaces setNumLines(n) method - now pulls # of lines
/* 2001.11.19: added check for change in line height (caused by changing font
                sizes) to do full refresh.
*/
    public void refresh(){
        refreshTheButtons() ;
        boolean allowGaps = lineNumbersCheckBox.getState() ;
        SourceCoordsI focus = commandProcessor.getCodeFocus();
        TMFileI file = focus.getFile() ;
        /*DBG System.out.println("Current file is " + file.getFileName());/*DBG*/
        Graphics screen = getGraphics();
        if (screen == null) return;
        screen.setFont(context.getCodeFont());
// lines are used for the vertical scale
        FontMetrics fm = screen.getFontMetrics();
        int lineHeight = fm.getHeight() + LINE_PADDING;
//        System.out.println("Line Height: " + lineHeight);

// If new code loaded, or (2001.11.19) font is changed, preferred size must be recomputed
        if (file != theFile || lineHeight != getVScale()
            || commandProcessor.getSelection() != theSelection ) {
            setScale(1, lineHeight);
            int portWidth;
            int n = commandProcessor.getNumSelectedCodeLines( file, allowGaps ) ;
//            System.out.println("# of lines: " + n);

            // Really we should measure the longest line.
            // That shouldn't be too hard, but, for now I'm just going to
            // set the width to a fixed number of pixels.
            portWidth = 1000;
            int portHeight = (n+2) * lineHeight; // blank line at top and bottom
//            System.out.println("portHeight: " + portHeight);

            cursorLine = 0;
            cursorLineCoords = null ;
            theFile = file ;
	        //System.out.println("Current file is " + theFile.getFileName());
            
            theSelection = commandProcessor.getSelection() ;
            setPreferredSize(new Dimension(portWidth, portHeight));
        }
        // Update the title on each refresh just to make sure.
        mySubWindow.setTitle( theFile.getFileName() );
        // The focus line might be off the screen.
        // So we search through all the selected lines of the
        // file and, if we need to, we adjust the vertical
        // focus to include the highlighted line.
        int focusLine = 0;
        boolean found = false ;
        for( int sz = commandProcessor.getNumSelectedCodeLines(theFile, allowGaps) ; focusLine < sz ; ++focusLine ) {
            CodeLineI codeLine = commandProcessor.getSelectedCodeLine(theFile, allowGaps, focusLine) ;
            if( codeLine != null && codeLine.getCoords().equals( focus ) ) {
                found = true ;
                break ; } }
        if( found ) {
            int topLine = (1 + focusLine)*getVScale();
            int bottomLine = topLine + getVScale();
            int vertValue = myWorkPane.getVerticalScrollBar().getValue();
            if (topLine < vertValue ||
                bottomLine > vertValue + myWorkPane.getViewport().getHeight()  ) {
                paintImmediately( getBounds() ) ;
                myWorkPane.getVerticalScrollBar().setValue(topLine - 3*getVScale()); }
        }
        super.refresh();
    }



    private void refreshTheButtons() {
        boolean isAutoStepping = commandProcessor.isInAuto() ;
        boolean haveSlider = slider != null ;
        if( isAutoStepping ) {
            if( ! haveSlider ) {
                slider = new JSlider(1, 100, 10);
                slider.addChangeListener(new ChangeListener(){
                    public void stateChanged(ChangeEvent evt){
                        // Note.  I removed the check that
                        // !slider.getValueIsAdjusting()
                        // since without it speed changes are
                        // more immediate.  TSN
                            rate = slider.getValue();
                            commandProcessor.setAutoStepRate(rate);
                    }
                });

                toolBar.add(slider);
                toolBar.doLayout();
            }
            slider.setValue( commandProcessor.getAutoStepRate() ) ;
            // TODO Change the appearance of the autostep button
            // to a red square or octagon or some such.
            // TODO Ensure all go forward and undo buttons are disabled.
        }
        else { // Not autostepping
            if( haveSlider ) {
                //TODO There is an exception thrown on the doLayout
                // below and then later during repaint.
                toolBar.remove(slider);
                slider = null ;
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


/* align focus with the line inside which the mouse clicked. The mouse event will be
    correctly located even when part of the display is scrolled offscreen
*/
    public void moveCursor(MouseEvent evt) {
//        System.out.println("Mouse clicked in " + toString());
//        if (justGotFocus) return;
  //      System.out.println("Didn't just get focus.");
 //       System.out.println("parent codeFont is: " + parent.getCodeFont().toString());
        cursorLine = (evt.getY() /*- TOP_MARGIN*/)/(getFontMetrics(context.getCodeFont()).getHeight()+ LINE_PADDING) - 1;
//		System.out.println("Cursor set to " + cursorLine + " for y at " + evt.getY());
        cursorChar = 0;     // Just for now
        refresh();
    }

    public void setLineNumbering(boolean on){
        lineNumbersCheckBox.setState( on ) ;
    }


// Button handler
    public void buttonPushed(int i) {
        if (i < 0 || i > 7) return;
        switch (i) {
        case 0: commandProcessor.goBack();
        break;
        case 1: commandProcessor.intoExp();
        break;
        case 2: commandProcessor.intoSub();
        break;
        case 3: commandProcessor.overAll();
        break;
        case 4: {
            String fileName = cursorLineCoords.getFile().getFileName() ;
            commandProcessor.toCursor(fileName, cursorLineCoords.getLineNumber()); }
        break;
        case 5: commandProcessor.reStart();
        break;
        case 6: commandProcessor.autoRun();
        break;
        case 7:
            if( commandProcessor.isInAuto() )
                commandProcessor.stopAuto() ;
            else
                commandProcessor.autoStep() ;
        break;
        }
    } 


/*  Parameter over-rides to save/restore view settings
*/
    public void notifyOfSave(Configuration config){
        super.notifyOfSave(config);
        config.setValue("fontMapper[NORMAL]", Integer.toString(fontMapper[NORMAL]));
        config.setValue("fontMapper[KEYWORD]", Integer.toString(fontMapper[KEYWORD]));
        config.setValue("fontMapper[COMMENT]", Integer.toString(fontMapper[COMMENT]));
        config.setValue("fontMapper[PREPROCESSOR]", Integer.toString(fontMapper[PREPROCESSOR]));
        config.setValue("fontMapper[CONSTANT]", Integer.toString(fontMapper[CONSTANT]));
        config.setValue("fontMapper[LINE_NUMBER]", Integer.toString(fontMapper[LINE_NUMBER]));
        config.setComment("fontMapper[NORMAL]", "0 = PLAIN, 1 = BOLD, 2 = ITALICS, 3 = BOLDITALICS");
        config.setValue("fontColor[NORMAL]", Integer.toString(0x00ffffff & fontColor[NORMAL].getRGB()));
        config.setValue("fontColor[KEYWORD]", Integer.toString(0x00ffffff & fontColor[KEYWORD].getRGB()));
        config.setValue("fontColor[COMMENT]", Integer.toString(0x00ffffff & fontColor[COMMENT].getRGB()));
        config.setValue("fontColor[PREPROCESSOR]", Integer.toString(0x00ffffff & fontColor[PREPROCESSOR].getRGB()));
        config.setValue("fontColor[CONSTANT]", Integer.toString(0x00ffffff & fontColor[CONSTANT].getRGB()));
        config.setValue("fontColor[LINE_NUMBER]", Integer.toString(0x00ffffff & fontColor[LINE_NUMBER].getRGB()));
        config.setValue("cursorColor", Integer.toString(0x00ffffff & cursorColor.getRGB()));
        String comment = "Using 24 bit RGB colour model. Take care when hand editing as colour support can be quite limited\n";
        comment +="See appendix C of \"Using HTML 4, 4th edition\" by Lee Anne Phillips, Que Books, ISBN 0-7897-1562-7\n";
        comment += "for a useful discussion of this issue.\n";	
        config.setComment( "fontColor[NORMAL]", comment);
        config.setValue("tabSpaces", Integer.toString(tabSpaces));
        config.setValue("lineNumbers", String.valueOf( lineNumbersCheckBox.getState() ) );
    }

    public void notifyOfLoad(Configuration config){
        super.notifyOfLoad(config);
        String temp = config.getValue("fontMapper[NORMAL]");
        Debug.getInstance().msg(Debug.DISPLAY, "temp is " + temp);
        if (temp != null)
        	fontMapper[NORMAL]
        	           = new Integer(temp).intValue();
        temp = config.getValue("fontMapper[KEYWORD]");
        if (temp != null) fontMapper[KEYWORD] = new Integer(temp).intValue();
        temp = config.getValue("fontMapper[COMMENT]");
        if (temp != null) fontMapper[COMMENT] = new Integer(temp).intValue();
        temp = config.getValue("fontMapper[PREPROCESSOR]");
        if (temp != null) fontMapper[PREPROCESSOR] = new Integer(temp).intValue();
        temp = config.getValue("fontMapper[CONSTANT]");
        if (temp != null) fontMapper[CONSTANT] = new Integer(temp).intValue();
        temp = config.getValue("fontMapper[LINE_NUMBER]");
        if (temp != null) fontMapper[LINE_NUMBER] = new Integer(temp).intValue();
        temp = config.getValue("fontColor[NORMAL]");
        if (temp != null) fontColor[NORMAL] = Color.decode(temp);
        temp = config.getValue("fontColor[KEYWORD]");
        if (temp != null) fontColor[KEYWORD] = Color.decode(temp);
        temp = config.getValue("fontColor[COMMENT]");
        if (temp != null) fontColor[COMMENT] = Color.decode(temp);
        temp = config.getValue("fontColor[PREPROCESSOR]");
        if (temp != null) fontColor[PREPROCESSOR] = Color.decode(temp);
        temp = config.getValue("fontColor[CONSTANT]");
        if (temp != null) fontColor[CONSTANT] = Color.decode(temp);
        temp = config.getValue("fontColor[LINE_NUMBER]");
        if (temp != null) fontColor[LINE_NUMBER] = Color.decode(temp);
        temp = config.getValue("cursorColor");
        if (temp != null) cursorColor = Color.decode(temp);
        temp = config.getValue("tabSpaces");
        if (temp != null) tabSpaces = new Integer(temp).intValue();
        temp = config.getValue("lineNumbers");
        if (temp != null){
            setLineNumbering(temp.compareTo("true") == 0);
            //(ViewStateManager.getViewStateManager()).setLineNumbering(lineNumbers);
        }

    }

// =================================================================
// Graphics Methods
// =================================================================

    public void drawArea(Graphics2D screen) {
                    
    	screen.setPaintMode();
        setMode( screen, NORMAL);     // Always start in normal mode
        FontMetrics fm = screen.getFontMetrics();

        int maxWidth = 0;
        Point scroll = getScrollPosition();
        Dimension port = getViewportExtent();

        final int lineHeight = fm.getHeight();
        final int descent = fm.getMaxDescent() ;
        final int ascent = fm.getMaxAscent() ;
//	System.out.println("Line height at draw is: " + lineHeight + " for font " + screen.getFont());
        int baseLine = lineHeight + LINE_PADDING;

        SourceCoords focus = (SourceCoords)commandProcessor.getCodeFocus();
        /*DBG System.out.println("Current file is " + file.getFileName());/*DBG*/
        if (theFile == null) theFile = focus.getFile();
        boolean allowGaps = lineNumbersCheckBox.getState() ;
        int n = commandProcessor.getNumSelectedCodeLines(theFile, allowGaps) ;
        // We need to print all lines that intersect the current viewing area
        // whose base line bl is such that
        // scroll.y <= bl+descent && bl-ascent <= scroll.y + port.height.
        // Invariant: baseLine == (i+1) * (lineHeight+ LINE_PADDING)
        for (int i = 0; i < n ; i++) {
            baseLine += lineHeight+ LINE_PADDING;
//            System.out.println("lastLine and baseLine are " + lastLine + " and " + baseLine);
            /*if ( scroll.y <= baseLine+descent && baseLine-ascent <= scroll.y + port.height) */ {     
                // assert baseLine - (lineHeight+LINEPADDING <= scroll.y + port.height
//            	System.out.println("Drawing line " + i);
	            CodeLineI theLine = commandProcessor.getSelectedCodeLine(theFile, allowGaps, i);
	            if ( theLine != null && theLine.getCoords().equals( focus ) ){
	                Paint save = screen.getPaint();
	                screen.setPaint(context.getHighlightColor());
	                screen.fill(new Rectangle(0, baseLine-fm.getAscent(), getSize().width, fm.getAscent()+fm.getDescent()));
	                screen.setPaint(save);
	            }
	            if (cursorLine == i) {
//	            	System.out.println("Drawing cursor at line " + i);
	            	Paint save = screen.getPaint();
	                screen.setPaint(cursorColor);
	                screen.fill(new Rectangle(0, baseLine-fm.getAscent(), 10, fm.getAscent()+fm.getDescent()));
	                screen.setPaint(save);
	                // Update the cursorLineCoords
	                if( theLine == null ) cursorLineCoords = null ;
	                else                  cursorLineCoords = theLine.getCoords() ;
	            }
	            drawLine(theLine, LEFT_MARGIN-0, baseLine-0, screen);
            }
        }
    }

    // Get codeline i
    //private CodeLine getCodeLine(TMFile file, int i){
    //
    //    return commandProcessor.getSelectedCodeLine(file, i);
    //}


    // Draws a single line, taking mode changes into account
    private void drawLine(CodeLineI codeLine, int x, int y, Graphics2D screen) {
        //System.out.println( "Displaying "+codeLine );
        setMode( screen, NORMAL ) ;
        FontMetrics fm = screen.getFontMetrics() ;
        final int em = fm.charWidth('M') ; /* Maybe we should use fm.getMaxAdvance() ;
                                            * BUT look out because that routine might return -1 */
        if( codeLine == null ) {
            // A null represents a gap in the selection. We draw three dots.
            fm = screen.getFontMetrics() ;
            screen.drawString( "...", x, y ) ;
            return ; }

        if( lineNumbersCheckBox.getState() ) {
            // 5 characters and a colon.
            int lineNum = codeLine.getCoords().getLineNumber() ;
            char [] lineNumArray = new char[] {' ', ' ', ' ', ' ', ' ', ':' } ;
            for( int i=0; i<5 ; ++i ) {
                lineNumArray[4-i] = (char)('0' + lineNum%10) ;
                lineNum /= 10 ;
                if( lineNum == 0 ) break ; }
            setMode( screen, LINE_NUMBER ) ;
            fm = screen.getFontMetrics() ;
            screen.drawChars( lineNumArray, 0, 6, x, y );
            x += 7 * em ; /* Assumption that an em in NORMAL and an em in LINENUMBER are similar*/
        }

        int column = 0 ;
        int m = 0 ; // index into the markup array
        char[] chars = codeLine.getChars() ;
        MarkUpI[] markUp = codeLine.markUp() ;
        setMode( screen, NORMAL ) ;
        fm = screen.getFontMetrics() ;
        SelectionInterface selection = commandProcessor.getSelection() ;
        boolean visible = selection.isValidForEmptyTagSet() ;
        final int x0 = x ; // x0 is the x of the first character.
        for( int i=0, sz = chars.length ; i < sz ; ++i ) {
            // Process all MarkUp commands that apply up to column i.
            while( m < markUp.length && markUp[m].getColumn() <= i ) {
                int command = markUp[m].getCommand() ;
                switch( command ) {
                    case MarkUpI.NORMAL: {
                        setMode( screen, NORMAL ) ;
                        fm = screen.getFontMetrics() ; }
                    break ;
                    case MarkUpI.KEYWORD : {
                        setMode( screen, KEYWORD ) ;
                        fm = screen.getFontMetrics() ; }
                    break ;
                    case MarkUpI.COMMENT : {
                        setMode( screen, COMMENT ) ;
                        fm = screen.getFontMetrics() ; }
                    break ;
                    case MarkUpI.PREPROCESSOR : {
                        setMode( screen, PREPROCESSOR ) ;
                        fm = screen.getFontMetrics() ; }
                    break ;
                    case MarkUpI.CONSTANT : {
                        setMode( screen, CONSTANT ) ;
                        fm = screen.getFontMetrics() ; }
                    break ;
                    case MarkUpI.CHANGE_TAG_SET : {
                        visible = markUp[m].getTagSet().selectionIsValid( selection ) ; }
                    break ;
                    default : {
                        Assert.check(false) ; } }
                m++ ; }
            
            // Now display the character.
            if( visible ) {
                if( chars[i] == '\t' ) {
                    // Expand the tabs
                    //   column   0  1  2  3  4  5  6  7  8  9 10 11 12 13
                    //newcolumn   4  4  4  4  8  8  8  8 12 12 12 12 16
                    int newColumn = (column/tabSpaces + 1) * tabSpaces ;
                    Assert.check(newColumn > column) ;
                    char [] space = new char[] {' '} ;
                    x += (newColumn-column) * fm.charsWidth( space, 0, 1 ) ; 
                    column = newColumn ; }
                else {
                    // A printable character, we hope!
                    column += 1 ;
                    screen.drawChars( chars, i, 1, x, y ) ;
                    x += fm.charsWidth( chars, i, 1 ) ; } } }
    }

// Replaces fontMetrics.stringWidth - takes mode changes into account
//    private int stringWidth(CodeLine theLine, Graphics screen){
//        int theWidth = 0;
//        if (theLine.length() > 0) {
//            String expanded = expandTabs(theLine);
//            for( int i = 0 ; i < expanded.length(); ++ i ) {
//                char c = theLine.charAt(i);
//                if ( c >= MARKER_BOUND)
//                    modeChange(NORMAL_MARK-c, screen);
//                else
//                    theWidth += advances[c];
//            }
//        }
//        return theWidth;
//    }

    // Change the current font colouring/style mode
    private void setMode(Graphics2D screen, int newMode){

        if (myFonts[0] != context.getCodeFont()) {
            myFonts[0] = context.getCodeFont();
            setSecondaryFonts(myFonts[0]);
        }

        screen.setFont(myFonts[fontMapper[newMode]]);
//        FontMetrics fm = screen.getFontMetrics();
        screen.setPaint(fontColor[newMode]);
    }

// Creates the style variations on the current font
    private void setSecondaryFonts(Font primary){
        myFonts[BOLD] = new Font(primary.getName(),Font.BOLD, primary.getSize());
        myFonts[ITALIC] = new Font(primary.getName(),Font.ITALIC, primary.getSize());
        myFonts[BOLD_ITALIC] = new Font(primary.getName(), Font.BOLD+Font.ITALIC,primary.getSize());
    }

}
