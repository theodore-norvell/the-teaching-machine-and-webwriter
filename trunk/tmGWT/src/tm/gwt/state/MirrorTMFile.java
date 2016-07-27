package tm.gwt.state;

import tm.interfaces.TMFileI ;

public class MirrorTMFile implements TMFileI {
    private String fileName ;
    
    public MirrorTMFile(String fileName) {
        this.fileName = fileName ;
    }

    @Override
    public String getFileName() {
        return fileName ;
    }

}
