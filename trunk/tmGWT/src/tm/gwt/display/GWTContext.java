package tm.gwt.display;

import telford.common.Font;
import telford.common.Kit;
import tm.interfaces.AsserterI ;
import tm.portableDisplays.CodeDisplayerInfo;
import tm.portableDisplays.StoreDisplayerInfo ;
import tm.portableDisplays.PortableContextInterface ;

/**
 * This class used for GWT special context requirement 
 **/
public class GWTContext implements PortableContextInterface {
    
    private AsserterI asserter = new GWTAsserter() ;
    
	public CodeDisplayerInfo getCodeDisplayerInfo() {
		CodeDisplayerInfo info = new CodeDisplayerInfo();
		// TODO should coming from server side
		info.setCursorColor(65280);
		info.setCursorLine(0);
		info.setFocusLineNumber(27);
		int fontMapper[] = { 0, 1, 2, 1, 1, 0 };
		int fontColor[] = { 0, 10040115, 37440, 10040115, 2752767, 16711680 };
		Font f1 = Kit.getKit().getFont("Monospaced", 0, 12);
		Font f2 = Kit.getKit().getFont("Monospaced", 1, 12);
		Font f3 = Kit.getKit().getFont("Monospaced", 2, 12);
		Font f4 = Kit.getKit().getFont("Monospaced", 3, 12);
		info.setMyFontByIndex(f1, 0);
		info.setMyFontByIndex(f2, 1);
		info.setMyFontByIndex(f3, 2);
		info.setMyFontByIndex(f4, 3);
		info.setFontMapper(fontMapper);
		info.setFontColor(fontColor);
		return info;
	}
	
	// TODO should coming from server side
	public StoreDisplayerInfo getStoreDisplayerInfo() {
		StoreDisplayerInfo info = new StoreDisplayerInfo();
		//MirrorRegion region = new MirrorRegion(); 
		//info.setRegion(region);
		return info;
	}

    @Override
    public Font getCodeFont() {
        return Kit.getKit().getFont("Monospaced", 0, 12) ;
    }

    @Override
    public Font getDisplayFont() {
        // TODO Auto-generated method stub
        return Kit.getKit().getFont("Dialog", 0, 12) ;
    }

    @Override
    public int getHighlightColor() {
        return 0xFFFF00;
    }

    @Override
    public AsserterI getAsserter() {
        return asserter ;
    }
}
