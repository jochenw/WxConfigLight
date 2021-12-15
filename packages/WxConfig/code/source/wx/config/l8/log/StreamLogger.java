package wx.config.l8.log;

import java.io.IOException;
import java.io.PrintStream;

public class StreamLogger extends AbstractLogger {
	private final PrintStream ps;

	public StreamLogger(Level pLevel, PrintStream pStream) {
		super(pLevel);
		ps = pStream;
	}

	@Override
	protected void append(String pMsg) {
		if (ps != null) {
			synchronized (ps) {
				ps.print(pMsg);
			}
		}
	}

	@Override
	public void close() throws IOException {
		ps.close();
	}

	@Override
	public StreamLogger cloneMe() {
		return new StreamLogger(getLevel(), ps);
	}
}
