package map;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

 public class mapRTM {

     public static void main(String[] args) {
        testHashMapAPIs();
    }
     
     private static void testHashMapAPIs() {
         // 初始化随机种子
         Random r = new Random();
         // 新建HashMap
         HashMap map = new HashMap();
         // 添加操作
         map.put("1", "user1");
         map.put("2", "user2");
         map.put("3", "user3");
 
         // 打印出map
         System.out.println("map:"+map );
 
         // 通过Iterator遍历key-value
         Iterator iter = map.entrySet().iterator();
         while(iter.hasNext()) {
             Map.Entry entry = (Entry) iter.next();
             System.out.println("next : "+ entry.getKey() +" - "+entry.getValue());
         }
        String value= (String) map.get("1");
        System.out.println(value);
         

         
         // HashMap的键值对个数        
         System.out.println("size:"+map.size());
 
         
         
         // containsKey(Object key) :是否包含键key
         System.out.println("contains key two : "+map.containsKey("two"));
         System.out.println("contains key five : "+map.containsKey("five"));
 
         // containsValue(Object value) :是否包含值value
         System.out.println("contains value 0 : "+map.containsValue(new Integer(0)));
 
         // remove(Object key) ： 删除键key对应的键值对
         map.remove("three");
 
         System.out.println("map:"+map );
 
         // clear() ： 清空HashMap
         map.clear();
 
         // isEmpty() : HashMap是否为空
         System.out.println((map.isEmpty()?"map is empty":"map is not empty") );
         
         
         
     }
 }
