/**
 * ViewHandler.java - play.ide.handler - PLAY
 * 
 * Created on Feb 9, 2012 by Kai Zhu
 */
package play.ide.handler;

import higraph.view.layout.NestedTreeLayoutManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingUtilities;

import play.controller.Controller;
import play.higraph.model.PLAYNode;
import play.higraph.model.PLAYPayload;
import play.higraph.model.PLAYSubgraph;
import play.higraph.model.PLAYTag;
import play.higraph.model.PLAYWholeGraph;
import play.higraph.swing.PLAYHigraphJComponent;
import play.higraph.swing.PLAYHigraphJComponentKeyAdapter;
import play.higraph.swing.PLAYSubgraphMouseAdapter;
import play.higraph.view.PLAYHigraphView;
import play.higraph.view.PLAYNodeView;
import play.higraph.view.PLAYSubgraphEventObserver;
import play.higraph.view.PLAYViewFactory;
import play.higraph.view.layout.PLAYBoxesInBoxesLayout;
import play.higraph.view.model.PLAYViewSelectionModel;
import play.ide.view.NewClass;
import play.ide.view.ClosableTabPanel;
import play.ide.view.ExpsPallet;
import play.ide.view.NodesOutline;
import play.ide.view.PropertyPanel;
import play.ide.view.Symbols;
import play.ide.view.SyntaxPallet;
import play.ide.view.ViewPropertiesPanel;
import tm.backtrack.BTTimeManager;

/**
 * @author Kai Zhu
 * 
 */
public class ViewHandler {

	// the main frame need to be showed
	private JFrame mainFrame;

	//private JTabbedPane tabbedPane;
	
	private JPanel jPanel;

	private PLAYWholeGraph playWholeGraph;

	private PLAYSubgraph playSubgraph;

	private PLAYHigraphJComponent playHigraphJComponent;

	private PLAYViewFactory viewFactory;

	private PLAYHigraphView playHigraphView;
	
	private NodesOutline nodesOutline;

	private PropertyPanel propertyPanel;

	private JMenuItem undoMenuItem;

	private BTTimeManager btTimeManager;
	
	private PLAYSubgraphEventObserver subgraphEventObserver;
	
	private PLAYSubgraphMouseAdapter subgraphMouseAdapter;
	
	private NewClass newClass;
	
	private SyntaxPallet sp;
	
	private int i = 0;

	public ViewHandler(BTTimeManager btTimeManager) {
		this.btTimeManager = btTimeManager;
		this.mainFrame = new JFrame();
		//this.tabbedPane = new JTabbedPane();
		this.jPanel = new JPanel(new BorderLayout());
		//this.tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		this.addPLAYHigraphJComponent();
		this.nodesOutline = new NodesOutline();
		this.sp = new SyntaxPallet(playWholeGraph);
		//this.classList = new ClassList(this);
		this.mainFrame.setTitle("PLAY - PLAY Language IDE");
		this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.mainFrame.addWindowListener(new WindowAdapter() {

			/**
			 * Exit application and save data file
			 */
			private void close() {
				System.exit(0);
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				this.close();
			}

			public void windowClosed(WindowEvent e) {
				this.close();
			}

		});
		this.mainFrame.setJMenuBar(this.getMenuBar());

		this.propertyPanel = new PropertyPanel();

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setPreferredSize(new Dimension(1024, 500));
		mainPanel.add(this.getFrameSplitPane(), BorderLayout.CENTER);
		this.mainFrame.getContentPane().add(mainPanel);
		this.mainFrame.pack();
		
		/*playWholeGraph = new PLAYWholeGraph(this.btTimeManager);
		viewFactory = new PLAYViewFactory(this.btTimeManager);
		playSubgraph = new PLAYSubgraph(playWholeGraph);
		playHigraphJComponent = new PLAYHigraphJComponent();
		playHigraphView = viewFactory.makeHigraphView(
				playWholeGraph, playHigraphJComponent);
		subgraphEventObserver = new PLAYSubgraphEventObserver(
				playHigraphView, playWholeGraph, viewFactory, playSubgraph);
		subgraphMouseAdapter = new PLAYSubgraphMouseAdapter(
				playHigraphView, subgraphEventObserver);
		playHigraphJComponent
		.addKeyListener(new PLAYHigraphJComponentKeyAdapter(
				playHigraphView));
		playHigraphJComponent.setBackground(Color.WHITE);
		playHigraphJComponent.setAutoscrolls(true);
		playHigraphJComponent.setSubgraphView(playHigraphView);
		playHigraphView.setLayoutManager(new PLAYBoxesInBoxesLayout(
				NestedTreeLayoutManager.Axis.Y));
		subgraphMouseAdapter.installIn(playHigraphJComponent);*/
		
	}

	private JSplitPane getFrameSplitPane() {
		JSplitPane frameSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		frameSplitPane.setOneTouchExpandable(true);
		frameSplitPane.setDividerLocation(750);
		frameSplitPane.setLeftComponent(this.getLeftCenterSplitPane());
		frameSplitPane.setRightComponent(this.getRightTopBottomSplitPane());
		return frameSplitPane;
	}

	private JSplitPane getLeftCenterSplitPane() {
		JSplitPane leftCenterSplitPane = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT);
		leftCenterSplitPane.setOneTouchExpandable(true);
		leftCenterSplitPane.setDividerLocation(150);
		leftCenterSplitPane.setLeftComponent(this.getLeftTopBottomSplitPane());
		//leftCenterSplitPane.setRightComponent(this.tabbedPane);
		leftCenterSplitPane.setRightComponent(this.jPanel);
		
		return leftCenterSplitPane;
	}

	private JSplitPane getLeftTopBottomSplitPane() {
		JSplitPane leftTopBottomSplitPane = new JSplitPane(
				JSplitPane.VERTICAL_SPLIT);
		leftTopBottomSplitPane.setOneTouchExpandable(true);
		leftTopBottomSplitPane.setDividerLocation(300);
		leftTopBottomSplitPane.setTopComponent(sp);
		leftTopBottomSplitPane.setBottomComponent(new Symbols());
		return leftTopBottomSplitPane;
	}

	private JSplitPane getRightTopBottomSplitPane() {
		JSplitPane rightTopBottomSplitPane = new JSplitPane(
				JSplitPane.VERTICAL_SPLIT);
		rightTopBottomSplitPane.setOneTouchExpandable(true);
		rightTopBottomSplitPane.setDividerLocation(300);
		JScrollPane scrollPane = new JScrollPane(this.nodesOutline);
		scrollPane.setFocusable(true);
		rightTopBottomSplitPane.setTopComponent(scrollPane);
		//rightTopBottomSplitPane.setBottomComponent(classList);
		// TODO
		rightTopBottomSplitPane.setBottomComponent(new JPanel());
		return rightTopBottomSplitPane;
	}
	

	private JMenuBar getMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(fileMenu);

		fileMenu.addSeparator();

		JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4,
				ActionEvent.ALT_MASK));
		exitMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ViewHandler.this.mainFrame.dispose();
			}

		});
		fileMenu.add(exitMenuItem);

		JMenu editMenu = new JMenu("Edit");
		editMenu.setMnemonic(KeyEvent.VK_E);
		menuBar.add(editMenu);

		this.undoMenuItem = new JMenuItem("Undo");
		this.undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				ActionEvent.CTRL_MASK));
		this.undoMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Controller.getInstance().undo();
				Controller.getInstance().refresh(playHigraphView);
			}

		});
		editMenu.add(this.undoMenuItem);
		editMenu.addSeparator();

		JMenu runMenu = new JMenu("Run");
		runMenu.setMnemonic(KeyEvent.VK_R);
		menuBar.add(runMenu);

		JMenuItem runMenuItem = new JMenuItem("Run");
		runMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F11,
				ActionEvent.CTRL_MASK));
		runMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Controller.getInstance().run();
			}

		});
		runMenu.add(runMenuItem);

		return menuBar;
	}

	public void addPLAYHigraphJComponent() {
		/*PLAYWholeGraph*/ playWholeGraph = new PLAYWholeGraph(this.btTimeManager);
		/*PLAYViewFactory*/ viewFactory = new PLAYViewFactory(this.btTimeManager);
		/*PLAYSubgraph*/ playSubgraph = playWholeGraph.makeSubGraph();
		/*PLAYHigraphJComponent*/ playHigraphJComponent = new PLAYHigraphJComponent();
		/*PLAYHigraphView*/ playHigraphView = viewFactory.makeHigraphView(
				playSubgraph, playHigraphJComponent);
		/*PLAYSubgraphEventObserver*/ subgraphEventObserver = new PLAYSubgraphEventObserver(
				playHigraphView, playWholeGraph, viewFactory, playSubgraph);
		/*PLAYSubgraphMouseAdapter*/ subgraphMouseAdapter = new PLAYSubgraphMouseAdapter(
				playHigraphView, subgraphEventObserver);
		playHigraphJComponent
		.addKeyListener(new PLAYHigraphJComponentKeyAdapter(
				playHigraphView));
		playHigraphJComponent.setBackground(Color.WHITE);
		playHigraphJComponent.setAutoscrolls(true);
		playHigraphJComponent.setSubgraphView(playHigraphView);
		playHigraphView.setLayoutManager(new PLAYBoxesInBoxesLayout(
				NestedTreeLayoutManager.Axis.Y));
		subgraphMouseAdapter.installIn(playHigraphJComponent);
		//this.jPanel.revalidate();
		//this.jPanel.removeAll();
		//this.jPanel.repaint();
		JScrollPane scrollPane = new JScrollPane(playHigraphJComponent);
		scrollPane.setFocusable(true);				
		
		this.jPanel.add(scrollPane);
		System.out.println("jcomponent" + playHigraphJComponent);
		//playWholeGraph.makeRootNode(new PLAYPayload(PLAYTag.CLASS
			//	.toString(),PLAYTag.CLASS));
		//Controller.getInstance().refresh(playHigraphView);
		
		//		Object[][] className = {{"class1"}};
		
		//JTable table = sp.getList();
		//System.out.println(table);
		//table.set
		
		//System.out.println(table.getRowCount());
		//System.out.println(table.getColumnCount());
		
		//table.getModel().
		
		//table.setValueAt(className, table.getRowCount()+1, table.getColumnCount());
		/*String title = "Class" + this.tabbedPane.getTabCount() ; 
		this.tabbedPane
		.add(title, scrollPane);
		this.tabbedPane.setTabComponentAt(
				this.tabbedPane.getTabCount() - 1,
				new ClosableTabPanel(this.tabbedPane, title));
		this.tabbedPane.setSelectedIndex(this.tabbedPane.getTabCount() - 1);*/
		
	}
	
	/* 
	 * creates a new class on click of the + button
	 */
	public void newClass(){
		System.out.println("new class");
		
		for (PLAYNode n:playSubgraph.getTops() ) {
			System.out.println(n);
			playSubgraph.removeTop( n ) ;
		}
		
		PLAYTag currentTag = PLAYTag.CLASS;
		PLAYPayload playLoad =new PLAYPayload(currentTag.toString(), currentTag);
		PLAYNode node = playWholeGraph.makeRootNode(playLoad);
		playSubgraph.addTop(node);
		System.out.println("jcomponent = "+ playHigraphJComponent);
		System.out.println("node = " + node);
		NewClass nc = new NewClass(playWholeGraph,viewFactory,playSubgraph,playHigraphJComponent,
				subgraphEventObserver,subgraphMouseAdapter,jPanel,btTimeManager,playHigraphView,i,node);
		SyntaxPallet.setClass(nc);
		i++;
		Controller.getInstance().refresh(this.playHigraphView);
		
	}
	
	public void showMainFrame() {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				ViewHandler.this.mainFrame.setVisible(true);
			}

		});
	}

	public void updateNodesOutline(PLAYHigraphView higraphView) {
		if (higraphView != null) {
			this.nodesOutline.update(higraphView,
					higraphView.getDeletedNodeViewList());
		}
	}

	/**
	 * @param string
	 */
	public void showMessage(String string) {
		JOptionPane.showMessageDialog(this.playHigraphJComponent, string);
	}

	/**
	 * @param currentDescription
	 */
	public void modifyUndoMenuItem(String description) {
		if (description != null) {
			this.undoMenuItem.setEnabled(true);
			this.undoMenuItem.setText("Undo " + description);
		} else {
			this.undoMenuItem.setText("Undo");
			this.undoMenuItem.setEnabled(false);
		}
	}

	public void updataPropertyPanel(
			PLAYViewSelectionModel playViewSelectionModel) {
		List<PLAYNodeView> list = playViewSelectionModel.getSelectedViewList();
		this.propertyPanel.update(list);
	}

	public ViewPropertiesPanel getViewPropertiesPanel() {
		return this.propertyPanel.getViewPropertiesPanel();
	}
}
