/**
 * Create on October 18, 2006
 */
package Viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import tm.configuration.Configuration;
import tm.configuration.ConfigurationServer;
import tm.displayEngine.DisplayInterface;
import tm.displayEngine.DisplayManager;
import tm.interfaces.CommandInterface;
import tm.interfaces.DisplayManagerInterface;
import tm.subWindowPkg.SubWindow;
import tm.subWindowPkg.ToolBar;
import tm.subWindowPkg.WorkArea;


/**
 * @author Yan Zhang
 * 
 */
public class ImageViewerJIF extends JInternalFrame implements DisplayInterface, ImageViewer{

	/**
	 * The main UI for TM image I/O Plug-in
	 */
	private static final long serialVersionUID = 1L;

	private JDesktopPane desk;

	private JInternalFrame inFrame;
	private JInternalFrame outFrame;

	private JMenuBar menuBar;

	private JMenu fileMenu;

	private JMenuItem openItem, saveItem, exitItem;

	private JLabel imageLabel;

	private JPanel imagePanel;

	private ImageIcon icon;
	
	private DisplayManagerInterface manager;
	private CommandInterface commandProcessor;
	private BufferedImage inImage; 
	private BufferedImage outImage; 
	
	private int numb;

	private int count;

	private int nextX, nextY, distance;

	public ImageViewerJIF(DisplayManagerInterface dm, String configId) {
        super();
        manager = dm;
		commandProcessor = manager.getCommandProcessor();
		System.out.println("Creating " + configId);
//		JFrame.setDefaultLookAndFeelDecorated(true);
		
//		frame = new JFrame("Image Viewer"); // change refs to w
		desk = new JDesktopPane();
		
		setJMenuBar(setMenuBar());
//		mySubWindow.setSize(300, 300);
//		mySubWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		desk.setVisible(true);
/*		frame.setLocation(
				(Toolkit.getDefaultToolkit().getScreenSize().width - frame
						.getWidth()) / 2, (Toolkit.getDefaultToolkit()
						.getScreenSize().height - frame.getHeight()) / 2);*/
		
		desk.addMouseListener(new MouseHandler());
		add(desk);
		ConfigurationServer server = ConfigurationServer.getConfigurationServer();
		server.register(this,configId);
		Configuration config = server.getConfiguration(this);
		if (config != null) {
			notifyOfLoad(config);   // load initial configuration
			config.dump();
		}
		else {
			setTitle(configId);
			setBounds(0,0,100,100);
			setVisible(true);
		}
		ImageServer.getImageServer().register(this);
	}
	

	private JMenuBar setMenuBar() {
		if (menuBar == null) {
			menuBar = new JMenuBar();
			menuBar.add(setFileMenu());
		}
		return menuBar;
	}

	private JMenu setFileMenu() {
		if (fileMenu == null) {
			fileMenu = new JMenu("File");
			fileMenu.setMnemonic(KeyEvent.VK_F);
			fileMenu.add(setOpenItem());
			fileMenu.add(setSaveItem());
			fileMenu.add(setExitItem());
		}
		return fileMenu;
	}
	private JMenuItem setOpenItem() {
		if (openItem == null) {
			openItem = new JMenuItem("Open.....");
			openItem.setMnemonic(KeyEvent.VK_O);
			openItem.setEnabled(true);
			openItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					openImage();
				}
			});
		}
		return openItem;
	}

	private void openImage() {
		System.out.println("******Trying to open an image!!******");
		JFileChooser openChooser = new JFileChooser();
		inImage = null;
		String[] doc = new String[] { "jpeg", "gif", "png", "jpg" };
		ImageFilters[] filters = new ImageFilters[doc.length];
		for (int i = 0; i < doc.length; i++) {
			filters[i] = new ImageFilters();
			filters[i].addPostFix(doc[i]);
			openChooser.addChoosableFileFilter(filters[i]);
		}
		int interVal = openChooser.showOpenDialog(this);
		if (interVal == JFileChooser.APPROVE_OPTION) {
			File openFile = openChooser.getSelectedFile();
			System.out.println("Open an image located in " + openFile.getPath());
			try {
				URL openURL = openFile.toURL();
				inImage = ImageIO.read(openURL);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Resource not found!!");
				System.exit(1);
			}
			int width = inImage.getWidth();
			int height = inImage.getHeight();
			int[][] openPixel = new int[width][height];
			if (inImage == null) {
				openPixel = null;
			} else {
				for (int row = 0; row < height; row++)
					for (int col = 0; col < width; col++) {
						openPixel[col][row] = inImage.getRGB(col, row);
					}
			}
			inFrame = new JInternalFrame(openFile.getName(), true, true, true, true);
			displayImage(inImage, inFrame);
		}
	}

	/**
	 * display a single image with InternalFram
	 * @param buff image buffer for the displayed image
	 */
	public void displayImage(BufferedImage buff, JInternalFrame frame) {
		JInternalFrame internalFrame = new JInternalFrame("image " + count, true, true, true, true);
		internalFrame.addMouseListener(new MouseHandler());
		
		icon = new ImageIcon(buff);
		
//		image.add(count++, buff);
		
		imageLabel = new JLabel();
		imageLabel.setIcon(icon);
		
		imagePanel = new JPanel();
		imagePanel.add(imageLabel, BorderLayout.CENTER);
		
		internalFrame.add(imagePanel, BorderLayout.CENTER);
		internalFrame.pack();
		
		desk.add(internalFrame);

//		frame.add(desk);
		
		// arrange location of the multiple images 
//		int FW = frame.getWidth() / 2;
//		int FH = frame.getHeight() / 2;
//		internalFrame.setLocation(nextX, nextY);
		internalFrame.setVisible(true);
		
		try {
			internalFrame.setSelected(false);	// No effect if only one internal frame there
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		
		if (distance == 0)
			distance = internalFrame.getHeight()
					   - internalFrame.getContentPane().getHeight();
		nextX += distance;
		nextY += distance;
/*		if (nextX + FW > FW * 2)
			nextX = 0;
		if (nextY + FH > FH * 2)
			nextY = 0;*/
	}
	
	public BufferedImage getImage(){return inImage;}

	public int getNumb(){return numb;}

	private JMenuItem setSaveItem() {
		if (saveItem == null) {
			saveItem = new JMenuItem("Save.....");
			saveItem.setMnemonic(KeyEvent.VK_S);
			saveItem.setEnabled(true);
			saveItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					saveImage();
				}
			});
		}
		return saveItem;
	}

	private void saveImage() {
		System.out.println("******Trying to save an image!******");
/*		JInternalFrame flag = desk.getSelectedFrame();
		int index = Integer.parseInt(flag.getTitle().split(" ")[1]);
		if (flag != null) {
			BufferedImage saveBuff = image.get(index);
			JFileChooser saveChooser = new JFileChooser();
			String[] doc = new String[] { "jpeg", "gif", "png", "jpg" };
			ImageFilters[] filters = new ImageFilters[doc.length];
			for (int i = 0; i < doc.length; i++) {
				filters[i] = new ImageFilters();
				filters[i].addPostFix(doc[i]);
				saveChooser.addChoosableFileFilter(filters[i]);
			}
			int interVal = saveChooser.showSaveDialog(frame);
			if (interVal != JFileChooser.APPROVE_OPTION) {
				return;
			} else {
				String suffix = saveChooser.getFileFilter().getDescription()
						.split(" ")[1];
				saveChooser.setToolTipText("Input the file name");
				saveChooser.setCurrentDirectory(new File("."));
				File saveFile = saveChooser.getSelectedFile();
				System.out.println("The image is saved in: "
						+ saveFile.getPath());
				try {
					ImageIO.write(saveBuff, suffix, saveFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}*/
	}

	private JMenuItem setExitItem() {
		if (exitItem == null) {
			exitItem = new JMenuItem("Exit");
			exitItem.setMnemonic(KeyEvent.VK_E);
			exitItem.setEnabled(true);
			exitItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.exit(0);
				}
			});
		}
		return exitItem;
	}


		private class MouseHandler extends MouseAdapter {
		public void mousePressed(MouseEvent event) {
/*			if(desk.getSelectedFrame()==null){
				System.err.println("select a internalFrame");
				return;
			}else{
				numb = Integer.parseInt(desk.getSelectedFrame().getTitle().split(" ")[1]);
			}*/
		}	
	}


	public ToolBar getToolBar() {
		// TODO Auto-generated method stub
		return null;
	}


	public void refresh() {
		// TODO Auto-generated method stub
		
	}


	public Component getWindowComponent() {
		return this;
	}


	public void notifyOfSave(Configuration arg0) {
		// TODO Auto-generated method stub
		
	}


	public void notifyOfLoad(Configuration config) {
	    Rectangle r = getBounds();
	    String temp = config.getValue("Position.x");
	    if (temp != null) r.x = new Integer(temp).intValue();
	    temp = config.getValue("Position.y");
	    if (temp != null) r.y = new Integer(temp).intValue();
	    temp = config.getValue("Width");
//        System.out.println("workArea loadNotify. Width = " + temp);
	    if (temp != null) r.width = new Integer(temp).intValue();
	    temp = config.getValue("Height");
	    if (temp != null) r.height = new Integer(temp).intValue();
	    temp = config.getValue("WindowTitle");
	    if (temp != null) setTitle(temp);
	    setBounds(r);
	    temp = config.getValue("Visible");
	    if (temp != null && temp.equalsIgnoreCase("false")) setVisible(false);
	    else
	    	setVisible(true);
//	    validate();
	}
	
	public String toString(){return "ImageViewerJIF";}

}
