package ca.mun.engr;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.*;


public class JP_1 {
	public static JP_1 obj = null;
	public DrawingPanel panel;
    private Shape circle = null;
    private Shape square = null;
    private Shape triangle = null;
    public MyJFrame frame = null;
    private JDesktopPane desk = null;
    private JInternalFrame internalframe = null;
    static int FrameCount = 0;
    
    public static JP_1 go(String[] args) {
    	obj = new JP_1();
        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                obj.go2();
            }
        });
        //while(ready == false){}
    	return obj;
    }
    
    public void go2(){
    	frame = new MyJFrame("Test JPanel");
//    	internalframe = new JInternalFrame("New Windows",true,true,true,true);
//    	internalframe.setSize(300,300);
    	frame.setLayout(new BorderLayout());
    	desk = new JDesktopPane();
//    	frame.setContentPane(desk);
    	frame.add(desk, BorderLayout.CENTER);
    	
    	
    	JPanel subPanel = new JPanel();
    	subPanel.setLayout(new BorderLayout());
    	subPanel.setBackground(Color.red);
    	
    	frame.add(subPanel,BorderLayout.NORTH);
//        desk.add(subPanel, BorderLayout.NORTH);
        subPanel.add(createWindowsButton(), BorderLayout.NORTH);
        subPanel.add(createButton1(), BorderLayout.CENTER);
        subPanel.add(createButton2(), BorderLayout.EAST);
        subPanel.add(createButton3(), BorderLayout.WEST);
    	
    	frame.setJMenuBar(createMenuBar());
    	
    	
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setSize(600,750);
        desk.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
//        frame.setMinimumSize(new Dimension(400,400));
//        panel.setVisible(false);
        frame.setVisible(true);
    }
    
    
    
    protected JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        //Set up the file menu.
        JMenu menu = new JMenu("File");
        menuBar.add(menu);
        
//        menu.getPopupMenu().setLightWeightPopupEnabled(false);
        menu.isLightweight();
        

        //Set up the new menu item.
        JMenuItem menunew = new JMenuItem("New");
//        menunew.addActionListener(new ActionListener(){
//    		public void actionPerformed(ActionEvent e){
////    			internalframe = new JInternalFrame("New Windows",true,true,true,true);
////    			panel = new DrawingPanel();
////    			internalframe.setContentPane(panel);
////    			desk.add(internalframe);
//////    			frame.add(internalframe, BorderLayout.CENTER);
////    			internalframe.setVisible(true);
//    			System.out.println("111");
//    		}
//    	});
        menu.add(menunew);
        
        menunew.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				internalframe = new JInternalFrame("New Window"+ (++FrameCount),true,true,true,true);
    			internalframe.setSize(300,300);
    			panel = new DrawingPanel();
    			internalframe.setContentPane(panel);
    			desk.add(internalframe);
//    			frame.add(internalframe, BorderLayout.CENTER);
    			internalframe.setVisible(true);
//				
			}
        });

//        //Set up the second menu item.
//        JMenuItem menuquit = new JMenuItem("Quit");
//        menuquit.addMouseListener(new MouseAdapter(){
//    		public void mouseClicked(MouseEvent e){
////    			internalframe = new JInternalFrame("New Windows",true,true,true,true);
////    			panel = new DrawingPanel();
////    			internalframe.setContentPane(panel);
////    			desk.add(internalframe);
//////    			frame.add(internalframe, BorderLayout.CENTER);
//    			internalframe.setVisible(false);
//    		}
//    	});
//        menu.add(menuquit);

        return menuBar;
    }
    
    private JButton createButton1() {
        JButton button1 = new JButton("Circle");
        button1.setMaximumSize(new Dimension(10,20));
        button1.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e) {
//            	if(internalframe.isVisible() ){
            	try{
	            	if(circle!=null){
	            		panel.shapes.remove(circle);
	                    circle = null;
	                    panel.repaint();
	                }
	                else{
	                	
	                    circle = new Circle(50,50,100,100);
	                    panel.shapes.add(circle);
	                    panel.repaint();
	                 }
            	}catch(Exception e1){
//            		JOptionPane.showMessageDialog(frame, 
//            				"You need to create a New Window!",
//            				"Warning",
//            				JOptionPane.ERROR_MESSAGE);
            	}
            }
             
        });
        return button1;
    }
    private JButton createButton2(){
    	JButton button2 = new JButton("Square");
    	 button2.setMaximumSize(new Dimension(10,20));
    	button2.addMouseListener(new MouseAdapter(){
    		public void mouseClicked(MouseEvent e){
//    			if(internalframe.isVisible() ){
    			try{
	    			if(square!=null){
	            		panel.shapes.remove(square);
	                    square = null;
	                    panel.repaint();
	                }
	                else{
	                	
	                    square = new Square(50,75,100,50);
	                    panel.shapes.add(square);
	                    panel.repaint();
	                 }
            	}catch(Exception e1){
//            		
//            		JOptionPane.showMessageDialog(frame, 
//            				"You need to create a New Window!",
//            				"Warning",
//            				JOptionPane.ERROR_MESSAGE);
            	}
    		}
    	});
    	return button2;
    }
    private JButton createButton3(){
    	JButton button3 = new JButton("Triangle");
    	 button3.setMaximumSize(new Dimension(10,20));
    	button3.addMouseListener(new MouseAdapter(){
    		public void mouseClicked(MouseEvent e){
//    			if(panel.hasFocus() ){
    			try{
	    			if(triangle!=null){
	            		panel.shapes.remove(triangle);
	                    triangle = null;
	                    panel.repaint();
	                }
	                else{
	                	
	                    triangle = new Triangle(100,50,100,75);
	                    panel.shapes.add(triangle);
	                    panel.repaint();
	                 }
            	}catch(Exception e1){
//            		JOptionPane.showMessageDialog(frame, 
//            				"You need to create a New Window!",
//            				"Warning",
//            				JOptionPane.ERROR_MESSAGE);
            	}
    		}
    	});
    	return button3;
    }
    
    private JButton createWindowsButton(){
    	JButton buttonWindows = new JButton("New Windows");
    	buttonWindows.addMouseListener(new MouseAdapter(){
    		public void mouseClicked(MouseEvent e){
    			
    			MyInternalFrame internalframe = new MyInternalFrame();
//    			internalframe = new JInternalFrame("New Window"+ (++FrameCount),true,true,true,true);
//    			internalframe.setSize(300,300);
    			panel = new DrawingPanel();
    			internalframe.setContentPane(panel);
    			panel.requestFocus();
    			JTextField txt = new JTextField();
    			panel.setLayout(new BorderLayout());
    			txt.setBounds(10, 150, 100, 20);
    			panel.add(txt, BorderLayout.NORTH);
    			desk.add(internalframe);
//    			frame.add(internalframe, BorderLayout.CENTER);
    			internalframe.setVisible(true);
    		}
    	});
    return buttonWindows;
      
    }
}