package de.rwth_aachen.kbsg.dq;

@SuppressWarnings("serial")
public class IllegalMoveException extends IllegalStateException {
	public IllegalMoveException() {
		super();
	}

	public IllegalMoveException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public IllegalMoveException(String arg0) {
		super(arg0);
	}

	public IllegalMoveException(Throwable arg0) {
		super(arg0);
	}
}
