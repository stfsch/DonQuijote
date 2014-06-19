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
	public Point getPoint(Color pActivePlayerColour);
	
	public void notifyPhase(Phase phase, Color color);
	
	public void notifyState(State state);
	
	public void notifyIllegalMove(Color active, State state, State newState);

	public void notifyWin(Color winner);
	
	public void notifyDraw();
}
