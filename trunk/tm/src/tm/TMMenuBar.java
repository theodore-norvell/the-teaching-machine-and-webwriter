package tm;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import tm.interfaces.ExternalCommandInterface;
import tm.interfaces.PlatformServicesInterface;
import tm.interfaces.SelectionInterface;
import tm.plugins.PlugInManager;
import tm.plugins.PlugInManagerDialog;
import tm.utilities.ApologyException;
import tm.virtualMachine.SelectionParser;
import static tm.interfaces.ExternalCommandInterface.* ;

public class TMMenuBar extends JMenuBar {

	protected TMMainPanel tmMainPanel;
	protected PlatformServicesInterface platform;
	protected JMenu fileMenu;
	protected JMenu projectMenu;
	protected JMenu menuView;
	protected JMenu menuGo;
	protected JMenu helpMenu;

	public TMMenuBar(TMMainPanel tmMainPanel, PlatformServicesInterface platform ) {
		this.tmMainPanel = tmMainPanel ;
		this.platform = platform ;

		// The FILE menu
		fileMenu = new JMenu("File");
		add(fileMenu);

		// Restart file
		Action restartAction = new RestartAction() ;
		JMenuItem restartFile = new JMenuItem( restartAction ) ;
		fileMenu.add( restartFile ) ;

		// Exit
		Action exitAction = new ExitAction() ;
		JMenuItem exit = new JMenuItem("Exit");
		fileMenu.add(exit);

		//MenuItem tempItem = new MenuItem( "Test Remote" ) ;
		//tempItem.addActionListener( new ActionListener() {
		//	    public void actionPerformed( ActionEvent e ) {
		//            loadRemoteFile("C++", "cpp_test/ds_revcopy.cpp") ; } } ) ;
		//fileMenu.add( tempItem ) ;


		// The PROJECT menu
		projectMenu = new JMenu("Project");
		add(projectMenu);

		// The VIEW menu
		menuView = new JMenu("View");
		add(menuView);

		// Selections
		JMenuItem selectItem ;
		Action selectAction = new SelectAction( "Select default", ExternalCommandInterface.DEFAULT_SELECTION  ) ;
		selectItem = new JMenuItem( selectAction ) ;
		selectItem.addActionListener( selectAction ) ;
		menuView.add( selectItem ) ;

		selectAction = new SelectAction("Select all", ExternalCommandInterface.COMPLETE_SELECTION ) ;
		selectItem = new JMenuItem( selectAction ) ;
		menuView.add( selectItem ) ;

		// TODO Need a pile of check boxes based on tags
		//            JMenu subMenu ;
		//            subMenu = new JMenu( label[i] ) ;
		//            menuView.add( subMenu ) ;
		//            String[] label = new String[] {"Select a-m", "Select n-z", "Select A-M", "Select N-Z"} ;
		//            char[] start = new char[] { 'a', 'n', 'A', 'N' } ;
		//            char[] end   = new char[] { 'm', 'z', 'M', 'Z' } ;
		//            for( int i=0 ; i < 4 ; ++i ) {
		//                subMenu = new JMenu( label[i] ) ;
		//                menuView.add( subMenu ) ;
		//                for( char ch = start[i] ; ch <= end[i] ; ++ch ) {
		//                    selectAction = new SelectAction( "", selectionString ) ;
		//                    selectItem = new JMenuItem( selectAction ) ;
		//                    subMenu.add( selectItem ) ; } }

		// The GO menu
		menuGo = new JMenu( "Go" ) ;
		add( menuGo ) ;


		JMenuItem goItem ;
		Action goAction = new GoAction("Forward", GOFORWARD ) ;
		goItem = new JMenuItem( goAction ) ;
		menuGo.add( goItem ) ;

		goAction = new GoAction("Undo", GOBACK ) ;
		goItem = new JMenuItem( goAction ) ;
		menuGo.add( goItem ) ;

		goAction = new GoAction("Redo", REDO ) ;
		goItem = new JMenuItem( goAction ) ;
		menuGo.add( goItem ) ;

		goAction = new GoAction("Micro Step", MICROSTEP ) ;
		goItem = new JMenuItem( goAction ) ;
		menuGo.add( goItem ) ;

		goAction = new GoAction("Over", OVERALL ) ;
		goItem = new JMenuItem( goAction ) ;
		menuGo.add( goItem ) ;

		goAction = new GoAction("Into Expression", INTOEXP ) ;
		goItem = new JMenuItem( goAction ) ;
		menuGo.add( goItem ) ;

		goAction = new GoAction("Into Subroutine", INTOSUB ) ;
		goItem = new JMenuItem( goAction ) ;
		menuGo.add( goItem ) ;

		// !!! Can't do toCursor without access to the cursor !!!
		// But we could pop up a dialog to get a line number.

		// The HELP menu
		helpMenu = new JMenu("Help");
		add(helpMenu);

		Action helpAction = new HelpAction() ;
		JMenuItem helpItem = new JMenuItem(helpAction);
		helpMenu.add( helpItem ) ;

		Action aboutAction = new AboutAction() ;
		JMenuItem aboutItem = new JMenuItem(aboutAction);
		helpMenu.add( aboutItem ) ;

		// End of menus

	}

	public JMenu getViewMenu() {
		return menuView ;
	}

	// Actions //
	///////////////

	class ExitAction extends AbstractAction {
		ExitAction() { super( "Exit" ) ; }

		public void actionPerformed( ActionEvent e ) {
			platform.exit(  ) ; } }

	class RestartAction extends AbstractAction {
		RestartAction() { super( "Restart" ) ; }

		public void actionPerformed( ActionEvent e ) {
			tmMainPanel.reStart( ) ; } }
	
	private static final int GOFORWARD=0, GOBACK=1,
			OVERALL = 2, MICROSTEP = 3, INTOEXP = 4, INTOSUB = 5, REDO = 6 ;

	class GoAction extends AbstractAction {
		int howMuch ;

		public GoAction(String name, int _howMuch ) { super(name) ; howMuch = _howMuch ; }

		public void actionPerformed( ActionEvent e ) {
			switch( howMuch ) {
			case GOFORWARD : tmMainPanel.goForward() ; break ;
			case GOBACK : tmMainPanel.goBack() ; break ;
			case REDO : tmMainPanel.redo() ; break ;
			case OVERALL : tmMainPanel.overAll() ; break ;
			case MICROSTEP : tmMainPanel.microStep() ; break ;
			case INTOEXP : tmMainPanel.intoExp() ; break ;
			case INTOSUB : tmMainPanel.intoSub() ; break ; } }
	}

	class SelectAction extends AbstractAction {
		String selectionString ;

		public SelectAction( String name, String selectionString ) {
			super( name ) ;
			this.selectionString = selectionString ; }

		public void actionPerformed( ActionEvent e ) {
			tmMainPanel.setSelectionString( selectionString ) ; }
	}

	class CustomizeSelectionAction extends AbstractAction {
		public CustomizeSelectionAction() {
			super( "Customize Selection ...") ;
		}

		public void actionPerformed( ActionEvent e ) {
			String str = tmMainPanel.getSelectionString() ;
			//TODO Put up a dialog for selection strings.
			//			SelectionInterface sel = null ;
			//			do {
			//				str = JOptionPane.showInputDialog(TMMainFrame.this,
			//						"Enter the new selection.\n"+
			//								"Use ! for 'not', & for 'and', and | for 'or'.\n"+
			//								"'s' is for scripts, 'l' is for library.",
			//								str ) ;
			//
			//				if( str == null ) return ; // Cancel. No change.
			//
			//				try { sel = SelectionParser.parse( str ) ; }
			//				catch(ApologyException apology) { }
			//
			//				if( sel == null ) {
			//					JOptionPane.showMessageDialog(TMMainFrame.this, "Could not parse." ) ; }
			//			} while( sel == null ) ;
			//			TMMainFrame.this.commandProcessor.setSelection( sel ) ;
		}
	}

	class AboutAction extends AbstractAction {
		AboutAction() { super( "About" ) ; }

		public void actionPerformed( ActionEvent e ) {
			String message = "The Teaching Machine.\n\n"
					+ tmMainPanel.getVersionString() + "\n\n"
					+ TMMainPanel.COPYRIGHT + "\n\n"
					+ TMMainPanel.PROGRAMMERS;
			tmMainPanel.attention( message ) ;
		} }

	class HelpAction extends AbstractAction {
		HelpAction() { super( "Help" ) ; }

		public void actionPerformed( ActionEvent e ) {
			tmMainPanel.help() ; } }



}