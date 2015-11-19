//     Copyright 1998--2010 Michael Bruce-Lockhart and Theodore S. Norvell
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License. 
// You may obtain a copy of the License at 
//
//     http://www.apache.org/licenses/LICENSE-2.0 
//
// Unless required by applicable law or agreed to in writing, 
// software distributed under the License is distributed on an 
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
// either express or implied. See the License for the specific language 
// governing permissions and limitations under the License.

package tm.virtualMachine;

import tm.backtrack.BTFunction;
import tm.backtrack.BTTimeManager;
import tm.backtrack.BTVar;
import tm.languageInterface.NodeInterface;

public abstract class Evaluation {

	protected BTVar<NodeInterface> root ;   // A NodeInterface
	protected BTVar<NodeInterface> selected ; // A NodeInterface
	protected BTFunction<NodeInterface, Object> val ;

	protected Evaluation(VMState vms, NodeInterface rt) {
	    BTTimeManager timeMan = vms.getTimeManager() ;
	    root = new BTVar<NodeInterface>( timeMan ) ;
		root.set( rt ) ;
		selected = new BTVar<NodeInterface>( timeMan ) ;
		selected.set( null ) ;
		val = new BTFunction<NodeInterface, Object>( timeMan ) ; }

	public NodeInterface getRoot() { return root.get() ; }

	public void setSelected( NodeInterface nd ) { selected.set(nd) ; }

	public NodeInterface getSelected( ) { return selected.get() ; }

	public void map(NodeInterface e, Object d) { val.map(e, d) ; }

	public Object at( NodeInterface e ) { return val.at(e) ; }

	public abstract boolean isDone() ;
}