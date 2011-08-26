package tagsc;

import java.util.ArrayList;
import java.util.List;

import higraph.model.taggedInterfaces.Tag;


public enum SCTag implements Tag <SCTag,TaggedPayloadSC> {PLAINSTATE, ORTHOGONALSTATE,REGION,
	                              FORKJOIN, HISTORY,INITIAL,TERMINATE,JUNCTION,CHOICE;
   
	public boolean contentModel(List<SCTag> seq) {
		switch(this){
		case PLAINSTATE :
		     for (int i=0;i<seq.size();i++){
		    	 return    seq.get(i) == PLAINSTATE
		    	         ||seq.get(i)==REGION
		    			 ||seq.get(i) == ORTHOGONALSTATE
		    			 ||seq.get(i) == FORKJOIN
		    			 ||seq.get(i) == HISTORY
		    			 ||seq.get(i) == INITIAL
		    			 ||seq.get(i) == TERMINATE
		    			 ||seq.get(i) == JUNCTION
		    			 ||seq.get(i) == CHOICE;
		     }
		     
		case ORTHOGONALSTATE :
			if(seq.size()!=2)
				return false;
			for (int i=0;i<seq.size();i++)
			    return seq.get(i)==REGION;
		case REGION:
			for (int i=0;i<seq.size();i++){
		    	 return seq.get(i) == PLAINSTATE
		    	         ||seq.get(i)==REGION
		    			 ||seq.get(i) == ORTHOGONALSTATE
		    			 ||seq.get(i) == FORKJOIN
		    			 ||seq.get(i) == HISTORY
		    			 ||seq.get(i) == INITIAL
		    			 ||seq.get(i) == TERMINATE
		    			 ||seq.get(i) == JUNCTION
		    			 ||seq.get(i) == CHOICE;
		    	 }
		case FORKJOIN :  return seq.size()==0 ;
		case HISTORY :   return seq.size()==0 ;
		case INITIAL :   return seq.size()==0 ;
		case TERMINATE : return seq.size()==0 ;
		case JUNCTION :  return seq.size()==0 ;
		case CHOICE :    return seq.size()==0 ;
		
		}
		return false; // ???????????????
		
	}
	

	@Override
	public TaggedPayloadSC defaultPayload() {
		TaggedPayloadSC temp = null;
		switch(this){
		case PLAINSTATE :
		     return temp = new TaggedPayloadSC("plain state", PLAINSTATE);
		     
		case REGION :
			return temp = new TaggedPayloadSC("region", REGION);
			
		case ORTHOGONALSTATE :
			return temp = new TaggedPayloadSC("othogonal state", ORTHOGONALSTATE);
		
		case FORKJOIN :
			return temp = new TaggedPayloadSC("forkjoin", FORKJOIN);
			 
		case HISTORY :
		     return temp = new TaggedPayloadSC("history", HISTORY);
			 
		case INITIAL :
			return temp = new TaggedPayloadSC("initial", INITIAL);
			 
		case TERMINATE :
			return temp = new TaggedPayloadSC("terminate", TERMINATE);
			 
		case JUNCTION :
			return temp = new TaggedPayloadSC("junction", JUNCTION);
			 
		case CHOICE :
			return temp = new TaggedPayloadSC("choice", CHOICE);
			 
		}
		return temp;
		 
	}

	@Override
	public List<SCTag> defaultTagSequence() {
		List<SCTag> temp = null ; 
		
		switch(this){
		case PLAINSTATE :
		     temp = new ArrayList<SCTag>();
		     break;
		case REGION :
			temp = new ArrayList<SCTag>();
			break;
		case ORTHOGONALSTATE :
			temp = new ArrayList<SCTag>();
			temp.add(SCTag.REGION);
			temp.add(SCTag.REGION);
			break;
		case FORKJOIN :
			 temp = new ArrayList<SCTag>();
			 break;
		case HISTORY :
			 temp = new ArrayList<SCTag>();
			 break;
		case INITIAL :
			 temp = new ArrayList<SCTag>();
			 break;
		case TERMINATE :
			 temp = new ArrayList<SCTag>();
			 break;
		case JUNCTION :
			 temp = new ArrayList<SCTag>();
			 break;
		case CHOICE :
			 temp = new ArrayList<SCTag>();
			 break;
		}
		return temp;
}
}
	


