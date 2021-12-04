package wx.config.l8;

import java.util.Properties;

public class PackageProperties {
	private final Properties props;

	public PackageProperties(Properties pProps) {
		if (pProps == null) {
			props = new Properties();
		} else {
			props = pProps;
		}
	}

	public String getProperty(String pKey) {
		return props.getProperty(pKey);
	}
}
