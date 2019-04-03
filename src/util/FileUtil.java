package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtil {
	
	public static String getEncodingType(File file) throws IOException {
		
		// 1. 파일 열기
		FileInputStream fis = new FileInputStream(file);
		 
		// 2. 파일 읽기 (4Byte)
		byte[] bom = new byte[4];
		fis.read(bom, 0, 4);
		fis.close();
		
		// 3. 파일 인코딩 확인하기
		if( (bom[0] & 0xFF) == 0xEF && (bom[1] & 0xFF) == 0xBB && (bom[2] & 0xFF) == 0xBF )
			return "UTF-8";
		else if( (bom[0] & 0xFF) == 0xFE && (bom[1] & 0xFF) == 0xFF )
			return "UTF-16BE";
		else if( (bom[0] & 0xFF) == 0xFF && (bom[1] & 0xFF) == 0xFE )
			return "UTF-16LE";
		else if( (bom[0] & 0xFF) == 0x00 && (bom[1] & 0xFF) == 0x00 &&
		(bom[0] & 0xFF) == 0xFE && (bom[1] & 0xFF) == 0xFF )
			return "UTF-32BE";
		else if( (bom[0] & 0xFF) == 0xFF && (bom[1] & 0xFF) == 0xFE &&
				(bom[0] & 0xFF) == 0x00 && (bom[1] & 0xFF) == 0x00 )
			return "UTF-32LE";
		else
			return "EUC-KR";
	}
}
