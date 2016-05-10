package tm.interfaces;

import java.net.URL;

public interface PlatformServicesInterface extends ImageSourceInterface {

	/** Return a URL for a file.
	 * 
	 * @param root  The path from the platform's document base to a directory
	 * @return null if the URL would be malformed. Otherwise a URL representing the directory.
	 */
	URL makeURL(String root);
	
	/** Return a URL for the document base.
	 * See AppletStub.
	 * @return null if the URL would be malformed. Otherwise a URL representing the directory.
	 */
	URL getDocumentBase();


	/** Is this program running as an applet?
	 * @return
	 */
	boolean isApplet();

	/** Get a URL representing the code base.
	 * See AppletStub.
	 * @return
	 */
	URL getCodeBase();

	/** Show a file in a browser. 
	 * For example, this can be used to show the help file for the TM.
	 * @param helpURL
	 * @param string
	 */
	void showDocument(URL helpURL, String string);

	/** Display a message to the user.  For example in a popup dialog.
	 * 
	 * @param title -- The dialogue title.
	 * @param message -- The message.  Newlines allowed.
	 * @param th -- a Throwable.  This can be null.
	 */
	void showMessage(String title, String message, Throwable th);

	/** Display a message to the user.  For example in a popup dialog.
	 * 
	 * @param title -- The dialogue title.
	 * @param message -- The message.  Newlines allowed.
	 */
	void showMessage(String title, String message);

	/** Obtain an object that can be used for input */
	Inputter getInputter();

	/** Ensure that the user can (or cannot) see the Teaching Machine */
	void showTheTM(boolean visible);

	boolean isTMShowing();

	/** Shutdown or hide the TM as appropriate. */
	void exit();
}
