package de.rwth_aachen.kbsg.dq;

@SuppressWarnings("serial")
public class IllegalOccupationException extends IllegalStateException {
	public IllegalOccupationException() {
		super();
	}

	public IllegalOccupationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public IllegalOccupationException(String arg0) {
		super(arg0);
	}

	public IllegalOccupationException(Throwable arg0) {
		super(arg0);
	}
}
