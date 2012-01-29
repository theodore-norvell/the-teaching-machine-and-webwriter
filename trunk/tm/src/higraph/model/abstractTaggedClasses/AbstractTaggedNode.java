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

/*
 * Created on 2009-09-07 by Theodore S. Norvell. 
 */
package higraph.model.abstractTaggedClasses;

import java.util.ArrayList;
import java.util.List;

import higraph.model.abstractClasses.*;
import higraph.model.interfaces.*;
import higraph.model.taggedInterfaces.*;
import tm.utilities.Assert;

public abstract class AbstractTaggedNode
    <   T extends Tag<T,NP>,
        NP extends TaggedPayload<T,NP>,
        EP extends Payload<EP>,
        HG extends AbstractTaggedHigraph<T,NP,EP,HG,WG,SG,N,E>,
        WG extends AbstractTaggedWholeGraph<T,NP,EP,HG,WG,SG,N,E>,
        SG extends AbstractTaggedSubgraph<T,NP,EP,HG,WG,SG,N,E>,
        N extends AbstractTaggedNode<T,NP,EP,HG,WG,SG,N,E>, 
        E extends AbstractTaggedEdge<T,NP,EP,HG,WG,SG,N,E>
    >
extends AbstractNode<NP,EP,HG,WG,SG,N,E>
implements TaggedNode<T,NP,EP,HG,WG,SG,N,E>
{

	protected AbstractTaggedNode(WG higraph, NP payload) {
        super( higraph, payload) ;
        }
    
    protected AbstractTaggedNode(N original, N parent ) {
        super( original, parent) ;
    }
    
    @Override 
    public T getTag() {
        return payload.get().getTag() ;
    }
    
    @Override
    public boolean canReplacePayload(NP payload) {
        Assert.check( ! deleted.get() ) ;
        return canReplace( payload.getTag() ) ;
    }

    @Override 
    public boolean canInsertChild(int position, T tag) {
        if( 0 <= position && position <= getNumberOfChildren() ) {
            List<T> tagList = getTagList() ;
            tagList.add(position, tag) ;
            return getTag().contentModel( tagList ) ; }
        else return false ;
    }
    
    @Override 
    public void insertChild(int position, T tag ) {
        assert( canInsertChild(position, tag) ) ;
        N newNode = getWholeGraph().makeRootNode(tag.defaultPayload()) ;
        insertChild( position, newNode ) ;
    }

    @Override 
    public boolean canInsertChild(int position, N node) {
        return super.canInsertChild(position, node) 
            &&  canInsertChild( position, node.getTag() ) ;
    }
    
    @Override
    public boolean canReplace(N node) {
        return super.canReplace(node) 
            && canReplace( node.getTag() ) ;
    }
    
    @Override 
    public boolean canReplace(T tag) {
        Assert.check(! deleted()) ;
        N parent = getParent() ;
        if(parent== null)
            return true;
        else {
            int position = getPosition() ;
            List<T> tagList = parent.getTagList() ;
            tagList.remove(position);
            tagList.add(position, tag);
            return parent.getTag().contentModel(tagList);
        }
    }
    
    @Override 
    public void replace(T tag) {
        assert( canReplace(tag) ) ;
        N newNode = getWholeGraph().makeRootNode(tag.defaultPayload()) ;
        replace( newNode ) ;
    }
    
    @Override
    public boolean canDetach() {
        Assert.check(! deleted()) ;
        N parent = getParent() ;
        if( parent == null)
            return true;
        else {
            int position = getPosition() ;
            List<T> tagList = parent.getTagList() ;
            tagList.remove(position);
            return parent.getTag().contentModel(tagList);
        }
    }
    
    
    @Override
    public List<T> getTagList() {
        List<T> result = new ArrayList<T>() ;
        for( int i=0, noc= getNumberOfChildren(); i < noc ; ++i )
            result.add( getChild(i).getTag() ) ;
        return result;
    }
}


