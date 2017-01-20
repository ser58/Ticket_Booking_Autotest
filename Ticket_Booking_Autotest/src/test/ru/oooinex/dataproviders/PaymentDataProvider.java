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

public class PaymentDataProvider {
	/**
	 * Gets the payment data.
	 *
	 * @param context the context
	 * @return the payment data
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 */
	@DataProvider(name = "payment")
	public static Object[][] getPaymentData(ITestContext context) throws IOException, ParseException {
		Object paramsList[][];
		Object params[] = null;

		int cardNumber;

		String projPath;
		String filePath = "";

		ArrayList<String> allParams = new ArrayList<String>();

		projPath = System.getProperty("user.dir"); // Определяем домашнюю папку теста
		
		filePath = projPath + context.getCurrentXmlTest().getParameter("payment_DataFile");

		// Work with json_simple
		FileReader fr = new FileReader(filePath);
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(fr);
		JSONArray jsonCards = (JSONArray) jsonObject.get("cards");

		cardNumber = Integer.parseInt(context.getCurrentXmlTest().getParameter("paymentCard").trim()) - 1;
		JSONObject jsonCard = (JSONObject) jsonCards.get(cardNumber);

		allParams.add((String) jsonCard.get("cardNumber"));
		allParams.add((String) jsonCard.get("cardholder"));
		allParams.add((String) jsonCard.get("CVV2"));
		allParams.add((String) jsonCard.get("validityPeriod"));
		
		allParams.add(context.getCurrentXmlTest().getParameter("yandexMoneyLogin"));
		allParams.add(context.getCurrentXmlTest().getParameter("yandexMoneyPassword"));
		allParams.add(context.getCurrentXmlTest().getParameter("yandexPayPassword"));
		
		paramsList = new Object[1][];
		
		params = new Object[allParams.size()];
		params = allParams.toArray();
		
		paramsList[0] = params;
		return paramsList;
	}
}
