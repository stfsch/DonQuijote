/**
 * 
 */
package de.rwth_aachen.kbsg.dq;

/**
 * @author mraschke
 * Abstract class for user interfaces.
 */
public abstract class UI {
	
	public abstract void showState(State pState);
	
	/**
	 * Prompts the player to select the next spot in the game.
	 * @param pActivePlayerColour Identifies the active player who should select
	 * his next spot 
	 * @return The selected spot.
	 * @throws IncorrectPositionException 
	 */
	public abstract int[] getSpot(Spot pActivePlayerColour) throws IncorrectPositionException;
	
	/**
	 * Defines what should happen when the player makes an incorrect move.
	 * Especially, a new spot should be selected by the player.
	 * @param activePlayer The active player.
	 * @return A new spot selected by the player
	 */
	public abstract int[] incorrectMove(Spot activePlayer);

}
