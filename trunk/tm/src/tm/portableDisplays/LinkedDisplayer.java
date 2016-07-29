//package tm.portableDisplays;
//
//import telford.common.Graphics;
//import tm.displayEngine.LinkedD3Iterator1;
//import tm.interfaces.StateInterface;
//
//public class LinkedDisplayer extends PortableDisplayer {
//	private LinkedDisplayerInfo displayInfo = new LinkedDisplayerInfo();
//	
//	public LinkedDisplayer(StateInterface model, PortableContextInterface context) {
//		super(model, context);
//	}
//
//	public LinkedDisplayerInfo getDisplayInfo() {
//		return displayInfo;
//	}
//
//	public void setDisplayInfo(LinkedDisplayerInfo displayInfo) {
//		this.displayInfo = displayInfo;
//	}
//
//	@Override
//	public void refresh() {
//		
//	}
//
//	@Override
//	public void paintComponent(Graphics g) {
//		drawArea(g);
//	}
//
//	public void drawArea(Graphics screen) {  
//		    if (screen == null || this.displayInfo.getMyGenerator() == null) return;
//		    int step;
//		    if(displayInfo.isAnimate())
//		        step = LinkedDatumDisplay.LAST_STEP;           
//		    else step = LinkedDatumDisplay.LAST_STEP;
//	   
//		    
//	        LinkedD3Iterator1 iterator  = displayInfo.getIterator();
//	        iterator.reset();
//			// Draw all the links
//	        while (!iterator.atEnd()) {   
//	            iterator.getNode().drawLinks(screen/*, scroll*/);
//	            iterator.increment();
//			}
//			iterator.reset();
//			
//	        while (!iterator.atEnd()) {   
//	            LinkedDatumDisplay dd = iterator.getNode();
////	            int h = dd.getSize().height;
//	            dd.setStep(step);
//	            dd.draw(screen);
//	            iterator.increment();
//			}
//			
//	        displayInfo.setAnimate(false);
//		}	
//}
