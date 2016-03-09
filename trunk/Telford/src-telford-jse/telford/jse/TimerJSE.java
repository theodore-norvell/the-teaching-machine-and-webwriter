package telford.jse;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class TimerJSE implements telford.common.Timer {
	
	Timer timer;
	
	public TimerJSE(int delay,boolean repeats, telford.common.Root root, final telford.common.ActionListener actionListener) {
		timer = new Timer(delay, new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				actionListener.actionPerformed(new ActionEventJSE(arg0));
			}});
		timer.setRepeats(repeats);
	}

	@Override
	public void stop() {
		timer.stop();
	}

	@Override
	public void start() {
		timer.start();
	}
}
