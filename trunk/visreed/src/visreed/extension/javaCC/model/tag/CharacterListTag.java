/**
 * CharacterListTag.java
 * 
 * @date: Nov 15, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import visreed.model.VisreedPayload;
import visreed.model.payload.CharacterListPayload;

/**
 * @author Xiaoyu Guo
 *
 */
public class CharacterListTag extends LexicalTerminalTag {

	protected CharacterListTag(){
		super();
	}

	/* (non-Javadoc)
	 * @see higraph.model.taggedInterfaces.Tag#defaultPayload()
	 */
	@Override
	public VisreedPayload defaultPayload() {
		return new CharacterListPayload(JavaCCTag.CHARACTER_LIST, "");
	}

	/* (non-Javadoc)
	 * @see visreed.model.IDescribable#getDescription()
	 */
	@Override
	public String getDescription() {
		return "CLIST";
	}

    private static final CharacterListTag instance = new CharacterListTag();
    protected static JavaCCTag getInstance() {
        return instance;
    }

}
