package wx.config.l8;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import wx.config.l8.WxConfigL8;
import wx.config.l8.PropertyRegistry.PkgProperties;
import com.wm.lang.ns.NSService;
import java.util.Properties;
import java.util.Stack;
import com.softwareag.util.IDataMap;
import com.wm.app.b2b.server.InvokeState;
// --- <<IS-END-IMPORTS>> ---

public final class pub

{
	// ---( internal utility methods )---

	final static pub _instance = new pub();

	static pub _newInstance() { return new pub(); }

	static pub _cast(Object o) { return (pub)o; }

	// ---( server methods )---




	public static final void getValue (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getValue)>> ---
		// @sigtype java 3.5
		// [i] field:0:required key
		// [i] field:0:optional wxConfigPkgName
		// [i] field:0:optional noServiceException {"true","false"}
		// [o] field:0:optional propertyValue
		final IDataMap map = new IDataMap(pipeline);
		final String key = map.getAsString("key");
		if (key == null) {
			throw new NullPointerException("Missing parameter: key");
		}
		if (key.length() == 0) {
			throw new NullPointerException("Empty parameter: key");
		}
		String packageName = map.getAsString("wxConfigPkgName");
		if (packageName == null  ||  packageName.length() == 0) {
			final InvokeState state = InvokeState.getCurrentState();
			@SuppressWarnings("unchecked")
			final Stack<NSService> stack = state.getCallStack();
			if (stack == null  ||  stack.isEmpty()) {
				throw new IllegalStateException("Empty Callstack: Parameter wxConfigPkgName is null, or empty,"
						+ " and the name of the calling package cannot be determined.");
			}
			packageName = stack.peek().getPackage().getName();
		}
		final boolean noServiceException = map.getAsBoolean("noServiceException", Boolean.FALSE).booleanValue();
		final PkgProperties pprops = WxConfigL8.getInstance().getPkgProperties(packageName);
		if (pprops == null) {
			if (!noServiceException) {
				throw new IllegalStateException("No package properties available for package: " + packageName);
			}
		} else {
			final Properties properties = pprops.getProperties();
			String value;
			if (properties == null) {
				value = null;
			} else {
				value = properties.getProperty(key);
			}
			if (value == null) {
				if (!noServiceException) {
					throw new IllegalStateException("Undefined property " + key + " for package: " + packageName);
				}
			} else {
				map.put("propertyValue", value);
			}
		}	
		// --- <<IS-END>> ---

                
	}
}

