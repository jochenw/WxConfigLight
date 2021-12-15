package wx.config.l8.util;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
// --- <<IS-END-IMPORTS>> ---

public final class thread

{
	// ---( internal utility methods )---

	final static thread _instance = new thread();

	static thread _newInstance() { return new thread(); }

	static thread _cast(Object o) { return (thread)o; }

	// ---( server methods )---




	public static final void sleep (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(sleep)>> ---
		// @sigtype java 3.5
		// [i] field:0:required milliseconds
		IDataCursor pipelineCursor = pipeline.getCursor();
		int sleepTimeInMilliSeconds = IDataUtil.getInt(pipelineCursor, "milliseconds", -1);
		pipelineCursor.destroy();
		
		if (sleepTimeInMilliSeconds != -1) {
			try {
				Thread.sleep(sleepTimeInMilliSeconds);
			} catch (InterruptedException e) {
				throw new ServiceException(e);
			}
		}		
		// --- <<IS-END>> ---

                
	}
}

