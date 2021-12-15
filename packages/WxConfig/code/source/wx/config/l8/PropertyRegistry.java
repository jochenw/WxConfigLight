package wx.config.l8;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import wx.config.l8.util.LockableObject;

public class PropertyRegistry {
	public static final class PkgProperties {
		private final Properties props;

		public PkgProperties(Properties pProperties) {
			props = pProperties;
		}

		public Properties getProperties() {
			return props;
		}
	}

	private final Map<String, PkgProperties> propertyMap = new HashMap<>();
	private final LockableObject<Map<String,PkgProperties>> properties = new LockableObject<Map<String,PkgProperties>>(propertyMap);

	public void clear() {
		properties.runExclusive((pr) -> {
			pr.clear();
		});
	}

	public PkgProperties getPkgProperties(String pPackageName) {
		return properties.call((p) -> p.get(pPackageName));
	}

	public void setPkgProperties(String pPackageName, PkgProperties pPkgProperties) {
		properties.runExclusive((p) -> p.put(pPackageName, pPkgProperties));
	}

	public boolean isInitialized(String pPackageName) {
		return properties.call((p) -> p.get(pPackageName) != null);
	}
}
