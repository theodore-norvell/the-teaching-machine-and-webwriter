package tm.gwt.state;

import com.google.gwt.user.client.rpc.IsSerializable ;

import tm.interfaces.TMFileI ;

public class MirrorTMFile implements TMFileI, IsSerializable {
    private String fileName ;
    
    // Needed for serializability
    private MirrorTMFile() {}
    
    public MirrorTMFile(String fileName) {
        this.fileName = fileName ;
    }

    @Override
    public String getFileName() {
        return fileName ;
    }

}
