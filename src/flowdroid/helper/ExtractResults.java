package flowdroid.helper;

import java.util.ArrayList;
import java.util.List;
import soot.jimple.infoflow.results.ResultSinkInfo;

public class ExtractResults {
	private String appName;
	private String androidJar;
	private String extractMode;
	private List<ResultSinkInfo> sensitiveSinks = new ArrayList<ResultSinkInfo>();
	private List<ResultSinkInfo> userSinks = new ArrayList<ResultSinkInfo>();
	private List<String> extractContents = new ArrayList<String>();

	public ExtractResults(String appName, String androidJar) {
		this.appName = appName;
		this.androidJar = androidJar;
	}

	public ExtractResults(String apkFile) {
		this.appName = apkFile;
	}
	
	public ExtractResults() {
		
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAndroidJar() {
		return androidJar;
	}

	public void setAndroidJar(String androidJar) {
		this.androidJar = androidJar;
	}

	public String getExtractMode() {
		return extractMode;
	}

	public void setExtractMode(String extractMode) {
		this.extractMode = extractMode;
	}

	public String getExtracModeText() {
		String contents = "";
		switch (this.extractMode) {
		case "1":
			contents = "Manifest";
			break;
		case "2":
			contents = "Flow";
		default:
			contents = "All";
			break;
		}
		return contents;
	}

	public List<ResultSinkInfo> getSensitiveSinks() {
		return sensitiveSinks;
	}

	public void setSensitiveSinks(List<ResultSinkInfo> sensitiveSinks) {
		this.sensitiveSinks = sensitiveSinks;
	}

	public List<ResultSinkInfo> getUserSinks() {
		return userSinks;
	}

	public void setUserSinks(List<ResultSinkInfo> userSinks) {
		this.userSinks = userSinks;
	}

	public List<String> getExtractContents() {
		return extractContents;
	}

	public void setExtractContents(List<String> extractContents) {
		this.extractContents = extractContents;
	}

	public void addData(String data) {
		this.extractContents.add(data);
	}

}
