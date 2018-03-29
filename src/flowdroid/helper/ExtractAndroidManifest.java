package flowdroid.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import org.xmlpull.v1.XmlPullParserException;

import soot.jimple.infoflow.android.manifest.ProcessManifest;

public class ExtractAndroidManifest {
	private String apkFile;
	private ProcessManifest manifest = null;
	private ExtractResults manifestResult = new ExtractResults();

	public void run() throws IOException, XmlPullParserException {
		this.manifest = new ProcessManifest(this.apkFile);
		Set<String> result = this.manifest.getPermissions();
		this.manifestResult.setExtractContents(new ArrayList<>(result));
	}

	public ExtractAndroidManifest(String apkFile, String mode) {
		this.apkFile = apkFile;
		this.manifestResult.setExtractMode(mode);
	}

	public ExtractAndroidManifest(String apkFile) {
		this.apkFile = apkFile;
	}


	public ExtractAndroidManifest() {

	}

	public String getApkFile() {
		return apkFile;
	}

	public void setApkFile(String apkFile) {
		this.apkFile = apkFile;
	}

	public ProcessManifest getManifest() {
		return manifest;
	}

	public void setManifest(ProcessManifest manifest) {
		this.manifest = manifest;
	}

	public ExtractResults getManifestResult() {
		return manifestResult;
	}

	public void setManifestResult(ExtractResults manifestResult) {
		this.manifestResult = manifestResult;
	}

}
