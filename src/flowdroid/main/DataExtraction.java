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
import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import flowdroid.helper.DataParsing;
import flowdroid.helper.ExtractAndroidManifest;
import flowdroid.helper.ExtractSourceSinkFlow;

public class DataExtraction {

	private static int repeatCount = 1;

	public static void main(String[] args) throws IOException, XmlPullParserException {
		if (args.length < 3) {
			printUsage();
			return;
		}

		List<String> apkFiles = new ArrayList<String>();
		String androidJar = args[1];
		String mode = args[2];

		// 验证参数args[]
		// args[0]
		File apkFile = new File(args[0]);
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
			// apk is a file so grab the extension
			String extension = apkFile.getName().substring(apkFile.getName().lastIndexOf("."));
			if (extension.equalsIgnoreCase(".apk"))
				apkFiles.add(args[0]);
			else {
				System.err.println("Invalid input file format: " + extension);
				return;
			}
		}

		// args[1]
		File sdkFile = new File(androidJar);
		if (!sdkFile.exists()) {
			System.out.println("android-jar-directory not exists: " + sdkFile.getName());
			return;
		}

		Path curDir = Paths.get(System.getProperty("user.dir"));
		Path sourceSinkPath = Paths.get(curDir.toString(), "SourcesAndSinks.txt");
		File sourceSinkFile = sourceSinkPath.toFile();
		if (!sourceSinkFile.exists()) {
			System.out.println("SourcesAndSinks.txt not exists");
			return;
		}

		List<DataParsing> allresults = new ArrayList<DataParsing>();
		ExtractAndroidManifest EAM = new ExtractAndroidManifest();
		ExtractSourceSinkFlow ESSF = new ExtractSourceSinkFlow();

		// 实现apk目录内所有文件提取
		int oldRepeatCount = repeatCount;
		for (final String fileName : apkFiles) {
			repeatCount = oldRepeatCount;
			final String fullFilePath;
			System.gc();

			// Directory handling
			if (apkFiles.size() > 1) {
				if (apkFile.isDirectory())
					fullFilePath = args[0] + File.separator + fileName;
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
				fullFilePath = fileName;

			// TODO
			// Run the extract
			while (repeatCount > 0) {
				System.gc();
				DataParsing results = new DataParsing(fullFilePath);
				// 提取权限
				EAM.setApkFile(fullFilePath);
				EAM.run();
				results.setManifestResults(EAM.getManifestResult().getExtractContents());

				// 提取流
				ESSF.setAndroidJar(androidJar);
				ESSF.setApkFile(fullFilePath);
				ESSF.run();
				results.setFlowResults(ESSF.getFlowResult().getExtractContents());

				// // 测试输出
				// // 假装清空输出
				// for (int i = 0; i < 400; i++) {
				// System.out.println("");// 输出400行空行
				// }
				// System.out.println(results.getApkName());
				// // 权限
				// System.out.println("以下是权限, 共 " + results.getManifestNum());
				// System.out.println(results.getManifestResults());
				// // 流
				// System.out.println("\n以下是流, 共 " + results.getFlowNum());
				// System.out.println(results.getFlowResults());

				allresults.add(results);

				repeatCount--;
			}

			System.gc();
		}

		// 保存结果
		System.out.println("正在保存结果");
		FileWriter fw = null;
		BufferedWriter bw = null;

		// 创建保存文件
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
		File finalResult = new File("FinalResult/finalResult_" + df.format(new Date()) + ".txt");
		File finalResultDir = finalResult.getParentFile();
		if (!finalResultDir.exists()) {  
			finalResultDir.mkdirs();  
	    }

		// 开始写文件
		fw = new FileWriter(finalResult, true);
		bw = new BufferedWriter(fw);

		for (DataParsing temp : allresults) {
			bw.write("Apk Name: " + temp.getApkName() + "\n");
			bw.write("\t-Manifest, 共 " + temp.getManifestNum() + ": " + temp.getManifestResults() + "\n");
			bw.write("\t-Flow, 共 " + temp.getFlowNum() + ": " + temp.getFlowResults() + "\n");
		}
		bw.close();
		System.out.println("已保存到 "+finalResult);

	}

	private static void printUsage() {
		System.out.println("Incorrect arguments: [0] = apk-dir, [1] = android-jar-path, [2] = extract-contents");
	}

}
