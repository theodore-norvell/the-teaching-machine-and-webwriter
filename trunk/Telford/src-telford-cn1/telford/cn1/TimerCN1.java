package telford.cn1;

import telford.common.Root;

import com.codename1.ui.Form;
import com.codename1.ui.util.UITimer;

public class TimerCN1 implements telford.common.Timer{		
		UITimer timer;
		private int delay;
		private boolean repeats;
		private Root root;
		
		TimerCN1(int delay,boolean repeats,telford.common.Root root, final telford.common.ActionListener actionListener) {
			this.delay = delay;
			this.repeats = repeats;
			this.root = root;
			
			timer = new UITimer(new Runnable(){
				@Override
				public void run() {
					actionListener.actionPerformed(new ActionEventCN1(null));
				}});
		}

		@Override
		public void stop() {
			timer.cancel();;
		}

		@Override
		public void start() {
			timer.schedule(delay, repeats, (Form) root.getPeer().getRepresentative());
		}
}
