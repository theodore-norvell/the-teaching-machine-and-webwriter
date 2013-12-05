/**
 * SyntaxPallet.java - play.ide.view - PLAY
 *
 * Created on 2012-2-15 by Kai Zhu
 */
package play.ide.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import play.controller.Controller;
import play.higraph.model.PLAYTag;
import play.higraph.model.PLAYWholeGraph;
import play.higraph.swing.SyntaxTransferHandler;


/**
 * @author Kai Zhu
 * 
 */
public class SyntaxPallet extends JPanel {

	private static final long serialVersionUID = 59182330371195540L;

	private JList<PLAYTag> syntaxList;

	private DefaultListModel<PLAYTag> syntaxListModel;
	
	private PLAYWholeGraph wg;

	private String[] colName = {"className"};
	private Object[][] data = {{"class1"}};
	private int i=0;
	
	private static ArrayList<NewClass> newClass = new ArrayList<NewClass>();
	DefaultTableModel model = new DefaultTableModel();
	JTable table = new JTable(model);

	public SyntaxPallet(PLAYWholeGraph wg) {
		this.syntaxList = new JList<>();
		this.syntaxListModel = new DefaultListModel<PLAYTag>();
		this.wg = wg;
		
		JLabel addClass = new JLabel("Class");
		this.add(addClass);
		JButton startButton = new JButton("+");
		this.add(startButton);
		
		startButton.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						//textField[i] = new JTextField();
						//add(textField[i]);
						//ViewHandler.	
						model.addRow(new Object [] {"Class"});
						Controller.getInstance().createNewClass();
						
					}
				});

		
		//JScrollPane sp = new JScrollPane();
	//	sp.setBounds(10,5,10,15);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		//JPanel jp = new JPanel();
		//jp.setBounds(0, 0, 10, 10);
		//JScrollPane sp = new JScrollPane(table);
		//jp.add(sp);
		//sp.add(table);
		
		this.add(table);
		model.addColumn(colName);
		//model.addRow(new Object[] {"class"});
		
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					JTable target = (JTable)e.getSource();
					int row = target.getSelectedRow();
					int column = target.getSelectedColumn();
					System.out.println(row);
					System.out.println(column);

					//if(row==0 && column == 0){


						NewClass nc = SyntaxPallet.getClass(row);
						System.out.println(nc);

						nc.displayClass();

						i++;
				}
			}
		});



		//this.syntaxListModel.addElement(PLAYTag.CLASS);
		this.syntaxListModel.addElement(PLAYTag.METHOD);
		this.syntaxListModel.addElement(PLAYTag.ASSIGN);
		this.syntaxListModel.addElement(PLAYTag.VARDECL);
		this.syntaxListModel.addElement(PLAYTag.IF);
		this.syntaxListModel.addElement(PLAYTag.WHILE);
		this.syntaxListModel.addElement(PLAYTag.EXPPLACEHOLDER);
		this.syntaxListModel.addElement(PLAYTag.NUMBERTYPE);
		this.syntaxListModel.addElement(PLAYTag.BOOLEANTYPE);
		this.syntaxListModel.addElement(PLAYTag.STRINGTYPE);
		this.syntaxListModel.addElement(PLAYTag.NULLTYPE);
		this.syntaxListModel.addElement(PLAYTag.NUMBERLITERAL);
		this.syntaxListModel.addElement(PLAYTag.STRINGLITERAL);
		this.syntaxListModel.addElement(PLAYTag.LOCALVAR);
		this.syntaxListModel.addElement(PLAYTag.WORLDVAR);
		//this.syntaxListModel.addElement(PLAYTag.PLUS);
		// expsRootTreeNode.add(new DefaultMutableTreeNode(PLAYTag.VAR));
		//this.syntaxListModel.addElement(PLAYTag.DOT);


		this.syntaxList.setModel(this.syntaxListModel);
		this.syntaxList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.syntaxList.setVisibleRowCount(-1);
		this.syntaxList.setDragEnabled(true);
		this.syntaxList.setTransferHandler(new SyntaxTransferHandler());
		this.syntaxList.setBackground(this.getBackground());

		this.add(this.syntaxList);
	}


	public JTable getList(){
		return table;
	}

	public static void setClass(NewClass nc)
	{
		newClass.add(nc);
	}

	public static NewClass getClass(int i){
		System.out.println("in get class = " + newClass.get(i));
		return newClass.get(i);

	}
}
