package com.remoteTMproject.model.map;

import java.util.HashMap;
import java.util.Map;

import com.remoteTMproject.model.RTM.remoteTM;

/**
 * 
 *  at first of the project, create this map class
 */
public class map {
	
	private remoteTM rtm;
	
	private final static Map<Integer,remoteTM> map = new HashMap<Integer,remoteTM>();
	
	public static void addMap(remoteTM rtm){
		map.put(1, rtm);
	}
	
	public static remoteTM getMap(int i){
	
		return map.get(1);
	}
	
}

//multiton pattern
/**
public class remoteTM implements StatusConsumer{

private remoteTM rtm;

private static final ConcurrentMap<Integer,remoteTM> maps = 
new ConcurrentHashMap<Integer,remoteTM>();

public static remoteTM getInstance(Integer key){

if(maps.get(key)==null){
//lazily create remoteTM and add to maps;
remoteTM instance = new remoteTM();
maps.putIfAbsent(key,instance);

}
return.maps.get(key);

}


}
**/
