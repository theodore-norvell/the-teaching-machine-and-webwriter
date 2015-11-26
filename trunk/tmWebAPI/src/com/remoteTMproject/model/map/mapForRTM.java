package com.remoteTMproject.model.map;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.remoteTMproject.model.RTM.remoteTM;
/**
 * 
 *
 *  the mapForRTM class is a multiton pattern, in which the maps class is 
 *  shared by all threads(only has one instance of maps). could has multiple 
 *  remoteTM and hold by the maps 
 *   
 */
public class mapForRTM{

		private remoteTM rtm;
		private static final ConcurrentMap<String,remoteTM> maps = 
															new ConcurrentHashMap<String,remoteTM>();

/*		public static remoteTM getInstance(String key){
			if(maps.get(key)==null){
				//lazily create remoteTM and add to maps;
				remoteTM instance = new remoteTM();
				maps.putIfAbsent(key,instance);

														}
				return maps.get(key);
					
							}*/
		
		//add a new RTM if not exsits 
															
															
		public static  void addInstance(String key){
			if(maps.get(key)==null){
				//lazily create remoteTM and add to maps;
				remoteTM instance = new remoteTM();
				maps.putIfAbsent(key,instance);
									}
			}
		
		//get the existing RTM
		public static  remoteTM getInstance(String key){
			return maps.get(key);
			}
		
		
		
		

}




/*public class Singleton {
private final static Singleton instance = new Singleton();

private Singleton() {}

public static Singleton getInstance() {
 return instance;
}
}
*/