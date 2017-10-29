package tm.gwt.state;

import com.google.gwt.user.client.rpc.IsSerializable ;

import tm.interfaces.SourceCoordsI ;
import tm.interfaces.TMFileI ;

public class MirrorCoords implements SourceCoordsI, IsSerializable{

    private MirrorTMFile file ;
    private int line ;

    public MirrorCoords(MirrorTMFile file, int line ) {
        this.file = file ;
        this.line = line ;
    }
    
    @Override
    public TMFileI getFile() {
        return file ;
    }

    @Override
    public int getLineNumber() {
        return line ;
    }

}
