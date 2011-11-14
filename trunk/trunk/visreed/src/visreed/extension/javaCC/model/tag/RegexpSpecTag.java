/**
 * RegexpSpecTag.java
 * 
 * @date: Oct 21, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import java.util.ArrayList;
import java.util.List;

import visreed.extension.javaCC.model.payload.RegexpSpecPayload;
import visreed.model.payload.VisreedPayload;
import visreed.model.tag.VisreedTag;

/**
 * @author Xiaoyu Guo
 */
public class RegexpSpecTag extends JavaCCTag {
	
	private RegexpSpecTag(){
		super();
	}
	
	private static RegexpSpecTag instance = new RegexpSpecTag();
	protected static RegexpSpecTag getInstance(){
		return instance;
	}

	/* (non-Javadoc)
	 * @see higraph.model.taggedInterfaces.Tag#contentModel(java.util.List)
	 */
	@Override
	public boolean contentModel(List<VisreedTag> seq) {
		return (seq != null && seq.size() == 1 && seq.get(0).is(SEQUENCE));
	}

	/* (non-Javadoc)
	 * @see higraph.model.taggedInterfaces.Tag#defaultTagSequence()
	 */
	@Override
	public List<VisreedTag> defaultTagSequence() {
		List<VisreedTag> seq = new ArrayList<VisreedTag>();
		seq.add(JavaCCTag.SEQUENCE);
		return seq;
	}

	/* (non-Javadoc)
	 * @see higraph.model.taggedInterfaces.Tag#defaultPayload()
	 */
	@Override
	public VisreedPayload defaultPayload() {
		return new RegexpSpecPayload();
	}

	/* (non-Javadoc)
	 * @see visreed.model.IDescribable#getDescription()
	 */
	@Override
	public String getDescription() {
		return "RegSpec";
	}

}
