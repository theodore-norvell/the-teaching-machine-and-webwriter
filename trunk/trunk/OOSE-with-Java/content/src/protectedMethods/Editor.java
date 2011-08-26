package protectedMethods;

import java.io.Writer;

public abstract class Editor {
    
    // ...
    
    protected Writer openFileForWriting() {
        //... here there is a complex algorithm to 
        //    prompt the user for a file, find the
        //    file and open it.
        return writer ;
    }
    
    public abstract void writeToFile() ;
}
