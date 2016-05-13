package tm;

//Copyright 1998--2015 Michael Bruce-Lockhart and Theodore S. Norvell
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License. 
//You may obtain a copy of the License at 
//
//http://www.apache.org/licenses/LICENSE-2.0 
//
//Unless required by applicable law or agreed to in writing, 
//software distributed under the License is distributed on an 
//"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
//either express or implied. See the License for the specific language 
//governing permissions and limitations under the License.

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel ;
import javax.swing.JTextField;

import tm.interfaces.EvaluatorInterface;

/*
Input Frame.  Provides the user a way to add input to the standard input.
 */

@SuppressWarnings("serial")
public class InputFrame extends TMDialog
{
	JLabel label1 = new JLabel();
	JLabel label2 = new JLabel();
	BigJButton button1 = new BigJButton();
	BigJButton button2 = new BigJButton();
	JTextField textField1 = new JTextField(15);

	
	public InputFrame(  TMMainPanel tmMainPanel, final EvaluatorInterface evaluator )
	{
	    super( tmMainPanel ) ;
		
		label1.setText("Keyboard input required.");
		mainPanel.add(label1);
		
		JPanel inputPanel = new JPanel() ;
		label2.setText("Text:") ;
		inputPanel.add(label2) ;
		inputPanel.add(textField1) ;
		
		mainPanel.add(  inputPanel ) ;
		
		JPanel buttonPanel = new JPanel() ;
		button1.setBackground(Color.lightGray);
		button1.setText("Enter text followed by newline character.");
		buttonPanel.add(button1);
		
		button2.setBackground(Color.lightGray);
		button2.setText("Enter text followed by end-of-file character.");
		buttonPanel.add(button2);
		
		mainPanel.add( buttonPanel ) ;
        
        textField1.requestFocus() ;
        
		ActionListener addNewlineAndDismiss = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textField1.getText() + "\n" ;
                evaluator.addInputString( text ) ;
                setVisible( false ) ;
                dismiss() ; } } ;
                
		button1.addActionListener( addNewlineAndDismiss );

        textField1.addActionListener( addNewlineAndDismiss ) ;
        
		button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textField1.getText() + "\0" ;
                evaluator.addInputString( text ) ;
                setVisible( false ) ;
                dismiss() ;
            } } ) ;
	}
}
