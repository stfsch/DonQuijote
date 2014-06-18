/**
 * 
 */
package de.rwth_aachen.kbsg.dq;

/**
 * @author mraschke
 * Abstract class for user interfaces.
 */
public interface UI {
	
	public void showState(State pState);
	
	/**
	 * Prompts the player to select the next point in the game.
	 * @param pActivePlayerColour Identifies the active player who should select
	 * his next spot 
	 * @return The selected point.
	 * @throws IncorrectPositionException 
	 */
	public Point getPoint(Color pActivePlayerColour);
	
	public void notifyPhase(Phase phase);
	
	public void notifyState(State state);
	
	/**
	 * Defines what should happen when the player makes an incorrect move.
	 * Especially, a new point should be selected by the player.
	 * @param activePlayer The active player.
	 * @return A new point selected by the player
	 */
	public void notifyIllegalMove(Player active, State state, State newState);

	public void notifyWin(Player winner);
	
	public void notifyDraw();
}
