package processor;

import java.io.File;

public class Mysql2Oracle implements IProcessor {

	private static final String TAG = Mysql2Oracle.class.getSimpleName();
	
	@Override
	public void doIt(File file) {
		
		/*
		 * 선행
		 * 1. `제거
		 * 2. 주석 제거
		 * 
		 * 3. 타입명 변경:
		 *   int -> NUMBER
		 *   varchar -> VARCHAR2
		 *   datetime -> DATE
		 * 
		 * 1. 파일전체
		 *   AUTO_INCREMENT 캡처 -> SEQUENCE 문으로 변경
		 *   
		 * 2. Create문 
		 *   COMMENT 캡처 -> COMMENT 문으로 변경
		 *   PK 캡처 -> CONSTRAINT 문으로 변경
		 *   FK 캡처 -> INDEX 문으로 변경
		 * 3. Insert문
		 */
		String fileName = file.getName();
		
		/*
		 * sed '//d' filename
		 */
		//Log.d(TAG, "sed '/\\/\\*\\*\\//d'");
		//ShellCommand.execute("sed '/\\/**\\//d'");
		
	}

	@Override
	public void writeFile(File file) {
		
	}

}
