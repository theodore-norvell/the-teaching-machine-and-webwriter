package porthole;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JLabel;

public class Porthole extends JApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2199622803944198057L;
    private ImagePanel myPanel;
    private String myId;
    private JLabel myLabel;

	public void init() {
	    //Execute a job on the event-dispatching thread:
	    //creating this applet's GUI.
	    try {
	        javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
	            public void run() {
	                createGUI();
	            }
	        });
	    } catch (Exception e) {
	        System.err.println("createGUI didn't successfully complete");
	    }
	}

	private void createGUI() {
	    myId = getParameter("instance");
	    myPanel = new ImagePanel();
	    Color backGround = myId.equals("first")? Color.cyan : myId.equals("second")? Color.green: Color.red;
	    myPanel.setBackground(backGround);
	    myPanel.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.black));
	    myLabel = new JLabel("Porthole " + myId + " waiting for snapshot.");
	    myPanel.add(myLabel, BorderLayout.CENTER);
	    getContentPane().add(myPanel, BorderLayout.CENTER);
//	    System.out.println("Trying to register " + myId + " porthole");
//	    PortholeRegistrar.getRegistrar().add(this, myId);	    
   }
	

	public void post(Object snap){
		Image snapShot = (Image) snap;
		myPanel.removeAll();
		System.out.println("Posting " + snap.toString());
		myPanel.setImage(((Image)snap));
		this.resize(myPanel.getPreferredSize().width, myPanel.getPreferredSize().height);
		repaint();
	}
	
	public String getId(){return myId;}
	
	

}
