package tm.portableDisplaysGWT;

import telford.common.Kit;

public class PortableContext implements PortableContextInterface {

    @Override
    public telford.common.Font getCodeFont() {
        // TODO The font sould be based on preferences from the configuration.
        return Kit.getKit().getFont() ;
    }

    @Override
    public telford.common.Font getDisplayFont() {
        // TODO The font sould be based on preferences from the configuration.
        return Kit.getKit().getFont() ;
    }
    
    @Override
    public int getHighlightColor() {
        // TODO The font sould be based on preferences from the configuration.
    	//greenyellow
        return 0xffff00;
    }
}