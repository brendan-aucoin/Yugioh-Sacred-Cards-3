/*
 *Brendan Aucoin
 *06/30/2019
 *The ai that extends from player 
 */
package player;

import java.util.ArrayList;

import actions.Action;
import actions.ActionList;
import boards.Board;
import cards.Monster;
import dueling.Field;
import dueling.Phase;
import dueling.Spot;
import states.DuelingState;

public class Ai extends Duelist{
	private DuelingState duelingState;
	public Ai(DuelingState duelingState,String name) {
		super(name);
		this.duelingState = duelingState;
	}
	//DuelingState.phase = Phase.PLAYERS_TURN;
	//duelingState.getCam().setY(250);
	/*the logic of the AI*/
	public void update() {
		//you only want to update the AI if its the AI's turn.
		if(DuelingState.phase == Phase.AI_TURN) {
			//ArrayList<Monster> cards = duelingState.getBoard().getPlayerField().getSortedMonstersByAttack();
			playMonster(0);
			try {Thread.sleep(150);}catch(Exception e) {}
			getAction(ActionList.END_TURN).performAction(this,duelingState.getOpponentField(),Phase.PLAYERS_TURN);
		}
	}
	
	
	private void playMonster(int index) {
		ArrayList<Monster> monsters = getHand().getMonsters();
		ArrayList<Monster> sortedMonsters = getHand().getMonstersSortedByAttack(monsters);
		ArrayList<Spot> monsterSpots = getHand().getMonsterSpots();
		ArrayList<Spot> sortedMonsterSpots = getHand().getMonsterSpotsSortedByAttack(monsterSpots);
		//goes through the monsters in your hand
		for(int i =0; i < sortedMonsters.size();i++) {
			Monster strongestMonster = sortedMonsters.get(i);
			Monster otherPlayersBestCard = getPlayersBestCard(index);
			Spot boardSpot = getFirstBoardSpot();
			
			//two cases
			//the player has nothing on his field
			if(otherPlayersBestCard == null) {
				boolean summoned = summonMonsterNoPlayerCards(strongestMonster,sortedMonsterSpots.get(i),boardSpot);
				draw();
				if(summoned) {
					return;
				}
				else {
					continue;
				}
				
			}
			
			//if the player does have cards on their field then you need to loop
			//through your hand and find a card that beats it by strength and then if none do then check hand again and check for elemental typing
			else {
				System.out.println("The player has stuff on their field");
			}
		}
	}
	
	
	
	
	private boolean summonMonsterNoPlayerCards(Monster strongestMonster,Spot handSpot,Spot boardSpot) {
		//System.out.println(strongestMonster);
		//System.out.println("BEGNINGIN = " + hasPlayedCard());
		//if its not a tribute just summon the card
		if(strongestMonster.getTributeCost() <=0) {
			if(boardSpot == null) {System.out.println("THERE ARE NO SPOTS LEFT ON THE BOARD");return false;}
			summonCard(strongestMonster,handSpot,boardSpot);
			//System.out.println("INSIDE IF = " + hasPlayedCard());
			return true;
		}
		//if it is a tribute monster
		else {
			//if you have enough cards on your field
			ArrayList<Monster> sortedMonsterField = duelingState.getOpponentField().getSortedMonstersByAttack();
			ArrayList<Spot> sortedMonsterSpots = duelingState.getOpponentField().getSortedMonsterSpots();
			if(sortedMonsterField.size() >= strongestMonster.getTributeCost()) {
				for(int i =sortedMonsterField.size()-1; i >-1; i--) {
					if(sortedMonsterField.get(i).getAttack() < strongestMonster.getAttack()) {
						getAction(ActionList.TRIBUTE).performAction(sortedMonsterField.get(i),this,sortedMonsterSpots.get(i),duelingState.getBoard(),duelingState.getOpponentField());
						Spot newBoardSpot = getFirstBoardSpot();
						if(getNumTributes() >= strongestMonster.getTributeCost()) {
							if(newBoardSpot == null) {System.out.println("THERE ARE NO SPOTS LEFT ON THE BOARD");return false;}
							summonCard(strongestMonster,handSpot,newBoardSpot);
							return true;
						}
					}
				}
			}
		}
		
		return false;
		
	}
	
	private void summonCard(Monster strongestMonster,Spot handSpot,Spot boardSpot) {
		getAction(ActionList.PLAY_CARD_FROM_HAND).performAction(this,handSpot,boardSpot,duelingState.getBoard(),duelingState.getOpponentField());
	}
	/*gets the players monster field as a sorted array by attack and then gets the monster at whichever index you enter*/
	private Monster getPlayersBestCard(int index) {
		//gets the highest card from the players field index will start at 0 when this method is called and it will go up to 1, 2, 3 and so on
		ArrayList<Monster> playersMonsters = duelingState.getPlayerField().getSortedMonstersByAttack();//the array list of sorted monsters on the players field
		if(playersMonsters.size() == 0) {return null;}
		if(index >=playersMonsters.size()-1) {
			return playersMonsters.get(playersMonsters.size()-1);
		}
		return playersMonsters.get(index);
	}
	
	private Spot getFirstBoardSpot() {
		for(int i =0; i < duelingState.getOpponentField().getMonsterSpots().size(); i++) {
			Spot s = duelingState.getOpponentField().getMonsterSpots().get(i);
			if(s.isOpen()) {
				return s;
			}
		}
		return null;
	}
	private Action getAction(ActionList action) {
		return DuelingState.actionHandler.getAction(action);
	}
	
	public void setDuelingState(DuelingState duelingState) {this.duelingState = duelingState;}
	public DuelingState getDuelingState() {return duelingState;}
	
}
