/**
 * Create on October 18, 2006
 */
package Viewer;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import tm.displayEngine.DisplayInterface;
import tm.interfaces.Datum;
import tm.interfaces.DisplayContextInterface;
import tm.subWindowPkg.ToolBar;
import tm.subWindowPkg.WorkArea;


/** 
 * The IOMage Image handling plug-in.
 *  
 * @author Michael Bruce-Lockhart, based on code by Yan Zhang
 * 
 */
public class ImageViewerSW extends WorkArea implements DisplayInterface, ImageViewer{

	/**
	 * The main UI for TM image I/O Plug-in
	 */
	private static final long serialVersionUID = 1L;

	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem openItem, saveItem, exitItem;
	
	private JInternalFrame inFrame;
	private JInternalFrame outFrame;
	private BufferedImage inImage ;
	private BufferedImage outImage ;
	
	private JLabel imageLabel;
	private JPanel imagePanel;

	private ImageIcon icon;
	
	public int numb;


	public ImageViewerSW(DisplayContextInterface dc, String configId) {
        super(dc.getImageSource(), configId);
		System.out.println("Creating " + configId);
		setPreferredSize(this.getViewportSize());
	
		mySubWindow.setJMenuBar(setMenuBar());
		
    	mySubWindow.addWorkArea(this, null);
		mySubWindow.setVisible(true);
		loadInitConfig();
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
		String[] doc = new String[] { "jpeg", "jpg" , "gif", "png"};
		ImageFilters[] filters = new ImageFilters[doc.length];
		for (int i = 0; i < doc.length; i++) {
			filters[i] = new ImageFilters();
			filters[i].addPostFix(doc[i]);
			openChooser.addChoosableFileFilter(filters[i]);
		}
		int interVal = openChooser.showOpenDialog(mySubWindow);
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
//		inFrame = new JInternalFrame("image " + count, true, true, true, true);
//		inFrame.addMouseListener(new MouseHandler());
		
		icon = new ImageIcon(buff);
		
		
		imageLabel = new JLabel();
		imageLabel.setIcon(icon);
		
		imagePanel = new JPanel();
		imagePanel.add(imageLabel, BorderLayout.CENTER);
		
		frame.add(imagePanel, BorderLayout.CENTER);
		frame.pack();
		
		add(frame, BorderLayout.CENTER);
//		frame.add(desk);
		
		// arrange location of the multiple images 
//		int FW = frame.getWidth() / 2;
//		int FH = frame.getHeight() / 2;
//		inFrame.setLocation(nextX, nextY);
		frame.setVisible(true);
		
	}

	public BufferedImage getImage(){
		if (inImage == null) openImage();
		return inImage;}
	
	public int getNumb(){return numb;}
	
	public void outputImage(int [] [] outArray){
		outImage = new BufferedImage(outArray.length, outArray[0].length,
				BufferedImage.TYPE_INT_RGB);
		for (int col = 0; col < outArray.length; col++)
			for (int row = 0; row < outArray[col].length; row++) {
				outImage.setRGB(col, row, outArray[row][col]);
			}
		outFrame = new JInternalFrame(inFrame.getName()+".modified", true, true, true, true);
		displayImage(outImage, outFrame);
	}


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
				System.err.println("select a inFrame");
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
	
	public String toString(){return "ImageViewerSW";}


	public Vector<Datum> getSelected() {
		// TODO Auto-generated method stub
		return null;
	}
	
//	public Component getWindowComponent(){return this;}
	
//	public void refresh(){}

}
