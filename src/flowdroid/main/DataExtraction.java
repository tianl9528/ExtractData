package flowdroid.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import flowdroid.helper.DataParsing;
import flowdroid.helper.ExtractAndroidManifest;
import flowdroid.helper.ExtractSourceSinkFlow;
import soot.dexpler.typing.Validate;

public class DataExtraction {

	private static int repeatCount = 1;

	public static void main(String[] args) throws XmlPullParserException, IOException {
		if (args.length < 3) {
			printUsage();
			return;
		}

		String apkPath = args[0];
		String androidJar = args[1];
		String mode = args[2];

		// 验证并处理参数
		List<String> apkFiles = validateAgrs(apkPath, androidJar, mode);

		// 运行并提取结果
		List<DataParsing> allResults = run(apkPath, apkFiles, androidJar, mode, 1);

		// 保存所有数据到单个文件
		savaOverview(allResults, 0);
		savaFormat(allResults, 0);

	}

	public static List<String> validateAgrs(String apkPath, String androidJar, String mode) {

		List<String> apkFiles = new ArrayList<String>();

		// args[0]
		File apkFile = new File(apkPath);
		// 目录
		if (apkFile.isDirectory()) {
			String[] dirFiles = apkFile.list(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					return (name.endsWith(".apk"));
				}

			});
			if (dirFiles.length == 0) {
				System.err.println("apk-file not exists: " + apkFile.getName());
			} else {
				for (String s : dirFiles)
					apkFiles.add(s);
			}
		} else {
			// 文件
			String extension = apkFile.getName().substring(apkFile.getName().lastIndexOf("."));
			if (extension.equalsIgnoreCase(".apk"))
				apkFiles.add(apkPath);
			else {
				System.err.println("Invalid input file format: " + extension);
				return null;
			}
		}

		// args[1]
		File sdkFile = new File(androidJar);
		if (!sdkFile.exists()) {
			System.out.println("android-jar-directory not exists: " + sdkFile.getName());
			return null;
		}

		// args[2]
		if (!mode.equals("1") & !mode.equals("0")) {
			printUsage();
		}

		return apkFiles;
	}

	private static List<DataParsing> run(String apkPath, List<String> apkFiles, String androidJar, String mode,
			int batch) throws IOException {

		List<DataParsing> allResults = new ArrayList<DataParsing>();

		ExtractAndroidManifest EAM = new ExtractAndroidManifest();
		EAM.getManifestResult().setExtractMode(mode);

		ExtractSourceSinkFlow ESSF = new ExtractSourceSinkFlow();
		ESSF.getFlowResult().setExtractMode(mode);

		// 实现apk目录内所有文件提取
		int oldRepeatCount = repeatCount;
		File apkFile = new File(apkPath);
		for (final String fileName : apkFiles) {
			repeatCount = oldRepeatCount;
			final String fullFilePath;
			System.gc();

			// 目录处理逻辑
			if (apkFiles.size() > 1) {
				if (apkFile.isDirectory())
					fullFilePath = apkPath + File.separator + fileName;
				else
					fullFilePath = fileName;
				System.out.println("Analyzing file " + fullFilePath + "...");

				File flagFile = new File("Processed/_Run_" + new File(fileName).getName());
				File flagFileDir = flagFile.getParentFile();
				if (!flagFileDir.exists()) {
					flagFileDir.mkdirs();
				}
				if (flagFile.exists())
					continue;
				flagFile.createNewFile();

			} else
				fullFilePath = apkPath + File.separator + fileName;

			// TODO
			// 提取文件数据
			System.out.println((apkFiles.indexOf(fileName) + 1) + " / " + apkFiles.size());
			while (repeatCount > 0) {
				System.gc();
				DataParsing results = new DataParsing(fullFilePath);
				// 提取权限
				EAM.setApkFile(fullFilePath);
				try {
					EAM.run();
				} catch (IOException e1) {
					File failureFile = new File("FinalResult/_Run_" + new File(fileName).getName());
					failureFile.createNewFile();
					savaFormat(allResults, 0);
					savaOverview(allResults, 0);
					System.out.println("Manifest IO: " + e1);
				} catch (XmlPullParserException e1) {
					File failureFile = new File("FinalResult/_Run_" + new File(fileName).getName());
					failureFile.createNewFile();
					savaFormat(allResults, 0);
					savaOverview(allResults, 0);
					System.out.println("Manifest XmlPullParser: " + e1);
				}
				results.setManifestResults(EAM.getManifestResult().getExtractContents());

				// 提取流
				ESSF.setAndroidJar(androidJar);
				ESSF.setApkFile(fullFilePath);
				try {
					ESSF.run();
				} catch (IOException e) {
					File failureFile = new File("FinalResult/_Run_" + new File(fileName).getName());
					failureFile.createNewFile();
					savaFormat(allResults, 0);
					savaOverview(allResults, 0);
					System.out.println("Flow IO: " + e);
				} catch (XmlPullParserException e) {
					File failureFile = new File("FinalResult/_Run_" + new File(fileName).getName());
					failureFile.createNewFile();
					savaFormat(allResults, 0);
					savaOverview(allResults, 0);
					System.out.println("Flow XmlPullParser: " + e);
				}
				results.setFlowResults(ESSF.getFlowResult().getExtractContents());
				results.setFlowTime(ESSF.getFlowResult().getCostTime());
				results.setFlowSources(ESSF.getFlowResult().getSelSources());
				results.setFlowSinks(ESSF.getFlowResult().getSelSinks());
				results.setFlowTime(ESSF.getFlowResult().getCostTime());
				results.setMode(mode);

				allResults.add(results);

				repeatCount--;
				if ((apkFiles.indexOf(fileName) + 1) % batch == 0) {
					savaOverview(allResults, batch);
					savaFormat(allResults, batch);
					System.gc();
				}

			}

			System.gc();
		}
		
		return allResults;
	}

	private static void savaOverview(List<DataParsing> allresults, int batch) throws IOException {
		// 保存 Overview 结果

		System.out.println("正在保存 Overview 结果");
		File finalResult;
		FileWriter fw = null;
		BufferedWriter bw = null;

		// 创建保存文件
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式

		if (batch == 0) {
			finalResult = new File("FinalResult/overview/OverviewResult_" + df.format(new Date()) + ".txt");
		} else {
			finalResult = new File(
					"FinalResult/overview/OverviewResult_" + df.format(new Date()) + "_" + batch + ".txt");
		}

		File finalResultDir = finalResult.getParentFile();
		if (!finalResultDir.exists()) {
			finalResultDir.mkdirs();
		}

		// 开始写文件
		fw = new FileWriter(finalResult, true);
		bw = new BufferedWriter(fw);

		for (DataParsing results : allresults) {
			bw.write("Apk Name: " + results.getApkName() + "\n");
			bw.write("\t--Manifest, 共 " + results.getManifestNum() + ": " + results.getManifestResults() + "\n");
			bw.write("\t--Flow, 共 " + results.getFlowNum() + ": " + results.getFlowResults() + "\n");
			bw.write("\t\t--Time: " + results.getFlowTime() + " s\n");
			bw.write("\t\t--Sources: " + results.getFlowSources() + "\n");
			bw.write("\t\t--Sinks: " + results.getFlowSinks() + "\n");
			bw.write("\t--Mode: " + results.getMode() + "\n\n");
		}
		bw.close();
		System.out.println("已保存到 " + finalResult);
	}

	private static void savaFormat(List<DataParsing> allresults, int batch) throws IOException {
		// 保存 Format 结果, 提取的字段“apkName | manifest | flow | soures | sinks”，字段之间用 |
		// 隔开，字段内不同元素之间用 & 隔开， source 和sink 用 @ 连接

		System.out.println("正在保存 Format 结果");
		File finalResult;
		FileWriter fw = null;
		BufferedWriter bw = null;

		// 创建保存文件
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
		if (batch == 0) {
			finalResult = new File("FinalResult/format/FormatResult_" + df.format(new Date()) + ".txt");
		} else {
			finalResult = new File("FinalResult/format/FormatResult_" + df.format(new Date()) + "_" + batch + ".txt");
		}
		File finalResultDir = finalResult.getParentFile();
		if (!finalResultDir.exists()) {
			finalResultDir.mkdirs();
		}

		// 开始写文件
		fw = new FileWriter(finalResult, true);
		bw = new BufferedWriter(fw);
		for (DataParsing results : allresults) {
			List<String> data = Arrays.asList(results.apkNameToString(), results.manifestToString(),
					results.flowToString(), results.sourcesToString(), results.sinksToString(),
					String.valueOf(results.getFlowTime()), results.getMode());
			StringBuilder result = new StringBuilder();
			boolean first = true;
			for (String string : data) {
				if (first) {
					first = false;
				} else {
					result.append(" ||| ");
				}
				result.append(string);
			}
			bw.write(result.toString() + "\n");
		}

		bw.close();
		System.out.println("已保存到 " + finalResult);
	}

	private static void printUsage() {
		System.out.println("Incorrect arguments: [0] = apk-dir, [1] = android-jar-path, [2] = extract-contents");
	}

}
