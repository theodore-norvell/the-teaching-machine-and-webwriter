package tm.gwt.shared.state;

import java.util.Set ;
import java.util.TreeSet ;

import com.google.gwt.user.client.rpc.IsSerializable ;

import tm.interfaces.SelectionInterface ;
import tm.interfaces.TagSetInterface ;

public class MirrorTagSet implements TagSetInterface, IsSerializable {

    private Set<String> rep ;
    private static Set<String> EMPTYSET = new TreeSet<String> () ;
    
    private MirrorTagSet() { }
    
    public MirrorTagSet( TagSetInterface that ) {
        if( that.isEmpty() ) rep = EMPTYSET ;
        else {
            rep = that.getRep() ;
        }
    }

    @Override
    public boolean selectionIsValid(SelectionInterface selection) {
        return selection.evaluate( this ) ;
    }

    @Override
    public boolean contains(String tag) {
        return rep.contains( tag ) ;
    }

    @Override
    public boolean isEmpty() {
        return rep.isEmpty() ;
    }

    @Override
    public Set<String> getRep() {
        return rep ;
    }

}
