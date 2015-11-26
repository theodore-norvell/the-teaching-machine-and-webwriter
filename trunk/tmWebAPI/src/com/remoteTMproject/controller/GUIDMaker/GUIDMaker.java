package com.remoteTMproject.controller.GUIDMaker;

import java.util.Random;



public class GUIDMaker {
    static GUIDMaker instance = new GUIDMaker() ; 
    private GUIDMaker() {}
    public static GUIDMaker getInstance() { return instance ; }

    Random rand = new Random( System.currentTimeMillis() ) ;

    public synchronized String nextGUID() {
        long time = System.currentTimeMillis() ;
        int r = rand.nextInt() ;
        return "RTM" + time + ":" + r ; }
}
/*
public class mapForRTM{

	private remoteTM rtm;
	private static final ConcurrentMap<String,remoteTM> maps = 
														new ConcurrentHashMap<String,remoteTM>();

	public static remoteTM getInstance(String key){
		if(maps.get(key)==null){
			//lazily create remoteTM and add to maps;
			remoteTM instance = new remoteTM();
			maps.putIfAbsent(key,instance);

													}
			return maps.get(key);
				
						}


}*/



//original version is like in below, Dr.Norvell's suggestion

/*class GUIDMaker {
    static GUIDMaker instance = new GUIDMaker() ; 
    private GUIDMaker() {}
    static GUIDMaker getInstance() { return instance ; }

    Random rand = new Random( System.currentTimeMillis() ) ;

    public synchronized String nextGUID() {
        long time = System.currentTimeMillis() ;
        int r = rand.nextInt() ;
        return "RTM" + time + ":" + r ; }
}*/