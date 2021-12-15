package wx.config.l8.log;

import java.io.Closeable;

/**
 *  Interface of the internal logger.
 */
public interface ILogger extends Closeable {
	public enum Level {
		trace, debug, info, warn, error, fatal;

		public static String nameOf(Level pLevel) {
			switch (pLevel) {
			case trace: return "TRACE";
			case debug: return "DEBUG";
			case info:  return "INFO ";
			case warn:  return "WARN ";
			case error: return "ERROR";
			case fatal: return "FATAL";
			default:
				throw new IllegalStateException("Invalid level: " + pLevel);
			}
		}
	}
	public ILogger cloneMe();
	public boolean isEnabled(Level pLevel);
	public default boolean isTraceEnabled() { return isEnabled(Level.trace); }
	public default boolean isDebugEnabled() { return isEnabled(Level.trace); }
	public default boolean isInfoEnabled() { return isEnabled(Level.trace); }
	public default boolean isWarnEnabled() { return isEnabled(Level.trace); }
	public default boolean isErrorEnabled() { return isEnabled(Level.trace); }
	public default boolean isFatalEnabled() { return isEnabled(Level.trace); }
	public void log(Level pLevel, String pMsg, Object... pParams);
	public default void trace(String pMsg, Object... pParams) {
		if (isTraceEnabled()) {
			log(Level.trace, pMsg, pParams);
		}
	}
	public default void debug(String pMsg, Object... pParams) {
		if (isDebugEnabled()) {
			log(Level.debug, pMsg, pParams);
		}
	}
	public default void info(String pMsg, Object... pParams) {
		if (isInfoEnabled()) {
			log(Level.info, pMsg, pParams);
		}
	}
	public default void warn(String pMsg, Object... pParams) {
		if (isWarnEnabled()) {
			log(Level.warn, pMsg, pParams);
		}
	}
	public default void error(String pMsg, Object... pParams) {
		if (isErrorEnabled()) {
			log(Level.error, pMsg, pParams);
		}
	}
	public default void fatal(String pMsg, Object... pParams) {
		if (isFatalEnabled()) {
			log(Level.fatal, pMsg, pParams);
		}
	}
}
