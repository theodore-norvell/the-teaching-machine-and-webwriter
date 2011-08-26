package play.layout;

import higraph.view.layout.SgLayoutManager;
import play.model.EdgePLAY;
import play.model.EdgePayloadPLAY;
import play.model.NodePLAY;
import play.model.NodePayloadPLAY;
import play.model.SubGraphPLAY;
import play.model.WholeGraphPLAY;
import play.tags.PLAYTags;
import play.view.NodeViewPLAY;
import play.view.SubGraphViewPLAY;

/**
 * @author  Charles
 */
public class LayoutManagerFactoryPLAY {

	/**
	 * @uml.property  name="result"
	 * @uml.associationEnd  
	 */
	SgLayoutManager<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY> result;

	public SgLayoutManager<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY> makeLayoutManager(
			PLAYTags tag, SubGraphViewPLAY sgv, NodeViewPLAY nv, double px,
			double py) {
		switch (tag) {
		
		/*case E_PLACE_HOLDER:{
			result = new SingleNodeLayoutManagerPLAY(sgv, nv, px, py);
			break;
		}*/
		default: {
			result = new TagLayoutManager(sgv, nv, px, py);
			break;
		}
		}
		return result;
	}

}
