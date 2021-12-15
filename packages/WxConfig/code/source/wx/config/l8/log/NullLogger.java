package wx.config.l8.log;

import java.io.IOException;

public class NullLogger implements ILogger {
	@Override
	public boolean isEnabled(Level pLevel) {
		return false;
	}
	

	@Override
	public boolean isTraceEnabled() {
		return false;
	}


	@Override
	public boolean isDebugEnabled() {
		return false;
	}


	@Override
	public boolean isInfoEnabled() {
		return false;
	}


	@Override
	public boolean isWarnEnabled() {
		return false;
	}


	@Override
	public boolean isErrorEnabled() {
		return false;
	}


	@Override
	public boolean isFatalEnabled() {
		return false;
	}


	@Override
	public void log(Level pLevel, String pMsg, Object... pParams) {
		// Do nothing
	}


	@Override
	public void close() throws IOException {
		// Do nothing
	}


	@Override
	public ILogger cloneMe() {
		// This object is completely stateless, so we may return it as the clone.
		return this;
	}

}
