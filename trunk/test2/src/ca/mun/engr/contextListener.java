package ca.mun.engr;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class contextListener implements ServletContextListener {
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}
	
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		ExternalRunner.create();
	}

}

