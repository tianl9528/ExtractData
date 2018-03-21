package flowdroid.helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import soot.jimple.Stmt;
import soot.jimple.infoflow.android.InfoflowAndroidConfiguration;
import soot.jimple.infoflow.android.SetupApplication;
import soot.jimple.infoflow.handlers.ResultsAvailableHandler;
import soot.jimple.infoflow.results.InfoflowResults;
import soot.jimple.infoflow.results.ResultSinkInfo;
import soot.jimple.infoflow.results.ResultSourceInfo;
import soot.jimple.infoflow.solver.cfg.IInfoflowCFG;

public class ExtractSourceSinkFlow {

	private String apkFile;
	private String androidJar;
	private String sourcesAndSinksFile = "SourcesAndSinks.txt";
	private boolean showLogSourcesAndSinks = false;
	private ExtractResults flowResult = new ExtractResults();
	private InfoflowAndroidConfiguration config = new InfoflowAndroidConfiguration();

	public ExtractSourceSinkFlow(String apkFile, String androidJar) {
		this.apkFile = apkFile;
		this.androidJar = androidJar;
	}

	public ExtractSourceSinkFlow() {
		// TODO Auto-generated constructor stub
	}

	public InfoflowResults run() throws IOException, XmlPullParserException {
		// 载入配置
		loadConfig();

		// 开始计时
		final long beforeRun = System.nanoTime();

		final SetupApplication app;
		app = new SetupApplication(this.androidJar, this.apkFile);
		app.setConfig(this.config);

		System.out.println("Running data flow analysis...");
		app.addResultsAvailableHandler(new MyResultsAvailableHandler());
		final InfoflowResults res = app.runInfoflow(this.sourcesAndSinksFile);
		this.flowResult.setCostTime((System.nanoTime() - beforeRun) / 1E9);
		System.out.println("Analysis has run for " + this.flowResult.getCostTime() + " seconds");

		this.flowResult.setSelSources(app.getCollectedSources());
		this.flowResult.setSelSinks(app.getCollectedSinks());

		// 显示所选的 sources 和 sinks
		if (this.config.getLogSourcesAndSinks()) {
			printSourcesAndSinks(app);
		}

		return res;
	}

	private void loadConfig() {
		// 是否打印 Sources and Sinks
		this.config.setLogSourcesAndSinks(true);

		// SourcesAndSinks.txt 文件是否存在
		Path curDir = Paths.get(System.getProperty("user.dir"));
		Path sourceSinkPath = Paths.get(curDir.toString(), this.sourcesAndSinksFile);
		File sourceSinkFile = sourceSinkPath.toFile();
		if (!sourceSinkFile.exists()) {
			System.out.println("SourcesAndSinks.txt not exists");
		}

	}

	private void printSourcesAndSinks(SetupApplication app) {
		if (this.showLogSourcesAndSinks) {
			if (!app.getCollectedSources().isEmpty()) {
				System.out.println("Collected sources:");
				for (Stmt s : app.getCollectedSources())
					System.out.println("\t" + s);
			}
			if (!app.getCollectedSinks().isEmpty()) {
				System.out.println("Collected sinks:");
				for (Stmt s : app.getCollectedSinks())
					System.out.println("\t" + s);
			}
		}

	}

	private class MyResultsAvailableHandler implements ResultsAvailableHandler {

		private final BufferedWriter wr;

		private MyResultsAvailableHandler() {
			this.wr = null;
		}

		private MyResultsAvailableHandler(BufferedWriter wr) {
			this.wr = wr;
		}

		@Override
		public void onResultsAvailable(IInfoflowCFG cfg, InfoflowResults results) {
			// 结果处理
			List<String> allFlow = new ArrayList<String>();
			String tempFlow = "";
			if (results == null) {
				print("No results found.");
			} else {
				// 报告结果

				for (ResultSinkInfo sink : results.getResults().keySet()) {
					if (config.isIccEnabled() && config.isIccResultsPurifyEnabled()) {
						print("Found an ICC flow to sink " + sink + ", from the following sources:");
					} else {
						print("Found a flow to sink " + sink + ", from the following sources:");
					}

					for (ResultSourceInfo source : results.getResults().get(sink)) {
						print("\t- " + source.getSource() + " (in " + cfg.getMethodOf(source.getSource()).getSignature()
								+ ")");
						if (source.getPath() != null)
							print("\t\ton Path " + Arrays.toString(source.getPath()));
						tempFlow = source.getSource() + " @ " + sink;
						allFlow.add(tempFlow);
					}
				}
				flowResult.setExtractContents(allFlow);

			}

		}

		private void print(String string) {
			try {
				System.out.println(string);
				if (wr != null)
					wr.write(string + "\n");
			} catch (IOException ex) {
				// ignore
			}
		}
	}

	public String getApkFile() {
		return apkFile;
	}

	public void setApkFile(String apkFile) {
		this.apkFile = apkFile;
	}

	public String getAndroidJar() {
		return androidJar;
	}

	public void setAndroidJar(String androidJar) {
		this.androidJar = androidJar;
	}

	public String getSourcesAndSinksFile() {
		return sourcesAndSinksFile;
	}

	public void setSourcesAndSinksFile(String sourcesAndSinksFile) {
		this.sourcesAndSinksFile = sourcesAndSinksFile;
	}

	public boolean isShowLogSourcesAndSinks() {
		return showLogSourcesAndSinks;
	}

	public void setShowLogSourcesAndSinks(boolean showLogSourcesAndSinks) {
		this.showLogSourcesAndSinks = showLogSourcesAndSinks;
	}

	public ExtractResults getFlowResult() {
		return flowResult;
	}

	public void setFlowResult(ExtractResults flowResult) {
		this.flowResult = flowResult;
	}
}
