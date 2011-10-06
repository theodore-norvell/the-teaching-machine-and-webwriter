/**
 * JavaCCParserHelper.java
 * 
 * @date: Oct 3, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.parser;

import org.javacc.parser.JavaCCErrors;

/**
 * @author Xiaoyu Guo
 * 
 */
public class JavaCCParserHelper {
	public static boolean hexchar(char ch) {
		if (ch >= '0' && ch <= '9')
			return true;
		if (ch >= 'A' && ch <= 'F')
			return true;
		if (ch >= 'a' && ch <= 'f')
			return true;
		return false;
	}

	public static int hexval(char ch) {
		if (ch >= '0' && ch <= '9')
			return ((int) ch) - ((int) '0');
		if (ch >= 'A' && ch <= 'F')
			return ((int) ch) - ((int) 'A') + 10;
		return ((int) ch) - ((int) 'a') + 10;
	}

	public static String remove_escapes_and_quotes(Token t, String str) {
		String retval = "";
		int index = 1;
		char ch, ch1;
		int ordinal;
		while (index < str.length() - 1) {
			if (str.charAt(index) != '\\') {
				retval += str.charAt(index);
				index++;
				continue;
			}
			index++;
			ch = str.charAt(index);
			if (ch == 'b') {
				retval += '\b';
				index++;
				continue;
			}
			if (ch == 't') {
				retval += '\t';
				index++;
				continue;
			}
			if (ch == 'n') {
				retval += '\n';
				index++;
				continue;
			}
			if (ch == 'f') {
				retval += '\f';
				index++;
				continue;
			}
			if (ch == 'r') {
				retval += '\r';
				index++;
				continue;
			}
			if (ch == '"') {
				retval += '\"';
				index++;
				continue;
			}
			if (ch == '\'') {
				retval += '\'';
				index++;
				continue;
			}
			if (ch == '\\') {
				retval += '\\';
				index++;
				continue;
			}
			if (ch >= '0' && ch <= '7') {
				ordinal = ((int) ch) - ((int) '0');
				index++;
				ch1 = str.charAt(index);
				if (ch1 >= '0' && ch1 <= '7') {
					ordinal = ordinal * 8 + ((int) ch1) - ((int) '0');
					index++;
					ch1 = str.charAt(index);
					if (ch <= '3' && ch1 >= '0' && ch1 <= '7') {
						ordinal = ordinal * 8 + ((int) ch1) - ((int) '0');
						index++;
					}
				}
				retval += (char) ordinal;
				continue;
			}
			if (ch == 'u') {
				index++;
				ch = str.charAt(index);
				if (hexchar(ch)) {
					ordinal = hexval(ch);
					index++;
					ch = str.charAt(index);
					if (hexchar(ch)) {
						ordinal = ordinal * 16 + hexval(ch);
						index++;
						ch = str.charAt(index);
						if (hexchar(ch)) {
							ordinal = ordinal * 16 + hexval(ch);
							index++;
							ch = str.charAt(index);
							if (hexchar(ch)) {
								ordinal = ordinal * 16 + hexval(ch);
								index++;
								continue;
							}
						}
					}
				}
				JavaCCErrors.parse_error(t, "Encountered non-hex character '"
						+ ch + "' at position " + index + " of string "
						+ "- Unicode escape must have 4 hex digits after it.");
				return retval;
			}
			JavaCCErrors.parse_error(t, "Illegal escape sequence '\\" + ch
					+ "' at position " + index + " of string.");
			return retval;
		}
		return retval;
	}
}
