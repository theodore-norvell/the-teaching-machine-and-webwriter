package play.ide.view;

import higraph.swing.SubgraphMouseAdapter;
import higraph.view.ViewFactory;
import higraph.view.interfaces.SubgraphEventObserver;
import higraph.view.layout.NestedTreeLayoutManager;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import play.controller.Controller;
import play.higraph.model.PLAYNode;
import play.higraph.model.PLAYPayload;
import play.higraph.model.PLAYSubgraph;
import play.higraph.model.PLAYWholeGraph;
import play.higraph.swing.PLAYHigraphJComponent;
import play.higraph.swing.PLAYHigraphJComponentKeyAdapter;
import play.higraph.swing.PLAYSubgraphMouseAdapter;
import play.higraph.swing.SyntaxTransferHandler;
import play.higraph.view.PLAYHigraphView;
import play.higraph.view.PLAYSubgraphEventObserver;
import play.higraph.view.PLAYViewFactory;
import play.higraph.view.layout.PLAYBoxesInBoxesLayout;
import tm.backtrack.BTTimeManager;

public class NewClass extends JPanel{
	private static final long serialVersionUID = 59182330371195540L;
	private int i;
	private String className;
	private PLAYHigraphView playHigraphView;
	private PLAYHigraphJComponent playHigraphJComponent;
	private PLAYSubgraph  playSubGraph;
	private PLAYWholeGraph playWholeGraph;
	private PLAYPayload payLoad;
	private PLAYNode node;
	private Controller controller;
	private PLAYViewFactory viewFactory;
	private JPanel jPanel;
	private BTTimeManager btTimeManager;
	//JPanel classList = new JPanel();

	//public NewClass(PLAYHigraphView playHiGraph,int i){
	//public NewClass(PLAYWholeGraph playWholeGraph,PLAYPayload playLoad,PLAYNode node,
	//	PLAYViewFactory viewFactory,PLAYHigraphView playHigraphView,int i){
	public NewClass(PLAYWholeGraph playWholeGraph,PLAYViewFactory viewFactory,PLAYSubgraph playSubGraph,
			PLAYHigraphJComponent playHigraphJComponent, SubgraphEventObserver subgraphEventObserver,
			SubgraphMouseAdapter subgraphMouseAdapter,JPanel jPanel,BTTimeManager btTimeManager,
			PLAYHigraphView playHigraphView,int i,PLAYNode node){
		// Controller.getInstance().createNewClass();
		this.controller = Controller.getInstance();
		this.playHigraphJComponent =playHigraphJComponent; 
		this.playHigraphView = playHigraphView;
		this.playWholeGraph = playWholeGraph;
		this.payLoad = payLoad;
		this.node = node;
		this.viewFactory = viewFactory;
		this.jPanel = jPanel;
		this.btTimeManager = btTimeManager;
		this.playSubGraph = playSubGraph;
		this.i = i;



		String name = getName();
		this.className = name;
		JLabel l = new JLabel(name);
		System.out.println("name = " +name);

		System.out.println("view = "+playHigraphView);
		System.out.println("wholegraph = "+playWholeGraph);
		System.out.println("viewFactory = "+viewFactory);
		System.out.println("playHigraphJComponent = "+playHigraphJComponent);

		//this.add(l);	
		this.setTransferHandler(new SyntaxTransferHandler());


	}

	public String getName(){
		String name = "class" + i;
		return name;
	}


	public void displayClass(){

		System.out.println("view = "+playHigraphView);
		System.out.println("wholegraph = "+playWholeGraph);
		System.out.println("viewFactory = "+viewFactory);
		System.out.println("playHigraphJComponent = "+playHigraphJComponent);
		System.out.println("playSubgraph = "+playSubGraph);
		System.out.println("");


		for (PLAYNode n:playSubGraph.getTops() ) {
			playSubGraph.removeTop( n ) ;
		}

		playSubGraph.addTop(node);



		if (playHigraphView != null) {
			playHigraphView.refresh();
			playHigraphView.getDisplay().repaint();
			//viewHandler.updateNodesOutline(playHigraphView);
			// this.viewHandler.updataPropertyPanel(higraphView.getHigraph()
			//    .getWholeGraph().getPLAYViewSelectionModel());
		}

	}

}
