package tm.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import telford.common.Kit;
import tm.gwt.display.CodeDisplay1;
import tm.gwt.display.ConcreteMirrorState;
import tm.gwt.display.ExpressionDisplay1;
import tm.gwt.display.GWTContext;
import tm.gwt.jsInterface.MirrorStateTest;
import tm.gwt.telford.KitGWT;
import tm.interfaces.Selection ;
import tm.portableDisplays.PortableContextInterface;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TmGWT implements EntryPoint {
	private static ExpressionDisplay1 expDisplay;
	public void onModuleLoad() {
		getExpressionDisplay();
		setMirrorState();
		
		GWT.log("Start GWT test.", null);
		Kit.setKit(new KitGWT());
		RootPanel menu = RootPanel.get("menuBar");
		menu.add(this.createMenu());
		PortableContextInterface context = new GWTContext();
		CodeDisplay1 codeDisplay = new CodeDisplay1(new ConcreteMirrorState(new Selection(Selection.TokenType.TRUE)),
				context);
		codeDisplay.refresh();
		
		expDisplay = new ExpressionDisplay1(new ConcreteMirrorState(new Selection(Selection.TokenType.TRUE)),
				context);
		expDisplay.refresh();
		
	}

	private static ExpressionDisplay1 getDisplayer() {
		return expDisplay;
	}

	private static void setEvaluator(ExpressionDisplay1 expDisplay, MirrorStateTest evaluator) {
		expDisplay.setJsMirrorStateTest(evaluator);
	}
	
	public Widget createMenu() {
	    // Create a command that will execute on menu item selection
	    Command menuCommand = new Command() {
	      private int curPhrase = 0;
	      private final String[] phrases = {"Don't click me","Simple menu sample","Try another one"};

	      public void execute() {
	        Window.alert(phrases[curPhrase]);
	        curPhrase = (curPhrase + 1) % phrases.length;
	      }
	    };

	    // Create a menu bar
	    MenuBar menu = new MenuBar();
	    menu.setAutoOpen(true);
	    menu.setWidth("325px");
	    menu.setAnimationEnabled(true);

	    // Create a sub menu of recent documents
	    MenuBar recentDocsMenu = new MenuBar(true);
	    String[] recentDocs = {"Finishing","How to","Guide"};
	    for (int i = 0; i < recentDocs.length; i++) {
	      recentDocsMenu.addItem(recentDocs[i], menuCommand);
	    }

	    // Create the file menu
	    MenuBar fileMenu = new MenuBar(true);
	    fileMenu.setAnimationEnabled(true);
	    menu.addItem(new MenuItem("File", fileMenu));
	    String[] fileOptions = {"New","Open","Close","Recent","Exit"};
	    for (int i = 0; i < fileOptions.length; i++) {
	      if (i == 3) {
	        fileMenu.addSeparator();
	        fileMenu.addItem(fileOptions[i], recentDocsMenu);
	        fileMenu.addSeparator();
	      } else {
	        fileMenu.addItem(fileOptions[i], menuCommand);
	      }
	    }
	    
	 // Create the edit menu
	    MenuBar editMenu = new MenuBar(true);
	    menu.addItem(new MenuItem("Edit", editMenu));
	    String[] editOptions = {"Undo","Redo","Cut","Copy","Paste"};
	    for (int i = 0; i < editOptions.length; i++) {
	      editMenu.addItem(editOptions[i], menuCommand);
	    }

	    // Create the GWT menu
	    MenuBar gwtMenu = new MenuBar(true);
	    menu.addItem(new MenuItem("GWT", true, gwtMenu));
	    String[] gwtOptions = {"Download","Examples","Source Code","GWT wit's the program"};
	    for (int i = 0; i < gwtOptions.length; i++) {
	      gwtMenu.addItem(gwtOptions[i], menuCommand);
	    }

	    // Create the help menu
	    MenuBar helpMenu = new MenuBar(true);
	    menu.addSeparator();
	    menu.addItem(new MenuItem("Help", helpMenu));
	    String[] helpOptions = {"Contents","Cookies","About GWT"};
	    for (int i = 0; i < helpOptions.length; i++) {
	      helpMenu.addItem(helpOptions[i], menuCommand);
	    }

	    // Return the menu
	    menu.ensureDebugId("cwMenuBar");
	    return menu;
	  }

	// ==========JSNI methods================
	private static native void getExpressionDisplay() /*-{
		$wnd.getExpressionDisplay = function() {
			return @tm.gwt.client.TmGWT::getDisplayer()();
		}
	}-*/;

	private static native void setMirrorState() /*-{
		$wnd.setMirrorState = function(obj1, obj2) {
			@tm.gwt.client.TmGWT::setEvaluator(Ltm/gwt/display/ExpressionDisplay1;Ltm/gwt/jsInterface/MirrorStateTest;)(obj1,obj2);
		}
	}-*/;
}
