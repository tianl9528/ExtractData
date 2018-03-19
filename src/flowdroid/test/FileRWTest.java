package flowdroid.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

public class FileRWTest {
	
	private String path = "Processed/_Run_.txt";
	
	@Test
	public void test2() throws IOException {
		
		File file = new File(path);  
	    File dir = file.getParentFile();  
	    if (!dir.exists()) {  
	        dir.mkdirs();  
	    }  
	    file.createNewFile();
	    
		FileWriter fw = null;
		BufferedWriter bw = null;
		fw = new FileWriter(file, true);
		bw = new BufferedWriter(fw);
		
		
		bw.write("a");
		bw.close();
	}
}
