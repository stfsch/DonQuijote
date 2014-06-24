package de.rwth_aachen.kbsg.drq;

@SuppressWarnings("serial")
public class GameInterruptedException extends RuntimeException {
	public GameInterruptedException() {
		super();
	}

	public GameInterruptedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public GameInterruptedException(String arg0) {
		super(arg0);
	}

	public GameInterruptedException(Throwable arg0) {
		super(arg0);
	}
}
