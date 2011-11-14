/**
 * CharacterListTag.java
 * 
 * @date: Nov 8, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.model.tag;

import visreed.model.payload.CharacterListPayload;
import visreed.model.payload.VisreedPayload;

/**
 * @author Xiaoyu Guo
 *
 */
public class CharacterListTag extends VisreedTag {

	/* (non-Javadoc)
	 * @see higraph.model.taggedInterfaces.Tag#defaultPayload()
	 */
	@Override
	public VisreedPayload defaultPayload() {
		return new CharacterListPayload("");
	}

	/* (non-Javadoc)
	 * @see visreed.model.IDescribable#getDescription()
	 */
	@Override
	public String getDescription() {
		return "CLIST";
	}

    private static final CharacterListTag instance = new CharacterListTag();
    protected static VisreedTag getInstance() {
        return instance;
    };

}
