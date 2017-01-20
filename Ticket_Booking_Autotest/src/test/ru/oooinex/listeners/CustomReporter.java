package test.ru.oooinex.listeners;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.reporters.SuiteHTMLReporter;
import org.testng.xml.XmlSuite;

public class CustomReporter extends SuiteHTMLReporter {
	private PrintWriter mOut;

	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		new File(outputDirectory).mkdirs();

		try {
			mOut = new PrintWriter(new BufferedWriter(new FileWriter(new File(outputDirectory, "custom-report.html"))));
		} catch (IOException e) {
			System.out.println("Error in creating writer: " + e);
		}

		startHtml();

		print("Suites run: " + suites.size());

		for (ISuite suite : suites) {
			print("Suite>" + suite.getName());
			Map<String, ISuiteResult> suiteResults = suite.getResults();
			for (String testName : suiteResults.keySet()) {
				print("    Test>" + testName);
				ISuiteResult suiteResult = suiteResults.get(testName);
				ITestContext testContext = suiteResult.getTestContext();
				print("        Failed>" + testContext.getFailedTests().size());
				IResultMap failedResult = testContext.getFailedTests();
				Set<ITestResult> testsFailed = failedResult.getAllResults();

				testsFailed.forEach(test -> {
					print("            " + test.getName());
					print("                " + test.getThrowable());
				});
				
				IResultMap passResult = testContext.getPassedTests();
				Set<ITestResult> testsPassed = passResult.getAllResults();
				print("        Passed>" + testsPassed.size());
				
				testsFailed.forEach(test -> print("            " + test.getName() + ">took " + (test.getEndMillis() - test.getStartMillis()) + "ms"));

				IResultMap skippedResult = testContext.getSkippedTests();
				Set<ITestResult> testsSkipped = skippedResult.getAllResults();
				print("        Skipped>" + testsSkipped.size());
				
				testsFailed.forEach(test -> print("            " + test.getName()));
			}
		}
		endHtml();
		mOut.flush();
		mOut.close();
	}

	@Override
	public String generateOutputDirectoryName(java.lang.String outputDirectory) {
		System.out.println(outputDirectory);
		return outputDirectory;
	}

	private void print(String text) {
		System.out.println(text);
		mOut.println(text + "");
	}

	private void startHtml() {
		mOut.println("");
		mOut.println("");
		mOut.println("TestNG Html Report Example");
		mOut.println("");
		mOut.println("");
	}

	private void endHtml() {
		mOut.println("");
	}

}
