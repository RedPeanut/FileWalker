package util;

public class Char {
	
	public static char OPEN_BRACKET = '(';
	public static char CLOSE_BRACKET = ')';
	
	public static char DOUBLE_QUOTE = '"';
	public static char SEMI_COLON = ';';
	public static String CRLF = "\r\n";
	
	public static boolean isAlpha(int ch) {
		if (('a' <= ch && ch <= 'z')
				|| ('A' <= ch && ch <= 'Z')) {
			return true;
		}
		return false;
	}
	
	public static boolean isSymbol(int ch) {
		if (('!' <= ch && ch <= '/')
				|| (':' <= ch && ch <= '@')
				|| ('[' <= ch && ch <= '`')
				|| ('{' <= ch && ch <= '~')) {
			return true;
		}
		return false;
	}
}
