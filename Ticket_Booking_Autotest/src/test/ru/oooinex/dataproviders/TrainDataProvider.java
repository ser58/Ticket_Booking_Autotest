/*
 * 
 */
package test.ru.oooinex.dataproviders;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

import test.ru.oooinex.utilities.Tools;

public class TrainDataProvider {
	static JSONArray departure;
	static int dayOfWeek;
	static int weekOffset;
	static String dateTo;
	static String dateFrom;
	
	@DataProvider(name = "DirectTrain")
	public static Object[][] getDirectTrainData(ITestContext context, Method method) throws IOException, ParseException {
		Object paramsList[][];
		Object params[] = null;

		int trainNumber;
		String projPath;
		String filePath = "";
		String testName;
		
		String directForeignTrain;
		String returnForeignTrain;
		
		JSONObject temp;

		JSONArray trains;
		JSONObject train;
		JSONArray routeOptions;
		JSONArray directTrain;
		JSONObject direct;
		
		ArrayList<Object> allParams = new ArrayList<Object>();
		
		projPath = System.getProperty("user.dir"); // Определяем домашнюю папку теста
		
		filePath = projPath + context.getCurrentXmlTest().getParameter("directTrain_DataFile");
		
		// Work with json_simple
		FileReader fr = new FileReader(filePath);
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(fr);

		// Порядковый номер поезда берём из testng.xml
		trainNumber = Integer.parseInt(context.getCurrentXmlTest().getParameter("trainNumber").trim()) - 1;
		
		// Вытаскиваем данные о всех поездках из json файла и запихиваем их в двумерный массив
		trains = (JSONArray) jsonObject.get("trains");
		
		// Берем нужный поезд по порядковому номеру
		train = (JSONObject) trains.get(trainNumber);
		routeOptions = (JSONArray) train.get("routeOptions");
		
		temp = (JSONObject) routeOptions.get(0);
		directForeignTrain = (String) temp.get("foreignTrain").toString();
		
		temp = (JSONObject) routeOptions.get(1);
		returnForeignTrain = (String) temp.get("foreignTrain").toString();
		
		temp = (JSONObject) routeOptions.get(0);
		directTrain = (JSONArray) temp.get("directTrain");
		
		temp = (JSONObject) routeOptions.get(1);
		
		testName = method.getName();
		
		departure = (JSONArray) train.get("dayOfWeek");
		dayOfWeek = ((Long) departure.get(0)).intValue();
		weekOffset = ((Long) departure.get(1)).intValue();
		
		dateTo = Tools.getDepartureDate(dayOfWeek, weekOffset);
		dateFrom = Tools.getDepartureDate(dayOfWeek + 1, weekOffset);

		if(testName.contentEquals("FillStartForm")) {
			allParams.add("Дата туда: " + dateTo);
			allParams.add("Дата обратно: " + dateFrom);
			allParams.add("Откуда: " + (String) train.get("from"));
			allParams.add("Куда: " + (String) train.get("to"));
			allParams.add("Только с билетами: " + (String) train.get("onlyWithTickets").toString());
			allParams.add("Электрички: " + (String) train.get("localTrain").toString());
			allParams.add("Обратно: " + (String) train.get("return").toString());
			allParams.add("Прямой зарубежный поезд: " + (String) directForeignTrain);
			allParams.add("Обратный зарубежный поезд: " + (String) returnForeignTrain);
			
		} else if (testName.contentEquals("FillPassengerForms")) {
			JSONArray passengers = (JSONArray) train.get("passengers"); // Список параметров пассажиров.
			String tempParams = "";
			
			for (int i = 0; i < passengers.size(); i++) {
				JSONObject passendger = (JSONObject) passengers.get(i);
				
				tempParams += "'" + (String) passendger.get("lastName") + "', ";
				tempParams += "'" + (String) passendger.get("firstName") + "', ";
				tempParams += "'" + (String) passendger.get("middleName") + "', ";
				tempParams += "'" + (String) passendger.get("documentType") + "', ";
				tempParams += "'" + (String) passendger.get("documentNumber") + "', ";
				tempParams += "'" + (String) passendger.get("tariff") + "', ";
				tempParams += "'" + (String) passendger.get("citizenship") + "', ";
				tempParams += "'" + (String) passendger.get("gender") + "', ";
				tempParams += "'" + (String) passendger.get("dateOfBirth") + "', ";
				tempParams += "'" + (String) passendger.get("insurance") + "'";
				
				tempParams += ";";
			} 

			allParams.add(tempParams);
		} else if (testName.contentEquals("CheckTariffs")) {
			JSONArray passengers = (JSONArray) train.get("passengers"); // Список параметров пассажиров.
			String tempParams = "";
			
			for (int i = 0; i < passengers.size(); i++) {
				JSONObject passendger = (JSONObject) passengers.get(i);
				
				tempParams += "'" + (String) passendger.get("lastName") + "', ";
				tempParams += "'" + (String) passendger.get("firstName") + "', ";
				tempParams += "'" + (String) passendger.get("middleName") + "', ";
				tempParams += ";";
			} 

			allParams.add(tempParams);
		} else if (testName.contentEquals("PayElectronicCard")) {
			
		} else if (testName.contentEquals("SelectDirectTrain")) {
			direct = (JSONObject) directTrain.get(0);
			
			allParams.add((String) direct.get("trainNumber"));
			allParams.add((String) direct.get("carCheckers"));
			
		} else if (testName.contentEquals("SelectWagonDirectTrain")) {
			direct = (JSONObject) directTrain.get(0);
			
			allParams.add((String) direct.get("wagonType"));
			allParams.add((String) direct.get("wagonCategory"));
			allParams.add((String) direct.get("servicesAndCapabilities"));
			
		} else if (testName.contentEquals("FillPlaceSigns")) {
			direct = (JSONObject) directTrain.get(0);
			
			temp = (JSONObject) direct.get("places");
			Object[] places = new Object[7];
			places[0] = temp.get("plGender");							// Гендерный признак
			places[1] = (boolean) temp.get("plKupe");					// В одном купе
			places[2] = (Long) temp.get("range0");						// Границы мест
			places[3] = (Long) temp.get("range1");
			places[4] = (boolean) temp.get("plBedding");				// Бельё
			places[5] = temp.get("plUpDown");							// Верхние / нижние
			places[6] = temp.get("plComp");								// Расположение мест
			allParams.add(places);
			
		} else if (testName.contentEquals("CancelRegistrationAndRefund")) {
			direct = (JSONObject) directTrain.get(0);
			allParams.add((String) direct.get("trainNumber"));
			
		} else if (testName.contentEquals("Results")) {
			direct = (JSONObject) directTrain.get(0);
			allParams.add((String) direct.get("wagonCategory"));
		}

		params = new Object[allParams.size()];
		params = allParams.toArray();
		
		paramsList = new Object[1][];
		paramsList[0] = params;
		return paramsList;
	}
	
	@DataProvider(name = "DirectTrainWithTransfer")
	public static Object[][] getDirectTrainWithTransferData(ITestContext context, Method method) throws IOException, ParseException {
		Object paramsList[][];
		Object params[] = null;

		int trainNumber;
		String projPath;
		String filePath = "";
		String testName;
		
		JSONObject temp;
		
		String directForeignTrain;
		String returnForeignTrain;

		JSONArray trains;
		JSONObject train;
		JSONArray routeOptions;
		JSONArray directTrain;
		JSONObject direct;
		JSONObject transfer;
		
		ArrayList<Object> allParams = new ArrayList<Object>();
		
		projPath = System.getProperty("user.dir"); // Определяем домашнюю папку теста
		
		filePath = projPath + context.getCurrentXmlTest().getParameter("directTrainWithTransfer_DataFile");
		
		// Work with json_simple
		FileReader fr = new FileReader(filePath);
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(fr);

		// Порядковый номер поезда берём из testng.xml
		trainNumber = Integer.parseInt(context.getCurrentXmlTest().getParameter("trainNumber").trim()) - 1;
		
		// Вытаскиваем данные о всех поездках из json файла и запихиваем их в двумерный массив
		trains = (JSONArray) jsonObject.get("trains");
		
		// Берем нужный поезд по порядковому номеру
		train = (JSONObject) trains.get(trainNumber);
		routeOptions = (JSONArray) train.get("routeOptions");
		
		temp = (JSONObject) routeOptions.get(0);
		directTrain = (JSONArray) temp.get("directTrain");
		
		directForeignTrain = (String) temp.get("foreignTrain").toString();
		
		temp = (JSONObject) routeOptions.get(1);
		returnForeignTrain = (String) temp.get("foreignTrain").toString();
		
		temp = (JSONObject) routeOptions.get(1);
		
		testName = method.getName();
		
		departure = (JSONArray) train.get("dayOfWeek");
		dayOfWeek = ((Long) departure.get(0)).intValue();
		weekOffset = ((Long) departure.get(1)).intValue();
		
		dateTo = Tools.getDepartureDate(dayOfWeek, weekOffset);
		dateFrom = Tools.getDepartureDate(dayOfWeek + 1, weekOffset);

		if (testName.contentEquals("FillStartForm")) {
			allParams.add("Дата туда: " + dateTo);
			allParams.add("Дата обратно: " + dateFrom);
			allParams.add("Откуда: " + (String) train.get("from"));
			allParams.add("Куда: " + (String) train.get("to"));
			allParams.add("Только с билетами: " + (String) train.get("onlyWithTickets").toString());
			allParams.add("Электрички: " + (String) train.get("localTrain").toString());
			allParams.add("Обратно: " + (String) train.get("return").toString());
			allParams.add("Прямой зарубежный поезд: " + (String) directForeignTrain);
			allParams.add("Обратный зарубежный поезд: " + (String) returnForeignTrain);
			
		} else if (testName.contentEquals("FillPassengerForms")) {
			JSONArray passengers = (JSONArray) train.get("passengers"); // Список параметров пассажиров.
			String tempParams = "";
			
			for (int i = 0; i < passengers.size(); i++) {
				JSONObject passendger = (JSONObject) passengers.get(i);
				
				tempParams += "'" + (String) passendger.get("lastName") + "', ";
				tempParams += "'" + (String) passendger.get("firstName") + "', ";
				tempParams += "'" + (String) passendger.get("middleName") + "', ";
				tempParams += "'" + (String) passendger.get("documentType") + "', ";
				tempParams += "'" + (String) passendger.get("documentNumber") + "', ";
				tempParams += "'" + (String) passendger.get("tariff") + "', ";
				tempParams += "'" + (String) passendger.get("citizenship") + "', ";
				tempParams += "'" + (String) passendger.get("gender") + "', ";
				tempParams += "'" + (String) passendger.get("dateOfBirth") + "', ";
				tempParams += "'" + (String) passendger.get("insurance") + "'";
				
				tempParams += ";";
			} 

			allParams.add(tempParams);
		} else if (testName.contentEquals("CheckTariffs")) {
			JSONArray passengers = (JSONArray) train.get("passengers"); // Список параметров пассажиров.
			String tempParams = "";
			
			for (int i = 0; i < passengers.size(); i++) {
				JSONObject passendger = (JSONObject) passengers.get(i);
				
				tempParams += "'" + (String) passendger.get("lastName") + "', ";
				tempParams += "'" + (String) passendger.get("firstName") + "', ";
				tempParams += "'" + (String) passendger.get("middleName") + "', ";
				tempParams += ";";
			} 

			allParams.add(tempParams);
		} else if (testName.contentEquals("PayElectronicCard")) {
			
		} else if (testName.contentEquals("SelectDirectTrainWithTransfer")) {
			direct = (JSONObject) directTrain.get(0);
			allParams.add((String) direct.get("trainNumber"));
			allParams.add((String) direct.get("carCheckers"));
			
			transfer = (JSONObject) directTrain.get(1);
			allParams.add((String) transfer.get("transferTrainNumber"));
			allParams.add((String) transfer.get("transferStation"));
			allParams.add((String) transfer.get("transferTimeFrom"));
			allParams.add((String) transfer.get("transferTimeTo"));
			
		} else if (testName.contentEquals("SelectWagonDirectTrainWithTransfer")) {
			direct = (JSONObject) directTrain.get(0);
			allParams.add((String) direct.get("wagonType"));
			allParams.add((String) direct.get("wagonCategory"));
			allParams.add((String) direct.get("servicesAndCapabilities"));
			
			transfer = (JSONObject) directTrain.get(1);
			allParams.add((String) transfer.get("transferWagonType"));
			allParams.add((String) transfer.get("transferWagonCategory"));
			allParams.add((String) transfer.get("transferServicesAndCapabilities"));
			
		} else if (testName.contentEquals("FillPlaceSigns")) {
			direct = (JSONObject) directTrain.get(0);
			
			temp = (JSONObject) direct.get("places");
			Object[] directPlaces = new Object[7];
			directPlaces[0] = temp.get("plGender");							// Гендерный признак
			directPlaces[1] = (boolean) temp.get("plKupe");					// В одном купе
			directPlaces[2] = (String) temp.get("range0");					// Границы мест
			directPlaces[3] = (String) temp.get("range1");
			directPlaces[4] = (boolean) temp.get("plBedding");				// Бельё
			directPlaces[5] = temp.get("plUpDown");							// Верхние / нижние
			directPlaces[6] = temp.get("plComp");							// Расположение мест
			allParams.add(directPlaces);
			
			transfer = (JSONObject) directTrain.get(1);
			Object[] returnPlaces = new Object[7];
			returnPlaces[0] = temp.get("plGender");							// Гендерный признак
			returnPlaces[1] = (boolean) temp.get("plKupe");					// В одном купе
			returnPlaces[2] = (String) temp.get("range0");					// Границы мест
			returnPlaces[3] = (String) temp.get("range1");
			returnPlaces[4] = (boolean) temp.get("plBedding");				// Бельё
			returnPlaces[5] = temp.get("plUpDown");							// Верхние / нижние
			returnPlaces[6] = temp.get("plComp");							// Расположение мест
			allParams.add(returnPlaces);
			
		} else if (testName.contentEquals("CancelRegistrationAndRefund")) {
			direct = (JSONObject) directTrain.get(0);
			allParams.add((String) direct.get("trainNumber"));
			
		} else if (testName.contentEquals("Results")) {
			direct = (JSONObject) directTrain.get(0);
			allParams.add((String) direct.get("wagonCategory"));
		}

		params = new Object[allParams.size()];
		params = allParams.toArray();
		
		paramsList = new Object[1][];
		paramsList[0] = params;
		return paramsList;
	}
	
	@DataProvider(name = "DirectTrainWithReturn")
	public static Object[][] getDirectTrainWithReturnData(ITestContext context, Method method) throws IOException, ParseException {
		Object paramsList[][];
		Object params[] = null;

		int trainNumber;
		String projPath;
		String filePath = "";
		String testName;
		
		JSONObject temp;
		
		String directForeignTrain;
		String returnForeignTrain;

		JSONArray trains;
		JSONObject train;
		JSONArray routeOptions;
		
		JSONArray directTrain;
		JSONArray returnTrain;
		
		JSONObject direct;
		JSONObject andReturn;
		
		ArrayList<Object> allParams = new ArrayList<Object>();
		
		projPath = System.getProperty("user.dir"); // Определяем домашнюю папку теста
		
		filePath = projPath + context.getCurrentXmlTest().getParameter("returnTrain_DataFile");
		
		// Work with json_simple
		FileReader fr = new FileReader(filePath);
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(fr);

		// Порядковый номер поезда берём из testng.xml
		trainNumber = Integer.parseInt(context.getCurrentXmlTest().getParameter("trainNumber").trim()) - 1;
		
		// Вытаскиваем данные о всех поездках из json файла и запихиваем их в двумерный массив
		trains = (JSONArray) jsonObject.get("trains");
		
		// Берем нужный поезд по порядковому номеру
		train = (JSONObject) trains.get(trainNumber);
		routeOptions = (JSONArray) train.get("routeOptions");
		
		temp = (JSONObject) routeOptions.get(0);
		directTrain = (JSONArray) temp.get("directTrain");
		
		temp = (JSONObject) routeOptions.get(1);
		returnTrain = (JSONArray) temp.get("returnTrain");
		
		directForeignTrain = (String) temp.get("foreignTrain").toString();
		
		temp = (JSONObject) routeOptions.get(1);
		returnForeignTrain = (String) temp.get("foreignTrain").toString();
		
		testName = method.getName();
		
		departure = (JSONArray) train.get("dayOfWeek");
		dayOfWeek = ((Long) departure.get(0)).intValue();
		weekOffset = ((Long) departure.get(1)).intValue();
		
		dateTo = Tools.getDepartureDate(dayOfWeek, weekOffset);
		dateFrom = Tools.getDepartureDate(dayOfWeek + 1, weekOffset);

		if (testName.contentEquals("FillStartForm")) {
			allParams.add("Дата туда: " + dateTo);
			allParams.add("Дата обратно: " + dateFrom);
			allParams.add("Откуда: " + (String) train.get("from"));
			allParams.add("Куда: " + (String) train.get("to"));
			allParams.add("Только с билетами: " + (String) train.get("onlyWithTickets").toString());
			allParams.add("Электрички: " + (String) train.get("localTrain").toString());
			allParams.add("Обратно: " + (String) train.get("return").toString());
			allParams.add("Прямой зарубежный поезд: " + (String) directForeignTrain);
			allParams.add("Обратный зарубежный поезд: " + (String) returnForeignTrain);
			
		} else if (testName.contentEquals("FillPassengerForms")) {
			JSONArray passengers = (JSONArray) train.get("passengers"); // Список параметров пассажиров.
			String tempParams = "";
			
			for (int i = 0; i < passengers.size(); i++) {
				JSONObject passendger = (JSONObject) passengers.get(i);
				
				tempParams += "'" + (String) passendger.get("lastName") + "', ";
				tempParams += "'" + (String) passendger.get("firstName") + "', ";
				tempParams += "'" + (String) passendger.get("middleName") + "', ";
				tempParams += "'" + (String) passendger.get("documentType") + "', ";
				tempParams += "'" + (String) passendger.get("documentNumber") + "', ";
				tempParams += "'" + (String) passendger.get("tariff") + "', ";
				tempParams += "'" + (String) passendger.get("citizenship") + "', ";
				tempParams += "'" + (String) passendger.get("gender") + "', ";
				tempParams += "'" + (String) passendger.get("dateOfBirth") + "', ";
				tempParams += "'" + (String) passendger.get("insurance") + "'";
				
				tempParams += ";";
			} 

			allParams.add(tempParams);
		} else if (testName.contentEquals("CheckTariffs")) {
			JSONArray passengers = (JSONArray) train.get("passengers"); // Список параметров пассажиров.
			String tempParams = "";
			
			for (int i = 0; i < passengers.size(); i++) {
				JSONObject passendger = (JSONObject) passengers.get(i);
				
				tempParams += "'" + (String) passendger.get("lastName") + "', ";
				tempParams += "'" + (String) passendger.get("firstName") + "', ";
				tempParams += "'" + (String) passendger.get("middleName") + "', ";
				tempParams += ";";
			} 

			allParams.add(tempParams);
		} else if (testName.contentEquals("PayElectronicCard")) {
			
		} else if (testName.contentEquals("SelectDirectTrainWithReturn")) {
			direct = (JSONObject) directTrain.get(0);
			allParams.add((String) direct.get("trainNumber"));
			allParams.add((String) direct.get("carCheckers"));
			
			andReturn = (JSONObject) returnTrain.get(0);
			allParams.add((String) andReturn.get("trainNumber"));
			allParams.add((String) andReturn.get("carCheckers"));
			
		} else if (testName.contentEquals("SelectWagonDirectTrainWithReturn")) {
			direct = (JSONObject) directTrain.get(0);
			allParams.add((String) direct.get("wagonType"));
			allParams.add((String) direct.get("wagonCategory"));
			allParams.add((String) direct.get("servicesAndCapabilities"));
			
			andReturn = (JSONObject) returnTrain.get(0);
			allParams.add((String) andReturn.get("wagonType"));
			allParams.add((String) andReturn.get("wagonCategory"));
			allParams.add((String) andReturn.get("servicesAndCapabilities"));
			
		} else if (testName.contentEquals("FillPlaceSigns")) {
			direct = (JSONObject) directTrain.get(0);
			
			temp = (JSONObject) direct.get("places");
			Object[] directPlaces = new Object[7];
			directPlaces[0] = temp.get("plGender");							// Гендерный признак
			directPlaces[1] = (boolean) temp.get("plKupe");					// В одном купе
			directPlaces[2] = (String) temp.get("range0");					// Границы мест
			directPlaces[3] = (String) temp.get("range1");
			directPlaces[4] = (boolean) temp.get("plBedding");				// Бельё
			directPlaces[5] = temp.get("plUpDown");							// Верхние / нижние
			directPlaces[6] = temp.get("plComp");							// Расположение мест
			allParams.add(directPlaces);
			
			andReturn = (JSONObject) returnTrain.get(0);
			Object[] returnPlaces = new Object[7];
			returnPlaces[0] = temp.get("plGender");							// Гендерный признак
			returnPlaces[1] = (boolean) temp.get("plKupe");					// В одном купе
			returnPlaces[2] = (String) temp.get("range0");					// Границы мест
			returnPlaces[3] = (String) temp.get("range1"); 
			returnPlaces[4] = (boolean) temp.get("plBedding");				// Бельё
			returnPlaces[5] = temp.get("plUpDown");							// Верхние / нижние
			returnPlaces[6] = temp.get("plComp");							// Расположение мест
			allParams.add(returnPlaces);
			
		} else if (testName.contentEquals("CancelRegistrationAndRefund")) {
			direct = (JSONObject) directTrain.get(0);
			allParams.add((String) direct.get("trainNumber"));
			
		} else if (testName.contentEquals("Results")) {
			direct = (JSONObject) directTrain.get(0);
			allParams.add((String) direct.get("wagonCategory"));
		}

		params = new Object[allParams.size()];
		params = allParams.toArray();
		
		paramsList = new Object[1][];
		paramsList[0] = params;
		return paramsList;
	}
	
	@DataProvider(name = "ReturnTrainWithTransfer")
	public static Object[][] getReturnTrainWithTransferData(ITestContext context, Method method) throws IOException, ParseException {
		Object paramsList[][];
		Object params[] = null;

		int trainNumber;
		String projPath;
		String filePath = "";
		String testName;
		
		String directForeignTrain;
		String returnForeignTrain;
		
		JSONObject temp;

		JSONArray trains;
		JSONObject train;
		JSONArray routeOptions;
		
		JSONArray directTrain;
		JSONArray returnTrain;
		
		JSONObject direct;
		JSONObject directTransfer;
		
		JSONObject andReturn;
		JSONObject andReturnTransfer;
		
		ArrayList<Object> allParams = new ArrayList<Object>();
		
		projPath = System.getProperty("user.dir"); // Определяем домашнюю папку теста
		
		filePath = projPath + context.getCurrentXmlTest().getParameter("returnTrainWithTransfer_DataFile");
		
		// Work with json_simple
		FileReader fr = new FileReader(filePath);
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(fr);

		// Порядковый номер поезда берём из testng.xml
		trainNumber = Integer.parseInt(context.getCurrentXmlTest().getParameter("trainNumber").trim()) - 1;
		
		// Вытаскиваем данные о всех поездках из json файла и запихиваем их в двумерный массив
		trains = (JSONArray) jsonObject.get("trains");
		
		// Берем нужный поезд по порядковому номеру
		train = (JSONObject) trains.get(trainNumber);
		routeOptions = (JSONArray) train.get("routeOptions");
		
		temp = (JSONObject) routeOptions.get(0);
		directTrain = (JSONArray) temp.get("directTrain");
		
		directForeignTrain = (String) temp.get("foreignTrain").toString();
		
		temp = (JSONObject) routeOptions.get(1);
		returnForeignTrain = (String) temp.get("foreignTrain").toString();
		
		temp = (JSONObject) routeOptions.get(1);
		returnTrain = (JSONArray) temp.get("returnTrain");
		
		testName = method.getName();

		departure = (JSONArray) train.get("dayOfWeek");
		dayOfWeek = ((Long) departure.get(0)).intValue();
		weekOffset = ((Long) departure.get(1)).intValue();
		
		dateTo = Tools.getDepartureDate(dayOfWeek, weekOffset);
		dateFrom = Tools.getDepartureDate(dayOfWeek + 1, weekOffset);

		if (testName.contentEquals("FillStartForm")) {
			allParams.add("Дата туда: " + dateTo);
			allParams.add("Дата обратно: " + dateFrom);
			allParams.add("Откуда: " + (String) train.get("from"));
			allParams.add("Куда: " + (String) train.get("to"));
			allParams.add("Только с билетами: " + (String) train.get("onlyWithTickets").toString());
			allParams.add("Электрички: " + (String) train.get("localTrain").toString());
			allParams.add("Обратно: " + (String) train.get("return").toString());
			allParams.add("Прямой зарубежный поезд: " + (String) directForeignTrain);
			allParams.add("Обратный зарубежный поезд: " + (String) returnForeignTrain);
			
		} else if (testName.contentEquals("FillPassengerForms")) {
			JSONArray passengers = (JSONArray) train.get("passengers"); // Список параметров пассажиров.
			String tempParams = "";
			
			for (int i = 0; i < passengers.size(); i++) {
				JSONObject passendger = (JSONObject) passengers.get(i);
				
				tempParams += "'" + (String) passendger.get("lastName") + "', ";
				tempParams += "'" + (String) passendger.get("firstName") + "', ";
				tempParams += "'" + (String) passendger.get("middleName") + "', ";
				tempParams += "'" + (String) passendger.get("documentType") + "', ";
				tempParams += "'" + (String) passendger.get("documentNumber") + "', ";
				tempParams += "'" + (String) passendger.get("tariff") + "', ";
				tempParams += "'" + (String) passendger.get("citizenship") + "', ";
				tempParams += "'" + (String) passendger.get("gender") + "', ";
				tempParams += "'" + (String) passendger.get("dateOfBirth") + "', ";
				tempParams += "'" + (String) passendger.get("insurance") + "'";
				
				tempParams += ";";
			} 

			allParams.add(tempParams);
		} else if (testName.contentEquals("CheckTariffs")) {
			JSONArray passengers = (JSONArray) train.get("passengers"); // Список параметров пассажиров.
			String tempParams = "";
			
			for (int i = 0; i < passengers.size(); i++) {
				JSONObject passendger = (JSONObject) passengers.get(i);
				
				tempParams += "'" + (String) passendger.get("lastName") + "', ";
				tempParams += "'" + (String) passendger.get("firstName") + "', ";
				tempParams += "'" + (String) passendger.get("middleName") + "', ";
				tempParams += ";";
			} 

			allParams.add(tempParams);
		} else if (testName.contentEquals("PayElectronicCard")) {
			
		} else if (testName.contentEquals("SelectReturnTrainWithTransfer")) {
			direct = (JSONObject) directTrain.get(0);
			String[] out = new String[2];
			out[0] = (String) direct.get("trainNumber");
			out[1] = (String) direct.get("carCheckers");
			allParams.add(out);
			
			directTransfer = (JSONObject) directTrain.get(1);
			out = new String[4];
			out[0] = (String) directTransfer.get("transferTrainNumber");
			out[1] = (String) directTransfer.get("transferStation");
			out[2] = (String) directTransfer.get("transferTimeFrom");
			out[3] = (String) directTransfer.get("transferTimeTo");
			allParams.add(out);
			
			andReturn = (JSONObject) returnTrain.get(0);
			out = new String[2];
			out[0] = (String) andReturn.get("trainNumber");
			out[1] = (String) andReturn.get("carCheckers");
			allParams.add(out);
			
			andReturnTransfer = (JSONObject) returnTrain.get(1);
			out = new String[4];
			out[0] = (String) andReturnTransfer.get("transferTrainNumber");
			out[1] = (String) andReturnTransfer.get("transferStation");
			out[2] = (String) andReturnTransfer.get("transferTimeFrom");
			out[3] = (String) andReturnTransfer.get("transferTimeTo");
			allParams.add(out);
			
		} else if (testName.contentEquals("SelectWagonReturnTrainWithTransfer")) {
			String[] out = new String[3];
			direct = (JSONObject) directTrain.get(0);
			out[0] = (String) direct.get("wagonType");
			out[1] = (String) direct.get("wagonCategory");
			out[2] = (String) direct.get("servicesAndCapabilities");
			allParams.add(out);
			
			directTransfer = (JSONObject) directTrain.get(1);
			out = new String[3];
			out[0] = (String) directTransfer.get("transferWagonType");
			out[1] = (String) directTransfer.get("transferWagonCategory");
			out[2] = (String) directTransfer.get("transferServicesAndCapabilities");
			allParams.add(out);
			
			andReturn = (JSONObject) returnTrain.get(0);
			out = new String[3];
			out[0] = (String) andReturn.get("wagonType");
			out[1] = (String) andReturn.get("wagonCategory");
			out[2] = (String) andReturn.get("servicesAndCapabilities");
			allParams.add(out);
			
			andReturnTransfer = (JSONObject) returnTrain.get(1);
			out = new String[3];
			out[0] = (String) andReturnTransfer.get("transferWagonType");
			out[1] = (String) andReturnTransfer.get("transferWagonCategory");
			out[2] = (String) andReturnTransfer.get("transferServicesAndCapabilities");
			allParams.add(out);
			
		} else if (testName.contentEquals("FillPlaceSigns")) {
			direct = (JSONObject) directTrain.get(0);
			temp = (JSONObject) direct.get("places");
			Object[] places = new Object[7];
			places[0] = (String) temp.get("plGender");					// Гендерный признак
			places[1] = (boolean) temp.get("plKupe");					// В одном купе
			places[2] = (String) temp.get("range0");					// Границы мест
			places[3] = (String) temp.get("range1");
			places[4] = (boolean) temp.get("plBedding");				// Бельё
			places[5] = (String) temp.get("plUpDown");					// Верхние / нижние
			places[6] = (String) temp.get("plComp");					// Расположение мест
			allParams.add(places);
			
			directTransfer = (JSONObject) directTrain.get(1);
			temp = (JSONObject) directTransfer.get("places");
			places = new Object[7];
			places[0] = (String) temp.get("plGender");					// Гендерный признак
			places[1] = (boolean) temp.get("plKupe");					// В одном купе
			places[2] = (String) temp.get("range0");					// Границы мест
			places[3] = (String) temp.get("range1");
			places[4] = (boolean) temp.get("plBedding");				// Бельё
			places[5] = (String) temp.get("plUpDown");					// Верхние / нижние
			places[6] = (String) temp.get("plComp");					// Расположение мест
			allParams.add(places);
			
			andReturn = (JSONObject) returnTrain.get(0);
			temp = (JSONObject) andReturn.get("places");
			places = new Object[7];
			places[0] = (String) temp.get("plGender");					// Гендерный признак
			places[1] = (boolean) temp.get("plKupe");					// В одном купе
			places[2] = (String) temp.get("range0");					// Границы мест
			places[3] = (String) temp.get("range1");
			places[4] = (boolean) temp.get("plBedding");				// Бельё
			places[5] = (String) temp.get("plUpDown");					// Верхние / нижние
			places[6] = (String) temp.get("plComp");					// Расположение мест
			allParams.add(places);
			
			andReturnTransfer = (JSONObject) returnTrain.get(1);
			temp = (JSONObject) andReturnTransfer.get("places");
			places = new Object[7];
			places[0] = (String) temp.get("plGender");					// Гендерный признак
			places[1] = (boolean) temp.get("plKupe");					// В одном купе
			places[2] = (String) temp.get("range0");					// Границы мест
			places[3] = (String) temp.get("range1");
			places[4] = (boolean) temp.get("plBedding");				// Бельё
			places[5] = (String) temp.get("plUpDown");					// Верхние / нижние
			places[6] = (String) temp.get("plComp");					// Расположение мест
			allParams.add(places);
			
		} else if (testName.contentEquals("CancelRegistrationAndRefund")) {
			direct = (JSONObject) directTrain.get(0);
			allParams.add((String) direct.get("trainNumber"));
			
		} else if (testName.contentEquals("Results")) {
			direct = (JSONObject) directTrain.get(0);
			allParams.add((String) direct.get("wagonCategory"));
		}

		params = new Object[allParams.size()];
		params = allParams.toArray();
		
		paramsList = new Object[1][];
		paramsList[0] = params;
		return paramsList;
	}
}
