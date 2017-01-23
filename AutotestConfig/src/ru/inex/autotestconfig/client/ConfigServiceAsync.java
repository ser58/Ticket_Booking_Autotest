package ru.inex.autotestconfig.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface ConfigServiceAsync {
	void getTestngFile(String projectRoot, AsyncCallback<String> callback);

	void getSuiteXML(String projectRoot, AsyncCallback<String> asyncCallback);

}
