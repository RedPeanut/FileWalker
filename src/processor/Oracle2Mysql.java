package processor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.tree.DefaultMutableTreeNode;

import util.Char;
import util.FileUtil;
import util.Log;
import util.StringUtil;
import util.Util;

@SuppressWarnings("unchecked")
public class Oracle2Mysql implements IProcessor {

	private static final String TAG = Oracle2Mysql.class.getSimpleName();
	
	//private static String PATTERN_CREATE = "(?m)CREATE TABLE .*?;";
	//private static String PATTERN_INSERT = "(?m)Insert into (.*?)(\\(.*?\\)) values (\\(.*?\\));";
	//private static String PATTERN_COMMENT = "(?m)COMMENT ON (COLUMN .*?) IS (.*?);";
	
	/** Create */
	
	
	/**
	 * {
	 * 	"테이블명": {
	 * 		"CREATE": {
	 * 			["COLUMN":"","TYPE":"","LENGTH":"","COMMENT":""],
	 * 			...
	 * 		},
	 * 		"INSERT COLUMN": "",
	 * 		"INSERT VALUES": [
	 * 			...
	 * 		]
	 * 	},
	 * 	...
	 * }
	 */
	private Map<String, Object> hashMap = new HashMap<String, Object>();
	private Map<String, Object> treeMap = new TreeMap<String, Object>(new Comparator<String>() {
		@Override
		public int compare(String lhs, String rhs) {
			return lhs.compareTo(rhs);
		}
	});
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	
	@Override
	public void doIt(File file) throws IOException {
		
		/*
		 * 1. 주석제거
		 * 2. 타입명 변경
		 *   NUMBER > INT
		 *   VARCHAR2 > VARCHAR
		 *   DATE > DATETIME
		 *   
		 * 3. create:
		 *   
		 * 4. insert:
		 *   
		 * DDL
		 * DML
		 * 
		 */

		InputStream is = new FileInputStream(file);
		int length = (int) file.length();
		byte[] buff = new byte[length];
		is.read(buff, 0, length);
		is.close();
		
		// TODO: 인코딩 타입
		
		String s = new String(buff, FileUtil.getEncodingType(file));
		
		scanCreateInfo(s);
		scanCommentInfo(s);
		scanIndexInfo(s);
		scanConstraintInfo(s);
		scanNotNullField(s);
		
		Util.printMap("", treeMap, "");
	}

	@Override
	public void writeFile(File file) throws IOException {
		/*
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
		}
		*/
		
		/*
		Collections.sort(list, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> lhs, Map<String, Object> rhs) {
				
				return 0;
			}
		});
		*/
		
		String output = new String();
		
		Set<String> keys = treeMap.keySet();
		for (Iterator<String> itor = keys.iterator(); itor.hasNext();) {
			
			String key = itor.next().toString();
			String tableName = key;
			StringUtil.wrap(tableName, '`');
			output += "--" + Char.CRLF
					+ "-- Table structure for table" + tableName + Char.CRLF
					+ "--" + Char.CRLF;
			output += "DROP TABLE IF EXISTS " + tableName + ";" + Char.CRLF;
			output += Char.CRLF;
			output += "CREATE TABLE " + tableName + "(" + Char.CRLF;
			
			Map<String, Object> tableMap = (Map<String, Object>) treeMap.get(tableName);
			//String body = (String) tableMap.get("BODY");
			//output += body + Char.CRLF;
			
			output += ")";
			output += Char.SEMI_COLON + Char.CRLF;
			output += Char.CRLF;
			
			
			
		}
		
		OutputStream os = new FileOutputStream(file);
		os.write(output.getBytes());
		os.flush();
		os.close();
	}

	private void scanCreateInfo(String s) {
		
		Pattern pattern = Pattern.compile("(?s)CREATE TABLE .*?;");
		Matcher matcher = pattern.matcher(s);
		
		while (matcher.find()) {
			
			//Util.printMatcher(matcher);
			
			String createStatement = matcher.group(0);
			CharSequence text = createStatement;
			//int length = text.length();
			
			int pos = 0; // root position
			DefaultMutableTreeNode top = new DefaultMutableTreeNode(new UserObject(pos, -1));
			makeBracketPositionTree(top, text, pos);
			
			UserObject uo = (UserObject) ((DefaultMutableTreeNode) top.getChildAt(0)).getUserObject();
			int startPos = uo.getStartPos();
			int endPos = uo.getEndPos();
			
			String head = createStatement.substring(0, startPos - 1);
			String body = createStatement.substring(startPos, endPos + 1);
			String tail = createStatement.substring(endPos + 1, createStatement.length());

			//Log.d(TAG, "head = " + head);
			Log.d(TAG, "body = " + body);
			//Log.d(TAG, "tail = " + tail);
			
			Pattern subPattern = Pattern.compile("CREATE TABLE \"(\\w*)\"\\.\"(\\w*)\"");
			Matcher subMatcher = subPattern.matcher(head);
			while (subMatcher.find()) {
				
				//Util.printMatcher(subMatcher);
				
				String sid = subMatcher.group(1);
				String tableName = subMatcher.group(2);
				
				//Log.d(TAG, "sid = " + sid);
				//Log.d(TAG, "tableName = " + tableName);
				if (treeMap.get(tableName) == null) {
					Map<String, Object> tableMap = new HashMap<String, Object>();
					//value.put("COMMENT", new HashMap<String, String>());
					//value.put("INSERT COLUMNS", "");
					//value.put("INSERT VALUES", new ArrayList<String>());
					treeMap.put(tableName, tableMap);
					
					scanBodyInfo(tableName, body);
					
					/*
					body = replaceBodyInfo(body);
					body = StringUtil.trimBracket(body).trim();
					tableMap.put("BODY", body);
					*/
				}
			}
			
			
		}
	}
	
	private void scanBodyInfo(String tableName, String body) {
		
		Map<String, Object> tableMap = (Map<String, Object>) treeMap.get(tableName);
		if (tableMap != null) {
			
			List<Map<String, String>> list = (List<Map<String, String>>) tableMap.get("CREATE");
			if (list == null) {
				list = new ArrayList<Map<String, String>>();
				tableMap.put("CREATE", list);
			}
			
			Pattern pattern = Pattern.compile("(\"\\w+\") (VARCHAR2|DATE|NUMBER)"
					+ "("
						+ "\\((\\d+) (CHAR|BYTE)\\)" // VARCHAR2
						+ "|\\((\\d+),(\\d+)\\)" // NUMBER
					+ ")*" // include DATE
					);
			Matcher matcher = pattern.matcher(body);
			while (matcher.find()) {
				//Util.printMatcher(matcher);
				String columnName = matcher.group(1);
				columnName = StringUtil.trim(columnName, Char.DOUBLE_QUOTE);
				String type = matcher.group(2);
				String length = "";
				if ("DATE".equals(type))
					;
				else if ("NUMBER".equals(type))
					length = matcher.group(6);
				else // VARCHAR2
					length = matcher.group(4);
				
				Map<String, String> e = new HashMap<String, String>();
				e.put("COLUMN", columnName);
				e.put("TYPE", type);
				e.put("LENGTH", length);
				list.add(e);
			}
		}
		
	}
	
	private String replaceBodyInfo(String body) {
		
		body = body.replaceAll("(\"\\w+\") VARCHAR2", "$1 varchar");
		body = body.replaceAll("(\"\\w+\") DATE", "$1 datetime");
		body = body.replaceAll("(\"\\w+\") NUMBER", "$1 int");
		
		body = body.replace("\"", "`");
		
		body = body.replaceAll("\\((\\d+) (CHAR|BYTE)\\)", "\\($1\\)");
		body = body.replaceAll("\\((\\d+),(\\d+)\\)", "\\($1\\)");
		
		//Log.d(TAG, "[after] body = " + body);
		
		return body;
	}
	
	private void scanCommentInfo(String s) {
		Pattern pattern = Pattern.compile("(?s)COMMENT ON (COLUMN .*?) IS (.*?);");
		Matcher matcher = pattern.matcher(s);
		while (matcher.find()) {
			
			Util.printMatcher(matcher);
			
			String columnField = matcher.group(1);
			String commentField = matcher.group(2);
			String tableName = null;
			String columnName = null;
			
			Pattern subPattern = Pattern.compile("COLUMN \"(\\w*)\"\\.\"(\\w*)\"\\.\"(\\w*)\"");
			Matcher subMatcher = subPattern.matcher(columnField);
			if (subMatcher.find()) {
				//Util.printMatcher(subMatcher);
				
				//String sid = matcher.group(1);
				tableName = subMatcher.group(2);
				columnName = subMatcher.group(3);
				
				Map<String, Object> tableMap = (Map<String, Object>) treeMap.get(tableName);
				if (tableMap != null) {
					List<Map<String, String>> createList = (List<Map<String, String>>) tableMap.get("CREATE");
					if (createList != null) {
						for (int i = 0; i < createList.size(); i++) {
							Map<String, String> e = createList.get(i);
							if (e.get("COLUMN").equals(columnName)) {
								e.put("COMMENT", commentField);
								break;
							}
						}
					}
				}
			}
		}
	}
	
	private void scanInsertInfo(String s) {
		Pattern pattern = Pattern.compile("(?s)Insert into (.*?) \\((.*?)\\) values \\((.*?)\\);");
		Matcher matcher = pattern.matcher(s);
		
		String tableField;
		String columnField;
		String valueField;
		
		while (matcher.find()) {
			//Util.printMatcher(matcher);
			
			tableField = matcher.group(1);
			columnField = matcher.group(2);
			valueField = matcher.group(3);
			
			//Log.d(TAG, "tableField = " + tableField);
			//Log.d(TAG, "columnField = " + columnField);
			//Log.d(TAG, "valueField = " + valueField);
			
			String tableName = tableField.split("\\.")[1];
			
			Map<String, Object> subMap = (Map<String, Object>) treeMap.get(tableName);
			Object insertColumns = subMap.get("INSERT COLUMNS");
			if (insertColumns != null)
				subMap.put("INSERT COLUMNS", columnField);
			
			ArrayList<String> insertValues = (ArrayList<String>) subMap.get("INSERT VALUES");
			if (insertValues != null)
				insertValues.add(valueField);
		}
	}
	
	private void scanIndexInfo(String s) {
		Pattern pattern = Pattern.compile("(?s)CREATE UNIQUE INDEX (.*?) ON (.*?) \\((.*?)\\) (.*?);");
		Matcher matcher = pattern.matcher(s);
		
		while (matcher.find()) {
			//Util.printMatcher(matcher);
			String nameField = matcher.group(1);
			String indexName = nameField.split("\\.")[1];
			indexName = StringUtil.trim(indexName, Char.DOUBLE_QUOTE);
			String tableField = matcher.group(2);
			String tableName = tableField.split("\\.")[1];
			tableName = StringUtil.trim(tableName, Char.DOUBLE_QUOTE);
			String columnName = matcher.group(3);
			columnName = StringUtil.trim(columnName, Char.DOUBLE_QUOTE);
			
			Map<String, Object> subMap = (Map<String, Object>) treeMap.get(tableName);
			if (subMap != null) {
				List<Map<String, String>> indexList = (List<Map<String, String>>) subMap.get("INDEX");
				if (indexList == null) {
					indexList = new ArrayList<Map<String, String>>();
					subMap.put("INDEX", indexList);
				}
				Map<String, String> e = new HashMap<String, String>();
				e.put("NAME", indexName);
				e.put("COLUMN", columnName);
				indexList.add(e);
			}
		}
	}
	
	private void scanConstraintInfo(String s) {
		Pattern pattern = Pattern.compile("(?s)ALTER TABLE ([\\w\"\\.]*?) ADD CONSTRAINT (.*?) (?:PRIMARY|FOREIGN) KEY \\((.*?)\\)\\s(.*?);");
		Matcher matcher = pattern.matcher(s);
		while (matcher.find()) {
			//Util.printMatcher(matcher);
			String tableField = matcher.group(1);
			String tableName = tableField.split("\\.")[1];
			tableName = StringUtil.trim(tableName, Char.DOUBLE_QUOTE);
			String constraintName = matcher.group(2);
			constraintName = StringUtil.trim(constraintName, Char.DOUBLE_QUOTE);
			String columnName = matcher.group(3);
			columnName = StringUtil.trim(columnName, Char.DOUBLE_QUOTE);
			
			Map<String, Object> subMap = (Map<String, Object>) treeMap.get(tableName);
			if (subMap != null) {
				List<Map<String, String>> list = (List<Map<String, String>>) subMap.get("CONSTRAINT");
				if (list == null) {
					list = new ArrayList<Map<String, String>>();
					subMap.put("CONSTRAINT", list);
				}
				Map<String, String> e = new HashMap<String, String>();
				e.put("NAME", constraintName);
				e.put("COLUMN", columnName);
				list.add(e);
			}
		}
	}
	
	private void scanNotNullField(String s) {
		Pattern pattern = Pattern.compile("ALTER TABLE (.*?) MODIFY \\((.*?) NOT NULL ENABLE\\);");
		Matcher matcher = pattern.matcher(s);
		
		String tableField;
		while (matcher.find()) {
			//Util.printMatcher(matcher);
			tableField = matcher.group(1);
			String tableName = tableField.split("\\.")[1];
			tableName = StringUtil.trim(tableName, Char.DOUBLE_QUOTE);
			String columnName = matcher.group(2);
			columnName = StringUtil.trim(columnName, Char.DOUBLE_QUOTE);
			
			Map<String, Object> subMap = (Map<String, Object>) treeMap.get(tableName);
			if (subMap != null) {
				ArrayList<String> list = (ArrayList<String>) subMap.get("NN");
				if (list == null) {
					list = new ArrayList<String>();
					subMap.put("NN", list);
				}
				list.add(columnName);
			}
		}
	}
	
	//private int count = 0;
	private int makeBracketPositionTree(DefaultMutableTreeNode curr, CharSequence text, int start) {
		for (int pos = start; pos < text.length(); pos++) {
			char c = text.charAt(pos);
			if (c == Char.OPEN_BRACKET || c == Char.CLOSE_BRACKET) {
				if (c == Char.OPEN_BRACKET) {
					//Log.d(TAG, "this.count = " + this.count++);
					DefaultMutableTreeNode child = new DefaultMutableTreeNode(new UserObject(pos, -1));
					curr.add(child); // make a node
					pos = makeBracketPositionTree(child, text, pos + 1); // recur
				} else if (c == Char.CLOSE_BRACKET) {
					UserObject userObject = (UserObject) curr.getUserObject();
					userObject.setEndPos(pos);
					//curr.setUserObject(userObject);
					return pos; // close a node
				}
			}
		}
		return -1;
	}
	
	/**
	 * @desc 괄호 위치 저장용
	 */
	class UserObject {
		public int startPos;
		public int endPos;
		
		public UserObject(int startPos, int endPos) {
			this.startPos = startPos;
			this.endPos = endPos;
		}

		public int getStartPos() {
			return startPos;
		}

		public void setStartPos(int startPos) {
			this.startPos = startPos;
		}

		public int getEndPos() {
			return endPos;
		}

		public void setEndPos(int endPos) {
			this.endPos = endPos;
		}

		@Override
		public String toString() {
			return "UserObject [startPos=" + startPos + ", endPos=" + endPos + "]";
		}
	}
}
