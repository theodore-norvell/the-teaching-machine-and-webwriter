/**
 * JavaCodeProductionTag.java
 * 
 * @date: Nov 15, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.tag;

import java.util.ArrayList;
import java.util.List;

import visreed.extension.javaCC.model.payload.JavaCodeProductionPayload;
import visreed.model.VisreedNode;
import visreed.model.VisreedPayload;
import visreed.model.VisreedTag;
import visreed.model.VisreedWholeGraph;

/**
 * @author Xiaoyu Guo
 *
 */
public class JavaCodeProductionTag extends JavaCCTag {
	public JavaCodeProductionTag() {
		super(TagCategory.TERMINAL, Field.GENERAL);
	}

	/* (non-Javadoc)
	 * @see higraph.model.taggedInterfaces.Tag#defaultTagSequence()
	 */
	@Override
	public List<VisreedTag> defaultTagSequence() {
		return new ArrayList<VisreedTag>(0);
	}

	/* (non-Javadoc)
	 * @see higraph.model.taggedInterfaces.Tag#defaultPayload()
	 */
	@Override
	public VisreedPayload defaultPayload() {
		return new JavaCodeProductionPayload();
	}

	/* (non-Javadoc)
	 * @see visreed.model.IDescribable#getDescription()
	 */
	@Override
	public String getDescription() {
		return "JAVA_CODE";
	}

	/* (non-Javadoc)
	 * @see visreed.model.VisreedTag#createSEQ(visreed.model.VisreedWholeGraph)
	 */
	@Override
	protected VisreedNode createSEQ(VisreedWholeGraph wholeGraph) {
		// JavaCodeProduction only appears as child of JavaCCRoot. no need for seq.
		return null;
	}

	private static final VisreedTag instance = new JavaCodeProductionTag();
	protected static VisreedTag getInstance(){
		return instance;
	}
}
