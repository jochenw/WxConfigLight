package wx.config.l8.log;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import wx.config.l8.log.ILogger.Level;
import wx.config.l8.util.Strings;

class StreamLoggerTest {
	@Test
	public void testFormat() {
		final StringBuilder sb = new StringBuilder();
		Strings.format(sb, "Msg #{}", "1");
		assertEquals("Msg #1", sb.toString());
	}

	@Test
	void testDebugLevelLoggedInLevelTrace() {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintStream ps = new PrintStream(baos);
		final StreamLogger sl = new StreamLogger(Level.trace, ps);
		sl.debug("Log message #{}", "1");
		assertTrue(baos.toString().contains("DEBUG: Log message #1" + System.lineSeparator()));
		
	}

}
