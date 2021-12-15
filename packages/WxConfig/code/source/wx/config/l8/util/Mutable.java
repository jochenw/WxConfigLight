package wx.config.l8.util;

import java.util.function.Supplier;

public class Mutable<O> implements Supplier<O> {
	public static class MString extends Mutable<String> {
		
	}

	private O value;

	public O get() {
		return value;
	}

	public void set(O value) {
		this.value = value;
	}

	public String toString() {
		return "<" + value + ">";
	}
}
