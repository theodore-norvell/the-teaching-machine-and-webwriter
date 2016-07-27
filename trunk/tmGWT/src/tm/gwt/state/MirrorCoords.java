package tm.gwt.state;

import tm.interfaces.SourceCoordsI ;
import tm.interfaces.TMFileI ;

public class MirrorCoords implements SourceCoordsI {

    private TMFileI file ;
    private int line ;

    public MirrorCoords(TMFileI file, int line ) {
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
