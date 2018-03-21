package flowdroid.helper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import soot.jimple.Stmt;



public class ExtractResults {
	private String appName;
	private String androidJar;
	private String extractMode;
	private double costTime;
	private Set<Stmt> selSources = new HashSet<Stmt>();
	private Set<Stmt> selSinks = new HashSet<Stmt>();
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

	public double getCostTime() {
		return costTime;
	}

	public void setCostTime(Double costTime) {
		this.costTime = costTime;
	}



	public Set<Stmt> getSelSources() {
		return selSources;
	}

	public void setSelSources(Set<Stmt> selSources) {
		this.selSources = selSources;
	}

	public Set<Stmt> getSelSinks() {
		return selSinks;
	}

	public void setSelSinks(Set<Stmt> selSinks) {
		this.selSinks = selSinks;
	}

	public void setCostTime(double costTime) {
		this.costTime = costTime;
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
