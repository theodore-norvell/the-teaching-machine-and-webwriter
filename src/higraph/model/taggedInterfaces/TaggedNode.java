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

package higraph.model.taggedInterfaces;

import java.util.List;

import higraph.model.interfaces.*;

public interface TaggedNode
    <   T extends Tag<T,NP>,
        NP extends TaggedPayload<T,NP>,
        EP extends Payload<EP>,
        HG extends TaggedHigraph<T,NP,EP,HG,WG,SG,N,E>,
        WG extends TaggedWholeGraph<T,NP,EP,HG,WG,SG,N,E>,
        SG extends TaggedSubgraph<T,NP,EP,HG,WG,SG,N,E>,
        N extends TaggedNode<T,NP,EP,HG,WG,SG,N,E>,
        E extends TaggedEdge<T,NP,EP,HG,WG,SG,N,E>
    >
extends Node<NP,EP,HG,WG,SG,N,E>{
	
    public T getTag() ;
    
	boolean canInsertChild( int position, T tag ) ;
	
	void insertChild( int position, T tag ) ;

	/** Can a segment of the children be replace?
	 * 
	 * @param first The beginning of the segment
	 * @param count Number of items in the segment
	 * @param tags The proposed tag sequence for the replacement children.
	 * @return
	 */
    boolean canReplaceChildrenTags( int first, int count, List<T> tags ) ;


	/** Replace a segment of children with new nodes based on a list of tags.
	 * 
	 * @param first The beginning of the segment
	 * @param count Number of items in the segment
	 * @param tags The proposed tag sequence for the replacement children.
	 * @return
	 */
    void replaceChildrenTags( int first, int count, List<T> tags ) ;
	
	boolean canReplace( T tag ) ;
	
	void replace( T tag ) ;
	
	List<T> getTagList() ;

}
