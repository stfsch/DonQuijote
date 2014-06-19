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
	private final int frame;
	private final int index;
	
	public Point(int frame, int index) {
		this.frame = frame;
		this.index = index;
	}
	
	public int getFrame() {
		return frame;
	}

	public int getIndex() {
		return index;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + frame;
		result = prime * result + index;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		if (frame != other.frame)
			return false;
		if (index != other.index)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "(" + frame + ", " + index + ")";
	}
}
