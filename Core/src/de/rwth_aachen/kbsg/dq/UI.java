/**
 * 
 */
package de.rwth_aachen.kbsg.dq;

/**
 * @author mraschke
 * Abstract class for user interfaces.
 */
public interface UI {
	
	/**
	 * Prompts the player to select the next point in the game.
	 * @param pActivePlayerColour Identifies the active player who should select
	 * his next spot 
	 * @return The selected point.
	 */
	public Point inputPoint(Color pActivePlayerColour);
	
	public void phaseChanged(Phase phase, Color color);
	
	public void stateChanged(State state);
	
	public void illegalMove(Color active, State state, State newState);

	public void gameWon(Color winner);
	
	public void gameDrawn();
}
