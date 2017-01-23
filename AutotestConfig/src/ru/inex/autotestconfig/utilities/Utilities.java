package ru.inex.autotestconfig.utilities;

import java.util.ArrayList;
import java.util.List;

import ru.inex.autotestconfig.modeldata.datafiles.DataFile;
import ru.inex.autotestconfig.modeldata.suites.Parameter;
import ru.inex.autotestconfig.modeldata.suites.SuiteTestsList;
import ru.inex.autotestconfig.modeldata.suites.TestMethodsList;
import ru.inex.autotestconfig.modeldata.testng.TestngParams;
import ru.inex.autotestconfig.modeldata.testng.TestngSuiteFiles;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class Utilities {
	public static TestngParams getTestngParams(String testngFile) {
		TestngParams testngParams = new TestngParams();

		String[] temp = testngFile.split("\n");

		for (String line : temp) {
			String paramName;
			String paramValue;

			if (!line.contains("<!--") && line.contains("parameter name=")) {
				paramName = line.substring(line.indexOf("parameter name=") + 15, line.indexOf("value=")).trim().replace("\"", "");
				paramValue = line.substring(line.indexOf("value=") + 6,	line.indexOf("/>")).trim().replace("\"", "");

				if (paramName.contentEquals("baseUrl"))
					testngParams.setBaseUrl(paramValue);
				else if (paramName.contentEquals("baseUrlPortal"))
					testngParams.setBaseUrlPortal(paramValue);
				else if (paramName.contentEquals("baseUrlNews"))
					testngParams.setBaseUrlNews(paramValue);
				else if (paramName.contentEquals("profile"))
					testngParams.setProfile(paramValue);
				else if (paramName.contentEquals("login"))
					testngParams.setLogin(paramValue);
				else if (paramName.contentEquals("password"))
					testngParams.setPassword(paramValue);
				else if (paramName.contentEquals("browser"))
					testngParams.setBrowser(paramValue);
				else if (paramName.contentEquals("holdBrowser"))
					testngParams.setHoldBrowser(paramValue);
				else if (paramName.contentEquals("reportsFolder"))
					testngParams.setReportsFolder(paramValue);
				else if (paramName.contentEquals("reportsUrl"))
					testngParams.setReportsUrl(paramValue);
				else if (paramName.contentEquals("timeout"))
					testngParams.setTimeout(paramValue);
				else if (paramName.contentEquals("newVersion"))
					testngParams.setNewVersion(paramValue);
				else if (paramName.contentEquals("paymentGatewayPlug"))
					testngParams.setPaymentGatewayPlug(paramValue);
			}
		}

		return testngParams;
	}
	
	public static List<DataFile> getDataFiles(String testngFile) {
		List<DataFile> dataFile = new ArrayList<DataFile>();

		String[] temp = testngFile.split("\n");
		
		for (String line : temp) {
			String paramName;
			String paramValue;
			DataFile _dataFile;
			
			if (line.contains("_DataFile") && line.contains("parameter name=")) {
				paramName = line.substring(line.indexOf("parameter name=") + 15, line.indexOf("value=")).trim().replace("\"", "");
				paramValue = line.substring(line.indexOf("value=") + 6, line.indexOf("/>")).trim().replace("\"", "");
				
				_dataFile = new DataFile();
				_dataFile.setName(paramName);
				_dataFile.setValue(paramValue);
				dataFile.add(_dataFile);
			}
		}
		
		return dataFile;
	}
	
	public static List<TestngSuiteFiles> getSuiteFiles(String testngFile) {
		List<TestngSuiteFiles> suiteFile = new ArrayList<TestngSuiteFiles>();

		String[] temp = testngFile.split("\n");
		
		for (String line : temp) {
			String path;
			boolean enabled;
			TestngSuiteFiles _suiteFile;
			line = line.replace("\t", "");
			
			if (line.contains("suite-file path=")) {
				if (line.startsWith("<!--")) {
					path = line.substring(line.indexOf("suite-file path=") + 17, line.indexOf(" /-->") - 1);
					enabled = false;
				}
				else {
					path = line.substring(line.indexOf("suite-file path=") + 17, line.indexOf(" />") - 1);
					enabled = true;
				}
				
				_suiteFile = new TestngSuiteFiles();
				_suiteFile.setPath(path);
				_suiteFile.setEnabled(enabled);
				suiteFile.add(_suiteFile);
			}
		}
		
		return suiteFile;
	}

	public static List<SuiteTestsList> getSuiteTestsList(NodeList tests) {
		List<SuiteTestsList> suiteTestsList = new ArrayList<SuiteTestsList>();
		
		for (int i = 0; i < tests.getLength(); i++) {
			SuiteTestsList suiteTest = new SuiteTestsList();

			Node test = tests.item(i);
			
			Node testParam = test.getAttributes().getNamedItem("name");
			suiteTest.setName(testParam.getNodeValue());
			
			testParam = test.getAttributes().getNamedItem("preserve-order");
			suiteTest.setPreserveOrder(testParam.getNodeValue().contains("true") ? true : false);
			
			testParam = test.getAttributes().getNamedItem("enabled");
			suiteTest.setEnabled(testParam.getNodeValue().contains("true") ? true : false);
			
			suiteTestsList.add(suiteTest);
		}
		
		return suiteTestsList;
	}
	
	public static List<Parameter> getTestParameters(Node test) {
		List<Parameter> testParameters = new ArrayList<Parameter>();

		Document docTest = XMLParser.parse(test.toString());
		
		NodeList params = docTest.getElementsByTagName("parameter");
		
		for (int i = 0; i < params.getLength(); i++) {
			Parameter testParameter = new Parameter();
			
			Node param = params.item(i);
			
			Node testParam = param.getAttributes().getNamedItem("name");
			testParameter.setName(testParam.getNodeValue());
			
			testParam = param.getAttributes().getNamedItem("value");
			testParameter.setValue(testParam.getNodeValue());
			
			testParameters.add(testParameter);
		}
		
		return testParameters;
	}
	
	public static String getTestClassName(Node test) {
		Document docTest = XMLParser.parse(test.toString());
		Node className = docTest.getElementsByTagName("class").item(0);
		return className.getAttributes().getNamedItem("name").getNodeValue();
	}
	
	public static List<TestMethodsList> getTestMethods(Node test) {
		List<TestMethodsList> methodsList = new ArrayList<TestMethodsList>();
		
		Document docTest = XMLParser.parse(test.toString());
		
		Node nodeMethods = docTest.getElementsByTagName("methods").item(0);
		NodeList methods = nodeMethods.getChildNodes();
		String nodeName;
		
		for (int i = 0; i < methods.getLength(); i++) {
			TestMethodsList testMethod = new TestMethodsList();
			
			Node method = methods.item(i);
			nodeName = method.getNodeName();
			
			if (!nodeName.contains("#text")) {
				testMethod.setName(method.getAttributes().getNamedItem("name").getNodeValue());
				testMethod.setIncluded(nodeName.contains("include") ? true : false);
				methodsList.add(testMethod);
			}
		}
		
		return methodsList;
	}
}
