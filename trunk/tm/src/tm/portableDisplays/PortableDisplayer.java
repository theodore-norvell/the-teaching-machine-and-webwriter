package tm.portableDisplays;

import tm.interfaces.DisplayContextInterface ;
import tm.interfaces.EvaluatorInterface ;

public abstract class PortableDisplayer extends telford.common.Canvas {

    protected EvaluatorInterface model ;
    protected DisplayContextInterface context ;

    public PortableDisplayer(EvaluatorInterface model, DisplayContextInterface context) {
        super() ;
        this.model = model ;
        this.context = context ;
    }

    public abstract void refresh() ;

}
