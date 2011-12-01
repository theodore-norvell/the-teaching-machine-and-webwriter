/**
 * JavaCCNodeToolBar.java
 * 
 * @date: Nov 30, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.swing.nodebar;

import visreed.extension.javaCC.model.tag.JavaCCTag;
import visreed.model.VisreedWholeGraph;
import visreed.swing.nodebar.VisreedNodeToolBar;
import visreed.swing.nodebar.VisreedNodeToolBarIconData;

/**
 * @author Xiaoyu Guo
 *
 */
public class JavaCCNodeToolBar extends VisreedNodeToolBar {
	private static final long serialVersionUID = -3399018118500071043L;

	/**
	 * @param wg
	 * @param data
	 */
	public JavaCCNodeToolBar(VisreedWholeGraph wg) {
		super(wg, PREDEFINED_NODE_DATA);
		// TODO Auto-generated constructor stub
	}
    
    private static final VisreedNodeToolBarIconData[] PREDEFINED_NODE_DATA = new VisreedNodeToolBarIconData[]{
        new VisreedNodeToolBarIconData("Regular Expression Production", "/images/toolbar_node_regp.png",     	JavaCCTag.REGULAR_PRODUCTION	),
        new VisreedNodeToolBarIconData("BNF Production",  				"/images/toolbar_node_bnfp.png",     	JavaCCTag.BNF_PRODUCTION		),
        
        
        new VisreedNodeToolBarIconData("LSEQ",      "/images/toolbar_node_lseq.png",     	JavaCCTag.LEXICAL_SEQUENCE		),
        new VisreedNodeToolBarIconData("LALT",    	"/images/toolbar_node_lalt.png",     	JavaCCTag.LEXICAL_ALTERNATION	),
        new VisreedNodeToolBarIconData("LKLN+",     "/images/toolbar_node_lkln+.png",   	JavaCCTag.LEXICAL_KLEENE_PLUS	),
        new VisreedNodeToolBarIconData("LKLN*",     "/images/toolbar_node_lklnstar.png", 	JavaCCTag.LEXICAL_KLEENE_STAR	),
        new VisreedNodeToolBarIconData("LOPT",      "/images/toolbar_node_lopt.png", 		JavaCCTag.LEXICAL_OPTIONAL		),
        new VisreedNodeToolBarIconData("LRPN",      "/images/toolbar_node_lrpn.png", 		JavaCCTag.LEXICAL_REPEAT_RANGE	),
        new VisreedNodeToolBarIconData("Terminal",  "/images/toolbar_node_lter.png",     	JavaCCTag.LEXICAL_TERMINAL		),
        
        new VisreedNodeToolBarIconData("GSEQ",  	"/images/toolbar_node_gseq.png",     	JavaCCTag.GRAMMAR_SEQUENCE		),
        new VisreedNodeToolBarIconData("GALT",  	"/images/toolbar_node_galt.png",     	JavaCCTag.GRAMMAR_ALTERNATION	),
        new VisreedNodeToolBarIconData("GKLN+",  	"/images/toolbar_node_gkln+.png",     	JavaCCTag.GRAMMAR_KLEENE_PLUS	),
        new VisreedNodeToolBarIconData("GKLN*",  	"/images/toolbar_node_gklnstar.png",    JavaCCTag.GRAMMAR_KLEENE_STAR	),
        new VisreedNodeToolBarIconData("GOPT",  	"/images/toolbar_node_gopt.png",     	JavaCCTag.GRAMMAR_OPTIONAL		),
        new VisreedNodeToolBarIconData("GRPN",  	"/images/toolbar_node_grpn.png",     	JavaCCTag.GRAMMAR_REPEAT_RANGE	),
        new VisreedNodeToolBarIconData("GTER",  	"/images/toolbar_node_gter.png",     	JavaCCTag.GRAMMAR_TERMINAL		),
    };

}
