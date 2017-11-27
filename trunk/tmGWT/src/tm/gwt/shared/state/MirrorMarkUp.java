package tm.gwt.shared.state;

import com.google.gwt.user.client.rpc.IsSerializable ;

import tm.interfaces.MarkUpI ;
import tm.interfaces.TagSetInterface ;

// TODO Delete this class
public class MirrorMarkUp implements MarkUpI, IsSerializable {

    private int column ;
    private int command ;
    private MirrorTagSet tagSet ;
    
    private MirrorMarkUp() {}
    
    public MirrorMarkUp(MarkUpI that ) {
        column = that.getColumn() ;
        command = that.getCommand() ;
        TagSetInterface ts = that.getTagSet() ;
        if( ts == null ) tagSet = null ;
        else tagSet = new MirrorTagSet( ts ) ;
    }

    @Override
    public int getColumn() {
        return column ;
    }

    @Override
    public int getCommand() {
        return command ;
    }

    @Override
    public TagSetInterface getTagSet() {
        return tagSet ;
    }

}
