package main;

import java.io.File;
import java.util.Arrays;
import processor.IProcessor;

public class FileWalker {
	
	private static final String TAG = FileWalker.class.getSimpleName();
	
	public static void main(String[] args) {
		
		Options parameters = processArgs(args);
		File inputDir = new File(parameters.dir());
		
		try {
			new FileWalker().walk(inputDir);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static Options processArgs(String[] args) {
		Options parameters = OptionsParser.parse(Arrays.asList(args));
		return parameters;
	}
	
	public void walk(File inputDir) throws Exception {
	
		File[] list = inputDir.listFiles();
		
		//IProcessor p = new Mysql2Oracle();
		//IProcessor p = new Oracle2Mysql();
		
		IProcessor p = new IProcessor() {
			@Override public void doIt(File file) throws Exception {}
			@Override public void writeFile(File file) throws Exception {}
		};
		
		if (list != null) {
			for (File f: list) {
				if (f.isDirectory()) {
					
				} else {
					String name = f.getName();
					int dot = name.lastIndexOf('.');
					String extension = (dot == -1) ? "" : name.substring(dot + 1);
					
					p.doIt(f);
					p.writeFile(f);
				}
			}
		}
	}
	
}