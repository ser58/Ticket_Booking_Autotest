package test.ru.oooinex.dataproviders;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

public class SuggestionsDataProvider {
	@DataProvider(name = "suggestions")
	public static Object[][] getSMIReviewsData(ITestContext context) throws IOException, ParseException {
		Object paramsList[][];
		Object params[] = null;
		
		String toEnter;
		String toSelect;
		String[] listTo;
		String fromEnter;
		String fromSelect;
		String[] listFrom;

		int suggestNumber;

		String projPath;
		String filePath = "";

		ArrayList<Object> allParams = new ArrayList<Object>();

		projPath = System.getProperty("user.dir"); 				// Определяем домашнюю папку теста
		
		filePath = projPath + context.getCurrentXmlTest().getParameter("suggestions_DataFile");

		// Work with json_simple
		FileReader fr = new FileReader(filePath);
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(fr);
		JSONArray jsonSuggests = (JSONArray) jsonObject.get("suggestions");

		suggestNumber = Integer.parseInt(context.getCurrentXmlTest().getParameter("suggestNumber").trim()) - 1;
		JSONObject jsonSuggest = (JSONObject) jsonSuggests.get(suggestNumber);
		
		toEnter = (String) jsonSuggest.get("toEnter");
		allParams.add(toEnter);
		
		toSelect = (String) jsonSuggest.get("toSelect");
		allParams.add(toSelect);
		
		JSONArray jsonStasionsTo = (JSONArray) jsonSuggest.get("listTo");
		listTo = new String[jsonStasionsTo.size()];
		
		for (int i = 0; i < jsonStasionsTo.size(); i++) {
			String station = (String) jsonStasionsTo.get(i);
			listTo[i] = station;
		}
		
		allParams.add(listTo);

		fromEnter = (String) jsonSuggest.get("fromEnter");
		allParams.add(fromEnter);
		
		fromSelect = (String) jsonSuggest.get("fromSelect");
		allParams.add(fromSelect);
		
		JSONArray jsonStasionsFrom = (JSONArray) jsonSuggest.get("listFrom");
		listFrom = new String[jsonStasionsFrom.size()];
		
		for (int i = 0; i < jsonStasionsFrom.size(); i++) {
			String station = (String) jsonStasionsFrom.get(i);
			listFrom[i] = station;
		}
		
		allParams.add(listFrom);
		
		paramsList = new Object[1][];
		
		params = new Object[allParams.size()];
		params = allParams.toArray();
		
		paramsList[0] = params;
		return paramsList;
	}
}
