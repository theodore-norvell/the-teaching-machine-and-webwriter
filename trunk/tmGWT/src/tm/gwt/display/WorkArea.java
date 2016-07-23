package tm.gwt.display;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public abstract class WorkArea {
	private RootPanel codeRoot;
	protected ScrollPanel myWorkPane;
	protected HorizontalPanel toolBar;
	final String TITLE_NAME_UNKNOWN = "";
	final String TITLE_NAME_EXPRESSION = "Expression Engine";
	final String TITLE_NAME_HEAP = "Heap";
	final String TITLE_NAME_STATIC = "Static Memory";
	final String TITLE_NAME_STACK = "Stack";
	final String TITLE_NAME_SCRATCH = "Scratch";

	public WorkArea(String title, String rootName) {
		toolBar = new HorizontalPanel();
		toolBar.setStyleName("tm-hPanel");
		toolBar.setSpacing(3);

		myWorkPane = new ScrollPanel();

		VerticalPanel vpanel = new VerticalPanel();
		vpanel.setStyleName("tm-vPanel");
		vpanel.setSpacing(5);
		if (title == null)
			title = getTitleByRootName(rootName);
		vpanel.add(new HTML(title));
		vpanel.add(toolBar);
		vpanel.add(myWorkPane);

		codeRoot = RootPanel.get(rootName);
		codeRoot.add(vpanel);
	}

	private String getTitleByRootName(String rootName) {
		String title = TITLE_NAME_UNKNOWN;
		if (rootName.equalsIgnoreCase("expDisplayPanel")) {
			title = TITLE_NAME_EXPRESSION;
		} else if (rootName.equalsIgnoreCase("Heap")) {
			title = TITLE_NAME_HEAP;
		} else if (rootName.equalsIgnoreCase("Static")) {
			title = TITLE_NAME_STATIC;
		} else if (rootName.equalsIgnoreCase("Stack")) {
			title = TITLE_NAME_STACK;
		} else if (rootName.equalsIgnoreCase("Scratch")) {
			title = TITLE_NAME_SCRATCH;
		} else {
			title = TITLE_NAME_UNKNOWN;
		}
		return title;
	}

}
