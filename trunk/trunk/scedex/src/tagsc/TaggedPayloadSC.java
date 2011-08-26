package tagsc;



import higraph.model.taggedInterfaces.TaggedPayload;


public class TaggedPayloadSC implements TaggedPayload<SCTag,TaggedPayloadSC> {
    private SCTag tag;
	private String name;
	
	public TaggedPayloadSC(String name, SCTag tag) {
		this.name = name;
		this.tag = tag;
	}


	public SCTag getTag() {
		   return tag;
	}
    public String getName(){
    	return name;
    }

	@Override
	public TaggedPayloadSC copy() {
	   return  this;
	}



	
	
	
    

}
