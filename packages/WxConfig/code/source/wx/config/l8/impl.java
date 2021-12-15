package wx.config.l8;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.b2b.server.ns.Namespace;
import com.wm.lang.ns.NSPackage;
import wx.config.l8.PropertyRegistry.PkgProperties;
import wx.config.l8.WxConfigL8;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import com.softwareag.util.IDataMap;
// --- <<IS-END-IMPORTS>> ---

public final class impl

{
	// ---( internal utility methods )---

	final static impl _instance = new impl();

	static impl _newInstance() { return new impl(); }

	static impl _cast(Object o) { return (impl)o; }

	// ---( server methods )---




	public static final void editFile (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(editFile)>> ---
		// @sigtype java 3.5
		// [i] field:0:required packageName
		// [i] field:0:required file
		// [i] field:0:required fileContent
		// [o] field:0:required fileContent
		final IDataMap map = new IDataMap(pipeline);
		final String packageName = map.getAsString("packageName");
		if (packageName == null) {
			throw new NullPointerException("Missing parameter: packageName");
		}
		if (packageName.length() == 0) {
			throw new IllegalArgumentException("Empty parameter: packageName");
		}
		final String fileStr = map.getAsString("file");
		if (fileStr == null) {
			throw new NullPointerException("Missing parameter: file");
		}
		if (fileStr.length() == 0) {
			throw new IllegalArgumentException("Empty parameter: file");
		}
		final WxConfigL8 wxConfigL8 = WxConfigL8.getInstance();
		final List<String> configFileCandidates = wxConfigL8.getConfigFileInfo(packageName);
		boolean valid = false;
		for (String s : configFileCandidates) {
			if (fileStr.equals(s)) {
				valid = true;
				break;
			}
		}
		if (!valid) {
			// This is a security feature. Without it, we could read/edit
			// *every* file in the file system.
			throw new IllegalStateException("The file " + fileStr
					+ " is not a valid config file candidate for the package " + packageName
					+ ", permission to edit denied.");
		}
		final Path file = Paths.get(fileStr);
		String fileContent = map.getAsString("fileContent");
		if (fileContent == null) {
			if (Files.isRegularFile(file)) {
				try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
					final StringBuilder sb = new StringBuilder();
					final char[] buffer = new char[8192];
					for (;;) {
						final int res = reader.read(buffer);
						if (res == -1) {
							break;
						} else if (res > 0) {
							sb.append(buffer, 0, res);
						}
					}
					fileContent = sb.toString();
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			} else {
				fileContent = "";
			}
		} else {
			try (Writer w = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
				w.write(fileContent);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
			wxConfigL8.refreshPkgProperties(packageName);
		}
		map.put("fileContent", fileContent);		
		// --- <<IS-END>> ---

                
	}



	public static final void getPackageDetails (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getPackageDetails)>> ---
		// @sigtype java 3.5
		// [i] field:0:required packageName
		// [o] record:1:required configFileCandidates
		// [o] - field:0:required path
		// [o] - field:0:required exists
		// [o] record:0:required values
		final IDataMap map = new IDataMap(pipeline);
		System.out.println("getPackageDetails: ->");
		final String packageName = map.getAsString("packageName");
		if (packageName == null) {
			throw new NullPointerException("Missing parameter: packageName");
		}
		if (packageName.length() == 0) {
			throw new NullPointerException("Empty parameter: packageName");
		}
		final WxConfigL8 wxConfigL8 = WxConfigL8.getInstance();
		final PkgProperties pkgProperties = wxConfigL8.getPkgProperties(packageName);
		System.out.println("getPackageDetails: pkgProperties=" + pkgProperties
				+ ", " + (pkgProperties == null ? "null" : pkgProperties.getProperties()));
		if (pkgProperties == null  ||  pkgProperties.getProperties() == null) {
		} else {
			final IData values = IDataFactory.create();
			final Properties props = pkgProperties.getProperties();
			final List<String> keys = new ArrayList<String>();
			props.keySet().forEach((k) -> keys.add(k.toString()));
			Collections.sort(keys, String::compareToIgnoreCase);
			final IDataCursor crsr = values.getCursor();
			for (String k : keys) {
				IDataUtil.put(crsr, k, props.getProperty(k));
			}
			crsr.destroy();
			map.put("values", values);
		}
		final List<String> cfgFilePaths = wxConfigL8.getConfigFileInfo(packageName);
		final IData[] configFiles = new IData[cfgFilePaths.size()];
		for (int i = 0;  i < configFiles.length;  i++) {
			String cfgFileStr = cfgFilePaths.get(i);
			final Path cfgFilePath = Paths.get(cfgFileStr);
			final IData data = IDataFactory.create();
			final IDataCursor crsr = data.getCursor();
			IDataUtil.put(crsr, "path", cfgFilePath.toString());
			IDataUtil.put(crsr, "exists", String.valueOf(Files.isRegularFile(cfgFilePath)));
			crsr.destroy();
			configFiles[i] = data;
		}
		map.put("configFileCandidates", configFiles);
		System.out.println("getPackageDetails: <- " + String.join(", ", cfgFilePaths));
		
			
		// --- <<IS-END>> ---

                
	}



	public static final void getPackageList (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getPackageList)>> ---
		// @sigtype java 3.5
		// [o] recref:1:required packages wx.config.l8.impl:PackageListElement
		final IDataMap map = new IDataMap(pipeline);
		final List<NSPackage> packages = Arrays.asList(Namespace.current().getAllPackages());
		final Comparator<NSPackage> comp = (p1, p2) -> {
			return p1.getName().compareToIgnoreCase(p2.getName());
		};
		Collections.sort(packages, comp);
		final IData[] dataArray = new IData[packages.size()];
		WxConfigL8 wxConfigL8 = WxConfigL8.getInstance();
		for (int i = 0;  i < dataArray.length;  i++) {
			final NSPackage nsp = packages.get(i);
			final IData data = IDataFactory.create();
			final IDataCursor crsr = data.getCursor();
			IDataUtil.put(crsr, "name", nsp.getName());
			IDataUtil.put(crsr, "type", nsp.getPackageTypeString());
			IDataUtil.put(crsr,  "initialized", wxConfigL8.getPropertyRegistry().isInitialized(nsp.getName()));
			crsr.destroy();
			dataArray[i] = data;
		}
		map.put("packages", dataArray);
			
		// --- <<IS-END>> ---

                
	}
}

