/**
 * PLAYPayload.java - play.higraph.model - PLAY
 * 
 * Created on Feb 13, 2012 by Kai Zhu
 */
package play.higraph.model;

import higraph.model.taggedInterfaces.TaggedPayload;
import play.ide.checker.symbolTable.Constness;

/**
 * @author Kai Zhu, Shiwei Han
 * 
 */
public class PLAYPayload implements TaggedPayload<PLAYTag, PLAYPayload> {

	private final String name;

	private final PLAYTag tag;

	/* Field payloadValue is used for different purposes in different tags. */
	private String payloadValue;

	private Constness constness;

	public PLAYPayload(String name, PLAYTag tag) {
		this.name = name;
		this.tag = tag;
		this.payloadValue = "";
		constness = Constness.VAR;
	}

	public PLAYPayload(String name, PLAYTag tag, String payloadValue) {
		this.name = name;
		this.tag = tag;
		this.payloadValue = payloadValue;
		constness = Constness.VAR;
	}

	/**
	 * @see higraph.model.interfaces.Payload#copy()
	 */
	@Override
	public PLAYPayload copy() {
		return this;
	}

	/**
	 * @see higraph.model.taggedInterfaces.TaggedPayload#getTag()
	 */
	@Override
	public PLAYTag getTag() {
		return this.tag;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.name;
	}

	public String getPayloadValue() {
		return payloadValue;
	}

	public void setPayloadValue(String payloadValue) {
		this.payloadValue = payloadValue;
	}


	public Constness getConstness() {
		return constness;
	}

	public void setConstness(Constness constness) {
		this.constness = constness;
	}

}
