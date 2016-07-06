package tm.portableDisplays;

import telford.common.Kit;

public class PortableContext implements PortableContextInterface {

    @Override
    public telford.common.Font getCodeFont() {
        // TODO The font sould be based on preferences from the configuration.
//        return Kit.getKit().getFont() ;
        return Kit.getKit().getFont("Monospaced", 0, 12) ;// Default for code displays
    }

    @Override
    public telford.common.Font getDisplayFont() {
        // TODO The font sould be based on preferences from the configuration.
    	return Kit.getKit().getFont("Dialog", 0, 12) ;// Default for most displays
    }
    
    @Override
    public int getHighlightColor() {
        //yellow
        return 0xFFFF00;
    }
}