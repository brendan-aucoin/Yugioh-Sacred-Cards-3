/*
 * Brendan Aucoin
 *03/30/2020
 *summons a card from your hand to the board
 * */
package actions;

import boards.Board;
import cards.Card;
import cards.MagicCard;
import cards.Monster;
import dueling.Field;
import dueling.Spot;
import player.Duelist;

public class PlayCardFromHandAction extends Action{
	public PlayCardFromHandAction() {}
	@Override
	public void performAction(Duelist player,Spot handSpot,Spot boardSpot,Board board,Field playerField,Duelist opponent,Field opponentField) {
		playCardFromHand(player,handSpot,boardSpot,board,playerField,opponent,opponentField);
	}
	
	
	/*loop through whichever dragged spot list is initialized 
	 * if you mouse is inside the bounds then summon that card */
	private void playCardFromHand(Duelist player,Spot handSpot,Spot boardSpot,Board board,Field field,Duelist opponent,Field opponentField) {
		boolean playedCard = summonCard(handSpot,boardSpot,board,player,field,opponent,opponentField);
		if(playedCard) {
			setPlayedCard(player,handSpot.getCard());
			player.playCardFromHand(handSpot);
		}
	}
	
	/*you can play as many magic cards as you want but if you play a monster card then set the player so that they have played a card*/
	public void setPlayedCard(Duelist player,Card c) {
		if(!(c instanceof MagicCard)) {
			player.setPlayedCard(true);
		}
	}
	
	
	//Spot c,Duelist player,Duelist opponent,Field playerField,Field opponentField,Board board
	
	/*if the spot is open and the card is a monster and the tribute cost is <= to how many tributes you have
	 * set the players current tribute cost and set the card on the spot and add it to the proper lists and buff the card*/
	public boolean summonCard(Spot handSpot,Spot boardSpot,Board board,Duelist player,Field field,Duelist opponent,Field opponentField) {
			if(boardSpot.isOpen()) {
				Card card = handSpot.getCard();
				if(card instanceof Monster) {
					Monster monster = (Monster)card;
					if(monster.getTributeCost() <= player.getNumTributes()) {
						player.setNumTributes(player.getNumTributes()-monster.getTributeCost());
						if(player.getNumTributes() <=0) {player.setNumTributes(0);}
					}
					else {
						return false;
					}
				}
				board.buffCard(card,player,opponent,field,opponentField);
				boardSpot.setCard(card);
				field.addCardToField(card);
				boardSpot.setOpen(false);
				card.setFirstTurn(true);
				card.setRevealed(true);
				return true;
			}
	
		return false;
	}
	
}
