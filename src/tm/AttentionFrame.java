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

package tm;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.*;
import javax.swing.text.BadLocationException;

public class AttentionFrame extends JFrame
{
    JTextArea topTextArea = new JTextArea(5,60) ;
    JScrollPane topScrollPane = new JScrollPane(topTextArea) ;
    JPanel buttonPanel = new JPanel() ;
    JButton closeButton = new JButton("Close") ;
    JButton detailButton = new JButton("Show details") ;
    JTextArea bottomTextArea = new JTextArea(20,60) ;
    JScrollPane bottomScrollPane = new JScrollPane(bottomTextArea) ;
    boolean detailsAreShowing = false ;
    
    public AttentionFrame(String title, String message, Throwable th) {
        this( title, message, th.getMessage() + "\n" + formatStackTrace( th ) ) ;
    }

    public AttentionFrame(String title, String message) {
	    this( title, message, (String) null ) ;
	}
	
	public AttentionFrame(String title, String message, String detail){
		super(title) ;
		
		buttonPanel.setLayout( new FlowLayout() ) ;
		buttonPanel.add( closeButton ) ;
		if( detail != null ) buttonPanel.add( detailButton ) ;
		
		LayoutManager lm = new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS) ;
		this.getContentPane().setLayout( lm ) ;
		this.add( topScrollPane ) ;
		this.add( buttonPanel ) ;

		topTextArea.setLineWrap( true ) ;
        topTextArea.append( message ) ;
		if( detail != null ) {
	        bottomTextArea.setLineWrap( true ) ;
		    bottomTextArea.append( detail ) ;
		}
		
		closeButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                dispose() ; }} ) ;

        detailButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if( detailsAreShowing ) {
                    remove(bottomScrollPane) ;
                    detailButton.setText( "Show details" ) ;
                    detailsAreShowing = false ;
                    AttentionFrame.this.pack() ; }
                else {
                    add(bottomScrollPane) ;
                    detailButton.setText( "Hide details" ) ;
                    detailsAreShowing = true ;
                    AttentionFrame.this.pack() ; }
                }} ) ;

        this.pack();
	}

    
    private static String formatStackTrace(Throwable th) {
        StringWriter w = new StringWriter() ;
        PrintWriter pw = new PrintWriter(w) ;
        th.printStackTrace( pw ) ;
        pw.close();
        return w.toString() ;
    }

}

