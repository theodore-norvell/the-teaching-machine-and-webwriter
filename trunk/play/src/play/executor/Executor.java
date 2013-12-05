/**
 * Executor.java
 * Created by: Ravneet Sandhu
 */
package play.executor;

import play.higraph.model.PLAYEdge;
import play.higraph.model.PLAYEdgeLabel;
import play.higraph.model.PLAYNode;
import play.higraph.model.PLAYSubgraph;
import play.higraph.model.PLAYTag;
import play.higraph.model.PLAYWholeGraph;
import play.higraph.swing.PLAYHigraphJComponent;
import play.higraph.view.PLAYNodeView;

public class Executor implements Runnable{
	private PLAYSubgraph sg;
	private PLAYWholeGraph wg;
	private Environment env;
	private PLAYNodeView view;
	private PLAYHigraphJComponent hj;
	
	public Executor(PLAYSubgraph sg,PLAYHigraphJComponent hj){
		this.sg = sg;
		this.hj = hj;
		wg = sg.getWholeGraph();
	}
	
	public void run(){
		System.out.println(sg.getNumberOfTops());
		env = new Environment(null);
		String s = null;
		for(PLAYNode n : sg.getTops()){
			view  = n.getView();
			if(n.getTag() == PLAYTag.CLASS){
				System.out.println("class");
				s = view.execute(env,n,sg);				
			}
		}
		System.out.println(s);
	}
}
