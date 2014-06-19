package de.rwth_aachen.kbsg.dq;

@SuppressWarnings("serial")
public class IllegalTakeException extends IllegalStateException {
	public IllegalTakeException() {
		super();
	}

	public IllegalTakeException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public IllegalTakeException(String arg0) {
		super(arg0);
	}

	public IllegalTakeException(Throwable arg0) {
		super(arg0);
	}
}
