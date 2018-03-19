package flowdroid.test;

import java.io.IOException;

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
		System.out.println(EAM.getManifest().getPermissions());
		System.out.println(EAM.getManifestResult().getExtractContents());
		

	}
}
