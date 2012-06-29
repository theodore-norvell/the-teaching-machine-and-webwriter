/**
 * ViewHandler.java - play.ide.handler - PLAY
 * 
 * Created on Feb 9, 2012 by Kai Zhu
 */
package play.ide.handler;

import higraph.model.interfaces.Node;
import higraph.view.layout.NestedTreeLayoutManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import play.controller.Controller;
import play.higraph.model.PLAYSubgraph;
import play.higraph.model.PLAYWholeGraph;
import play.higraph.swing.PLAYHigraphJComponent;
import play.higraph.swing.PLAYHigraphJComponentKeyAdapter;
import play.higraph.swing.PLAYSubgraphMouseAdapter;
import play.higraph.view.PLAYHigraphView;
import play.higraph.view.PLAYNodeView;
import play.higraph.view.PLAYSubgraphEventObserver;
import play.higraph.view.PLAYViewFactory;
import play.higraph.view.layout.PLAYBoxesInBoxesLayout;
import play.higraph.view.model.NodeViewSelectionModel;
import play.ide.view.ExpsPallet;
import play.ide.view.NodesOutline;
import play.ide.view.SyntaxPallet;
import tm.backtrack.BTTimeManager;

/**
 * @author Kai Zhu
 * 
 */
public class ViewHandler {

    // the main frame need to be showed
    private JFrame mainFrame;

    private PLAYWholeGraph playWholeGraph;

    private PLAYSubgraph playSubgraph;

    private PLAYHigraphJComponent playHigraphJComponent;

    private PLAYHigraphView playHigraphView;

    private NodeViewSelectionModel nodeViewSelectionModel;

    private NodesOutline nodesOutline;

    private JMenuItem undoMenuItem;

    public ViewHandler(BTTimeManager btTimeManager) {
	this.mainFrame = new JFrame();
	this.playWholeGraph = new PLAYWholeGraph(btTimeManager);
	this.playSubgraph = new PLAYSubgraph(this.playWholeGraph);
	this.playHigraphJComponent = new PLAYHigraphJComponent();
	PLAYViewFactory viewFactory = new PLAYViewFactory(btTimeManager);
	this.playHigraphView = viewFactory.makeHigraphView(this.playWholeGraph,
		this.playHigraphJComponent);
	PLAYSubgraphEventObserver subgraphEventObserver = new PLAYSubgraphEventObserver(
		this.playHigraphView, this.playWholeGraph, viewFactory,
		this.playSubgraph);
	PLAYSubgraphMouseAdapter subgraphMouseAdapter = new PLAYSubgraphMouseAdapter(
		this.playHigraphView, subgraphEventObserver);
	this.nodesOutline = new NodesOutline(this.playHigraphView);
	this.playHigraphJComponent
		.addKeyListener(new PLAYHigraphJComponentKeyAdapter(
			this.playHigraphView));
	this.playHigraphJComponent.setBackground(Color.WHITE);
	this.playHigraphJComponent.setAutoscrolls(true);
	this.playHigraphJComponent.setSubgraphView(this.playHigraphView);
	this.playHigraphView.setLayoutManager(new PLAYBoxesInBoxesLayout(
		NestedTreeLayoutManager.Axis.Y));
	subgraphMouseAdapter.installIn(this.playHigraphJComponent);

	this.nodeViewSelectionModel = new NodeViewSelectionModel();

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

	JPanel mainPanel = new JPanel();
	mainPanel.setLayout(new BorderLayout());
	mainPanel.setPreferredSize(new Dimension(1024, 768));
	mainPanel.add(this.getFrameSplitPane(), BorderLayout.CENTER);
	JButton button = new JButton("Temp");
	button.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		for (Node<?, ?, ?, ?, ?, ?, ?> node : playWholeGraph.getTops()) {
		    node.delete();
		}
		playHigraphJComponent.scrollRectToVisible(new Rectangle(0,
			(int) playHigraphJComponent.getSize().getHeight() - 50,
			100, 100));
		playHigraphJComponent
			.setPreferredSize(new Dimension(
				(int) playHigraphJComponent.getSize()
					.getWidth() + 100,
				(int) playHigraphJComponent.getSize()
					.getHeight() + 100));
		playHigraphView.refresh();

		playHigraphJComponent.revalidate();
		playHigraphJComponent.repaint();
	    }

	});
	mainPanel.add(button, BorderLayout.SOUTH);
	this.mainFrame.getContentPane().add(mainPanel);
	this.mainFrame.pack();
    }

    private JSplitPane getFrameSplitPane() {
	JSplitPane frameSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	frameSplitPane.setOneTouchExpandable(true);
	frameSplitPane.setDividerLocation(850);
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
	JScrollPane scrollPane = new JScrollPane(this.playHigraphJComponent);
	scrollPane.setFocusable(true);
	leftCenterSplitPane.setRightComponent(scrollPane);
	return leftCenterSplitPane;
    }

    private JSplitPane getLeftTopBottomSplitPane() {
	JSplitPane leftTopBottomSplitPane = new JSplitPane(
		JSplitPane.VERTICAL_SPLIT);
	leftTopBottomSplitPane.setOneTouchExpandable(true);
	leftTopBottomSplitPane.setDividerLocation(300);
	leftTopBottomSplitPane.setTopComponent(new SyntaxPallet());
	leftTopBottomSplitPane.setBottomComponent(new ExpsPallet());
	return leftTopBottomSplitPane;
    }

    private JSplitPane getRightTopBottomSplitPane() {
	JSplitPane rightTopBottomSplitPane = new JSplitPane(
		JSplitPane.VERTICAL_SPLIT);
	rightTopBottomSplitPane.setOneTouchExpandable(true);
	rightTopBottomSplitPane.setDividerLocation(500);
	JScrollPane scrollPane = new JScrollPane(this.nodesOutline);
	scrollPane.setFocusable(true);
	rightTopBottomSplitPane.setTopComponent(scrollPane);
	rightTopBottomSplitPane.setBottomComponent(new JPanel());
	return rightTopBottomSplitPane;
    }

    private JMenuBar getMenuBar() {
	JMenuBar menuBar = new JMenuBar();

	JMenu fileMenu = new JMenu("File");
	fileMenu.setMnemonic(KeyEvent.VK_F);
	menuBar.add(fileMenu);

	fileMenu.addSeparator();

	JMenuItem exitMenuItem = new JMenuItem("Exit", KeyEvent.VK_E);
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

	this.undoMenuItem = new JMenuItem("Undo", KeyEvent.VK_U);
	this.undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
		ActionEvent.CTRL_MASK));
	this.undoMenuItem.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		Controller.getInstance().undo();
		Controller.getInstance().refresh();
	    }

	});
	editMenu.add(this.undoMenuItem);
	editMenu.addSeparator();

	return menuBar;
    }

    public void showMainFrame() {
	SwingUtilities.invokeLater(new Runnable() {

	    public void run() {
		ViewHandler.this.mainFrame.setVisible(true);
	    }

	});
    }

    public void updatePLAYHigraphView() {
	this.playHigraphView.refresh();
	this.playHigraphView.getDisplay().repaint();
    }

    /**
     * @param o
     */
    public void updateNodesOutline() {
	this.nodesOutline.update(null,
		this.playHigraphView.getDeletedNodeViewList());
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

    /**
     * @return the currentNodeView
     */
    public PLAYNodeView getCurrentNodeView() {
	return this.nodeViewSelectionModel.getSelectedNodeView();
    }

    /**
     * @param currentNodeView
     *            the currentNodeView to set
     */
    public void setCurrentNodeView(PLAYNodeView currentNodeView) {
	this.nodeViewSelectionModel.setSelectedNodeView(currentNodeView);
    }

    /**
     * 
     */
    public void getNextNodeView() {
	this.nodeViewSelectionModel.getNextNodeView(this.playHigraphView);
    }

    public void getPreviousNodeView() {
	this.nodeViewSelectionModel.getPreviousNodeView(this.playHigraphView);
    }

    /**
     * @return
     */
    public PLAYNodeView getLastHoverNodeView() {
	return this.nodeViewSelectionModel.getLastHoverNodeView();
    }

    /**
     * @param nodeView
     */
    public void setLastHoverNodeView(PLAYNodeView nodeView) {
	this.nodeViewSelectionModel.setLastHoverNodeView(nodeView);
    }

}
