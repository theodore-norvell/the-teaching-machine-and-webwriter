/**
 * RegexMainFrame.java
 * 
 * @date: Nov 14, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.regex.app;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import visreed.app.VisreedMainFrame;
import visreed.extension.regex.model.tag.RegexTag;
import visreed.model.VisreedNode;
import visreed.model.payload.AlternationPayload;
import visreed.model.payload.RepeatRangePayload;
import visreed.model.payload.TerminalPayload;
import visreed.swing.SwingHelper;

/**
 * @author Xiaoyu Guo
 *
 */
public class RegexMainFrame extends VisreedMainFrame {
	private static final long serialVersionUID = -4306227443468279340L;

	@SuppressWarnings("serial")
	@Override
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
                    VisreedNode n = wholeGraph.makeRootNode(RegexTag.SEQUENCE);
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
                    
                    VisreedNode newNode = wholeGraph.makeRootNode(RegexTag.SEQUENCE);
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
                root = wholeGraph.makeRootNode(RegexTag.SEQUENCE);
                rootSubgraph.addTop(root);
                
                kplus = wholeGraph.makeRootNode(RegexTag.KLEENE_PLUS);
                current = wholeGraph.makeRootNode(RegexTag.SEQUENCE);
                leaf = wholeGraph.makeRootNode(new TerminalPayload(RegexTag.TERMINAL, "e"));
                current.insertChild(0, leaf);
                kplus.insertChild(0, current);
                root.insertChild(0, kplus);
                
                leaf = wholeGraph.makeRootNode(new TerminalPayload(RegexTag.TERMINAL, "d"));
                root.insertChild(0, leaf);
                
                alt = wholeGraph.makeRootNode(new AlternationPayload(RegexTag.ALTERNATION));
                leaf = wholeGraph.makeRootNode(new TerminalPayload(RegexTag.TERMINAL, "c"));
                alt.insertChild(2, leaf);
                leaf = wholeGraph.makeRootNode(new TerminalPayload(RegexTag.TERMINAL, "b"));
                current = wholeGraph.makeRootNode(RegexTag.SEQUENCE);
                current.insertChild(0, leaf);
                alt.getChild(1).replace(current);
                leaf = wholeGraph.makeRootNode(new TerminalPayload(RegexTag.TERMINAL, "a"));
                current = wholeGraph.makeRootNode(RegexTag.SEQUENCE);
                current.insertChild(0, leaf);
                kplus = wholeGraph.makeRootNode(RegexTag.KLEENE_STAR);
                kplus.insertChild(0, current);
                current = wholeGraph.makeRootNode(RegexTag.SEQUENCE);
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
                root = wholeGraph.makeRootNode(RegexTag.SEQUENCE);
                alt = wholeGraph.makeRootNode(new AlternationPayload(RegexTag.ALTERNATION));
                leaf = wholeGraph.makeRootNode(new TerminalPayload(RegexTag.TERMINAL, "c"));
                alt.insertChild(2, leaf);
                leaf = wholeGraph.makeRootNode(new TerminalPayload(RegexTag.TERMINAL, "b"));
                current = wholeGraph.makeRootNode(RegexTag.SEQUENCE);
                current.insertChild(0, leaf);
                alt.getChild(1).replace(current);
                leaf = wholeGraph.makeRootNode(new TerminalPayload(RegexTag.TERMINAL, "a"));
                current = wholeGraph.makeRootNode(RegexTag.SEQUENCE);
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
                root = wholeGraph.makeRootNode(RegexTag.SEQUENCE);
                rootSubgraph.addTop(root);
                
                a = wholeGraph.makeRootNode(RegexTag.SEQUENCE);
                b = wholeGraph.makeRootNode(RegexTag.SEQUENCE);
                c = wholeGraph.makeRootNode(RegexTag.SEQUENCE);
                a.insertChild(0, b);
                a.insertChild(0, c);
                root.insertChild(0, a);
                
                d = wholeGraph.makeRootNode(RegexTag.SEQUENCE);
                root.insertChild(1, d);
                
                e = wholeGraph.makeRootNode(new TerminalPayload(RegexTag.TERMINAL, 'g'));
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
                root = wholeGraph.makeRootNode(RegexTag.SEQUENCE);
                rootSubgraph.addTop(root);
                
                kplus = wholeGraph.makeRootNode(new RepeatRangePayload(RegexTag.REPEAT_RANGE, 5));
                current = wholeGraph.makeRootNode(RegexTag.SEQUENCE);
                leaf = wholeGraph.makeRootNode(new TerminalPayload(RegexTag.TERMINAL, "c"));
                current.insertChild(0, leaf);
                kplus.insertChild(0, current);
                root.insertChild(0, kplus);
                
                kplus = wholeGraph.makeRootNode(RegexTag.KLEENE_PLUS);
                current = wholeGraph.makeRootNode(RegexTag.SEQUENCE);
                leaf = wholeGraph.makeRootNode(new TerminalPayload(RegexTag.TERMINAL, "e"));
                current.insertChild(0, leaf);
                kplus.insertChild(0, current);
                root.insertChild(0, kplus);
                
                kplus = wholeGraph.makeRootNode(RegexTag.KLEENE_STAR);
                current = wholeGraph.makeRootNode(RegexTag.SEQUENCE);
                leaf = wholeGraph.makeRootNode(new TerminalPayload(RegexTag.TERMINAL, "f"));
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
                root = wholeGraph.makeRootNode(RegexTag.SEQUENCE);
                rootSubgraph.addTop(root);
                
                current = wholeGraph.makeRootNode(new RepeatRangePayload(RegexTag.REPEAT_RANGE, 4));
                root.insertChild(0, current);
                
                leaf = wholeGraph.makeRootNode(new TerminalPayload(RegexTag.TERMINAL, "\\d"));
                current.insertChild(0, leaf);
                
                current = wholeGraph.makeRootNode(RegexTag.OPTIONAL);
                seq = wholeGraph.makeRootNode(RegexTag.SEQUENCE);
                leaf = wholeGraph.makeRootNode(new TerminalPayload(RegexTag.TERMINAL, "[.\\-]"));
                seq.insertChild(0, leaf);
                current.insertChild(0, seq);
                root.insertChild(0, current);
                
                current = wholeGraph.makeRootNode(new RepeatRangePayload(RegexTag.REPEAT_RANGE, 3));
                root.insertChild(0, current);
                
                leaf = wholeGraph.makeRootNode(new TerminalPayload(RegexTag.TERMINAL, "\\d"));
                current.insertChild(0, leaf);
                
                current = wholeGraph.makeRootNode(RegexTag.OPTIONAL);
                seq = wholeGraph.makeRootNode(RegexTag.SEQUENCE);
                leaf = wholeGraph.makeRootNode(new TerminalPayload(RegexTag.TERMINAL, "[.\\-]"));
                seq.insertChild(0, leaf);
                current.insertChild(0, seq);
                root.insertChild(0, current);
                
                current = wholeGraph.makeRootNode(RegexTag.OPTIONAL);
                seq = wholeGraph.makeRootNode(RegexTag.SEQUENCE);
                leaf = wholeGraph.makeRootNode(new TerminalPayload(RegexTag.TERMINAL, ")"));
                seq.insertChild(0, leaf);
                current.insertChild(0, seq);
                root.insertChild(0, current);

                current = wholeGraph.makeRootNode(new RepeatRangePayload(RegexTag.REPEAT_RANGE, 2));
                root.insertChild(0, current);
                
                leaf = wholeGraph.makeRootNode(new TerminalPayload(RegexTag.TERMINAL, "\\d"));
                current.insertChild(0, leaf);
                
                leaf = wholeGraph.makeRootNode(new TerminalPayload(RegexTag.TERMINAL, "[1-9]"));
                root.insertChild(0, leaf);
                
                current = wholeGraph.makeRootNode(RegexTag.OPTIONAL);
                seq = wholeGraph.makeRootNode(RegexTag.SEQUENCE);
                leaf = wholeGraph.makeRootNode(new TerminalPayload(RegexTag.TERMINAL, "("));
                seq.insertChild(0, leaf);
                current.insertChild(0, seq);
                root.insertChild(0, current);

                refreshGraph();
            }
        };
        toolBar.add(action);
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
}
