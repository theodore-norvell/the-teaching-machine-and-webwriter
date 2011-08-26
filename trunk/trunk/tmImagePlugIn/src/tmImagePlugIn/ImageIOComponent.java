package tmImagePlugIn;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.*;

import tm.interfaces.DisplayContextInterface;
import tm.utilities.Debug;
import tm.utilities.TMFile;

import tm.utilities.FastFileChooser;
import mmgood.StatusI;

public class ImageIOComponent extends ImageComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4147738033451800613L;

	protected JFileChooser mFileChooser = null ;
	
	protected DisplayContextInterface displayContext ;
	
	SaveAction mSaveAction  ; {
		java.net.URL imageURL = getClass().getResource("Save32x32.PNG") ;
		Icon loadIcon = imageURL == null ? null : new ImageIcon(imageURL);
		mSaveAction = new SaveAction( loadIcon ) ;
		mSaveAction.setEnabled(true ) ;
	}
	
	LoadAction mLoadAction  ; {
		java.net.URL imageURL = getClass().getResource("Open32x32.PNG") ;
		Icon loadIcon = imageURL == null ? null : new ImageIcon(imageURL);
		mLoadAction = new LoadAction( loadIcon ) ;
		mLoadAction.setEnabled(true ) ;
	}
	
	
	ImageIOComponent(StatusI pStatus, String name, DisplayContextInterface context ) {
		super(pStatus ) ;
        displayContext = context ;
		Border blackline = BorderFactory.createLineBorder(Color.black);
		TitledBorder title = BorderFactory.createTitledBorder(
			       blackline, name);
		title.setTitleJustification(TitledBorder.CENTER);
		setBorder(title);
		add( mImagePanel ) ;
	}
	
	public void invokeSaveAction(){
	    mSaveAction.actionPerformed(null);
	}
	
	public void invokeLoadAction(){
		mLoadAction.actionPerformed(null);
	}
	
	public BufferedImage getImage(){
		return mImagePanel.getImage();
	}
	
	
	public void setImage(BufferedImage image){
		System.out.println("Setting " + image.getWidth() + " x " +
				image.getHeight() + " image.");
		mImagePanel.changeImage(image);
	}

	public void updateImage(){
		mImagePanel.updateImage();
	}
	


private class SaveAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 3403086620423523186L;
		
		SaveAction(Icon pSaveIcon) {
			super("Save", pSaveIcon ) ;
		}
		public void actionPerformed(ActionEvent arg0) {
			int value = mFileChooser.showSaveDialog(ImageIOComponent.this) ;
			if( value == JFileChooser.APPROVE_OPTION ) {
				try {
				    if( mFileChooser == null )
				        mFileChooser =  new FastFileChooser(new File(".")) ;
	                File file = mFileChooser.getSelectedFile() ;
					mImagePanel.writeFile( file ) ;}
                catch( SecurityException exc ) {
                    Debug.getInstance().msg("Exception on write image") ;
                    Debug.getInstance().msg(exc) ;
                    String msg = exc.getMessage() ;
                    mStatus.popupWarning("Warning", "Could not write file because of a security exception. "+msg ) ;
                    return ; }
                catch( IOException exc ) {
                    Debug.getInstance().msg("Exception on write image") ;
                    Debug.getInstance().msg(exc) ;
                    String msg = exc.getMessage() ;
                    mStatus.popupWarning("Warning", "Could not write file because of an IO exception. "+msg) ;
                    return ; }
				catch( Throwable exc ) {
                    Debug.getInstance().msg("Exception on write image") ;
                    Debug.getInstance().msg(exc) ;
				    String msg = exc.getMessage() ;
					mStatus.popupWarning("Warning", "Could not write file. "+msg) ;
					return ;
				}
			}
		}
	}
	
	
	private class LoadAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7915718973468469881L;
		
		LoadAction(Icon pLoadIcon) {
			super("Load", pLoadIcon ) ;
		}
		
		public void actionPerformed(ActionEvent arg0) {
		    try {
		        TMFile tmFile = displayContext.getCommandProcessor().getDataFile() ;
		        if( tmFile != null  ) {
		            mImagePanel.readFile( tmFile ) ; }
		    } 
		    catch( Throwable exc ) {
		        Debug.getInstance().msg("Exception on load image") ;
		        Debug.getInstance().msg(exc) ;
		        mStatus.popupWarning("Warning", "Could not read file. "+exc.getMessage()) ;
		        return ;
		    }           
		}
	}
}
