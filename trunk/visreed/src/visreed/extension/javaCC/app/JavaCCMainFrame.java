/**
 * JavaCCMainFrame.java
 * 
 * @date: Sep 13, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.app;

import java.awt.BorderLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import tm.backtrack.BTTimeManager;
import visreed.app.VisreedMainFrame;
import visreed.awt.VisreedSubgraphMouseAdapter;
import visreed.extension.javaCC.model.JavaCCWholeGraph;
import visreed.extension.javaCC.swing.JavaCCProductionsTreeView;
import visreed.extension.javaCC.swing.editor.JavaCCTextArea;
import visreed.swing.SwingHelper;
import visreed.swing.VisreedJComponent;
import visreed.swing.VisreedSubgraphEventObserver;
import visreed.swing.TreeView.VisreedOutlineTreeView;
import visreed.swing.properties.PropertyEditor;
import visreed.view.SyntaxViewFactory;
import visreed.view.VisreedViewFactory;
import visreed.view.VoidPointDecorator;
import visreed.view.layout.AlternationLayoutManager;
import visreed.view.layout.SyntaxTreeLayoutManager;

/**
 * @author Xiaoyu Guo
 *
 */
public class JavaCCMainFrame extends VisreedMainFrame {
    public static void main(String[] args) {
        SwingHelper.setSystemLookAndFeel();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	JavaCCMainFrame frame = new JavaCCMainFrame();
                frame.setVisible(true);
            }
        });
    }
    
	private static final long serialVersionUID = -8133879588309521708L;

	public JavaCCMainFrame() {
		super();
	}

	/* (non-Javadoc)
	 * @see visreed.app.VisreedMainFrame#initializeControl()
	 */
	@Override
	protected void initializeControl(){
		super.initializeControl();
	}
	
	/* (non-Javadoc)
	 * @see visreed.app.VisreedMainFrame#initializeSidePanel()
	 */
	@Override
	protected void initializeSidePanel(){
		super.initializeSidePanel();
		
		// create and add properties panel
		PropertyEditor propertiesEditor = new PropertyEditor();
		propertiesEditor.setFillsViewportHeight(true);
		
		JScrollPane propertiesPanel = new JScrollPane(propertiesEditor);
		propertiesPanel.setBorder(BorderFactory.createEtchedBorder());
		
		this.sidePanelContainer.add("Properties", propertiesPanel);
		
		// create and add the outline panel
		JPanel outlinePanel = new JPanel();
		outlinePanel.setLayout(new BorderLayout());
		
		VisreedOutlineTreeView outline = new VisreedOutlineTreeView(this.wholeGraph);
		outlinePanel.add(outline, BorderLayout.CENTER);
		outlinePanel.setBorder(BorderFactory.createEtchedBorder());
		
		this.sidePanelContainer.add("Outline", outlinePanel);
		
		// create and add the productions panel
		JPanel productionsPanel = new JPanel();
		productionsPanel.setLayout(new BorderLayout());

		JavaCCProductionsTreeView productions = new JavaCCProductionsTreeView(this.wholeGraph);
		productionsPanel.add(productions, BorderLayout.CENTER);
		productionsPanel.setBorder(BorderFactory.createEtchedBorder());
		
		this.sidePanelContainer.add("Productions", productionsPanel);
	}
	

    /* (non-Javadoc)
     * @see visreed.app.VisreedMainFrame#initializeGraph()
     */
	@Override
    protected void initializeGraph(){
        this.timeMan = new BTTimeManager();
        this.wholeGraph = new JavaCCWholeGraph(this.timeMan);
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
        syntaxDisplay.setForeground(SystemColor.control);
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
        this.regexText = new JavaCCTextArea();
        this.regexText.setSubHigraph(subgraph);
        this.regexText.getInputMap().put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK), 
            "refreshModelFromText"
        );
        this.regexText.setAutoscrolls(true);
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
}
