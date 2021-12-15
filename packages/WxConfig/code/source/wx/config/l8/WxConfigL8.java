package wx.config.l8;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.softwareag.util.IDataMap;
import com.wm.data.IData;

import wx.config.l8.PropertyRegistry.PkgProperties;
import wx.config.l8.log.ILogger;
import wx.config.l8.log.StreamLogger;
import wx.config.l8.log.ILogger.Level;
import wx.config.l8.log.NullLogger;
import wx.config.l8.util.Exceptions;
import wx.config.l8.util.LockableObject;
import wx.config.l8.util.Mutable.MString;


public class WxConfigL8 {
	private static WxConfigL8 instance = new WxConfigL8();

	public static WxConfigL8 getInstance() {
		synchronized (WxConfigL8.class) {
			return instance;
		}
	}

	public static void setInstance(WxConfigL8 pInstance) {
		synchronized (WxConfigL8.class) {
			instance = pInstance;
		}
	}

	private final PropertyRegistry propertyRegistry = new PropertyRegistry();
	private final LockableObject<L8Configuration> config;
	private final LockableObject<ILogger> logger;

	public WxConfigL8() {
		final L8Configuration cfg = getConfiguration();
		config = new LockableObject<L8Configuration>(cfg);
		final Path logFile = cfg.getLogFile();
		final ILogger lg = asLogger(Level.info, logFile);
		System.out.println("WxConfigLight: Logger initialized at " + DateTimeFormatter.ISO_DATE_TIME.format(ZonedDateTime.now()));
		lg.info("Logging starts at " + DateTimeFormatter.ISO_DATE_TIME.format(ZonedDateTime.now()));
		logger = new LockableObject<ILogger>(lg);
	}

	protected void log(String pMsg, Object... pParams) {
		getLogger().debug(pMsg, pParams);
	}

	public ILogger getLogger() {
		return logger.get((o) -> o);
	}
	
	public void clear() {
		propertyRegistry.clear();
	}
	
	protected void saveConfiguration() {
		final Properties props = config.call((cfg) -> {
			return getConfigurationProperties(cfg);
		});
		saveConfiguration(props);
	}

	protected void saveConfiguration(final Properties pProperties) {
		final Path file = getSavedPropertiesPath();
		final Path dir = file.getParent();
		try {
			if (dir != null) {
				Files.createDirectories(dir);
			}
			try (OutputStream out = Files.newOutputStream(file)) {
				pProperties.store(out, null);
			}
		} catch (IOException e) {
			throw Exceptions.show(e);
		}
	}

	protected L8Configuration asConfiguration(Properties pProperties) {
		final L8Configuration cfg = new L8Configuration();
		cfg.setEnvironment(pProperties.getProperty("environment"));
		final List<String> propertyFiles = new ArrayList<String>();
		boolean haveDefaultPropertyFile = false;
		final String defaultPropertyFile = getDefaultConfigFile();
		for (int i = 0;   ;  i++) {
			final String key = "propertyFile." + i;
			final String pf = pProperties.getProperty(key);
			if (pf == null  ||  pf.length() == 0) {
				break;
			}
			if (pf.equals(defaultPropertyFile)) {
				haveDefaultPropertyFile = true;
			}
			propertyFiles.add(pf);
		}
		if (!haveDefaultPropertyFile) {
			propertyFiles.add(defaultPropertyFile);
		}
		final String logFileStr = pProperties.getProperty("logFile");
		if (logFileStr != null  &&  logFileStr.length() > 0) {
			cfg.setLogFile(Paths.get(logFileStr));
		}
		cfg.setPropertyFiles(propertyFiles);
		return cfg;
	}

	protected Properties getConfigurationProperties(L8Configuration cfg) {
		final Properties props = new Properties();
		props.put("logFile", cfg.getLogFile().toString());
		final List<String> propertyFiles = cfg.getPropertyFiles();
		for (int i = 0;  i < propertyFiles.size();  i++) {
			props.put("propertyFile." + i, propertyFiles.get(i));
		}
		if (cfg.getEnvironment() != null) {
			props.put("environment", cfg.getEnvironment());
		}
		return props;
	}

	protected Path getBaseDir() {
		return Paths.get(".");
	}

	protected L8Configuration getConfiguration() {
		System.out.println("getConfiguration: ->");
		final Path configuration;
		final Path factoryConfiguration = getFactoryProperties();
		final Path savedConfiguration = getSavedPropertiesPath();
		if (Files.isRegularFile(savedConfiguration)) {
			configuration = savedConfiguration;
		} else if (Files.isRegularFile(factoryConfiguration)) {
			configuration = factoryConfiguration;
		} else {
			throw new IllegalStateException("Factory configuration file not found: " + factoryConfiguration);
		}
		final Properties props = new Properties();
		try (InputStream in = Files.newInputStream(configuration)) {
			props.load(in);
		} catch (IOException e) {
			throw Exceptions.show(e);
		}
		System.out.println("getConfiguration: <- " + props);
		return asConfiguration(props);
	}

	protected L8Configuration getDefaultConfiguration() {
		final L8Configuration cfg = new L8Configuration();
		cfg.setEnvironment(null);
		cfg.setLogFile(Paths.get("./logs/WxConfigL8.log"));
		cfg.setPropertyFiles(Arrays.asList(getDefaultConfigFile()));
		return cfg;
	}

	public IData getConfigurationAsIData() {
		return config.call((cfg) -> {
			final IDataMap map = new IDataMap();
			final String env = cfg.getEnvironment();
			if (env == null) {
				map.put("environment", "");
			} else {
				map.put("environment", env);
			}
			final Path logFile = cfg.getLogFile();
			if (logFile == null) {
				map.put("logFile", "");
			} else {
				map.put("logFile", logFile.toString());
			}
			final List<String> configFiles = cfg.getPropertyFiles();
			if (configFiles == null) {
				map.put("configFiles", new String[0]);
			} else {
				map.put("configFiles", configFiles.toArray(new String[configFiles.size()]));
			}
			return map.getIData();
		});
	}
	protected String getDefaultConfigFile() {
		return "packages/[PKG_NAME]/wxconfig[ENV].cnf";
	}

	protected Path getSavedPropertiesPath() {
		return Paths.get("config/packages/WxConfigL8.properties");
	}

	protected Path getFactoryProperties() {
		return Paths.get("packages/WxConfig/config/WxConfigL8.properties");
	}

	public PropertyRegistry getPropertyRegistry() {
		return propertyRegistry;
	}

	public List<String> getConfigFileInfo(String pPackageName) {
		System.out.println("getConfigFileInfo: -> " + pPackageName);
		final List<String> files = new ArrayList<String>();
		final MString menv = new MString();
		final List<String> configFiles = config.call((cfg) -> {
			final List<String> list = new ArrayList<>();
			list.addAll(cfg.getPropertyFiles());
			menv.set(cfg.getEnvironment());
			return list;
		});
		final String env = menv.get();
		System.out.println("getConfigFileInfo: env=" + env + ", configFiles=" + String.join(", ", configFiles));
		for (String s : configFiles) {
			final String cf = s.replace("[PKG_NAME]", pPackageName);
			if (cf.contains("[ENV]")) {
				if (env != null  &&  env.length() > 0) {
					files.add(cf.replace("[ENV]", "-" + env));
				}
				files.add(cf.replace("[ENV]", ""));
			} else {
				files.add(cf);
			}
		}
		System.out.println("getConfigFileInfo: <- " + String.join(", ", files));
		return files;
	}

	protected PkgProperties findProperties(String pPackageName) {
		final List<String> configFiles = config.call((cfg) -> {
			final List<String> list = new ArrayList<>();
			list.addAll(cfg.getPropertyFiles());
			return list;
		});
		final String env = config.call((cfg) -> {
			return cfg.getEnvironment();
		});
		if (env == null) {
			final Properties props = findProperties(configFiles, "[PKG_NAME]", pPackageName, "[ENV]", "");
			return new PkgProperties(props);
		} else {
			Properties props = findProperties(configFiles, "[PKG_NAME]", pPackageName, "[ENV]",
					                          "-" + env);
			if (props == null) {
				props = findProperties(configFiles, "[PKG_NAME]", pPackageName, "[ENV]", "");
			}
			return new PkgProperties(props);
		}
	}

	Properties findProperties(List<String> pCandidates, String... pInterpolations) {
		Properties props = null;
		for (String candidate : pCandidates) {
			String file = candidate;
			if (pInterpolations != null) {
				for (int i = 0;  i < pInterpolations.length;  i += 2) {
					final String var = pInterpolations[i];
					final String value = pInterpolations[i+1];
					file = file.replace(var, value);
				}
			}
			final Path p1 = Paths.get(file + ".xml");
			final Path p2 = Paths.get(file);
			final Properties pr;
			if (Files.isRegularFile(p1)) {
				pr = new Properties();
				try (InputStream in = Files.newInputStream(p1)) {
					pr.loadFromXML(in);
				} catch (IOException e) {
					throw Exceptions.show(e);
				}
			} else if (Files.isRegularFile(p2)) {
				pr = new Properties();
				try (InputStream in = Files.newInputStream(p2)) {
					pr.load(in);
				} catch (IOException e) {
					throw Exceptions.show(e);
				}
			} else {
				pr = null;
			}
			if (pr != null) {
				if (props == null) {
					props = new Properties();
				}
				props.putAll(pr);
			}
		}
		return props;
	}

	protected ILogger asLogger(Level pLevel, Path pPath) {
		if (pPath == null) {
			return new NullLogger();
		} else {
			try {
				final Path dir = pPath.getParent();
				if (dir != null) {
					Files.createDirectories(dir);
				}
				final OutputStream out = Files.newOutputStream(pPath);
				final PrintStream ps = new PrintStream(out);
				return new StreamLogger(pLevel, ps);
			} catch (IOException e) {
				throw Exceptions.show(e);
			}
		}
	}

	public void setLogFile(Level pLevel, Path pPath) {
		final ILogger oldLogger = logger.get((o) -> o);
		final ILogger newLogger = asLogger(pLevel, pPath);
		try {
			oldLogger.close();
		} catch (IOException e) {
			throw Exceptions.show(e);
		}
		logger.replace(newLogger);
		clear();
	}

	public void setEnvironment(String pEnvironment) {
		config.run((cfg) -> cfg.setEnvironment(pEnvironment));
		clear();
	}

	public void setConfigFiles(List<String> pConfigFiles) {
		final List<String> list = new ArrayList<>();
		list.addAll(pConfigFiles);
		final String defaultConfigFile = getDefaultConfigFile();
		if (!list.contains(defaultConfigFile)) {
			list.add(defaultConfigFile);
		}
		config.run((cfg) -> cfg.setPropertyFiles(list));
		clear();
	}

	public void addConfigFile(String pConfigFile) {
		config.run((cfg) -> {
			final List<String> list = new ArrayList<>();
			list.add(pConfigFile);
			list.addAll(cfg.getPropertyFiles());
			cfg.setPropertyFiles(list);
		});
		clear();
	}

	public PkgProperties getPkgProperties(String pPackageName) {
		PkgProperties pkgProperties = propertyRegistry.getPkgProperties(pPackageName);
		if (pkgProperties == null) {
			pkgProperties = findProperties(pPackageName);
			propertyRegistry.setPkgProperties(pPackageName, pkgProperties);
		}
		return pkgProperties;
	}

	public void refreshPkgProperties(String pPackageName) {
		PkgProperties pkgProperties = findProperties(pPackageName);
		propertyRegistry.setPkgProperties(pPackageName, pkgProperties);
	}
}
