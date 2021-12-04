package wx.config.l8.util;

public class Strings {
	public static void format(StringBuilder pSb, String pMsg, Object... pParams) {
		if (pMsg == null) {
			pSb.append("null");
		} else {
			int paramIndex = 0;
			for (int i = 0;  i < pMsg.length();  i++) {
				final char c = pMsg.charAt(i);
				if (c == '{') {
					if (i+1 < pMsg.length()  &&  pMsg.charAt(i+1) == '}') {
						if (pParams == null) {
							throw new IllegalArgumentException("Format string contains parameter"
									+ ", but no parameters supplied.");
						} else if (paramIndex < pParams.length) {
							pSb.append(pParams[paramIndex++]);
						} else {
							throw new IllegalArgumentException("Format string contains"
									+ (paramIndex+1) + " parameter references, or more, but only "
									+ pParams.length + " parameters are supplied.");
						}
						++i;
					} else {
					    pSb.append(c);
					}
				} else {
				    pSb.append(c);
				}
			}
		}
	}
}
