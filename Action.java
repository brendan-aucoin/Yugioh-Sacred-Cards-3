/*
 *Brendan Aucoin
 *03/30/2020
 *holds the method signatures for any action you would ever need
 * */
package actions;

import boards.Board;
import cards.MagicCard;
import cards.Monster;
import dueling.Field;
import dueling.Phase;
import dueling.Spot;
import dueling.TextPopupPane;
import player.Duelist;

public abstract class Action {
	public void performAction(Monster card) {}//defense
	public void performAction(Duelist player,Spot handSpot,Spot boardSpot,Board board,Field field) {}//playing a card
	public void performAction(Monster card, Duelist player,Spot TributeSpot, Board board,Field field) {}//tribute
	public void performAction(Monster card,Duelist player,Duelist opponent,Field playerField,Field opponentField) {}//monster effect
	public void performAction(MagicCard magicCard,Duelist player,Duelist opponent,Board board,Field playerField,Field opponentField,Spot spellSpot,TextPopupPane textPopupPane) {}//spell effect
	public void performAction(Duelist currentPlayer,Field currentPlayerField,Phase nextPhase) {}//end turn
	public void performAction(Spot removeSpot,Board board,Field field) {}//removing card from board
	public void performAction(Duelist player) {}//starting your turn
}	
