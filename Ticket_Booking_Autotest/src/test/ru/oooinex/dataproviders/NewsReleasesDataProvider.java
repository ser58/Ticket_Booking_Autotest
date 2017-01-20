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

public class NewsReleasesDataProvider {
	@DataProvider(name = "smiReview")
	public static Object[][] getSMIReviewsData(ITestContext context) throws IOException, ParseException {
		Object paramsList[][];
		Object params[] = null;
		Hashtable<String, Boolean> goals = new Hashtable<String, Boolean>();
		Hashtable<String, Object> reviewOptions = new Hashtable<String, Object>();

		int reviewNumber;

		String projPath;
		String filePath = "";

		ArrayList<Object> allParams = new ArrayList<Object>();

		projPath = System.getProperty("user.dir"); 				// Определяем домашнюю папку теста
		
		filePath = projPath + context.getCurrentXmlTest().getParameter("smiReviews_DataFile");

		// Work with json_simple
		FileReader fr = new FileReader(filePath);
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(fr);
		JSONArray jsonNews = (JSONArray) jsonObject.get("news");

		reviewNumber = Integer.parseInt(context.getCurrentXmlTest().getParameter("newsNumber").trim()) - 1;
		JSONObject jsonReview = (JSONObject) jsonNews.get(reviewNumber);
		JSONObject jsonGoals = (JSONObject) jsonReview.get("goals");
		
		goals.put("newsForPassengers", (boolean) jsonGoals.get("newsForPassengers"));
		goals.put("newsForTrucks", (boolean) jsonGoals.get("newsForTrucks"));
		goals.put("internationalNews", (boolean) jsonGoals.get("internationalNews"));
		goals.put("dzoNews", (boolean) jsonGoals.get("dzoNews"));
		goals.put("investorsNews", (boolean) jsonGoals.get("investorsNews"));
		goals.put("onlineAccreditation", (boolean) jsonGoals.get("onlineAccreditation"));
		goals.put("includeInNewsletter", (boolean) jsonGoals.get("includeInNewsletter"));
		
		allParams.add(goals);

		reviewOptions.put("header", (String) jsonReview.get("header"));
		reviewOptions.put("highlightInColor", (boolean) jsonReview.get("highlightInColor"));
		reviewOptions.put("announcement", (String) jsonReview.get("announcement"));
		reviewOptions.put("position", (String) jsonReview.get("position"));
		reviewOptions.put("text", (String) jsonReview.get("text"));
		reviewOptions.put("reference", (String) jsonReview.get("reference"));
		reviewOptions.put("etbPosition", (String) jsonReview.get("etbPosition"));
		reviewOptions.put("startingOfPasengers", (String) jsonReview.get("startingOfPasengers"));
		reviewOptions.put("sections", (String) jsonReview.get("sections"));
		reviewOptions.put("headings", (String) jsonReview.get("headings"));
		reviewOptions.put("newsPhotoreportID", (String) jsonReview.get("newsPhotoreportID"));
		reviewOptions.put("publicationDate", (String) jsonReview.get("publicationDate"));
		reviewOptions.put("dateForCalendar", (String) jsonReview.get("dateForCalendar"));
		reviewOptions.put("publicationStatus", (String) jsonReview.get("publicationStatus"));
		reviewOptions.put("pressFolderName", (String) jsonReview.get("pressFolderName"));
		reviewOptions.put("comment", (String) jsonReview.get("comment"));
		reviewOptions.put("actionName", (String) jsonReview.get("actionName"));
		reviewOptions.put("accreditationCompletionDate", (String) jsonReview.get("accreditationCompletionDate"));
		
		allParams.add(reviewOptions);
		
		paramsList = new Object[1][];
		
		params = new Object[allParams.size()];
		params = allParams.toArray();
		
		paramsList[0] = params;
		return paramsList;
	}
}
