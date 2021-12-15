package wx.config.l8;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import wx.config.l8.log.ILogger.Level;

public class L8Configuration {
	private List<String> propertyFiles = new ArrayList<>();
	private Path logFile;
	private String environment;
	private Level logLevel;

	public List<String> getPropertyFiles() {
		return propertyFiles;
	}
	public void setPropertyFiles(List<String> propertyFiles) {
		this.propertyFiles = propertyFiles;
	}
	public Path getLogFile() {
		return logFile;
	}
	public void setLogFile(Path logFile) {
		this.logFile = logFile;
	}
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	public void setLogLevel(Level pLogLevel) {
		logLevel = pLogLevel;
	}
	public Level getLogLevel() {
		return logLevel;
	}
}
