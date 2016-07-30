package tm.gwt.client;

import java.util.ArrayList ;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler ;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import telford.common.Kit;
import tm.gwt.display.CodeDisplay1;
import tm.gwt.display.DisplayAdapter ;
import tm.gwt.display.ExpressionDisplay1;
import tm.gwt.display.GWTContext;
import tm.gwt.display.StoreDisplay1;
import tm.gwt.state.MirrorState ;
import tm.gwt.state.StateCommander ;
import tm.gwt.telford.KitGWT;
import tm.gwt.test.TestController ;
import tm.portableDisplays.PortableContextInterface;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TmGWT implements EntryPoint {
    
    ArrayList<DisplayAdapter> displays = new ArrayList<DisplayAdapter>() ;
    
	public void onModuleLoad() {
		
		GWT.log("Start GWT test.", null);
		Kit.setKit(new KitGWT());
		RootPanel menu = RootPanel.get("menuBar");
		menu.add(this.createMenu());
		PortableContextInterface context = new GWTContext();
		MirrorState theState = new MirrorState() ;
		StateCommander controller = new TestController( theState ) ;
		StateCommander commander = this.new TestStateCommander(controller) ;
		
		CodeDisplay1 codeDisplay = new CodeDisplay1(theState, context);
		displays.add( codeDisplay ) ;
		
		ExpressionDisplay1 expDisplay = new ExpressionDisplay1( theState, commander, context);
        displays.add( expDisplay ) ;
		
		StoreDisplay1 staticDisplay = new StoreDisplay1(theState, context, "Static");
        displays.add( staticDisplay ) ;
		
		StoreDisplay1 heapDisplay = new StoreDisplay1(theState, context, "Heap");
        displays.add( heapDisplay ) ;
		
		StoreDisplay1 stackDisplay = new StoreDisplay1(theState, context, "Stack");
        displays.add( stackDisplay ) ;
		
		StoreDisplay1 scratchDisplay = new StoreDisplay1(theState, context, "Scratch");
        displays.add( scratchDisplay ) ;
		
		refresh() ;
	}
	
	private void refresh() {
	    for( DisplayAdapter d : displays ) d.refresh(); 
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
	
	private class TestStateCommander implements StateCommander {

	    private StateCommander controller ;

        TestStateCommander( StateCommander controller ) {
	        this.controller = controller ;
	    }
        
        private Scheduler.ScheduledCommand refreshCommand
        = new Scheduler.ScheduledCommand() {
            @Override public void execute() { TmGWT.this.refresh() ; } } ;
        
        private void refresh() {
            Scheduler.get().scheduleDeferred( refreshCommand );
        }
        
        @Override
        public void goForward() {
            controller.goForward();
            refresh() ;
        }
        
        @Override
        public void goBack() {
            controller.goBack();
            refresh() ;
        }

        @Override
        public void intoExp() {
            controller.intoExp();
            refresh() ;   
        }

        @Override
        public void overAll() {
            controller.overAll();
            refresh() ;
        }

        @Override
        public void intoSub() {
            controller.intoSub();
            refresh() ;
        }
	    
	}
}
