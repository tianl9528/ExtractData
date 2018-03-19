package flowdroid.test;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;

public class Args0Test {
	@Test
	public void testArgs0() {
		Scanner sc = new Scanner(System.in);
		System.out.println("输入apks路径：");
		String m = sc.next();
		System.out.println("路径是" + m+"\n--------");
		
		String args0 = m;

		List<String> apkFiles = new ArrayList<String>();
		File apkFile = new File(args0);
		if (apkFile.isDirectory()) {
			String[] dirFiles = apkFile.list(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					return (name.endsWith(".apk"));
				}

			});
			for (String s : dirFiles)
				apkFiles.add(s);
		} else {
			// apk is a file so grab the extension
			String extension = apkFile.getName().substring(apkFile.getName().lastIndexOf("."));
			if (extension.equalsIgnoreCase(".apk"))
				apkFiles.add(args0);
			else {
				System.err.println("Invalid input file format: " + extension);
				return;
			}
		}

		System.out.println(apkFiles);
	}

}
