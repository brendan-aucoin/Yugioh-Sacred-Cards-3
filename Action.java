/*
 *Brendan Aucoin
 *03/30/2020
 *holds the method signatures for any action you would ever need
 * */
package actions;

import boards.Board;
import cards.Card;
import cards.Monster;
import cards.SpellCard;
import dueling.Field;
import dueling.Phase;
import dueling.Spot;
import dueling.TextPopupPane;
import game.Game;
import player.Ai;
import player.Duelist;

public abstract class Action {
	public void performAction(Monster card,Duelist duelist) {}//defense
	public void performAction(Duelist player,Spot handSpot,Spot boardSpot,Board board,Field playerField,Duelist opponent,Field opponentField) {}//playing a card from your hand
	public void performAction(Card card,Spot boardSpot,Duelist player,Duelist opponent,Board board,Field playerField,Field opponentField) {} //summoning card from not your hand
	public void performAction(Monster card, Duelist player,Spot TributeSpot, Board board,Field field) {}//tribute
	public void performAction(Monster card,Duelist player,Duelist opponent,Field playerField,Field opponentField,Board board) {}//monster effect
	public void performAction(SpellCard magicCard,Duelist player,Duelist opponent,Board board,Field playerField,Field opponentField,Spot spellSpot,TextPopupPane textPopupPane) {}//spell effect
	public void performAction(Duelist currentPlayer,Field currentPlayerField,Phase nextPhase) {}//end turn
	public void performAction(Spot removeSpot,Board board,Field field) {}//removing card from board
	public void performAction(Duelist player) {}//starting your turn
	public void performAction(Duelist attacker,Duelist reciever,Spot attackersMonster,Spot recieversMonster,Game game,Board board,Field attackersField,Field receiversField) {}//attacking
	public void performAction(Spot attackingSpot,Spot receivingSpot,Duelist player,Duelist Receiver,Field playerField,Field opponentField,Board board) {}//trap effect
	
	public void signalAiMonster(Duelist player) {
		
	}
}	
