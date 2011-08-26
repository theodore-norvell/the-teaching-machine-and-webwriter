/*
 * Created on 7-Apr-2005
 *
 * @author mpbl
 *
 */

import tm.scripting.ScriptManager;

public class ScriptTests {
	
	public ScriptTests(){
	}
	
	public void doTests(){
		int [] A = new int[10];
		for (int i = 0; i < A.length; i++)
			A[i] = -10 + (int) (20 * Math.random());

		int from = 3;
		int to = 7;
		if(to>from){
			ScriptManager.stopAuto();
		}
	}
	
	

	public static void main() {
		ScriptTests mt = new ScriptTests();
		mt.doTests();
		
	}
}
