/**
 * ViewHandler.java - play.ide.handler - PLAY
 * 
 * Created on Feb 9, 2012 by Kai Zhu
 */
package play.ide.handler;

import higraph.swing.HigraphJComponent;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import play.higraph.model.PLAYNode;
import play.higraph.model.PLAYPayload;
import play.higraph.model.PLAYSubgraph;
import play.higraph.model.PLAYTag;
import play.higraph.model.PLAYWholeGraph;
import play.higraph.swing.PLAYSubgraphMouseAdapter;
import play.higraph.view.PLAYHigraphView;
import play.higraph.view.PLAYSubgraphEventObserver;
import play.higraph.view.PLAYViewFactory;
import play.higraph.view.layout.PLAYBoxesInBoxesLayout;
import play.ide.view.SyntaxListPanel;
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

    private JPanel syntaxListPanel;

    private BTTimeManager btTimeManager;

    private PLAYWholeGraph wholeGraph;

    private PLAYSubgraph subgraph;

    private HigraphJComponent higraphJComponent;

    private PLAYViewFactory viewFactory;

    private PLAYHigraphView higraphView;

    private PLAYBoxesInBoxesLayout boxesInBoxesLayout;

    private PLAYSubgraphEventObserver subgraphEventObserver;

    private PLAYSubgraphMouseAdapter subgraphMouseAdapter;

    public ViewHandler() {
	this.mainFrame = new JFrame();
	this.graphPanel = new JPanel();
	this.syntaxListPanel = new SyntaxListPanel();
	this.btTimeManager = new BTTimeManager();
	this.wholeGraph = new PLAYWholeGraph(this.btTimeManager);
	this.subgraph = new PLAYSubgraph(this.wholeGraph);
	this.higraphJComponent = new HigraphJComponent();
	this.viewFactory = new PLAYViewFactory(this.btTimeManager);
	this.higraphView = this.viewFactory.makeHigraphView(this.wholeGraph,
		this.higraphJComponent);
	this.boxesInBoxesLayout = new PLAYBoxesInBoxesLayout();
	this.subgraphEventObserver = new PLAYSubgraphEventObserver(
		this.higraphView, this.wholeGraph, this.viewFactory,
		this.subgraph);
	this.subgraphMouseAdapter = new PLAYSubgraphMouseAdapter(
		this.higraphView, this.subgraphEventObserver);

	this.higraphJComponent.setSubgraphView(this.higraphView);
	this.higraphView.setLayoutManager(this.boxesInBoxesLayout);
	this.subgraphMouseAdapter.installIn(this.higraphJComponent);

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
	JSplitPane leftRightSplitPane = new JSplitPane(
		JSplitPane.HORIZONTAL_SPLIT);
	leftRightSplitPane.setOneTouchExpandable(true);
	leftRightSplitPane.setDividerLocation(50);
	leftRightSplitPane.setLeftComponent(this.syntaxListPanel);
	leftRightSplitPane.setRightComponent(this.higraphJComponent);

	JPanel mainPanel = new JPanel();
	mainPanel.setLayout(new BorderLayout());
	mainPanel.setPreferredSize(new Dimension(800, 600));
	mainPanel.add(leftRightSplitPane, BorderLayout.CENTER);

	JButton button = new JButton("Temp");
	button.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		wholeGraph.makeRootNode(new PLAYPayload(Integer.toString(0),
			PLAYTag.SEQ));
		higraphView.refresh();
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
