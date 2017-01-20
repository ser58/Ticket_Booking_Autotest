package test.ru.oooinex.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.codeborne.selenide.Selenide;

public class CustomTest implements ITestListener{
	static String screenshotURL;
	
    @Override
    public void onFinish(ITestContext context) {
        
    }
   
	@Override
	public void onStart(ITestContext arg0) {
		screenshotURL = arg0.getCurrentXmlTest().getParameter("reportsUrl");
	}

	@Override
	public void onTestFailure(ITestResult arg0) {
		String screenshot = takeScreenshotPath(arg0.getThrowable().toString());
	    
		if (!screenshot.isEmpty()) {
			Reporter.log("<br><a href='" + screenshotURL + screenshot + "' target='_blank'><img src='" + screenshotURL + "/images/foto.png' width='30' height='30' alt='Screenshot'></a><br>");
		}
		
		/*StackTraceElement[] stackTrace = arg0.getThrowable().getStackTrace();
		String stackTraceHider = "<input class='hide' id='stacktrace' type='checkbox' >" +
								 "<label for='stacktrace'>Жмём сюда, чтобы увидеть Stacktrace!</label>" +
								 "<div>" + getStackTrace(stackTrace) + "</div><br>";
		
		Reporter.log(stackTraceHider);
		
		// Удаляем нахрен стектрейс
		arg0.getThrowable().setStackTrace(new StackTraceElement[0]);*/ // Не получилось
		
		if (arg0.getName().contains("CheckOrderFormsWithoutRefund")) 
			Selenide.navigator.back();
	}

	@Override
	public void onTestStart(ITestResult arg0) {
		System.out.println(arg0.getName());
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestSkipped(ITestResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestSuccess(ITestResult arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private String takeScreenshotPath(String buff) {
		String ret = "";
		
		if (buff.contains("Screenshot")) {
			String[] temp = buff.split("\n");
			for (String str: temp) {
				if (str.contains("Screenshot")) {
					String[] ru = str.split(" ");
					ret = ru[1].substring(ru[1].lastIndexOf("/"));
					break;
				}
			}
		}
		
		return ret;
	}
	
	/*private String getStackTrace(StackTraceElement[] stackTrace) {
		String out = "";
		
		for (int i = 0; i < stackTrace.length; i++) 
			out += stackTrace[i].toString() + "\n";
				
		return out;
	}*/
 }