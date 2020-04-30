/*
 *Brendan Aucoin
 *06/30/2019
 *The ai that extends from player 
 */
package player;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import actions.Action;
import actions.ActionList;
import cards.Card;
import cards.EffectMonster;
import cards.MagicCard;
import cards.Monster;
import cards.SpellCard;
import dueling.Phase;
import dueling.Spot;
import dueling.TextPopupPane;
import states.DuelingState;
import types.CardType;

public class Ai extends Duelist{
	private DuelingState duelingState;
	private final int COMPARE_BY_STRENGTH =0, COMPARE_BY_TYPE = 1, COMPARE_BY_NOTHING = 2;
	private final int PLAY_MONSTER_PHASE =0 ,PLAY_MAGIC_PHASE = 1,USE_MAGIC_PHASE = 2,USE_MONSTER_PHASE = 3,CHANGE_TURN_PHASE = 4;
	public static final int AI_CAMERA = 75;
	private TextPopupPane textPopupPane;
	private ScheduledExecutorService executor;
	private boolean aiPhaseDone;
	private ArrayList<Runnable> useMonsterTasks;
	private int numPlayedMagicCardsActivated,numPlayedMagicCards,numUsedMagicCardsActivated,numUsedMagicCards,numUsedMonsters,numUsedMonstersActivated;
	private int phase;
	private boolean usedMagicCard,usedMonsterCard;
	private boolean playedMonster,playedMagic;
	private int usedMonsterIndex;
	private boolean [] monstersHaveGone;
	private Semaphore playMagicSemaphore,useMagicSemaphore,textPopupPaneSemaphore;
	public Ai(DuelingState duelingState,String name) {
		super(name);
		this.duelingState = duelingState;
		this.textPopupPane = new TextPopupPane(duelingState,duelingState.getGame(),this);
		executor = Executors.newSingleThreadScheduledExecutor();
		aiPhaseDone = true;
		useMonsterTasks = new ArrayList<Runnable>();
		numPlayedMagicCardsActivated =  numPlayedMagicCards = numUsedMagicCards = numUsedMagicCardsActivated = numUsedMonsters = numUsedMonstersActivated = 0;
		phase = PLAY_MONSTER_PHASE;
		playedMonster = playedMagic = usedMagicCard = usedMonsterCard = false;
		setPlayedCard(false);
		usedMonsterIndex = 0;
		monstersHaveGone = new boolean[5];
		clearMonstersHaveGone();
		playMagicSemaphore = useMagicSemaphore = new Semaphore(1);
		textPopupPaneSemaphore = new Semaphore(0);
	}
	/*the logic of the AI*/
	public void update() {
		//you only want to update the AI if its the AI's turn.
		if(DuelingState.phase == Phase.AI_TURN) {
			duelingState.getCam().setY(AI_CAMERA);
			aiPhaseDone = false;
			
			if(phase == PLAY_MONSTER_PHASE && !playedMonster) {
				playMonster(0,COMPARE_BY_STRENGTH);
				playedMonster = true;
				phase = PLAY_MAGIC_PHASE;
			}
			
			else if(phase == PLAY_MAGIC_PHASE && !playedMagic) {
				ArrayList<Runnable> playMagicCardsTasks = playMagicCards();
				if(playMagicCardsTasks == null || playMagicCardsTasks.size() == 0) {
					phase = USE_MAGIC_PHASE;
					return;
				}
				for(int i =0;i<playMagicCardsTasks.size();i++) {
					 executor.schedule(playMagicCardsTasks.get(i),500, TimeUnit.MILLISECONDS);
				}
			}
			
			else if(phase == USE_MAGIC_PHASE && !usedMagicCard) {
				ArrayList<Runnable> useMagicCardTasks = useMagicCards();
				if(useMagicCardTasks.size() == 0) {
					phase = USE_MONSTER_PHASE;
					return;
				}
				for(int i=0; i < useMagicCardTasks.size();i++) {
					executor.schedule(useMagicCardTasks.get(i),500,TimeUnit.MILLISECONDS);
				}
			}
			
			else if(phase == USE_MONSTER_PHASE && !usedMonsterCard) {
				useMonsterCards();
				if(useMonsterTasks.size() ==0) {
					phase = CHANGE_TURN_PHASE;
					return;
				}
			}
			/*this is to constantly try and run the attacking events but will block it if the current one has already been used*/
			else if(phase == USE_MONSTER_PHASE && usedMonsterCard) {
				if(useMonsterTasks!= null && useMonsterTasks.size() >0) {
					try {
						if(!monstersHaveGone[usedMonsterIndex]) {
							executor.schedule(useMonsterTasks.get(usedMonsterIndex),300, TimeUnit.MILLISECONDS);
							monstersHaveGone[usedMonsterIndex] = true;
						}
					}catch(Exception e) {phase = CHANGE_TURN_PHASE;}
				}
				else {
					phase = CHANGE_TURN_PHASE;
				}
			}
			
			else if(phase == CHANGE_TURN_PHASE) {
				changeTurn();
			}
			
		}
	}
	
	
	private void changeTurn() {
		duelingState.getCam().setY(0);
		setPlayedCard(false);
		useMonsterTasks.clear();
		numPlayedMagicCardsActivated =  numPlayedMagicCards =  numUsedMagicCardsActivated =  numUsedMagicCards = numUsedMonsters = usedMonsterIndex = numUsedMonstersActivated = 0;
		phase = PLAY_MONSTER_PHASE;
		playedMonster = playedMagic =  usedMagicCard = usedMonsterCard =false;
		aiPhaseDone = true;
		executor.shutdown();
		clearMonstersHaveGone();
		playMagicSemaphore = useMagicSemaphore = new Semaphore(1);
		textPopupPaneSemaphore = new Semaphore(0);
		executor = Executors.newSingleThreadScheduledExecutor();
		getAction(ActionList.END_TURN).performAction(duelingState.getOpponent(),duelingState.getOpponentField(),Phase.PLAYERS_TURN);//go back to the players phase
		getAction(ActionList.START_TURN).performAction(duelingState.getPlayer());//start the players turn 
	}
	
	
	private ArrayList<Runnable> playMagicCards() {
		ArrayList<Spot> magicCardSpots = getHand().getMagicCardsSpots();
		ArrayList<MagicCard> magicCards = getHand().getMagicCards();
		ArrayList<Runnable> tasks = new ArrayList<Runnable>();
		for(int i =0; i < magicCards.size();i++) {
			MagicCard magicCard = magicCards.get(i);
			if(magicCard == null || magicCardSpots.get(i) == null) {return null;}
			if(magicCard.playCondition(this, duelingState.getPlayer(), duelingState.getOpponentField(), duelingState.getPlayerField())) {
				Spot currentMagicCardSpot = magicCardSpots.get(i);
				Runnable task = new Runnable() {
					public void run() {
						try {
							playMagicSemaphore.acquire(); 
							Spot boardSpot = getFirstMagicCardBoardSpot();
							if(boardSpot == null) {return;}
							summonCard(magicCard,currentMagicCardSpot,boardSpot);
							numPlayedMagicCardsActivated++;
							if(numPlayedMagicCardsActivated == numPlayedMagicCards) {
								phase = USE_MAGIC_PHASE;
							}
							Thread.sleep(500);
							playMagicSemaphore.release(); 
						}catch(InterruptedException e) {System.err.println("playing a magic card couldnt use semaphore");}
						
					}
				};
				tasks.add(task);
				numPlayedMagicCards++;
			}
		}
		playedMagic = true;
		return tasks;
	}
	
	private ArrayList<Runnable> useMagicCards() {
		ArrayList<Runnable> tasks = new ArrayList<Runnable>();
		for(int i =0; i < duelingState.getOpponentField().getMagicCards().size();i++) {
			MagicCard magicCard = (MagicCard)duelingState.getOpponentField().getMagicCards().get(i);
			SpellCard spellCard;
			if(!(magicCard instanceof SpellCard)) {continue;}
			else {
				spellCard = (SpellCard)magicCard;
			}
			//you can only activate spells
			if(spellCard.playCondition(this,duelingState.getPlayer(), duelingState.getOpponentField(), duelingState.getPlayerField())) {
				Spot currentMagicCardSpot = duelingState.getOpponentField().getMagicSpots().get(i);
				Runnable task = new Runnable() {
					public void run() {
						try {
							useMagicSemaphore.acquire();
							getAction(ActionList.ACTIVATE_SPELL).performAction(
									spellCard,
									duelingState.getOpponent(),duelingState.getPlayer(),
									duelingState.getBoard(),duelingState.getOpponentField(),duelingState.getPlayerField(),
									currentMagicCardSpot,textPopupPane
									);
							numUsedMagicCardsActivated++;
							if(numUsedMagicCardsActivated == numUsedMagicCards) {
								phase = USE_MONSTER_PHASE;
							}
							textPopupPaneSemaphore.acquire();
							Thread.sleep(500);
							useMagicSemaphore.release();
						}catch(InterruptedException e) {System.err.println("using a magic card semaphore was interrupted");}
					}
					
				};
				tasks.add(task);
				numUsedMagicCards++;
			}
		}
		usedMagicCard = true;
		return tasks;
	}
	
	public void signalTextPopupPane() {
		textPopupPaneSemaphore.release();
	}
	
	/*picks your strongest card in your hand if your players field is empty
	 * if the player has cards on their field then choose the strongest card or the card with the best type against the 
	 * players strongest card and cascade the players card down to their 5 best card if necessary
	 * if no possible card can beat the players then play the card with the highest defense and change it to defense*/
	private void playMonster(int index,int check) {
		ArrayList<Monster> monsters = getHand().getMonsters();//all the monsters in your hand 
		ArrayList<Monster> sortedMonsters = getHand().getMonstersSortedByAttack(monsters);//the monsters in your hand sorted from highest attack to lowest attack
		ArrayList<Spot> monsterSpots = getHand().getMonsterSpots();//all the spots with monsters in them
		ArrayList<Spot> sortedMonsterSpots = getHand().getMonsterSpotsSortedByAttack(monsterSpots);//the spots with monsters in them but in the same order as the sorted monsters array
		ArrayList<Monster> sortedMonstersDefense = getHand().getMonstersSortedByDefense(monsters);
		ArrayList<Spot> sortedMonsterSpotsDefense =  getHand().getMonsterSpotsSortedByDefense(monsterSpots);
		
		//goes through the monsters in your hand
		for(int i =0; i < sortedMonsters.size();i++) {
			Monster strongestMonster = sortedMonsters.get(i);
			Monster otherPlayersBestCard = getPlayersBestCard(index);
			Spot boardSpot = getFirstMonsterBoardSpot();//get the first available spot to put a card
			
			//two cases
			//the player has nothing on his field just summon the strongest card and return
			if(otherPlayersBestCard == null) {
				boolean summoned = summonMonster(strongestMonster,sortedMonsterSpots.get(i),boardSpot);
				if(summoned) {return;}
				else {continue;}
			}
			
			//if the player does have cards on their field then you need to loop
			//through your hand and find a card that beats it by strength and then if none do then check hand again and check for
			//elemental typing
			else {
				//if the index of the players highest card is out of bounds. kinda the base case
				if(index == duelingState.getPlayerField().getRevealedSortedMonstersByAttack().size()) {
					if(check > COMPARE_BY_NOTHING) {break;}//no more checks so there was literally nothing you could play.
					playMonster(0,check+1);//try playing a monster but with the next check which is either the check by type or by nothing
					break;
				}
				
				//if my attack is better than their attack or  my attack is better than their defense and the other card is in defense
				boolean checkNextPlayerCard = true;
				if(check == COMPARE_BY_STRENGTH) {
					if(((strongestMonster.getAttack() >= otherPlayersBestCard.getAttack()) && (!otherPlayersBestCard.isInDefense())
					|| ((strongestMonster.getAttack() >= otherPlayersBestCard.getDefense()) && (otherPlayersBestCard.isInDefense()))) &&
					!strongestMonster.weakTo(otherPlayersBestCard.getAttribute())) {
						//you successfully played a card so you exit
						if(summonMonster(strongestMonster,sortedMonsterSpots.get(i),boardSpot)) {return;}
						//if you couldnt play a card then make sure you can go to the next strongest player card
						else {checkNextPlayerCard = true;}
					}
				}
				//if you are checking your cards in your hand by the attribute
				else if(check == COMPARE_BY_TYPE) {
					if(!strongestMonster.weakTo(otherPlayersBestCard) && otherPlayersBestCard.weakTo(strongestMonster.getAttribute())) {//if the player is weak to your strongest cards attribute then play it
						if(summonMonster(strongestMonster,sortedMonsterSpots.get(i),boardSpot)) {return;}
						else {checkNextPlayerCard = true;}
					}
				}
				//the last case where you cant beat your opponents card so just play your best card.
				else if(check == COMPARE_BY_NOTHING) {
					//loop through your hand again and find the strongest monster you can play
					for(int j =0; j < sortedMonstersDefense.size(); j++) {
						if(summonMonster(sortedMonstersDefense.get(j),sortedMonsterSpotsDefense.get(j),boardSpot)) {
							//activate its effect or put in defense right away
							//getAction(ActionList.DEFEND).performAction(sortedMonstersDefense.get(j),this,false);
							sortedMonstersDefense.get(j).setRevealed(false);
							return;
							}
						else {checkNextPlayerCard = true;}
					}
				}
				
				//my strongest monster did not beat the opponents strongest monster
				if(checkNextPlayerCard) {//if you want to move onto the players next highest card
					//if you are at the last card in the players field
					if(i == sortedMonsters.size() -1) {//if you finished looking at your hand
						playMonster(index+1,check);//Recursively call this function with the next player index
					}
				}
				
			}
		}
		
	}
	
	private void useMonsterCards() {
		numUsedMonstersActivated = 0;
		usedMonsterIndex = 0;
		clearMonstersHaveGone();
		usedMonsterCard = false;
		/*to do other actions you will want to put the entire code inside a runnable task
		 * becuase if you dont then the field that you are accessing is out of sync.*/
		ArrayList<Monster> sortedMonstersByAttack = duelingState.getOpponentField().getSortedMonstersByAttack();
		ArrayList<Spot> sortedMonstersByAttackSpots = duelingState.getOpponentField().getSortedMonsterSpots();
		numUsedMonsters = duelingState.getOpponentField().getMonsterCards().size();
		
		for(int i= 0; i < sortedMonstersByAttack.size(); i++) {
			Monster currentMonster = sortedMonstersByAttack.get(i);
			Spot currentMonsterSpot =  sortedMonstersByAttackSpots.get(i);
			/*create a new task for each card*/
			Runnable task = new Runnable() {
				public void run() {
					if(currentMonster.hasUsedAction()) {return;}
					//activate the cards effect
					activateEffect(currentMonster);
					
					//attacking directly
					if(duelingState.getPlayerField().getMonsterCards().size() == 0) {
						getAction(ActionList.ATTACK).performAction(
						duelingState.getOpponent(),duelingState.getPlayer(),
						currentMonsterSpot,null,
						duelingState.getGame(),duelingState.getBoard(),
						duelingState.getOpponentField(),duelingState.getPlayerField()
						);
					}
						
					/*attacking another card*/
					else {
						ArrayList<Monster> playersRevealedMonsters = duelingState.getPlayerField().getRevealedSortedMonstersByCurrentStat();
						ArrayList<Spot> playersRevealedMonsterSpots = duelingState.getPlayerField().getSortedRevealedMonsterSpotsByCurrentStat();
						//try and attack any of the players revealed cards
						for(int j=0; j < playersRevealedMonsters.size();j++) {
							//do the condition check
							Monster currentPlayerMonster = playersRevealedMonsters.get(j);
							int currentPlayerMonsterStat = currentPlayerMonster.getAttack();
							if(currentPlayerMonster.isInDefense()) {currentPlayerMonsterStat = currentPlayerMonster.getDefense();}
							Spot currentPlayerSpot = playersRevealedMonsterSpots.get(j);
							//if the ai's card is stronger
							if(!currentMonster.weakTo(currentPlayerMonster) && (currentMonster.getAttack() > currentPlayerMonsterStat || currentPlayerMonster.weakTo(currentMonster))) {
								getAction(ActionList.ATTACK).performAction(
										duelingState.getOpponent(),duelingState.getPlayer(),
										currentMonsterSpot,currentPlayerSpot,
										duelingState.getGame(),duelingState.getBoard(),
										duelingState.getOpponentField(),duelingState.getPlayerField()
										);
								break;
							}
	
						}//end of for loop through the players cards
									
						//if your current card could not beat any of the players revealed cards
						ArrayList<Monster> playersNonRevealedCards = duelingState.getPlayerField().getNonRevealedMonsters();
						ArrayList<Spot> playersNonRevealedCardsSpots = duelingState.getPlayerField().getNonRevealedMonstersSpots();
						for(int j = 0; j < playersNonRevealedCards.size();j++) {
							getAction(ActionList.ATTACK).performAction(
								duelingState.getOpponent(),duelingState.getPlayer(),
								currentMonsterSpot,playersNonRevealedCardsSpots.get(j),
								duelingState.getGame(),duelingState.getBoard(),
								duelingState.getOpponentField(),duelingState.getPlayerField()
							);
							break;
						}
						if(!currentMonster.hasUsedAction()) {	
							getAction(ActionList.DEFEND).performAction(currentMonster,duelingState.getOpponent());
						}
					}
						
					numUsedMonstersActivated++;
					if(numUsedMonstersActivated == numUsedMonsters) {
						phase = CHANGE_TURN_PHASE;
					}
				}
			};
			useMonsterTasks.add(task);
			
		}
			
		usedMonsterCard = true;
	}	
	
	private void activateEffect(Monster monster) {
		if(monster instanceof EffectMonster) {
			EffectMonster effectMonster = (EffectMonster)monster;
			if(!effectMonster.hasUsedAction() && !effectMonster.hasUsedEffect()) {
				if(effectMonster.effectCondition(this, duelingState.getPlayer(), duelingState.getOpponentField(), duelingState.getPlayerField())) {
					getAction(ActionList.ACTIVATE_MONSTER_EFFECT).performAction(
							effectMonster,this,duelingState.getPlayer(),duelingState.getOpponentField(),duelingState.getPlayerField(),duelingState.getBoard()
					);
					usedMonsterIndex++;	
				}
			}
		}
	}
	
	/*summons a card basically if it does not require tributes
	 * if it requires tributes then call the summonTributes function 
	 * and returns if it was summoned succesfully or not*/
	private boolean summonMonster(Monster strongestMonster,Spot handSpot,Spot boardSpot) {
		if(strongestMonster.getTributeCost() <=0) {
			//if the board spot is full then you dont want to summon a card so just return false
			if(boardSpot == null) {return false;}
			summonCard(strongestMonster,handSpot,boardSpot);
			return true;
		}
		//if it is a tribute monster
		else {
			return summonTribute(strongestMonster,handSpot);
		}
	}
	
	/*first checks if you have enough cards on your field to summon that tribute.
	 * then loops through the sorted list of monsters from lowest to highest and call the tribute action on the
	 * lowest ones until the number of tributes
	 * reaches the number of tributes required for the monster you pass in*/
	private boolean summonTribute(Monster strongestMonster,Spot handSpot) {
		ArrayList<Monster> sortedMonsterField = duelingState.getOpponentField().getSortedMonstersByAttack();//your monster field sorted
		ArrayList<Spot> sortedMonsterSpots = duelingState.getOpponentField().getSortedMonsterSpots();//the corresponding spots to the sorted monster field
		//if the size of your field is >= to the number of tributes required to summon this monster
		if(sortedMonsterField.size() >= strongestMonster.getTributeCost()) {
			int numPossibleTributes = 0;
			//count how many possible tributes there are that actually have less attack than the monster you want to summon
			for(int i =sortedMonsterField.size()-1; i >-1; i--) {
				if(sortedMonsterField.get(i).getAttack() < strongestMonster.getAttack()) {
					numPossibleTributes++;
				}
				if(numPossibleTributes >= strongestMonster.getTributeCost()) {//this means you found enough possible tributes
					break;
				}
			}
			//if you have enough possible tributes
			if(numPossibleTributes == strongestMonster.getTributeCost()) {
				//loop through the monster field from lowest attack to highest attack
				for(int i =sortedMonsterField.size()-1; i >-1; i--) {
					if(sortedMonsterField.get(i).getAttack() < strongestMonster.getAttack()) {
						getAction(ActionList.TRIBUTE).performAction(sortedMonsterField.get(i),this,sortedMonsterSpots.get(i),duelingState.getBoard(),duelingState.getOpponentField());
						//if you have enough tributes after calling the tribute action
						if(getNumTributes() >= strongestMonster.getTributeCost()) {
							Spot newBoardSpot = getFirstMonsterBoardSpot();//since you got rid of at least 1 spot you need to find the first available one
							if(newBoardSpot == null) {return false;}
							summonCard(strongestMonster,handSpot,newBoardSpot);//summon the card
							return true;
						}
					}
				}
			}
		}
		return false;//you were not able to summon anything
	}
	
	
	/*calls the play card action for whatever card you pass in */
	private void summonCard(Card card,Spot handSpot,Spot boardSpot) {
		getAction(ActionList.PLAY_CARD_FROM_HAND).performAction(duelingState.getOpponent(),handSpot,boardSpot,duelingState.getBoard(),duelingState.getOpponentField());
		card.setRevealed(false);
	}
	
	/*gets the players monster field as a sorted array by attack and then gets the monster at whichever index you enter*/
	private Monster getPlayersBestCard(int index) {
		//gets the highest card from the players field index will start at 0 when this method is called and it will go up to 1, 2, 3 and so on
		ArrayList<Monster> playersMonsters = duelingState.getPlayerField().getRevealedSortedMonstersByAttack();//the array list of sorted monsters on the players field
		if(playersMonsters.size() == 0) {return null;}
		if(index >=playersMonsters.size()-1) {
			return playersMonsters.get(playersMonsters.size()-1);
		}
		return playersMonsters.get(index);
	}
	
	/*loops through the the spots on the monster field and returns the first available spot*/
	private Spot getFirstMonsterBoardSpot() {
		for(int i =0; i < duelingState.getOpponentField().getMonsterSpots().size(); i++) {
			Spot s = duelingState.getOpponentField().getMonsterSpots().get(i);
			if(s.isOpen()) {
				return s;
			}
		}
		return null;
	}
	/*loops through the the spots on the magic field and returns the first available spot*/
	private Spot getFirstMagicCardBoardSpot() {
		for(int i =0; i < duelingState.getOpponentField().getMagicSpots().size(); i++) {
			Spot s = duelingState.getOpponentField().getMagicSpots().get(i);
			if(s.isOpen()) {
				return s;
			}
		}
		return null;
	}
	
	private void clearMonstersHaveGone() {
		for(int i = 0; i < monstersHaveGone.length;i++) {
			monstersHaveGone[i] = false;
		}
	}
	
	public void updateTextPopupPane() {
		if(textPopupPane != null) {
			textPopupPane.update();
		}
	}
	
	public void renderTextPopupPane(Graphics2D g) {
		if(textPopupPane != null) {
			if(textPopupPane.isStart()) {
				textPopupPane.render(g);
			}
		}
	}
	/*returns an action based on the name of the action you give it. to reduce the amount of ugly code*/
	private Action getAction(ActionList action) {
		return DuelingState.actionHandler.getAction(action);
	}
	
	/*getters and setters*/
	public void setDuelingState(DuelingState duelingState) {this.duelingState = duelingState;}
	public DuelingState getDuelingState() {return duelingState;}
	public boolean isAiPhaseDone() {return aiPhaseDone;}
	public int getUsedMonsterIndex() {return usedMonsterIndex;}
	public void setUsedMonsterIndex(int usedMonsterIndex) {this.usedMonsterIndex = usedMonsterIndex;}
}
