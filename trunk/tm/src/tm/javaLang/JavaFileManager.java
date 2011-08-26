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
 * JavaFileManager.java
 *
 * Created on August 3, 2004, 8:34 AM
 */

package tm.javaLang;

/**
 *
 * @author  mpbl
 */

import tm.clc.analysis.ScopedName;
import tm.javaLang.parser.SimpleNode;
import tm.javaLang.analysis.SHProgram;
import tm.utilities.*;
import tm.utilities.TMFile;

public class JavaFileManager {
    static private JavaFileManager theManager = null;
    private JLang javaLang; // reference back to jLang for compilation passthrough
    /** The libraryFileSource is static so that it can be a cache that is maintained
     * throughout the lifetime of an applet or application. This solve a major
     * performance problem for applets, I hope.
     */
    static private FileSource libraryFileSource = null ;	// locations to find files
    private FileSource userFileSource;
    private java.util.Vector fileList;
    private int iterator;

    /** Creates a new instance of JavaFileManager */
    private JavaFileManager() {
    }

    private void setup(FileInfo startFile, JLang jLang){
        javaLang = jLang;

        // As the libraryFileSource is static, we only initialize it once.
        if( libraryFileSource == null ) {
            libraryFileSource = new CachingFileSource(
                    new LocalResourceFileSource(this.getClass(), "library/", ".jlib") ) ;
        }
        userFileSource = startFile.file.getFileSource();
        fileList = new java.util.Vector(5,5);
        fileList.addElement(startFile);    	
    }

    public static JavaFileManager createManager(FileInfo startFile, JLang jLang) {
        if (theManager == null)
            theManager = new JavaFileManager();
        theManager.setup(startFile, jLang);
        return theManager;
    }

    public static JavaFileManager getFileManager(){
        return theManager;
    }

    public void addToList(FileInfo file){
        fileList.addElement(file);
    }

    public TMFile getFileFromPackage(String fName, ScopedName packageName){
        String path = (packageName.equals(SHProgram.getDefaultPackageName())) ?
                fName :
                    packageName.getName("/") + "/" + fName ;
        TMFile theFile = new TMFile(userFileSource, path + ".java");
        if (theFile.isReadable()) return theFile;           
        theFile = new TMFile(libraryFileSource, path);
        if (theFile.isReadable()) return theFile;           
        return null;
    }



    public SimpleNode doFirstTwoPasses(TMFile file) {
        return javaLang.doPreviousPasses(file).compilationUnitNode;
    }

    public Iterator getIterator(){
        return new Iterator();
    }

    // These two methods are used by the iterators

    private int getListSize(){return fileList.size();}

    private FileInfo getFileFromList(int pos) {
        return (pos >= 0 && pos < fileList.size() ) ?
                (FileInfo)(fileList.elementAt(pos)) : null;       
    }

    /* Internal iterator class
     * Design Note. By making this class internal it has access to private methods in its
     *          parent's class, allowing the iterator to access data that cannot be
     *          accessed directly
     */
    class Iterator{
        private int index;
        //        private JavaFileManager myManager;

        Iterator(/*JavaFileManager manager*/){
            //            myManager = manager;
            index = 0;
        }

        void reset(){index = 0;}

        boolean atEnd(){
            return index == getListSize();
        }

        void increment(){
            if (!atEnd()) index++;
        }

        FileInfo getFile(){
            return (atEnd()) ? null : getFileFromList(index);
        }   
    }


}

// structure to hold file tuple
class FileInfo{
    TMFile file;
    SimpleNode compilationUnitNode;

    FileInfo(TMFile file, SimpleNode compilationUnitNode){
        this.file = file;
        this.compilationUnitNode = compilationUnitNode;
    }
}



