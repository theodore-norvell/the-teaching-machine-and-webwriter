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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import play.higraph.model.PLAYSubgraph;
import play.higraph.model.PLAYWholeGraph;
import play.higraph.swing.PLAYHigraphJComponent;
import play.higraph.swing.PLAYHigraphJComponentKeyAdapter;
import play.higraph.swing.PLAYSubgraphMouseAdapter;
import play.higraph.view.PLAYHigraphView;
import play.higraph.view.PLAYSubgraphEventObserver;
import play.higraph.view.PLAYViewFactory;
import play.higraph.view.layout.PLAYBoxesInBoxesLayout;
import play.ide.view.ExpsPallet;
import play.ide.view.SyntaxPallet;
import tm.backtrack.BTTimeManager;

/**
 * @author Kai Zhu
 * 
 */
public class ViewHandler {

    // the main frame need to be showed
    private JFrame mainFrame;

    // menubar
    private JMenuBar menuBar;

    // file menu
    private JMenu fileMenu;

    private JPanel graphPanel;

    private JPanel syntaxPallet;

    private PLAYWholeGraph wholeGraph;

    private PLAYHigraphJComponent higraphJComponent;

    private PLAYHigraphView higraphView;

    public ViewHandler() {
	this.mainFrame = new JFrame();
	this.graphPanel = new JPanel();
	this.syntaxPallet = new SyntaxPallet();
	BTTimeManager btTimeManager = new BTTimeManager();
	this.wholeGraph = new PLAYWholeGraph(btTimeManager);
	PLAYSubgraph subgraph = new PLAYSubgraph(this.wholeGraph);
	this.higraphJComponent = new PLAYHigraphJComponent();
	PLAYViewFactory viewFactory = new PLAYViewFactory(btTimeManager);
	this.higraphView = viewFactory.makeHigraphView(this.wholeGraph,
		this.higraphJComponent);
	PLAYSubgraphEventObserver subgraphEventObserver = new PLAYSubgraphEventObserver(
		this.higraphView, this.wholeGraph, viewFactory, subgraph);
	PLAYSubgraphMouseAdapter subgraphMouseAdapter = new PLAYSubgraphMouseAdapter(
		this.higraphView, subgraphEventObserver);

	this.higraphJComponent
		.addKeyListener(new PLAYHigraphJComponentKeyAdapter(
			this.higraphView));
	this.higraphJComponent.setBackground(Color.WHITE);
	this.higraphJComponent.setAutoscrolls(true);
	this.higraphJComponent.setSubgraphView(this.higraphView);
	this.higraphView.setLayoutManager(new PLAYBoxesInBoxesLayout(
		NestedTreeLayoutManager.Axis.Y));
	subgraphMouseAdapter.installIn(this.higraphJComponent);

	this.mainFrame.setTitle("PLAY");
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
	JPanel leftPanel = new JPanel(new BorderLayout());
	leftPanel.add(this.syntaxPallet, BorderLayout.NORTH);
	leftPanel.add(new ExpsPallet(), BorderLayout.SOUTH);
	JSplitPane leftRightSplitPane = new JSplitPane(
		JSplitPane.HORIZONTAL_SPLIT);
	leftRightSplitPane.setOneTouchExpandable(true);
	leftRightSplitPane.setDividerLocation(150);
	leftRightSplitPane.setLeftComponent(leftPanel);
	JScrollPane scrollPane = new JScrollPane(this.higraphJComponent);
	scrollPane.setFocusable(true);
	leftRightSplitPane.setRightComponent(scrollPane);

	JPanel mainPanel = new JPanel();
	mainPanel.setLayout(new BorderLayout());
	mainPanel.setPreferredSize(new Dimension(800, 600));
	mainPanel.add(leftRightSplitPane, BorderLayout.CENTER);
	JButton button = new JButton("Temp");
	button.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		for (Node<?, ?, ?, ?, ?, ?, ?> node : wholeGraph.getTops()) {
		    node.delete();
		}
		higraphJComponent.scrollRectToVisible(new Rectangle(0,
			(int) higraphJComponent.getSize().getHeight() - 50,
			100, 100));
		higraphJComponent.setPreferredSize(new Dimension(
			(int) higraphJComponent.getSize().getWidth() + 100,
			(int) higraphJComponent.getSize().getHeight() + 100));
		higraphView.refresh();

		higraphJComponent.revalidate();
		higraphJComponent.repaint();
	    }

	});
	mainPanel.add(button, BorderLayout.SOUTH);
	this.mainFrame.getContentPane().add(mainPanel);
	this.mainFrame.pack();
    }

    public void showMainFrame() {
	SwingUtilities.invokeLater(new Runnable() {

	    public void run() {
		ViewHandler.this.mainFrame.setVisible(true);
	    }

	});
    }

}
