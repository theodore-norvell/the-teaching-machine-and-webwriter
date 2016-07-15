package tm.gwt.display;
import telford.common.Font ;
import telford.common.Kit ;
import tm.interfaces.AsserterI ;
import tm.portableDisplays.PortableContextInterface ;
import tm.utilities.Asserter ;


/**
 * This class used for GWT special context requirement 
 **/
public class GWTContext implements PortableContextInterface {

    @Override
    public Font getCodeFont() {
        return Kit.getKit().getFont("Monospaced", 0, 12) ;
    }

    @Override
    public Font getDisplayFont() {
        return Kit.getKit().getFont("Dialog", 0, 12) ;// Default for most displays
    }

    @Override
    public int getHighlightColor() {
        return 0xFFFF00;
    }

    
    static AsserterI asserter = new GWTAsserter() ;

    @Override
    public AsserterI getAsserter() {
        return asserter ;
    }
}
