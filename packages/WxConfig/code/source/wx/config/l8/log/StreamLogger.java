package wx.config.l8.log;

import java.io.IOException;
import java.io.PrintStream;

public class StreamLogger extends AbstractLogger {
	private PrintStream ps;

	public StreamLogger(Level pLevel, PrintStream pStream) {
		super(pLevel);
		ps = pStream;
	}

	@Override
	protected void append(String pMsg) {
		if (ps != null) {
			ps.print(pMsg);
		}
	}

	@Override
	public void close() throws IOException {
		ps.close();
	}
}
