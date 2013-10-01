package telford.cn1;

import com.codename1.ui.*;

public class FontCN1 implements telford.common.Font {
	Font font ;
	
	public FontCN1( Font f) { this.font = f ; }

	public int stringWidth(String message) { return font.stringWidth( message ) ; }

	public int getHeight() { return font.getHeight() ; }
}
