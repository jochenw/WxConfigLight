package wx.config.l8.tests.util;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.softwareag.util.IDataMap;
// --- <<IS-END-IMPORTS>> ---

public final class string

{
	// ---( internal utility methods )---

	final static string _instance = new string();

	static string _newInstance() { return new string(); }

	static string _cast(Object o) { return (string)o; }

	// ---( server methods )---




	public static final void makePathCanonical (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(makePathCanonical)>> ---
		// @sigtype java 3.5
		// [i] field:0:required path
		// [o] field:0:required canonicalPath
		final IDataMap map = new IDataMap(pipeline);
		final String path = map.getAsString("path");
		if (path == null) {
			throw new NullPointerException("Missing parameter: path");
		}
		if (path.length() == 0) {
			throw new NullPointerException("Empty parameter: path");
		}
		map.put("canonicalPath", path.replace('\\', '/'));
		// --- <<IS-END>> ---

                
	}
}

