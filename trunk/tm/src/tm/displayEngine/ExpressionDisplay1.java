package tm.displayEngine;

import tm.interfaces.ImageSourceInterface ;
import tm.portableDisplays.ExpressionDisplayer ;
import tm.subWindowPkg.SmallButton ;
import tm.subWindowPkg.ToolBar ;

@SuppressWarnings("serial")
public class ExpressionDisplay1 extends SwingDisplay {

    public ExpressionDisplay1(DisplayManager dm, String configId ) {
        super( dm, configId, new ExpressionDisplayer(dm.getCommandProcessor(), dm ) ) ;
        

        ImageSourceInterface imageSource = context.getImageSource();
        SmallButton[] buttons = new SmallButton[2];
        buttons[0] = new SmallButton(SmallButton.BACKUP, imageSource);
        buttons[0].setToolTipText("Backup Expression Engine");
        buttons[1] = new SmallButton(SmallButton.ADVANCE, imageSource);
        buttons[1].setToolTipText("Step Expression Engine");
        toolBar = new ToolBar(buttons, "West");
        mySubWindow.addToolBar(toolBar);

//      System.out.println("ExpressionDisplay size is " + getSize());
        setPreferredSize(this.getViewportSize());
        
    }

    @Override
    public void buttonPushed(int i) {
        if (i == 0) commandProcessor.goBack();
        if (i == 1) commandProcessor.goForward();
    }
}
