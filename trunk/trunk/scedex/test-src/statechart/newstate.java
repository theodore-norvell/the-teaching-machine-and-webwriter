package statechart;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import layout.scLayout;

import sc.model.NodePayloadSC;
import sc.model.NodeSC;
import sc.model.SubGraphSC;
import sc.model.WholeGraphSC;
import higraph.higraph.swing.HigraphJComponent;
import higraph.higraph.view.interfaces.SubgraphEventObserver;
import higraph.higraph.view.mousing.SubgraphMouseAdapter;
import display.ExampleLayoutManager;
import display.ExampleSubgraphView;
import display.ExampleViewFactory;
import display.SCobserver;

public class newstate extends JFrame {
	
	private WholeGraphSC graph = new WholeGraphSC() ;
	private SubGraphSC subgraph = graph.makeSubGraph() ;
	private HigraphJComponent graphDisplay= new HigraphJComponent( ) ;
	private SubgraphEventObserver observer = new SCobserver();
	private ExampleViewFactory vf = new ExampleViewFactory() ;
	private ExampleSubgraphView subgraphView = vf.makeSubgraphView( subgraph, graphDisplay) ;
		{ graph.registerView( subgraphView ) ; }
		{ graphDisplay.setSubgraphView( subgraphView ) ; }
	private SubgraphMouseAdapter sm = new SubgraphMouseAdapter(subgraphView);
	{sm.register(observer);}
	private scLayout graphLayoutManger = new scLayout(subgraphView) ;
		{ subgraphView.setLayoutManager( graphLayoutManger  ) ; }
	
	private int k = 0 ;
    private JPanel jPanel1 = new JPanel();
    private JButton insert = new JButton();
    private JButton delete = new JButton();
    private JButton insertChild = new JButton();
    private JButton deleteChild = new JButton();
    private JButton detach = new JButton();
    

    public newstate() {
        try {
            Init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Init() throws Exception {
        
    	add( graphDisplay, BorderLayout.CENTER ) ;
        this.setSize(new Dimension(784, 459));
        jPanel1.setBounds(new Rectangle(545, 5, 225, 415));
        insert.setText("insert");
        delete.setText("delete");
        insertChild.setText("insertChild");
        deleteChild.setText("deleteChild");
        detach.setText("detach");
       
        jPanel1.add(insert, null);
        insert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                insert_actionPerformed(e);
            }
        });
        
        jPanel1.add(delete, null);
        delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                delete_actionPerformed(e);
            }
        });
        
        
        jPanel1.add(insertChild, null);
        insertChild.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                insertChild_actionPerformed(e);
            }
        });
        
        
        jPanel1.add(deleteChild, null);
        jPanel1.add(detach, null);
        detach.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                detach_actionPerformed(e);
            }
        });
        
        add(jPanel1, BorderLayout.EAST);
        
    }
    private void insert_actionPerformed(ActionEvent e) {
    	NodeSC n = graph.makeRootNode( new NodePayloadSC(Integer.toString(k++)) ) ;
		subgraph.addTop( n ) ;
        subgraphView.refresh() ;
        graphDisplay.repaint();
    }
    private void delete_actionPerformed(ActionEvent e) {
    	List<NodeSC> tops = graph.getTops() ;
		if( tops.size() > 0 ) {
		    NodeSC root = tops.get(1) ;
		    if( root.canDelete() ) {
		        root.delete() ; } }
		subgraphView.refresh() ;
        graphDisplay.repaint();
    }
    private void insertChild_actionPerformed(ActionEvent e) {
    	List<NodeSC> tops = graph.getTops() ;
        if( tops.size() > 0 ) {
            NodeSC root =  tops.get(1) ;
            NodeSC newNode = graph.makeRootNode(  new NodePayloadSC(Integer.toString(k) ) ) ;
            if( root.canInsertChild(0, newNode) ) {
                root.insertChild(0, newNode) ; } }
        subgraphView.refresh() ;
        graphDisplay.repaint();
    }
    private void detach_actionPerformed(ActionEvent e) {
    	List<NodeSC> tops = graph.getTops() ;
        if( tops.size() > 0 ) {
            NodeSC root =  tops.get(1) ;
            NodeSC child = root.getChild(0);
            NodeSC newNode = graph.makeRootNode(  new NodePayloadSC(Integer.toString(k) ) ) ;
            if( child.canInsertChild(0, newNode)) {
                child.insertChild(0, newNode); } }
        subgraphView.refresh() ;
        graphDisplay.repaint();
    }
}

