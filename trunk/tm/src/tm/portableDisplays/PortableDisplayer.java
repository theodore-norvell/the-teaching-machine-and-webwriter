package tm.portableDisplays;

import tm.interfaces.EvaluatorInterface ;
import tm.interfaces.PortableDisplayContext ;

public abstract class PortableDisplayer extends telford.common.Canvas {

    protected EvaluatorInterface model ;
    protected PortableDisplayContext context ;

    public PortableDisplayer(EvaluatorInterface model, PortableDisplayContext context) {
        super() ;
        this.model = model ;
        this.context = context ;
    }

    public abstract void refresh() ;

}
