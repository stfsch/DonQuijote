package de.rwth_aachen.kbsg.dq;

import static org.junit.Assert.*;

import org.junit.Test;

import de.rwth_aachen.kbsg.dq.Color;

public class ColorTest {

	@Test
	public void test() {
		assertSame(Color.BLACK, Color.BLACK);
		assertSame(Color.WHITE, Color.WHITE);
		assertSame(Color.BLACK.opponentOf(), Color.WHITE);
		assertSame(Color.WHITE.opponentOf(), Color.BLACK);
	}

}
