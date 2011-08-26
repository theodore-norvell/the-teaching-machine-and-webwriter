package display;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.*;

import sc.model.NodePayloadSC;
import sc.model.NodeSC;
import sc.model.SubGraphSC;
import sc.model.WholeGraphSC;
import higraph.swing.HigraphJComponent;
import higraph.view.SubgraphView;






public class MainFrame extends JFrame {
	
	
    
	private WholeGraphSC graph = new WholeGraphSC() ;
	
	private SubGraphSC subgraph = graph.makeSubGraph() ;
    private ExampleSubgraphView subgraphView = new ExampleSubgraphView( subgraph ) ;
    //private ExampleNodeView nodeView = new ExampleNodeView(subgraphView, , background, Color.blue, maximizedBounds);
	private ExampleLayoutManager graphLayoutManger = new ExampleLayoutManager(subgraphView) ;
	{ subgraphView.setLayoutManager( graphLayoutManger  ) ; }
	private HigraphJComponent graphDisplay= new HigraphJComponent( subgraphView ) ;
    private int k = 0 ;
	
	MainFrame() {
	    
		
		
		add( graphDisplay, BorderLayout.CENTER ) ;
		JToolBar toolBar = new JToolBar() ;
		add( toolBar, BorderLayout.NORTH ) ;
		
		Action action = new AbstractAction("add root") {
			public void actionPerformed(ActionEvent e) {
				graph.makeRootNode( new NodePayloadSC(Integer.toString(k)) ) ;
                subgraphView.refresh() ;
			}} ;
		toolBar.add( action ) ;
		
		action = new AbstractAction("delete root") {
			public void actionPerformed(ActionEvent e) {
				List<NodeSC> tops = graph.getTops() ;
				if( tops.size() > 0 ) {
				    NodeSC root = tops.get(0) ;
				    if( root.canDelete() ) {
				        root.delete() ; } }
				subgraphView.refresh() ;
			}} ;
		toolBar.add( action ) ;
		
		action = new AbstractAction("add nested node") {
			public void actionPerformed(ActionEvent e) {
				List<NodeSC> tops = graph.getTops() ;
                if( tops.size() > 0 ) {
                    NodeSC root =  tops.get(0) ;
                    NodeSC newNode = graph.makeRootNode(  new NodePayloadSC(Integer.toString(k) ) ) ;
                    if( root.canInsertChild(0, newNode) ) {
                        root.insertChild(0, newNode) ; } }
                subgraphView.refresh() ;
			}} ;
		toolBar.add( action ) ;
		
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE ) ;
		setSize(300, 400) ;
	}
	
	public static void main(String[] args ) {
		SwingUtilities.invokeLater( new Runnable() {

			public void run() {
				MainFrame frame = new MainFrame() ;
				frame.setVisible(true) ;
			}} ) ;
	}

}
