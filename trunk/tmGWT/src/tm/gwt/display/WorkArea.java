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
	
	public WorkArea(String title ,String rootName){
		toolBar = new HorizontalPanel();
		toolBar.setStyleName("tm-hPanel");
	    toolBar.setSpacing(3);
	    
	    myWorkPane = new ScrollPanel();
	    
	    VerticalPanel vpanel = new VerticalPanel();
	    vpanel.setStyleName("tm-vPanel");
	    vpanel.setSpacing(5);
	    
	    vpanel.add(new HTML(title));
	    vpanel.add(toolBar);
	    vpanel.add(myWorkPane);
		
		codeRoot = RootPanel.get(rootName);
		codeRoot.add(vpanel);
	}

}
