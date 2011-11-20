package visreed.app;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import tm.backtrack.BTTimeManager;
import visreed.awt.VisreedSubgraphMouseAdapter;
import visreed.extension.regex.swing.RegexJList;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.pattern.IObserver;
import visreed.swing.SwingHelper;
import visreed.swing.VisreedJComponent;
import visreed.swing.VisreedSubgraphEventObserver;
import visreed.swing.editor.VisreedTextArea;
import visreed.view.IGraphContainer;
import visreed.view.SyntaxViewFactory;
import visreed.view.VisreedHigraphView;
import visreed.view.VisreedNodeView;
import visreed.view.VisreedViewFactory;
import visreed.view.VoidPointDecorator;
import visreed.view.layout.SequenceLayoutManager;
import visreed.view.layout.SyntaxTreeLayoutManager;
import visreed.view.layout.VisreedNodeLayoutManager;

/**
 * This shall be the main entry for the project
 * @author Xiaoyu Guo
 */
public class VisreedMainFrame
extends JFrame 
implements IGraphContainer, IObserver<VisreedHigraph>{

    private static final long serialVersionUID = -6388967497486007956L;

    /**
     * Initialization of the graphs
     */
    protected void initializeGraph(){
        this.timeMan = new BTTimeManager();
        this.wholeGraph = new VisreedWholeGraph(this.timeMan);
        this.rootSubgraph = this.wholeGraph.makeSubGraph();
        this.currentSubgraph = this.rootSubgraph;
        
        // main graph
        this.mainGraphDisplay = new VisreedJComponent();
        this.mainViewFactory = new VisreedViewFactory(timeMan, this);
        
        // secondary graph
        this.syntaxDisplay = new VisreedJComponent();
        syntaxDisplay.setForeground(SystemColor.control);
        this.syntaxViewFactory = new SyntaxViewFactory(timeMan);
        
        // text area
        this.regexText = new VisreedTextArea();
        this.regexText.setSubHigraph(rootSubgraph);
        this.regexText.getInputMap().put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK), 
            "refreshModelFromText"
        );
        this.regexText.getActionMap().put(
            "refreshModelFromText", 
            new AbstractAction(){
                private static final long serialVersionUID = -3922739423828722390L;

                public void actionPerformed(ActionEvent e){
                    if(regexText.tryParseText() == true){
                        regexText.refreshFromText();
                        refreshGraph();
                    }
                }
            }
        );
        
        this.setSubgraph(rootSubgraph);
        
        // register this as the observer
        this.wholeGraph.registerObserver(this);
    }
    
    // main wholeGraph
    protected BTTimeManager timeMan;
    protected VisreedWholeGraph wholeGraph;
    protected VisreedSubgraph rootSubgraph;
    protected VisreedHigraph currentSubgraph;

    protected VisreedJComponent mainGraphDisplay;
    protected VisreedViewFactory mainViewFactory;
    protected VisreedHigraphView mainGraphView;
    protected VisreedNodeLayoutManager graphLayoutManger;
    
    // Set up an event mainGraphObserver.
    protected VisreedSubgraphEventObserver mainGraphObserver;
    protected VisreedSubgraphEventObserver syntaxGraphObserver;
    protected VisreedSubgraphMouseAdapter sgm;
    protected VisreedSubgraphMouseAdapter sgmSyntax;

    // secondary wholeGraph
    protected VisreedJComponent syntaxDisplay; 
    protected SyntaxViewFactory syntaxViewFactory;
    protected VisreedHigraphView syntaxView;
    
    protected SyntaxTreeLayoutManager syntaxLayoutManager;
    
    protected VisreedTextArea regexText;
    
    protected RegexJList nodeListBar;
    
    // containers
    protected JTabbedPane sidePanelContainer;

    /**
     * Construct the main frame
     */
    public VisreedMainFrame() {
        this.initializeGraph();
        this.initializeControl();
    }

    /**
     * Initialization of all the controls, which is irrelevant to graph
     */
    protected void initializeControl() {
        getContentPane().setLayout(new BorderLayout());
        
        /* the panels */
        /* The node tool bar */
        this.nodeListBar = new RegexJList(this.wholeGraph);
        
        /* The main graph panel */
        JPanel mainGraphPanel = new JPanel();
        mainGraphPanel.setLayout(new BorderLayout());
        mainGraphPanel.add(nodeListBar, BorderLayout.EAST);
        
        /* The main graph display */
        JScrollPane mainGraphScroller = new JScrollPane();
        mainGraphScroller.getViewport().setView(mainGraphDisplay);
        
        mainGraphPanel.add(mainGraphScroller, BorderLayout.CENTER);
        
        JSplitPane secondPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JScrollPane editorScroller = new JScrollPane(regexText);
        secondPanel.setTopComponent(editorScroller);
        secondPanel.setBottomComponent(mainGraphPanel);
        
        secondPanel.setResizeWeight(0.3);

        /* The side panel */
        this.sidePanelContainer = new JTabbedPane();

        initializeSidePanel();
        
        /* The main panel container */
        JSplitPane mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainPanel.setLeftComponent(secondPanel);
        mainPanel.setRightComponent(sidePanelContainer);
        mainPanel.setResizeWeight(0.80);
        
        getContentPane().add(mainPanel, BorderLayout.CENTER);

        /* tool bars */
        JPanel toolBarContainer = new JPanel();
        FlowLayout flowLayout = (FlowLayout) toolBarContainer.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        JToolBar testToolBar = new JToolBar();
        toolBarContainer.add(testToolBar, BorderLayout.NORTH);
        this.fillTestToolBar(testToolBar);
        
        JToolBar optionToolBar = new JToolBar();
        this.fillOptionToolBar(optionToolBar);
        toolBarContainer.add(optionToolBar, BorderLayout.SOUTH);
        
        getContentPane().add(toolBarContainer, BorderLayout.NORTH);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
    }

	/**
	 * Initialize the side panel
	 * Note: overridden methods shall call super.initializeSidePanel() first.
	 * @param sidePanel the container of the side panel
	 */
	protected void initializeSidePanel() {
		JPanel skeletonPanel = new JPanel();
        skeletonPanel.setLayout(new BorderLayout());
        this.sidePanelContainer.add("Skeleton View", skeletonPanel);
        
        JScrollPane skeletonGraphScroller = new JScrollPane();
        skeletonGraphScroller.getViewport().setView(syntaxDisplay);
        skeletonPanel.add(skeletonGraphScroller, BorderLayout.CENTER);
	}

    /**
     * Fills in the option toolbar
     * @param toolBar
     */
    @SuppressWarnings("serial")
    protected void fillOptionToolBar(JToolBar toolBar) {
        Action action;
        action = new AbstractAction("Toggle Debug Border"){
            public void actionPerformed(ActionEvent e) {
                VisreedNodeView.setDebugMode(!VisreedNodeView.getDebugMode());
                repaint();
            }
            
        };
        toolBar.add(action);
    }

    /**
     * Fills in the test toolbar
     * @param toolBar
     */
    protected void fillTestToolBar(JToolBar toolBar) {
    }
    
    /* (non-Javadoc)
     * @see visreed.view.IGraphContainer#refreshGraph()
     */
    @Override
    public void refreshGraph(){
    	this.wholeGraph.notifyObservers();
    }

	/* (non-Javadoc)
	 * @see visreed.view.IGraphContainer#setSubgraph(visreed.model.VisreedHigraph)
	 */
	@Override
	public void setSubgraph(VisreedHigraph subgraph) {
		if(subgraph != null){
			this.currentSubgraph = subgraph;
			
			// main view
	        this.mainGraphView = mainViewFactory.makeHigraphView(
                subgraph,
                mainGraphDisplay
            );
            this.mainGraphDisplay.setSubgraphView(mainGraphView);
            this.graphLayoutManger = SequenceLayoutManager.getInstance();
            this.mainGraphView.setLayoutManager(graphLayoutManger);
            
            this.mainGraphView.setDefaultParentDecorator(
                new VoidPointDecorator(mainGraphView, timeMan)
            );
            this.mainGraphView.setDefaultChildDecorator(
                new VoidPointDecorator(mainGraphView, timeMan)
            );
            
            this.mainGraphObserver = new VisreedSubgraphEventObserver(this, this.wholeGraph);
            this.sgm = new VisreedSubgraphMouseAdapter(
                mainGraphView,
                mainGraphObserver
            );
            this.sgm.installIn(mainGraphDisplay);
			
            // subview
            this.syntaxView = syntaxViewFactory.makeHigraphView(
                subgraph, 
                syntaxDisplay
            );
            this.syntaxDisplay.setSubgraphView(syntaxView);
            
            this.syntaxLayoutManager = new SyntaxTreeLayoutManager();
            this.syntaxView.setLayoutManager(syntaxLayoutManager);

            this.syntaxView.setDefaultParentDecorator(
                new VoidPointDecorator(syntaxView, timeMan)
            );
            this.syntaxView.setDefaultChildDecorator(
                new VoidPointDecorator(syntaxView, timeMan)
            );

            this.syntaxGraphObserver = new VisreedSubgraphEventObserver(this, this.wholeGraph);
            this.sgmSyntax = new VisreedSubgraphMouseAdapter(
                syntaxView,
                syntaxGraphObserver
            );
            this.sgmSyntax.installIn(syntaxDisplay);
		}
		
		refreshGraph();
	}
    
    private void privateRefreshGraph(){
        this.mainGraphView.refresh();
        this.mainGraphDisplay.repaint();
        
        this.syntaxView.refresh();
        this.syntaxDisplay.repaint();
        
        this.currentSubgraph.notifyObservers();
        
        this.regexText.refreshFromModel();
    }

	/**
	 * called after the wholegraph has changed.
	 */
	protected void refreshHook() {}

    public static void main(String[] args) {
        SwingHelper.setSystemLookAndFeel();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                VisreedMainFrame frame = new VisreedMainFrame();
                frame.setVisible(true);
            }
        });
    }

    /* (non-Javadoc)
     * @see visreed.pattern.IObserver#changed(visreed.pattern.IObservable)
     */
    @Override
    public final void changed(VisreedHigraph regexHigraph) {
        if(regexHigraph == this.wholeGraph){
            this.privateRefreshGraph();
            this.refreshHook();
        }
    }
}
