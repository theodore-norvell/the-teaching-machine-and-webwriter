package regex;

import java.awt.BorderLayout;
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
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import regex.awt.RegexSubgraphMouseAdapter;
import regex.model.IRegexHigraphObserver;
import regex.model.RegexHigraph;
import regex.model.RegexNode;
import regex.model.RegexPayload;
import regex.model.RegexSubgraph;
import regex.model.RegexWholeGraph;
import regex.model.payload.AlternationPayload;
import regex.model.payload.KleenePlusPayload;
import regex.model.payload.KleeneStarPayload;
import regex.model.payload.OptionalPayload;
import regex.model.payload.SequencePayload;
import regex.model.payload.TerminalPayload;
import regex.swing.RegexJComponent;
import regex.swing.RegexJList;
import regex.swing.RegexTextArea;
import regex.swing.SwingHelper;
import regex.view.IGraphContainer;
import regex.view.RegexHigraphView;
import regex.view.RegexNodeView;
import regex.view.RegexViewFactory;
import regex.view.SyntaxViewFactory;
import regex.view.VoidPointDecorator;
import regex.view.layout.AlternationLayoutManager;
import regex.view.layout.SyntaxTreeLayoutManager;
import tm.backtrack.BTTimeManager;

/**
 * This shall be the main entry for the project
 * @author Xiaoyu Guo
 */
public class RegexMainFrame
extends JFrame 
implements IGraphContainer, IRegexHigraphObserver{

    private static final long serialVersionUID = -6388967497486007956L;

    private void initializeGraph(){
        this.timeMan = new BTTimeManager();
        this.wholeGraph = new RegexWholeGraph(this.timeMan);
        this.subgraph = this.wholeGraph.makeSubGraph();
        
        // main graph
        this.mainGraphDisplay = new RegexJComponent();
        this.mainViewFactory = new RegexViewFactory(timeMan);
        this.mainGraphView = mainViewFactory.makeHigraphView(
            subgraph,
            mainGraphDisplay
        );
        this.mainGraphDisplay.setSubgraphView(mainGraphView);
        this.graphLayoutManger = new AlternationLayoutManager();
        this.mainGraphView.setLayoutManager(graphLayoutManger);

        
        this.mainGraphView.setDefaultParentDecorator(
            new VoidPointDecorator(mainGraphView, timeMan)
        );
        this.mainGraphView.setDefaultChildDecorator(
            new VoidPointDecorator(mainGraphView, timeMan)
        );
        
        this.mainGraphObserver = new RegexSGEventObserver(this, this.wholeGraph);
        this.sgm = new RegexSubgraphMouseAdapter(
            mainGraphView,
            mainGraphObserver
        );
        this.sgm.installIn(mainGraphDisplay);
        
        // secondary graph
        this.syntaxDisplay = new RegexJComponent(); 
        this.syntaxViewFactory = new SyntaxViewFactory(timeMan);
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

        this.syntaxGraphObserver = new RegexSGEventObserver(this, this.wholeGraph);
        this.sgm2 = new RegexSubgraphMouseAdapter(
            syntaxView,
            syntaxGraphObserver
        );
        this.sgm2.installIn(syntaxDisplay);
        
        // text area
        this.regexText = new RegexTextArea();
        this.regexText.setSubHigraph(subgraph);
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
        
        // register this as the observer
        this.wholeGraph.registerObserver(this);
    }
    
    // main wholeGraph
    private BTTimeManager timeMan;
    private RegexWholeGraph wholeGraph;
    private RegexSubgraph subgraph;

    private RegexJComponent mainGraphDisplay;
    private RegexViewFactory mainViewFactory;
    private RegexHigraphView mainGraphView;
    private AlternationLayoutManager graphLayoutManger;
    
    // Set up an event mainGraphObserver.
    private RegexSGEventObserver mainGraphObserver;
    private RegexSGEventObserver syntaxGraphObserver;
    private RegexSubgraphMouseAdapter sgm;
    private RegexSubgraphMouseAdapter sgm2;

    // secondary wholeGraph
    private RegexJComponent syntaxDisplay; 
    private SyntaxViewFactory syntaxViewFactory;
    private RegexHigraphView syntaxView;
    
    private SyntaxTreeLayoutManager syntaxLayoutManager;
    
    private RegexTextArea regexText;
    
    private RegexJList nodeListBar;

    /**
     * Construct the main frame
     */
    public RegexMainFrame() {
        this.initializeGraph();
        this.initializeControl();
    }

    private void initializeControl() {
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
        secondPanel.setTopComponent(regexText);
        secondPanel.setBottomComponent(mainGraphPanel);
        
        secondPanel.setResizeWeight(0.3);

        JSplitPane mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainPanel.setLeftComponent(secondPanel);
        mainPanel.setRightComponent(syntaxDisplay);
        mainPanel.setResizeWeight(0.66);
        
        getContentPane().add(mainPanel, BorderLayout.CENTER);

        /* tool bars */
        JPanel toolBarContainer = new JPanel();
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
     * @param testToolBar
     */
    @SuppressWarnings("serial")
    private void fillOptionToolBar(JToolBar toolBar) {
        Action action;
        action = new AbstractAction("Toggle Debug Border"){
            public void actionPerformed(ActionEvent e) {
                RegexNodeView.setDebugMode(!RegexNodeView.getDebugMode());
                repaint();
            }
            
        };
        toolBar.add(action);
    }

    /**
     * @param toolBar
     */
    @SuppressWarnings("serial")
    private void fillTestToolBar(JToolBar toolBar) {
        Action action = new AbstractAction("refresh") {
            public void actionPerformed(ActionEvent e) {
                refreshGraph();
            }
        };
        toolBar.add(action);

        action = new AbstractAction("add the root") {
            public void actionPerformed(ActionEvent e) {
                List<RegexNode> tops = subgraph.getTops();
                if(tops == null || tops.size() == 0){
                    RegexNode n = wholeGraph.makeRootNode(new SequencePayload());
                    subgraph.addTop(n);
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
                List<RegexNode> tops = wholeGraph.getTops();
                if (tops.size() > 0) {
                    RegexNode root = tops.get(0);
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
                List<RegexNode> nodes = subgraph.getNodes();
                if (nodes.size() > 0) {
                    Random rand = new Random();
                    int randIndex = rand.nextInt(nodes.size());
                    RegexNode root = nodes.get(randIndex);
                    
                    RegexPayload newPayLoad = new SequencePayload();
                    RegexNode newNode = wholeGraph.makeRootNode(newPayLoad);
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
                List<RegexNode> tops = subgraph.getTops();
                for(int i = 0; i < tops.size(); i++){
                    RegexNode top = tops.get(i);
                    if(top.canDelete()){
                        top.delete();
                    }
                }
                
                // then add new nodes, one by one
                RegexNode root, alt, kplus, current, leaf;
                root = wholeGraph.makeRootNode(new SequencePayload());
                subgraph.addTop(root);
                
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
                current = wholeGraph.makeRootNode(new SequencePayload());
                current.insertChild(0, leaf);
                alt.insertChild(0, current);
                leaf = wholeGraph.makeRootNode(new TerminalPayload("b"));
                current = wholeGraph.makeRootNode(new SequencePayload());
                current.insertChild(0, leaf);
                alt.insertChild(0, current);
                leaf = wholeGraph.makeRootNode(new TerminalPayload("a"));
                current = wholeGraph.makeRootNode(new SequencePayload());
                current.insertChild(0, leaf);
                kplus = wholeGraph.makeRootNode(new KleeneStarPayload());
                kplus.insertChild(0, current);
                current = wholeGraph.makeRootNode(new SequencePayload());
                current.insertChild(0, kplus);
                alt.insertChild(0, current);
                root.insertChild(0, alt);
                
                refreshGraph();
            }
        };
        toolBar.add(action);
        
        action = new AbstractAction("Alternation"){
            public void actionPerformed(ActionEvent e){
                // first delete all the nodes
                List<RegexNode> tops = subgraph.getTops();
                for(int i = 0; i < tops.size(); i++){
                    RegexNode top = tops.get(i);
                    if(top.canDelete()){
                        top.delete();
                    }
                }
                
                // then add new nodes, one by one
                RegexNode alt, current, leaf;
                
                alt = wholeGraph.makeRootNode(new AlternationPayload());
                leaf = wholeGraph.makeRootNode(new TerminalPayload("c"));
                current = wholeGraph.makeRootNode(new SequencePayload());
                current.insertChild(0, leaf);
                alt.insertChild(0, current);
                leaf = wholeGraph.makeRootNode(new TerminalPayload("b"));
                current = wholeGraph.makeRootNode(new SequencePayload());
                current.insertChild(0, leaf);
                alt.insertChild(0, current);
                leaf = wholeGraph.makeRootNode(new TerminalPayload("a"));
                current = wholeGraph.makeRootNode(new SequencePayload());
                current.insertChild(0, leaf);
                alt.insertChild(0, current);
                
                subgraph.addTop(alt);
                
                refreshGraph();
            }
        };
        toolBar.add(action);
        
        action = new AbstractAction("Nested_SEQ"){
            public void actionPerformed(ActionEvent ae){
                // first delete all the nodes
                List<RegexNode> tops = subgraph.getTops();
                for(int i = 0; i < tops.size(); i++){
                    RegexNode top = tops.get(i);
                    if(top.canDelete()){
                        top.delete();
                    }
                }
                
                // then add new nodes, one by one
                RegexNode root, a, b, c, d, e;
                root = wholeGraph.makeRootNode(new SequencePayload());
                subgraph.addTop(root);
                
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
                List<RegexNode> tops = subgraph.getTops();
                for(int i = 0; i < tops.size(); i++){
                    RegexNode top = tops.get(i);
                    if(top.canDelete()){
                        top.delete();
                    }
                }
                
                // then add new nodes, one by one
                RegexNode root, kplus, current, leaf;
                root = wholeGraph.makeRootNode(new SequencePayload());
                subgraph.addTop(root);
                
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
                List<RegexNode> tops = subgraph.getTops();
                for(int i = 0; i < tops.size(); i++){
                    RegexNode top = tops.get(i);
                    if(top.canDelete()){
                        top.delete();
                    }
                }
                
                // then add new nodes, one by one
                RegexNode root, seq, current, leaf;
                root = wholeGraph.makeRootNode(new SequencePayload());
                subgraph.addTop(root);
                
                leaf = wholeGraph.makeRootNode(new TerminalPayload("\\d{4}"));
                root.insertChild(0, leaf);
                
                current = wholeGraph.makeRootNode(new OptionalPayload());
                seq = wholeGraph.makeRootNode(new SequencePayload());
                leaf = wholeGraph.makeRootNode(new TerminalPayload("[.\\-]"));
                seq.insertChild(0, leaf);
                current.insertChild(0, seq);
                root.insertChild(0, current);
                
                leaf = wholeGraph.makeRootNode(new TerminalPayload("\\d{3}"));
                root.insertChild(0, leaf);
                
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
                
                leaf = wholeGraph.makeRootNode(new TerminalPayload("\\d{2}"));
                root.insertChild(0, leaf);
                
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
    
    public void refreshGraph(){
        this.mainGraphView.refresh();
        this.mainGraphDisplay.repaint();
        
        this.syntaxView.refresh();
        this.syntaxDisplay.repaint();
        
        this.subgraph.notifyObservers();
        
        this.regexText.refreshFromModel();
    }

    public static void main(String[] args) {
        SwingHelper.setSystemLookAndFeel();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                RegexMainFrame frame = new RegexMainFrame();
                frame.setVisible(true);
            }
        });
    }

    /* (non-Javadoc)
     * @see regex.model.IRegexHigraphObserver#changed(regex.model.RegexHigraph)
     */
    @Override
    public void changed(RegexHigraph regexHigraph) {
        if(regexHigraph == this.wholeGraph){
            this.refreshGraph();
        }
    }
}
