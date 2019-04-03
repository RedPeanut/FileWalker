package processor;

import java.io.File;

public interface IProcessor {
	void doIt(File file) throws Exception;
	void writeFile(File file) throws Exception;
}
