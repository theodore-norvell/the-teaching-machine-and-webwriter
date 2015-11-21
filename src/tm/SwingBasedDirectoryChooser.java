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
 * Created on Apr 6, 2005
 *
 */
package tm;

import java.awt.Frame;
import java.io.File;
import java.io.IOException;

import javax.swing.* ;

/** This class is a hold-over from the old days when
 * you couldn't be sure that swing was supported
 * (i.e. from when IE + jview were still a going concern).
 * We used to have a common interface that AWT and swing directory
 * choosers conformed to and this class adapted the JFileChooser
 * to that interface. Since we dropped the AWT chooser, this
 * class has little purpose.
 * 
 * @author theo
 *
 */
public class SwingBasedDirectoryChooser {

    private JFileChooser jfc = new JFileChooser() {
        private static final long serialVersionUID = 1541813407103968848L;

        // Work around slowness on Windows XP.
        // See http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6372808
        @Override
        public void updateUI() {
            putClientProperty("FileChooser.useShellFolder", Boolean.FALSE);
            super.updateUI();
        }
    };
    private Frame parent ;
    private File selectedFile;
    private String buttonText;
    
    public SwingBasedDirectoryChooser(Frame parent) {
        this.jfc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY ) ;
        this.parent = parent ; }

    /** 
     * @see tm.DirectoryChooserInterface#setTitle(java.lang.String)
     */
    public void setTitle(String title) {
       this.jfc.setDialogTitle( title ) ;
    }

    /** 
     * @see tm.DirectoryChooserInterface#setDirectory(java.lang.String)
     */
    public void setDirectory(String directory) {
        this.jfc.setCurrentDirectory( new File( directory ) );
    }

    /** 
     * Show the dialog.
     */
    public void show() {
        int status = this.jfc.showDialog( parent, buttonText ) ;
        if (status == JFileChooser.APPROVE_OPTION) {
            selectedFile = this.jfc.getSelectedFile() ; }
        else {
            selectedFile = null ; }
        }

    /** 
     * @see tm.DirectoryChooserInterface#getDirectory()
     */
    public String getDirectory() {
        
        try {
            if( selectedFile == null ) return null ;
            return selectedFile.getCanonicalPath() ;
        }
        catch (IOException e) {
            return null ;
        }
    }

    /** 
     * @see tm.DirectoryChooserInterface#setButtonText(java.lang.String)
     */
    public void setButtonText(String buttonText) {
        this.buttonText = buttonText ;
    }
}
