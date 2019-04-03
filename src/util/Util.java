package util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

public class Util {
	
	private static String TAG = Util.class.getSimpleName();
	public static String INDENT = "    ";
	
	public static <K, V> void printMap(String key, Map<K, V> map, String indent) {
		if (map != null) {
			
			Log.d(TAG, indent + (key.isEmpty() ? "" : key + " = ") + "{");
			
			Set<K> keys = map.keySet();
			for (Iterator<K> itor = keys.iterator(); itor.hasNext();) {
				String _key = itor.next().toString();
				V value = map.get(_key);
				if (value instanceof Map) {
					printMap(_key, (Map) value, indent + INDENT);
				} else if (value instanceof List) {
					printList(_key, (List) value, indent + INDENT);
				} else {
					boolean addComma = itor.hasNext();
					Log.d(TAG, indent + INDENT + _key + " = " + value + (addComma ? "," : ""));
				}
			}
			
			Log.d(TAG, indent + "}");
		}
	}
	
	public static <T> void printList(String key, List<T> list, String indent) {
		if (list != null) {
			
			Log.d(TAG, indent + (key.isEmpty() ? "" : key + " = ") + "[");
			
			for (int i = 0; i < list.size(); i++) {
				T value = list.get(i);
				if (value instanceof Map)
					printMap("", (Map) value, indent + INDENT);
				else {
					boolean addComma = (i != list.size() - 1);
					Log.d(TAG, String.format(indent + INDENT + "[%s] %s" + (addComma ? "," : ""), i, list.get(i)));
				}
			}
			
			Log.d(TAG, indent + "]");
		}
	}
	
	public static void printMatcher(Matcher matcher) {
		int count = matcher.groupCount();
		for (int i = 0; i <= count; i++) {
			Log.d(TAG, String.format("matcher.group[%d] = %s", i, matcher.group(i)));
		}
	}
	
	public static void print(String msg) {
		System.out.print(msg);
	}
}
