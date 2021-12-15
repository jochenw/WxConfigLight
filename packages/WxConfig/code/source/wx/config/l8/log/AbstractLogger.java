package wx.config.l8.log;

import wx.config.l8.util.Strings;

public abstract class AbstractLogger implements ILogger {
	private final long startTime = System.currentTimeMillis();
	private final Level level;

	protected AbstractLogger(Level pLevel) {
		level = pLevel;
	}

	public Level getLevel() {
		return level;
	}

	protected abstract void append(String pMsg);

	@Override
	public boolean isEnabled(Level pLevel) {
		return level.ordinal() <= pLevel.ordinal();
	}

	@Override
	public void log(Level pLevel, String pMsg, Object... pParams) {
		final StringBuilder sb = new StringBuilder();
		sb.append((System.currentTimeMillis() - startTime));
		sb.append(" ");
		sb.append(Level.nameOf(pLevel));
		sb.append(": ");
		format(sb, pMsg, pParams);
		sb.append(System.lineSeparator());
		append(sb.toString());
	}

	protected void format(StringBuilder pSb, String pMsg, Object... pParams) {
		Strings.format(pSb, pMsg, pParams);
	}
}
