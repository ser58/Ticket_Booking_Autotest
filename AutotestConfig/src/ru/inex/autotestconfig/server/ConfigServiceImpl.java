package ru.inex.autotestconfig.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import ru.inex.autotestconfig.client.ConfigService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ConfigServiceImpl extends RemoteServiceServlet implements ConfigService {

	private static final long serialVersionUID = 4505603944013976965L;
	private BufferedReader br;
	
	@Override
	public String getTestngFile(String projectRoot) {
		String testngFile = "";
		String pomXMLFile = projectRoot + "/pom.xml";

		testngFile = projectRoot + "/" + getValueFromPOM(pomXMLFile, "suiteXmlFile");

		return readFile(testngFile);
	}

	private String getValueFromPOM(String pomFile, String key) {
		File file = new File(pomFile);
		String testngFile = "";

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(file);
			Element root = document.getDocumentElement();
			Element suiteXmlFiles = (Element) root.getElementsByTagName(key).item(0);

			testngFile = suiteXmlFiles.getTextContent();

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

		return testngFile;
	}

	@Override
	public String getSuiteXML(String suiteXMLFile) {
		return readFile(suiteXMLFile);
	}
	
	private String readFile(String fileName) {
		File file = new File(fileName);
		String outBuff = new String();
		
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			String eachLine = br.readLine();

			while (eachLine != null) {
				outBuff += eachLine + "\n";
				eachLine = br.readLine();
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return outBuff;
	}
}