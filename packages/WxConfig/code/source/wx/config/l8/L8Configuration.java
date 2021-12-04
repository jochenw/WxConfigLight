package wx.config.l8;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class L8Configuration {
	private List<String> propertyFiles = new ArrayList<>();
	private Path logFile;
	private String environment;

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
}
