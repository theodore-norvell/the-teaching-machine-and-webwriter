package tm;

import java.awt.Image;
import java.net.URL;

import tm.interfaces.Inputter;
import tm.interfaces.PlatformServicesInterface;

public class ApplicationPlatform implements PlatformServicesInterface {

	@Override
	public Image fetchImage(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URL makeURL(String root) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URL getDocumentBase() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isApplet() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public URL getCodeBase() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void showDocument(URL helpURL, String string) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showMessage(String title, String message, Throwable th) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showMessage(String title, String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public Inputter getInputter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void showTheTM(boolean visible) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isTMShowing() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void exit() {
		// TODO Auto-generated method stub

	}

}
