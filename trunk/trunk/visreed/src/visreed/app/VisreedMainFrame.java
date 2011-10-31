package visreed.app;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;

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
import visreed.model.VisreedNode;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.model.payload.AlternationPayload;
import visreed.model.payload.KleenePlusPayload;
import visreed.model.payload.KleeneStarPayload;
import visreed.model.payload.OptionalPayload;
import visreed.model.payload.RepeatRangePayload;
import visreed.model.payload.SequencePayload;
import visreed.model.payload.TerminalPayload;
import visreed.model.payload.VisreedPayload;
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
    @SuppressWarnings("serial")
    protected void fillTestToolBar(JToolBar toolBar) {
        Action action = new AbstractAction("refresh") {
            public void actionPerformed(ActionEvent e) {
                refreshGraph();
            }
        };
        toolBar.add(action);

        action = new AbstractAction("add the root") {
            public void actionPerformed(ActionEvent e) {
                List<VisreedNode> tops = rootSubgraph.getTops();
                if(tops == null || tops.size() == 0){
                    VisreedNode n = wholeGraph.makeRootNode(new SequencePayload());
                    rootSubgraph.addTop(n);
                    mainGraphView.refresh();
                    mainGraphDisplay.repaint();
                    
                    syntaxView.refresh();
                    syntaxDisplay.repaint();
                    
                    regexText.refreshFromModel();
                }
            }
        };
        toolBar.add(action);

        action = new AbstractAction("delete the root") {
            public void actionPerformed(ActionEvent e) {
                List<VisreedNode> tops = wholeGraph.getTops();
                if (tops.size() > 0) {
                    VisreedNode root = tops.get(0);
                    if (root.canDelete()) {
                        root.delete();
                    }
                }
                refreshGraph();
            }
        };
        toolBar.add(action);

        action = new AbstractAction("add nested node") {
            public void actionPerformed(ActionEvent e) {
                List<VisreedNode> nodes = rootSubgraph.getNodes();
                if (nodes.size() > 0) {
                    Random rand = new Random();
                    int randIndex = rand.nextInt(nodes.size());
                    VisreedNode root = nodes.get(randIndex);
                    
                    VisreedPayload newPayLoad = new SequencePayload();
                    VisreedNode newNode = wholeGraph.makeRootNode(newPayLoad);
                    if (root.canInsertChild(0, newNode)) {
                        root.insertChild(0, newNode);
                    }
                }
                refreshGraph();
            }
        };
        toolBar.add(action);
        
        action = new AbstractAction("Whole tree"){
            public void actionPerformed(ActionEvent e){
                // first delete all the nodes
                List<VisreedNode> tops = rootSubgraph.getTops();
                for(int i = 0; i < tops.size(); i++){
                    VisreedNode top = tops.get(i);
                    if(top.canDelete()){
                        top.delete();
                    }
                }
                
                // then add new nodes, one by one
                VisreedNode root, alt, kplus, current, leaf;
                root = wholeGraph.makeRootNode(new SequencePayload());
                rootSubgraph.addTop(root);
                
                kplus = wholeGraph.makeRootNode(new KleenePlusPayload());
                current = wholeGraph.makeRootNode(new SequencePayload());
                leaf = wholeGraph.makeRootNode(new TerminalPayload("e"));
                current.insertChild(0, leaf);
                kplus.insertChild(0, current);
                root.insertChild(0, kplus);
                
                leaf = wholeGraph.makeRootNode(new TerminalPayload("d"));
                root.insertChild(0, leaf);
                
                alt = wholeGraph.makeRootNode(new AlternationPayload());
                leaf = wholeGraph.makeRootNode(new TerminalPayload("c"));
                alt.insertChild(2, leaf);
                leaf = wholeGraph.makeRootNode(new TerminalPayload("b"));
                current = wholeGraph.makeRootNode(new SequencePayload());
                current.insertChild(0, leaf);
                alt.getChild(1).replace(current);
                leaf = wholeGraph.makeRootNode(new TerminalPayload("a"));
                current = wholeGraph.makeRootNode(new SequencePayload());
                current.insertChild(0, leaf);
                kplus = wholeGraph.makeRootNode(new KleeneStarPayload());
                kplus.insertChild(0, current);
                current = wholeGraph.makeRootNode(new SequencePayload());
                current.insertChild(0, kplus);
                alt.getChild(0).replace(current);
                root.insertChild(0, alt);
                
                refreshGraph();
            }
        };
        toolBar.add(action);
        
        action = new AbstractAction("Alternation"){
            public void actionPerformed(ActionEvent e){
                // first delete all the nodes
                List<VisreedNode> tops = rootSubgraph.getTops();
                for(int i = 0; i < tops.size(); i++){
                    VisreedNode top = tops.get(i);
                    if(top.canDelete()){
                        top.delete();
                    }
                }
                
                // then add new nodes, one by one
                VisreedNode root, alt, current, leaf;
                root = wholeGraph.makeRootNode(new SequencePayload());
                alt = wholeGraph.makeRootNode(new AlternationPayload());
                leaf = wholeGraph.makeRootNode(new TerminalPayload("c"));
                alt.insertChild(2, leaf);
                leaf = wholeGraph.makeRootNode(new TerminalPayload("b"));
                current = wholeGraph.makeRootNode(new SequencePayload());
                current.insertChild(0, leaf);
                alt.getChild(1).replace(current);
                leaf = wholeGraph.makeRootNode(new TerminalPayload("a"));
                current = wholeGraph.makeRootNode(new SequencePayload());
                current.insertChild(0, leaf);
                alt.getChild(0).replace(current);
                
                root.appendChild(alt);
                rootSubgraph.addTop(root);
                
                refreshGraph();
            }
        };
        toolBar.add(action);
        
        action = new AbstractAction("Nested_SEQ"){
            public void actionPerformed(ActionEvent ae){
                // first delete all the nodes
                List<VisreedNode> tops = rootSubgraph.getTops();
                for(int i = 0; i < tops.size(); i++){
                    VisreedNode top = tops.get(i);
                    if(top.canDelete()){
                        top.delete();
                    }
                }
                
                // then add new nodes, one by one
                VisreedNode root, a, b, c, d, e;
                root = wholeGraph.makeRootNode(new SequencePayload());
                rootSubgraph.addTop(root);
                
                a = wholeGraph.makeRootNode(new SequencePayload());
                b = wholeGraph.makeRootNode(new SequencePayload());
                c = wholeGraph.makeRootNode(new SequencePayload());
                a.insertChild(0, b);
                a.insertChild(0, c);
                root.insertChild(0, a);
                
                d = wholeGraph.makeRootNode(new SequencePayload());
                root.insertChild(1, d);
                
                e = wholeGraph.makeRootNode(new TerminalPayload('g'));
                d.insertChild(0, e);
                
                refreshGraph();
            }
        };
        toolBar.add(action);
        
        action = new AbstractAction("KleeneTest"){
            public void actionPerformed(ActionEvent e){
                // first delete all the nodes
                List<VisreedNode> tops = rootSubgraph.getTops();
                for(int i = 0; i < tops.size(); i++){
                    VisreedNode top = tops.get(i);
                    if(top.canDelete()){
                        top.delete();
                    }
                }
                
                // then add new nodes, one by one
                VisreedNode root, kplus, current, leaf;
                root = wholeGraph.makeRootNode(new SequencePayload());
                rootSubgraph.addTop(root);
                
                kplus = wholeGraph.makeRootNode(new RepeatRangePayload(5));
                current = wholeGraph.makeRootNode(new SequencePayload());
                leaf = wholeGraph.makeRootNode(new TerminalPayload("c"));
                current.insertChild(0, leaf);
                kplus.insertChild(0, current);
                root.insertChild(0, kplus);
                
                kplus = wholeGraph.makeRootNode(new KleenePlusPayload());
                current = wholeGraph.makeRootNode(new SequencePayload());
                leaf = wholeGraph.makeRootNode(new TerminalPayload("e"));
                current.insertChild(0, leaf);
                kplus.insertChild(0, current);
                root.insertChild(0, kplus);
                
                kplus = wholeGraph.makeRootNode(new KleeneStarPayload());
                current = wholeGraph.makeRootNode(new SequencePayload());
                leaf = wholeGraph.makeRootNode(new TerminalPayload("f"));
                current.insertChild(0, leaf);
                kplus.insertChild(0, current);
                root.insertChild(0, kplus);
                
                refreshGraph();
            }
        };
        toolBar.add(action);
        
        action = new AbstractAction("Telephone"){
            public void actionPerformed(ActionEvent e){
                // first delete all the nodes
                List<VisreedNode> tops = rootSubgraph.getTops();
                for(int i = 0; i < tops.size(); i++){
                    VisreedNode top = tops.get(i);
                    if(top.canDelete()){
                        top.delete();
                    }
                }
                
                // then add new nodes, one by one
                VisreedNode root, seq, current, leaf;
                root = wholeGraph.makeRootNode(new SequencePayload());
                rootSubgraph.addTop(root);
                
                current = wholeGraph.makeRootNode(new RepeatRangePayload(4));
                root.insertChild(0, current);
                
                seq = wholeGraph.makeRootNode(new SequencePayload());
                current.insertChild(0, seq);
                
                leaf = wholeGraph.makeRootNode(new TerminalPayload("\\d"));
                seq.insertChild(0, leaf);
                
                current = wholeGraph.makeRootNode(new OptionalPayload());
                seq = wholeGraph.makeRootNode(new SequencePayload());
                leaf = wholeGraph.makeRootNode(new TerminalPayload("[.\\-]"));
                seq.insertChild(0, leaf);
                current.insertChild(0, seq);
                root.insertChild(0, current);
                
                current = wholeGraph.makeRootNode(new RepeatRangePayload(3));
                root.insertChild(0, current);
                
                seq = wholeGraph.makeRootNode(new SequencePayload());
                current.insertChild(0, seq);
                
                leaf = wholeGraph.makeRootNode(new TerminalPayload("\\d"));
                seq.insertChild(0, leaf);
                
                current = wholeGraph.makeRootNode(new OptionalPayload());
                seq = wholeGraph.makeRootNode(new SequencePayload());
                leaf = wholeGraph.makeRootNode(new TerminalPayload("[.\\-]"));
                seq.insertChild(0, leaf);
                current.insertChild(0, seq);
                root.insertChild(0, current);
                
                current = wholeGraph.makeRootNode(new OptionalPayload());
                seq = wholeGraph.makeRootNode(new SequencePayload());
                leaf = wholeGraph.makeRootNode(new TerminalPayload(")"));
                seq.insertChild(0, leaf);
                current.insertChild(0, seq);
                root.insertChild(0, current);

                current = wholeGraph.makeRootNode(new RepeatRangePayload(2));
                root.insertChild(0, current);
                
                seq = wholeGraph.makeRootNode(new SequencePayload());
                current.insertChild(0, seq);
                
                leaf = wholeGraph.makeRootNode(new TerminalPayload("\\d"));
                seq.insertChild(0, leaf);
                
                leaf = wholeGraph.makeRootNode(new TerminalPayload("[1-9]"));
                root.insertChild(0, leaf);
                
                current = wholeGraph.makeRootNode(new OptionalPayload());
                seq = wholeGraph.makeRootNode(new SequencePayload());
                leaf = wholeGraph.makeRootNode(new TerminalPayload("("));
                seq.insertChild(0, leaf);
                current.insertChild(0, seq);
                root.insertChild(0, current);

                refreshGraph();
            }
        };
        toolBar.add(action);
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
    public void changed(VisreedHigraph regexHigraph) {
        if(regexHigraph == this.wholeGraph){
            this.privateRefreshGraph();
        }
    }
}
