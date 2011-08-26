package sc.model;

import higraph.model.interfaces.Payload;

public class EdgePayloadSC implements Payload<EdgePayloadSC>{
    
	private String name;
    
    public EdgePayloadSC( String name ) {
        this.name = name;
    }
    
    public String getName(){
    	return name;
    }


    public EdgePayloadSC copy() {
        return this;
    }

}