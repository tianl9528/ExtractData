package flowdroid.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import flowdroid.helper.DataParsing;

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
	
	@Test
	public void test() {
		DataParsing temp = new DataParsing();
		temp.setApkName("testApkName");
		temp.setManifestResults(Arrays.asList("a","b","c","d"));
		temp.setFlowResults(Arrays.asList("1","2","3","4"));
		
		
		List<String> data = Arrays.asList(temp.apkNameToString(),temp.manifestToString(),temp.flowToString());
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (String string : data) {
			if (first) {
				first = false;
			} else {
				result.append(",");
			}
			result.append(string);
		}
		
		System.out.println(result.toString());
		System.out.println(result.toString());
		
		
		}

	}

