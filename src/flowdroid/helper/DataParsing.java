package flowdroid.helper;

import java.util.ArrayList;
import java.util.List;

public class DataParsing {
	private String apkName;
	private int manifestNum;
	private int flowNum;
	private List<String> manifestResults = new ArrayList<String>();
	private List<String> flowResults = new ArrayList<String>();
	
	
	public DataParsing() {
		
	}
		
	
	public DataParsing(String apkName) {
		this.apkName = apkName;
	}

	public String getApkName() {
		return apkName;
	}

	public void setApkName(String apkName) {
		this.apkName = apkName;
	}

	public int getManifestNum() {
		return manifestNum;
	}

	public void setManifestNum(int manifestNum) {
		this.manifestNum = manifestNum;
	}

	public int getFlowNum() {
		return flowNum;
	}

	public void setFlowNum(int flowNum) {
		this.flowNum = flowNum;
	}

	public List<String> getManifestResults() {
		return manifestResults;
	}
	
	public void setManifestResults(List<String> manifestResults) {
		this.manifestResults = manifestResults;
		this.manifestNum = manifestResults.size();
	}
	
	public List<String> getFlowResults() {
		return flowResults;
	}
	
	public void setFlowResults(List<String> flowResults) {
		this.flowResults = flowResults;
		this.flowNum = flowResults.size();
	}
}
