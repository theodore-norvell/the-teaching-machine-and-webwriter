package tm.portableDisplays;

public abstract class PortableDisplayer extends telford.common.Canvas {

	protected StateInterface model;
	protected PortableContextInterface context;

	public PortableDisplayer(StateInterface model, PortableContextInterface context) {
		super();
		this.model = model;
		this.context = context;
	}

	public abstract void refresh();
}
