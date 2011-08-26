package sc.model;

import higraph.model.interfaces.Payload;



public class NodePayloadSC implements Payload<NodePayloadSC> {



   private String name;
    
    public NodePayloadSC( String name ) {
        this.name = name;
    }
    
    public String getName(){
    	return name;
    }


    public NodePayloadSC copy() {
        return this;
    }

}
