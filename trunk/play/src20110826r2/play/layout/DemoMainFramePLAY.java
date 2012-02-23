package play.layout;

import higraph.swing.HigraphJComponent;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import play.model.SubGraphPLAY;
import play.model.WholeGraphPLAY;
import play.observer.SubGraphEventObserverPLAY;
import play.observer.SubGraphMouseAdapterPLAY;
import play.view.SubGraphViewPLAY;
import play.view.ViewFactoryPLAY;

/**
 * @author  Charles
 */
public class DemoMainFramePLAY extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6030482160867249298L;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				DemoMainFramePLAY frame = new DemoMainFramePLAY();
				frame.setVisible(true);
			}
		});
	}

	/**
	 * @uml.property  name="graph"
	 * @uml.associationEnd  
	 */
	private static WholeGraphPLAY graph = new WholeGraphPLAY();
	/**
	 * @uml.property  name="viewFactory"
	 * @uml.associationEnd  
	 */
	private static ViewFactoryPLAY viewFactory = new ViewFactoryPLAY();

	// main subgraph
	/**
	 * @uml.property  name="subGraph"
	 * @uml.associationEnd  
	 */
	private static SubGraphPLAY subGraph = graph.makeSubGraph();
	/**
	 * @uml.property  name="sgDisplay"
	 * @uml.associationEnd  
	 */
	private static HigraphJComponent sgDisplay = new HigraphJComponent();
	/**
	 * @uml.property  name="sgv"
	 * @uml.associationEnd  
	 */
	private SubGraphViewPLAY sgv = viewFactory.makeSubgraphView(subGraph,
			sgDisplay);
	/**
	 * @uml.property  name="sglm"
	 * @uml.associationEnd  
	 */
	private SubgraphLayoutManagerPLAY sglm = new SubgraphLayoutManagerPLAY(sgv);
	/**
	 * @uml.property  name="sgObserver"
	 * @uml.associationEnd  
	 */
	private SubGraphEventObserverPLAY sgObserver = new SubGraphEventObserverPLAY(
			sgv, graph, viewFactory, subGraph);
	/**
	 * @uml.property  name="sgm"
	 * @uml.associationEnd  
	 */
	private SubGraphMouseAdapterPLAY sgm = new SubGraphMouseAdapterPLAY(sgv,
			sgObserver, null);

	// trash bin
	/**
	 * @uml.property  name="tb"
	 * @uml.associationEnd  
	 */
	private SubGraphPLAY tb = graph.makeSubGraph();
	/**
	 * @uml.property  name="tbDisplay"
	 * @uml.associationEnd  
	 */
	private static HigraphJComponent tbDisplay = new HigraphJComponent();

	/**
	 * @uml.property  name="tbv"
	 * @uml.associationEnd  
	 */
	private SubGraphViewPLAY tbv = viewFactory.makeSubgraphView(tb, tbDisplay);
	/**
	 * @uml.property  name="tblm"
	 * @uml.associationEnd  
	 */
	private SubgraphLayoutManagerPLAY tblm = new SubgraphLayoutManagerPLAY(tbv);
	/**
	 * @uml.property  name="tbObserver"
	 * @uml.associationEnd  
	 */
	private SubGraphEventObserverPLAY tbObserver = new SubGraphEventObserverPLAY(
			tbv, graph, viewFactory, tb);
	/**
	 * @uml.property  name="tbm"
	 * @uml.associationEnd  
	 */
	private SubGraphMouseAdapterPLAY tbm = new SubGraphMouseAdapterPLAY(tbv,
			tbObserver, null);

	{
		graph.registerSubgraphView(sgv);
		graph.registerSubgraphView(tbv);
	}

	{
		sgDisplay.setSubgraphView(sgv);
		tbDisplay.setSubgraphView(tbv);
	}

	{
		sgv.setLayoutManager(sglm);
		tbv.setLayoutManager(tblm);
	}
	{
		sgm.installIn(sgDisplay);
		tbm.installIn(tbDisplay);
	}

	DemoMainFramePLAY() {

		// main subgraph frame
		JInternalFrame mainFrame = new JInternalFrame("main", false, false,
				false, false);
		mainFrame.setVisible(true);
		mainFrame.pack();
		mainFrame.add(sgDisplay);

		// pallet frame
		JInternalFrame leftPalletFrame = new JInternalFrame("pallet",
				false, false, false, false);
		leftPalletFrame.setVisible(true);
		leftPalletFrame.pack();

		JInternalFrame rightPalletFrame = new JInternalFrame("right pallet",
				false, false, false, false);
		rightPalletFrame.setVisible(true);
		rightPalletFrame.pack();

		JPanel tbPanel = new JPanel();
		tbPanel.setName("Trash Bin");
		tbPanel.add(tbDisplay);

		PalletListPLAY palletList = new PalletListPLAY();
		JPanel palletPanel = new JPanel();
		palletPanel.add(palletList);

		VarTypeListPLAY varTypeList = new VarTypeListPLAY();
		JPanel varTypePanel = new JPanel();
		varTypePanel.add(varTypeList);

		TypeListPLAY typeList = new TypeListPLAY();
		JPanel typePanel = new JPanel();
		typePanel.add(typeList);

		JSplitPane typeSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				varTypePanel, typePanel);
		
		JSplitPane leftSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				palletPanel, typeSplitPane);
		leftPalletFrame.add(leftSplitPane);
		rightPalletFrame.add(tbDisplay);

		add(leftPalletFrame, BorderLayout.WEST);
		add(mainFrame, BorderLayout.CENTER);
		add(rightPalletFrame, BorderLayout.EAST);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}

}
