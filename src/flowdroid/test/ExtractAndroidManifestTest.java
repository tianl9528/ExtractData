package flowdroid.test;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.xmlpull.v1.XmlPullParserException;

import flowdroid.helper.ExtractAndroidManifest;

public class ExtractAndroidManifestTest {
	@Test
	public void testAll() throws IOException, XmlPullParserException {
		String apkFile = "/Users/tianl9528/Downloads/com.viber.voip.apk";

		ExtractAndroidManifest EAM = new ExtractAndroidManifest(apkFile);
		EAM.run();
		System.out.println(EAM.getApkFile());

		List<String> temp = EAM.getManifestResult().getExtractContents();
		System.out.println(temp);

		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (String string : temp) {
			if (first) {
				first = false;
			} else {
				result.append(",");
			}
			result.append(string);
		}
		
		System.out.println(result.toString());

	}
	
	@Test
	public void test() {
		ExtractAndroidManifest E = new ExtractAndroidManifest();
		E.getManifestResult().setExtractMode("1");
//		E.setMode("1");
		System.out.println(E.getManifestResult().getExtractMode());
	}
}
