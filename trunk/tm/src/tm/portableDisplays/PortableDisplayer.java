package tm.portableDisplays;

import tm.configuration.Configuration;
import tm.interfaces.EvaluatorInterface;
import tm.portableDisplaysGWT.PortableContextInterface;

public abstract class PortableDisplayer extends telford.common.Canvas {

	protected EvaluatorInterface model;
	protected PortableContextInterface context;

	public PortableDisplayer(EvaluatorInterface model, PortableContextInterface context) {
		super();
		this.model = model;
		this.context = context;
	}

	public abstract void refresh();

	//currently only used for CodeDisplay. It may be abstract later if shared by more than displayer
	public void notifyOfSave(Configuration config) {
	};
	//currently only used for CodeDisplay
	public void notifyOfLoad(Configuration config) {
	};
}
