package visreed.extension.regex.model;

import visreed.model.payload.VisreedPayload;

/**
 * Provides formatting methods to RegexNode
 * @author Xiaoyu Guo
 */
public class RegexFormatter {
	/**
	 * Recursively get the regular expression from the given {@link visreed.extension.regex.model.RegexNode}
	 * <p>Note: only the first top node is formatted.
	 * @param top the top node of the regular expression syntax tree
	 * @return 
	 */
	public static String getRegex(RegexNode top){
		StringBuilder result = new StringBuilder();
		
		VisreedPayload payload = top.getPayload();
		if(payload != null){
			return payload.format(top);
		}else{
			// empty Higraph
			result.append("\"\"");
		}
		
		return result.toString();
	}
}
