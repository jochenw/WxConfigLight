package wx.config.l8.log;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import wx.config.l8.log.ILogger.Level;
import wx.config.l8.util.Strings;

public class StreamLoggerTest {
	@Test
	public void testFormat() {
		final StringBuilder sb = new StringBuilder();
		Strings.format(sb, "Msg #{}", "1");
		assertEquals("Msg #1", sb.toString());
	}

	@Test
	public void testDebugLevelLoggedInLevelTrace() throws Exception {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintStream ps = new PrintStream(baos);
		final StreamLogger sl = new StreamLogger(Level.trace, ps);
		sl.debug("Log message #{}", "1");
		sl.close();
		assertTrue(baos.toString().contains("DEBUG: Log message #1" + System.lineSeparator()));
		
	}
}
