package tm.gwt.display;


import telford.common.ActionEvent ;
import telford.common.ActionListener ;
import telford.common.Button;
import telford.common.MouseEvent;
import tm.gwt.jsInterface.GWTSuperTMFile;
import tm.gwt.state.StateCommander ;
import tm.interfaces.CodeLineI ;
import tm.interfaces.SourceCoordsI ;
import tm.interfaces.StateInterface ;
import tm.portableDisplays.CodeDisplayer;
import tm.portableDisplays.CodeDisplayerInfo;
import tm.portableDisplays.PortableContextInterface;

public class CodeGWTDisplay extends DisplayAdapterGWT {
    // TODO Delete the next two fields. The really information
    // should be in the CodeDisplayInfo object.
	private GWTSuperTMFile theFile;
	private int cursorLine; 
	
	StateInterface evaluator;
	PortableContextInterface context = new GWTContext();
	CodeDisplayer codeDisplayer ;
	StateCommander commander ;
	

	public CodeGWTDisplay(StateInterface e, final StateCommander commander, PortableContextInterface context) {
		super(new CodeDisplayer(e, context), "codeDisplayPanel", "Test.java", 300, 600);
		this.evaluator = e;
        this.commander = commander ;
		this.context = context;
		cursorLine = 0;
		
		context.getAsserter().check( this.displayer instanceof CodeDisplayer ) ;
        this.codeDisplayer = (CodeDisplayer)this.displayer;
        

		Button bBackup = new Button("<img src='/images/Backup.gif'/>");
		bBackup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                commander.goBack() ;
            }
        });
		Button bStepOver = new Button("<img src='/images/stepOver.gif'/>");
		bStepOver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                commander.intoExp(); ;
            }
        });
		Button bStepInto = new Button("<img src='/images/stepInto.gif'/>");
		bStepInto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                commander.intoSub(); ;
            }
        });
		Button bStepOut = new Button("<img src='/images/stepOut.gif'/>");
		bStepOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                commander.overAll(); ;
            }
        });
		Button bToCursor = new Button("<img src='/images/ToCursor.gif'/>");
		bToCursor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                String fileName = theFile.getFileName();
                commander.toCursor(fileName, cursorLine);
            }
        });
		//Button bUpArrow = new Button("<img src='/images/UpArrow.gif'/>");
		//Button bVW = new Button("<img src='/images/VW.gif'/>");
		//Button bAutoStep = new Button("<img src='/images/AutoStep.gif'/>");

		toolBar.add((com.google.gwt.user.client.ui.Button) bBackup.getPeer().getRepresentative() );
		toolBar.add((com.google.gwt.user.client.ui.Button) bStepOver.getPeer().getRepresentative());
		toolBar.add((com.google.gwt.user.client.ui.Button) bStepInto.getPeer().getRepresentative());
		toolBar.add((com.google.gwt.user.client.ui.Button) bStepOut.getPeer().getRepresentative());
		toolBar.add((com.google.gwt.user.client.ui.Button) bToCursor.getPeer().getRepresentative());
		//toolBar.add((com.google.gwt.user.client.ui.Button) bUpArrow.getPeer().getRepresentative());
		//toolBar.add((com.google.gwt.user.client.ui.Button) bVW);
		//toolBar.add((com.google.gwt.user.client.ui.Button) bAutoStep);
		myWorkPane.setStyleName("tm-largeScrollPanel");
	}

	public void refresh() {
	    // TODO: Compare to CodeDisplaySwing: Why so different?
	    
//		myWorkPane.setStyleName("tm-scrollPanel");

	    //codeDisplayer.setDisplayInfo(((GWTContext) context).getCodeDisplayerInfo());
		boolean allowGaps = true;
		setScale(1, 16);

		SourceCoordsI focus = evaluator.getCodeFocus();
		int focusLine = 0;
		boolean found = false;
		int sz = evaluator.getNumSelectedCodeLines(theFile, allowGaps);
		height = sz * (1 + codeDisplayer.getDisplayInfo().getDelta_y() ) ;
		codeDisplayer.resetSize(1000, height);
		for ( ; focusLine < sz; ++focusLine) {
			CodeLineI codeLine = evaluator.getSelectedCodeLine(theFile, allowGaps, focusLine);
			if (codeLine != null && codeLine.getCoords().equals(focus)) {
				found = true;
				break;
			}
		}
		if (found) {
			int topLine = (1 + focusLine) * getVScale();
			int bottomLine = topLine + getVScale();
			int vertValue = myWorkPane.getHorizontalScrollPosition();
			if (topLine < vertValue || bottomLine > vertValue + myWorkPane.getElement().getClientHeight()) {
				//should convert to PX unit
				myWorkPane.setVerticalScrollPosition(((topLine - 3 * getVScale())*300/600));
			}
		}
		super.refresh();
	}
	
	//Select the line that the mouse clicked inside the code display window 
	public void moveCursor(MouseEvent event){
		CodeDisplayerInfo displayInfo = codeDisplayer.getDisplayInfo() ;
		cursorLine = (event.getY()-displayInfo.getDelta_y()) / displayInfo.getDelta_y();
        context.log(  "delta_y is " + displayInfo.getDelta_y() );
        context.log(  "coursorLine is " + cursorLine );
		codeDisplayer.getDisplayInfo().setCursorLine(cursorLine);
		refresh();
	}

}
