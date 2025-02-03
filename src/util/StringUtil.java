package util;

public class StringUtil {
	
	public static String trim(String org, char c) {
		int len = org.length();
		int st = 0;
		char[] val = org.toCharArray();
		
		while ((st < len) && (val[st] == c)) {
			st++;
		}
		while ((st < len) && (val[len - 1] == c)) {
			len--;
		}
		
		return ((st > 0) || (len < val.length)) ? org.substring(st, len) : org;
	}
	
	public static String trimBracket(String org) {
		org = trim(org, Char.OPEN_BRACKET);
		org = trim(org, Char.CLOSE_BRACKET);
		return org;
	}
	
	public static String wrap(String org, char c) {
		return c + org + c;
	}
	
	public static String addCRLF(String src) {
		return src += "\r\n";
	}
	
	public static String addSemiColon(String src) {
		return src += ";";
	}
	
	public static void addLine(String src, int n) {
		for (int i = 0; i < n; i++)
			addCRLF(src);
	}
	
	public static String fixNull(Object obj) {
		return fixNull(obj, "");
	}
	
	public static String fixNull(Object obj, String str) {
		if(obj == null)
			return str;
		else if(obj.toString().toLowerCase().equals("null"))
			return str;
		else if(obj.toString().trim().length() == 0)
			return str;
		else
			return obj.toString();
	}
}
