package flowdroid.test;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.Test;
import org.xmlpull.v1.XmlPullParserException;

import flowdroid.helper.ExtractSourceSinkFlow;
import soot.jimple.infoflow.results.InfoflowResults;

public class ExtractSourceSinkFlowTest {
	@Test
	public void testAll() throws IOException, XmlPullParserException {
		String apkFile = "/Users/tianl9528/Downloads/com.viber.voip.apk";
		String androidJar = "/Users/tianl9528/Library/Android/sdk/platforms/android-16/android.jar";
		ExtractSourceSinkFlow ESSF = new ExtractSourceSinkFlow(apkFile, androidJar);
		System.out.println(ESSF.getApkFile());
		
		String sas = Paths.get(System.getProperty("user.dir"),"SourcesAndSinks.txt").toString();
		ESSF.setSourcesAndSinksFile(sas);
		System.out.println(ESSF.getSourcesAndSinksFile());
		ESSF.setShowLogSourcesAndSinks(true);
		InfoflowResults a = ESSF.run();
		System.out.println(ESSF.getFlowResult().getExtractContents());

		

	}
}
