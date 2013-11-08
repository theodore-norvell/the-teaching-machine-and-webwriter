package ratRace;

import java.io.IOException;

import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;

import telford.common.Kit;

public class Main {

    public void init(Object context) {
        try{
            Resources theme = Resources.openLayered("/theme");
            UIManager.getInstance().setThemeProps(theme.getTheme(theme.getThemeResourceNames()[0]));
            Kit.setKit( new telford.cn1.KitCN1() ) ; 
       }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public void start() {
		RatRace ratRace = new RatRace() ;
    }

    public void stop() {
    }
    
    public void destroy() {
    }
}
