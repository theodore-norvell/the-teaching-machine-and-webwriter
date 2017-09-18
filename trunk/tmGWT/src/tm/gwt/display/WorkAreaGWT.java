package tm.gwt.display;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


public abstract class WorkAreaGWT {
	private RootPanel codeRoot;
	protected ScrollPanel workPane;
	protected ScrollPanelPeer myWorkPane;
	protected HorizontalPanel toolBar;
	final String TITLE_NAME_UNKNOWN = "";
	final String TITLE_NAME_EXPRESSION = "Expression Engine";
	final String TITLE_NAME_HEAP = "Heap";
	final String TITLE_NAME_STATIC = "Static Memory";
	final String TITLE_NAME_STACK = "Stack";
	final String TITLE_NAME_SCRATCH = "Scratch";

	public WorkAreaGWT(String title, String rootName) {
		toolBar = new HorizontalPanel();
		toolBar.setStyleName("tm-hPanel");
		toolBar.setSpacing(3);

		workPane = new ScrollPanel();
		myWorkPane = new ScrollPanelPeer(workPane);
	    myWorkPane.addClickHandler(new ClickHandler() {
	        public void onClick(ClickEvent event) {
	        	MouseJustClicked(event);
	        }
	      });
		
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

	// This is not object oriented and need to change.
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
	
	public void MouseJustClicked(ClickEvent event){
		
	}
	
	//Not finished yet
	public void setPreferredSize(int width, int heght){
		
	}

}
