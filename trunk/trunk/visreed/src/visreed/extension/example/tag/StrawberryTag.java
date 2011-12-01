/**
 * StawberryTag.java
 * 
 * @date: Nov 30, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.example.tag;

import java.util.ArrayList;
import java.util.List;

import visreed.extension.example.model.ExamplePayload;
import visreed.model.VisreedPayload;
import visreed.model.VisreedTag;

/**
 * @author Xiaoyu Guo
 *
 */
public class StrawberryTag extends ExampleTag {

	/**
	 * @param category
	 */
	private StrawberryTag() {
		super(TagCategory.TERMINAL);
	}

	/* (non-Javadoc)
	 * @see higraph.model.taggedInterfaces.Tag#defaultTagSequence()
	 */
	@Override
	public List<VisreedTag> defaultTagSequence() {
        ArrayList<VisreedTag> seq = new ArrayList<VisreedTag>(0);
        return seq;
	}

	/* (non-Javadoc)
	 * @see higraph.model.taggedInterfaces.Tag#defaultPayload()
	 */
	@Override
	public VisreedPayload defaultPayload() {
		return new ExamplePayload(this);
	}

	/* (non-Javadoc)
	 * @see visreed.model.IDescribable#getDescription()
	 */
	@Override
	public String getDescription() {
		return "Strawberry";
	}

    
    private static final StrawberryTag instance = new StrawberryTag();

    protected static StrawberryTag getInstance() {
        return instance;
    };
}
