/**
 * RegexDialog.java
 * 
 * @date: Aug 8, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.regex.app;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import tm.backtrack.BTTimeManager;
import visreed.awt.VisreedSubgraphMouseAdapter;
import visreed.extension.regex.swing.RegexJList;
import visreed.model.VisreedNode;
import visreed.model.VisreedPayload;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.model.payload.AlternationPayload;
import visreed.model.payload.KleenePlusPayload;
import visreed.model.payload.KleeneStarPayload;
import visreed.model.payload.OptionalPayload;
import visreed.model.payload.SequencePayload;
import visreed.model.payload.TerminalPayload;
import visreed.swing.VisreedSubgraphEventObserver;
import visreed.swing.VisreedJComponent;
import visreed.swing.editor.VisreedTextArea;
import visreed.view.IGraphContainer;
import visreed.view.SyntaxViewFactory;
import visreed.view.VisreedHigraphView;
import visreed.view.VisreedNodeView;
import visreed.view.VisreedViewFactory;
import visreed.view.VoidPointDecorator;
import visreed.view.layout.AlternationLayoutManager;
import visreed.view.layout.SyntaxTreeLayoutManager;

/**
 * @author Xiaoyu Guo
 *
 */
public class RegexDialog extends JDialog implements IGraphContainer {
    private static final long serialVersionUID = 5262927236579638509L;

    private void initializeGraph(){
        this.timeMan = new BTTimeManager();
        this.wholeGraph = new VisreedWholeGraph(this.timeMan);
        this.subgraph = this.wholeGraph.makeSubGraph();
        
        // main graph
        this.mainGraphDisplay = new VisreedJComponent();
        this.mainViewFactory = new VisreedViewFactory(timeMan);
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
        
        this.mainGraphObserver = new VisreedSubgraphEventObserver(this, this.wholeGraph);
        this.sgm = new VisreedSubgraphMouseAdapter(
            mainGraphView,
            mainGraphObserver
        );
        this.sgm.installIn(mainGraphDisplay);
        
        // secondary graph
        this.syntaxDisplay = new VisreedJComponent(); 
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

        this.syntaxGraphObserver = new VisreedSubgraphEventObserver(this, this.wholeGraph);
        this.sgm2 = new VisreedSubgraphMouseAdapter(
            syntaxView,
            syntaxGraphObserver
        );
        this.sgm2.installIn(syntaxDisplay);
        
        // text area
        this.regexText = new VisreedTextArea();
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
    }
    
    // main wholeGraph
    private BTTimeManager timeMan;
    private VisreedWholeGraph wholeGraph;
    private VisreedSubgraph subgraph;

    private VisreedJComponent mainGraphDisplay;
    private VisreedViewFactory mainViewFactory;
    private VisreedHigraphView mainGraphView;
    private AlternationLayoutManager graphLayoutManger;
    
    // Set up an event mainGraphObserver.
    private VisreedSubgraphEventObserver mainGraphObserver;
    private VisreedSubgraphEventObserver syntaxGraphObserver;
    private VisreedSubgraphMouseAdapter sgm;
    private VisreedSubgraphMouseAdapter sgm2;

    // secondary wholeGraph
    private VisreedJComponent syntaxDisplay; 
    private SyntaxViewFactory syntaxViewFactory;
    private VisreedHigraphView syntaxView;
    
    private SyntaxTreeLayoutManager syntaxLayoutManager;
    
    private VisreedTextArea regexText;
    
    private RegexJList nodeListBar;

    /**
     * Construct the main frame
     */
    public RegexDialog() {
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
        
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
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
                VisreedNodeView.setDebugMode(!VisreedNodeView.getDebugMode());
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
                List<VisreedNode> tops = subgraph.getTops();
                if(tops == null || tops.size() == 0){
                    VisreedNode n = wholeGraph.makeRootNode(new SequencePayload());
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
                List<VisreedNode> nodes = subgraph.getNodes();
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
                List<VisreedNode> tops = subgraph.getTops();
                for(int i = 0; i < tops.size(); i++){
                    VisreedNode top = tops.get(i);
                    if(top.canDelete()){
                        top.delete();
                    }
                }
                
                // then add new nodes, one by one
                VisreedNode root, alt, kplus, current, leaf;
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
                List<VisreedNode> tops = subgraph.getTops();
                for(int i = 0; i < tops.size(); i++){
                    VisreedNode top = tops.get(i);
                    if(top.canDelete()){
                        top.delete();
                    }
                }
                
                // then add new nodes, one by one
                VisreedNode alt, current, leaf;
                
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
                List<VisreedNode> tops = subgraph.getTops();
                for(int i = 0; i < tops.size(); i++){
                    VisreedNode top = tops.get(i);
                    if(top.canDelete()){
                        top.delete();
                    }
                }
                
                // then add new nodes, one by one
                VisreedNode root, a, b, c, d, e;
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
                List<VisreedNode> tops = subgraph.getTops();
                for(int i = 0; i < tops.size(); i++){
                    VisreedNode top = tops.get(i);
                    if(top.canDelete()){
                        top.delete();
                    }
                }
                
                // then add new nodes, one by one
                VisreedNode root, kplus, current, leaf;
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
                List<VisreedNode> tops = subgraph.getTops();
                for(int i = 0; i < tops.size(); i++){
                    VisreedNode top = tops.get(i);
                    if(top.canDelete()){
                        top.delete();
                    }
                }
                
                // then add new nodes, one by one
                VisreedNode root, seq, current, leaf;
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
    
    public String getText(){
        return this.regexText.getText();
    }
    
    public void setText(String value){
        this.regexText.setText(value);
        regexText.refreshFromText();
        refreshGraph();
    }
}
