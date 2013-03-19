package play.ide.checker.error;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import play.higraph.model.PLAYNode;

/**
 * 
 * @author Shiwei Han
 * 
 */
public class ErrorMap {
	HashMap<PLAYNode,List<PLAYError>> map;
	
	public ErrorMap(){
		map = new HashMap<PLAYNode,List<PLAYError>>();
	}
	
	public void addError(PLAYNode n, PLAYError e){

		if(map.get(n)==null)
			map.put(n, new ArrayList<PLAYError>());

		map.get(n).add(e);

		/*print errors*/
		System.out.println("ERROR: "+e.getMessage());	
		//System.err.println(e.getMessage());
		
	}
	
	public List<PLAYError> getErrorMsg(PLAYNode n){
		return map.get(n);
	}

}
