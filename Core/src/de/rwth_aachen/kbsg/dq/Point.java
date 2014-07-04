package de.rwth_aachen.kbsg.dq;

/**
 * A point on the field is where a piece may be located at.
 * The following field has three frames, each of which has eight indices, which are 24 points:
 * <pre>
 *   +-----------+-----------+
 *   |           |           |
 *   |   +-------+-------+   |
 *   |   |       |       |   |
 *   |   |   +---+---+   |   |
 *   |   |   |       |   |   |
 *   +---+---+       +---+---+
 *   |   |   |       |   |   |
 *   |   |   +---+---+   |   |
 *   |   |       |       |   |
 *   |   +-------+-------+   |
 *   |           |           |
 *   +-----------+-----------+
 * </pre>
 */
public class Point {
	private final int i;
	
	public Point(int i) {
		this.i = i;
	}
	
	public Point(int frame, int index) {
		this.i = frame * 8 + index;
	}
	
	public int getFrame() {
		return i / 8;
	}

	public int getIndex() {
		return i % 8;
	}
	
	int getNumber() {
		return i;
	}

	@Override
	public int hashCode() {
		return i;
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj || (obj != null && obj instanceof Point && i == ((Point) obj).i);
	}
	
	@Override
	public String toString() {
		return "(" + getFrame() + ", " + getIndex() + ")";
	}
}
