/**
 * CharacterListTag.java
 * 
 * @date: Nov 8, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.regex.model.tag;

import visreed.model.VisreedPayload;
import visreed.model.VisreedTag;
import visreed.model.payload.CharacterListPayload;

/**
 * @author Xiaoyu Guo
 *
 */
public class CharacterListTag extends RegexTag {
	protected CharacterListTag(){
		super(TagCategory.TERMINAL);
	}

	/* (non-Javadoc)
	 * @see higraph.model.taggedInterfaces.Tag#defaultPayload()
	 */
	@Override
	public VisreedPayload defaultPayload() {
		return new CharacterListPayload(RegexTag.CHARACTER_LIST, "");
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
