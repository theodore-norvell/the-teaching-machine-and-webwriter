package tm.portableDisplays;

import telford.common.Canvas ;
import tm.interfaces.CommandInterface ;

public abstract class PortableDisplay {

    public abstract void drawArea(Canvas canvas, CommandInterface commandProcessor) ;

    public abstract void refresh(CommandInterface commandProcessor) ;

}
