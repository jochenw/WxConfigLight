package wx.config.l8;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import wx.config.l8.WxConfigL8;
import wx.config.l8.PropertyRegistry.PkgProperties;
import wx.config.l8.log.ILogger;
import wx.config.l8.log.ILogger.Level;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import com.softwareag.util.IDataMap;
// --- <<IS-END-IMPORTS>> ---

public final class admin

{
	// ---( internal utility methods )---

	final static admin _instance = new admin();

	static admin _newInstance() { return new admin(); }

	static admin _cast(Object o) { return (admin)o; }

	// ---( server methods )---




	public static final void getConfiguration (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getConfiguration)>> ---
		// @sigtype java 3.5
		// [o] recref:0:required configuration wx.config.l8.doc:L8Configuration
		final IDataMap map = new IDataMap(pipeline);
		map.put("configuration", WxConfigL8.getInstance().getConfigurationAsIData());
			
		// --- <<IS-END>> ---

                
	}



	public static final void init (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(init)>> ---
		// @sigtype java 3.5
		// [o] recref:0:required configuration wx.config.l8.doc:L8Configuration
		System.out.println("init: ->");
		final IDataMap map = new IDataMap(pipeline);
		final WxConfigL8 wxConfigL8 = new WxConfigL8();
		wxConfigL8.clear();
		WxConfigL8.setInstance(wxConfigL8);
		final IData configData = wxConfigL8.getConfigurationAsIData();
		map.put("configuration", configData);
		System.out.println("init: <-");
			
		// --- <<IS-END>> ---

                
	}



	public static final void setConfigFiles (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(setConfigFiles)>> ---
		// @sigtype java 3.5
		// [i] field:1:required configFiles
		// [o] recref:0:required configuration wx.config.l8.doc:L8Configuration
		final IDataMap map = new IDataMap(pipeline);
		final String[] configFiles = map.getAsStringArray("configFiles");
		if (configFiles == null) {
			throw new NullPointerException("Missing parameter: configFiles");
		}
		for (int i = 0; i < configFiles.length;  i++) {
			if (configFiles[i] == null) {
				throw new NullPointerException("The element configFiles[" + i + "] is null.");
			} else if (configFiles[i].length() == 0) {
				throw new IllegalArgumentException("The element configFiles[" + i + "] is empty.");
			}
		}
		WxConfigL8 wxConfigL8 = WxConfigL8.getInstance();
		final ILogger logger = wxConfigL8.getLogger();
		if (configFiles.length == 0) {
			logger.info("Clearing list of config files.");
		} else {
			for (int i = 0;  i < configFiles.length;  i++) {
				logger.info("Setting config file {} to {}", String.valueOf(i), configFiles[i]);
			}
		}
		wxConfigL8.setConfigFiles(Arrays.asList(configFiles));
		map.put("configuration", wxConfigL8.getConfigurationAsIData());
			
		// --- <<IS-END>> ---

                
	}



	public static final void setEnvironment (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(setEnvironment)>> ---
		// @sigtype java 3.5
		// [i] field:0:required environment
		// [o] recref:0:required configuration wx.config.l8.doc:L8Configuration
		final IDataMap map = new IDataMap(pipeline);
		final String environment = map.getAsString("environment");
		final WxConfigL8 wxConfigL8 = WxConfigL8.getInstance();
		final ILogger logger = wxConfigL8.getLogger();
		logger.info("Setting environment name to '{}'", environment);
		wxConfigL8.setEnvironment(environment);
		map.put("configuration", wxConfigL8.getConfigurationAsIData());
		// --- <<IS-END>> ---

                
	}



	public static final void setLogFile (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(setLogFile)>> ---
		// @sigtype java 3.5
		// [i] field:0:required logFile
		// [i] field:0:required logLevel {"trace","debug","info","warn","error","fatal"}
		// [o] recref:0:required configuration wx.config.l8.doc:L8Configuration
		final IDataMap map = new IDataMap(pipeline);
		final String logLevelStr = map.getAsString("logLevel");
		if (logLevelStr == null) {
			throw new NullPointerException("Missing parameter: logLevel");
		}
		if (logLevelStr.length() == 0) {
			throw new IllegalArgumentException("Empty parameter: logLevel");
		}
		final Level level;
		try {
			level = Level.valueOf(logLevelStr);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid value for parameter logLevel:"
					+ " Expected trace|debug|info|warn|error|fatal, got " + logLevelStr);
		}
		final String logFileStr = map.getAsString("logFile");
		final Path logFile;
		if (logFileStr == null  ||  logFileStr.length() == 0) {
			logFile = null;
		} else {
			logFile = Paths.get(logFileStr);
		}
		WxConfigL8 wxConfigL8 = WxConfigL8.getInstance();
		final ILogger logger = wxConfigL8.getLogger();
		logger.info("Setting log level to {}, and log file to {}", level, logFile);
		wxConfigL8.setLogFile(level, logFile);
		map.put("configuration", wxConfigL8.getConfigurationAsIData());
			
		// --- <<IS-END>> ---

                
	}
}

