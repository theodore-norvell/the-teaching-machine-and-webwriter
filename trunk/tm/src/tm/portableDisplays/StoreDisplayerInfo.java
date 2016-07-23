package tm.portableDisplays;

import java.util.ArrayList;
import java.util.List;

//import tm.displayEngine.StoreLayoutManager1;
import tm.interfaces.RegionInterface;

public class StoreDisplayerInfo {
	private List<DatumDisplay> listDD = new ArrayList<DatumDisplay>();
	private RegionInterface region;	// Reference to client
	public RegionInterface getRegion() {
		return region;
	}

//	public StoreLayoutManager1 getLayoutManager() {
//		return layoutManager;
//	}

	public void setListDD(List<DatumDisplay> listDD) {
		this.listDD = listDD;
	}

//	private StoreLayoutManager1 layoutManager;
//	public void setLayoutManager(StoreLayoutManager1 lm){
//		this.layoutManager = lm;
//	}
	
	public void setRegion(RegionInterface region){
		this.region = region;
	}
	
	public List<DatumDisplay> getListDD(){
		return this.listDD;
	}
	
}
