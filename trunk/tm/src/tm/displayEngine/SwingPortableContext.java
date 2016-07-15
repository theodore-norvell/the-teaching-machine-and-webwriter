package tm.displayEngine;

import telford.common.Kit ;
import tm.interfaces.AsserterI ;
import tm.portableDisplays.PortableContextInterface ;
import tm.utilities.Asserter ;

public class SwingPortableContext implements PortableContextInterface {

    @Override
    public telford.common.Font getCodeFont() {
        // TODO The font should be based on preferences from the configuration.
//        return Kit.getKit().getFont() ;
        return Kit.getKit().getFont("Monospaced", 0, 12) ;// Default for code displays
    }

    @Override
    public telford.common.Font getDisplayFont() {
        // TODO The font should be based on preferences from the configuration.
    	return Kit.getKit().getFont("Dialog", 0, 12) ;// Default for most displays
    }
    
    @Override
    public int getHighlightColor() {
        //yellow
        return 0xFFFF00;
    }

    static Asserter asserter = new Asserter() ;
    
    @Override
    public AsserterI getAsserter() {
        return asserter ;
    }
}