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
import javax.swing.JTextField;

import tm.interfaces.EvaluatorInterface;

/*
Input Frame.  Provides the user a way to add input to the standard input.
 */

@SuppressWarnings("serial")
public class InputFrame extends JFrame
{
	EvaluatorInterface evaluator ; // Where to send the strings.

	JLabel label1 = new JLabel();
	JLabel label2 = new JLabel();
	BigJButton button1 = new BigJButton();
	BigJButton button2 = new BigJButton();
	JTextField textField1 = new JTextField(15);
	
	public InputFrame( EvaluatorInterface evaluator )
	{
		super("Input request");

		this.evaluator = evaluator ;

		GridBagLayout gbl = new GridBagLayout() ;
		GridBagConstraints constr = new GridBagConstraints() ;
		setLayout(gbl);
		setVisible(false);
		
		label1.setText("Keyboard input required");
		constr.gridx = 0 ;
		constr.gridy = 0 ;
		constr.gridheight = 1 ;
		constr.gridwidth = 4 ;
		constr.anchor = GridBagConstraints.LINE_START ;
		add(label1, constr);
		
		label2.setText("Text:");
		constr.gridx = 0 ;
		constr.gridy = 1 ;
		constr.gridheight = 1 ;
		constr.gridwidth = 1 ;
		constr.anchor = GridBagConstraints.LINE_START ;
		add(label2, constr);
		
		constr.gridx = 1 ;
		constr.gridy = 1 ;
		constr.gridheight = 1 ;
		constr.gridwidth = 1 ;
		constr.anchor = GridBagConstraints.LINE_START ;
		add(textField1, constr);
		
		button1.setBackground(Color.lightGray);
		button1.setText("Enter text followed by newline character.");
		constr.gridx = 3 ;
		constr.gridy = 1 ;
		constr.gridheight = 1 ;
		constr.gridwidth = 2 ;
		constr.anchor = GridBagConstraints.LINE_START ;
		add(button1, constr);
		
		button2.setBackground(Color.lightGray);
		button2.setText("Enter text followed by end-of-file character.");
		constr.gridx = 0 ;
		constr.gridy = 2 ;
		constr.gridheight = 1 ;
		constr.gridwidth = 4 ;
		constr.anchor = GridBagConstraints.LINE_START ;
		add(button2, constr);
		
		SymAction lSymAction = new SymAction();
		button1.addActionListener(lSymAction);
		button2.addActionListener(lSymAction);
		textField1.addActionListener(lSymAction) ;
		
		this.pack(); 
		this.setVisible(true) ;
		textField1.requestFocus() ;
	}
	
	class SymAction implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			Object object = event.getSource();
			if (object == button1)
				button1_Action(event);
			else if (object == button2)
				button2_Action(event);
			else if (object == textField1)
				button1_Action(event);
		}
	}

	void button1_Action(ActionEvent event)
	{
		String text = textField1.getText() + "\n" ;
		evaluator.addInputString( text ) ;
		setVisible( false ) ;
		dispose() ;
	}

	void button2_Action(ActionEvent event)
	{
		String text = textField1.getText() + "\0" ;
		evaluator.addInputString( text ) ;
		setVisible( false ) ;
		dispose() ;
	}
}
