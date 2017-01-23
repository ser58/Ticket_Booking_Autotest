package ru.inex.autotestconfig.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("config")
public interface ConfigService extends RemoteService {
	String getTestngFile(String projectRoot);

	String getSuiteXML(String projectRoot);

}
