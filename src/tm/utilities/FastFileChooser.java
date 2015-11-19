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
 * Created on 2009-06-26 by Theodore S. Norvell. 
 */
package tm.utilities;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

public class FastFileChooser extends JFileChooser {

    private static final long serialVersionUID = 4013403397486740617L;

    public FastFileChooser() {
        super() ;
    }

    public FastFileChooser(String currentDirectoryPath) {
        super(currentDirectoryPath);
    }

    public FastFileChooser(File currentDirectory) {
        super(currentDirectory);
    }

    public FastFileChooser(FileSystemView fsv) {
        super(fsv);
    }

    public FastFileChooser(File currentDirectory, FileSystemView fsv) {
        super(currentDirectory, fsv);
    }

    public FastFileChooser(String currentDirectoryPath, FileSystemView fsv) {
        super(currentDirectoryPath, fsv);
    }
    
    // Work around slowness on Windows XP.
    // See http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6372808
    @Override
    public void updateUI() {
        putClientProperty("FileChooser.useShellFolder", Boolean.FALSE);
        super.updateUI();
    }

}


