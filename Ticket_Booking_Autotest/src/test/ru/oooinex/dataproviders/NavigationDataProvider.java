package test.ru.oooinex.dataproviders;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

public class NavigationDataProvider {
	@DataProvider(name = "navigation")
	public static Object[][] getSMIReviewsData(ITestContext context) throws IOException, ParseException {
		Object paramsList[][];
		Object params[] = null;
		Hashtable<String, Object> topicOptions;

		String projPath;
		String filePath = "";

		ArrayList<Object> allParams = new ArrayList<Object>();

		projPath = System.getProperty("user.dir"); 				// Определяем домашнюю папку теста
		
		filePath = projPath + context.getCurrentXmlTest().getParameter("navigation_DataFile");

		// Work with json_simple
		FileReader fr = new FileReader(filePath);
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(fr);
		JSONArray jsonTopics = (JSONArray) jsonObject.get("topics");
		
		for (int i = 0; i < jsonTopics.size(); i++) {
			topicOptions = new Hashtable<String, Object>();
			JSONObject jsonTopic = (JSONObject) jsonTopics.get(i);

			topicOptions.put("name", (String) jsonTopic.get("name"));
			topicOptions.put("sortOrder", (String) jsonTopic.get("sortOrder"));
			topicOptions.put("linkType", (String) jsonTopic.get("linkType"));
			topicOptions.put("template", (String) jsonTopic.get("template"));
			topicOptions.put("url", (String) jsonTopic.get("url"));
			topicOptions.put("publishType", (String) jsonTopic.get("publishType"));
			topicOptions.put("parent", (String) jsonTopic.get("parent"));
			topicOptions.put("keywords", (String) jsonTopic.get("keywords"));
			topicOptions.put("description", (String) jsonTopic.get("description"));
			topicOptions.put("text", (String) jsonTopic.get("text"));
			
			allParams.add(topicOptions);
		}
		
		paramsList = new Object[1][];
		
		params = new Object[allParams.size()];
		params = allParams.toArray();
		
		paramsList[0] = params;
		return paramsList;
	}
}