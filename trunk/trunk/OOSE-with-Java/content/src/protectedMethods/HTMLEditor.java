package protectedMethods;

import java.io.Writer;

public class HTMLEditor extends Editor {

    public void writeToFile() {
        Writer writer = openFileForWriting() ;
        // ... code to write an HTML document to the file
    }
}
