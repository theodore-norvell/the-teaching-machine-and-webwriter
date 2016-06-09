package tm.portableDisplays;

import tm.interfaces.EvaluatorInterface;
import tm.portableDisplaysGWT.PortableContextInterface;

public abstract class PortableDisplayer extends telford.common.Canvas {

    protected EvaluatorInterface model ;
    protected PortableContextInterface context ;

    public PortableDisplayer(EvaluatorInterface model, PortableContextInterface context) {
        super() ;
        this.model = model ;
        this.context = context ;
    }

    public abstract void refresh() ;

}
