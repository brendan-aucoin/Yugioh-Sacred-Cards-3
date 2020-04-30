/*
 *Brendan Aucoin
 *04/03/2020
 *the state where two monsters attack each other or a monster attacks a duelist directly
 *this state lasts for a certain amount of time then goes back to the dueling state
 * */
package states;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import actions.ActionList;
import boards.Board;
import cards.Monster;
import dueling.Field;
import dueling.Spot;
import entities.Particle;
import game.Game;
import gui.Display;
import images.Animation;
import images.Texture;
import interaction_pane.InteractionPane;
import player.Ai;
import player.Duelist;

public class AttackingState extends InteractionPane implements State{
	private Monster attackersMonster,receiversMonster;
	private Spot attackersMonsterSpot,receiversMonsterSpot;
	private Rectangle attackersMonsterBounds,receiversMonsterBounds;
	private Duelist attacker,receiver;
	private boolean started;
	private Timer timer;
	private Duelist loser;
	private Rectangle loserBounds;
	private boolean bothLost;
	private boolean destroyMonster;
	private int damageTaken;
	private float attackersHealthAlpha;
	private float receiversHealthAlpha;
	private float attackersCardAlpha;
	private float receiversCardAlpha;
	private int newHealth;
	private Animation swordSlashAnimation;
	private boolean startSwordSlashAnimation;
	private Board board;
	private Field attackersField,receiversField;
	private boolean transparencyFinished;
	private final int NUM_PARTICLES = 50;
	private final int PARTICLE_SIZE  = 25;
	private final int PARTICLE_LIFE = 30;
	private Particle [] particles,secondParticles;
	private boolean doingInteraction;
	private Random rand;
	public AttackingState(Game game) {
		super(game,Display.FULL_SCREEN);
		init();
	}
	/*set all of the variables to null or the default*/
	public void init() {
		super.init();
		attackersMonster = null;
		receiversMonster = null;
		attackersMonsterSpot = null;
		receiversMonsterSpot = null;
		attackersMonsterBounds = new Rectangle(
				(int) (Display.SCREEN_SIZE.width/2 - Display.SCREEN_SIZE.width/2.3),
				(int) (Display.SCREEN_SIZE.height/2 - Display.SCREEN_SIZE.height/2.6),
				(int) (Display.SCREEN_SIZE.width/3),
				(int) (Display.SCREEN_SIZE.height/1.3)
		);
		receiversMonsterBounds = new Rectangle(
				(int) (Display.SCREEN_SIZE.width/2 + Display.SCREEN_SIZE.width/10),
				(int) (Display.SCREEN_SIZE.height/2 - Display.SCREEN_SIZE.height/2.6),
				(int) (Display.SCREEN_SIZE.width/3),
				(int) (Display.SCREEN_SIZE.height/1.3)
		);
		attacker = null;
		receiver = null;
		started = false;
		timer = new Timer();
		loser = null;
		loserBounds = new Rectangle();
		bothLost = false;
		damageTaken = 0;
		destroyMonster = false;
		attackersHealthAlpha = 0;
		attackersCardAlpha =1;
		receiversHealthAlpha = 0;
		receiversCardAlpha = 1;
		newHealth = Duelist.STARTING_HEALTH;
		swordSlashAnimation = new Animation(2,Texture.swordSlash);
		startSwordSlashAnimation = false;
		board = null;
		attackersField = null;
		receiversField =null;
		transparencyFinished = false;
		rand = new Random();
		particles = new Particle[NUM_PARTICLES];
		secondParticles = new Particle[NUM_PARTICLES];
		doingInteraction = true;
	}
	
	public void update() {
		if(started) {
			//if the animation is over then start destroying the monster by making it more transparent
			if(swordSlashAnimation.isOver()) {
				startSwordSlashAnimation = false;
				//make the losing card go more transparent
				if(destroyMonster) {
					if(!transparencyFinished) {
						makeCardTransparent();
					}
				}
				
				//if a monster wasnt destroyed
				else {transparencyFinished = true;}
				/*once the card is fully gone start decreasing the life points */
				if(transparencyFinished) {
					reduceHealth();
				}
			}
			//if the animation isnt over then run the animation and update all the particles
			else {
				swordSlashAnimation.runAnimation();
				for(int i=0; i < NUM_PARTICLES;i++) {
					if(particles[i] != null) {
						particles[i].update();
					}
					if(secondParticles[i] !=null) {
						secondParticles[i].update();
					}
				}
			}
		}
	}
	
	private void reduceHealth() {
		//if there was only 1 loser
		if(loser != null) {
			if(loser == attacker) {
				doHealthAnimation(attacker,receiversMonster);
			}
			else if(loser == receiver) {
				doHealthAnimation(receiver,attackersMonster);
			}
		}

		//if both lost then neither healths go down.
		if(bothLost) {try {Thread.sleep(50);endState();return;}catch(InterruptedException e) {e.printStackTrace();}}
	}
	
	private void doHealthAnimation(Duelist loser,Monster winningMonster) {
		loser.setHealth(loser.getHealth() - winningMonster.getAttack()/50);//slowly decrease the life points
		//schedule an event to make this state end 
		if(loser.getHealth() <=0) {
			 loser.setHealth(0);
			timer.schedule(new TimerTask() {
				public void run() {
					endState();
				}
			}, 100);
			return;
		}
		/*same thing as 0*/
		 else if(loser.getHealth() <= newHealth) {
			 loser.setHealth(newHealth);
			timer.schedule(new TimerTask() {
				public void run() {
					endState();
				}
			}, 100);
			return;
		}
		
	}
	private void makeCardTransparent() {
		//if there was only 1 loser
		if(loser != null) {
			//if the loser is the attacker
			if(loser == attacker) {
				attackersCardAlpha -= 0.06f;//decrease the cards alpha
				attackersHealthAlpha +=1f;//incraese the health alpha
				if(attackersCardAlpha <=0) {
					attackersCardAlpha = 0;
					transparencyFinished = true;
				}
				if(attackersHealthAlpha >= 1) {attackersHealthAlpha = 1;}
			}
			
			else if(loser == receiver) {
				receiversCardAlpha -= 0.06f;
				receiversHealthAlpha += 1f;
				if(receiversCardAlpha <=0) {
					receiversCardAlpha = 0;
					transparencyFinished = true;
				}
				if(receiversHealthAlpha >= 1) {receiversHealthAlpha = 1;}
			}
		}
		else if(bothLost) {
			attackersCardAlpha -= 0.06;
			receiversCardAlpha -= 0.06;
			
			if(attackersCardAlpha <= 0) {
				attackersCardAlpha = 0;
				transparencyFinished = true;
			}
			if(receiversCardAlpha <=0) {
				receiversCardAlpha = 0;
				transparencyFinished = true;
			}
			attackersHealthAlpha += 1f;
			if(attackersHealthAlpha >= 1) {attackersHealthAlpha =1;}
			receiversHealthAlpha += 1f;
			if(receiversHealthAlpha >= 1) {receiversHealthAlpha = 1;}
		}
	}
	
	/*finds out which card one the interaction*/
	private void decideWinner() {
		//if the receiver doesnt have a monster then the attacker won by default
		if(receiversMonster == null) {
			receiversHealthAlpha = 1;
			damageTaken = attackersMonster.getAttack();
			loser = receiver;
			loserBounds = receiversMonsterBounds;
			newHealth = receiver.getHealth() - damageTaken;
			if(newHealth <=0) {newHealth = 0;}
			destroyMonster = true;
			return;
		}
		/*attribute check*/
		
		//if the attackers monster is weak to the receivers monster and the receivers monster is weak to the attackers monster
		if(receiversMonster.weakTo(attackersMonster) && attackersMonster.weakTo(receiversMonster)) {
			damageTaken = 0;
			bothLost();
			destroyMonster = true;
		}
		//if the attacker wins by attribute
		else if(receiversMonster.weakTo(attackersMonster)) {
			//if the receiver was in defense then the receiver takes no damage
			if(receiversMonster.isInDefense()) {
				damageTaken = 0;
				receiverLost();
				destroyMonster = true;
			}
			//if the receiver was in attack
			else{
				receiverLost();
				damageTaken = attackersMonster.getAttack() - receiversMonster.getAttack();
				destroyMonster = true;
			}
		}
		//if the attacker loses by attribute
		else if(attackersMonster.weakTo(receiversMonster)) {
			int stat = receiversMonster.isInDefense() ? receiversMonster.getDefense() : receiversMonster.getAttack();//the attack or defense of the receivers monster
			damageTaken = stat - attackersMonster.getAttack();
			attackerLost();
			destroyMonster = true;
		}
		
		//if no one lost because of attribute
		else {
			//if the receivers monster is in defense
			if(receiversMonster.isInDefense()) {
				//if the attacker attack is > than the receivers monster's defense
				if(attackersMonster.getAttack() > receiversMonster.getDefense()) {
					damageTaken = 0;
					receiverLost();
					destroyMonster = true;
				}
				//if the attacker attack is < than the receivers monster's defense
				else if(attackersMonster.getAttack() < receiversMonster.getDefense()) {
					damageTaken = receiversMonster.getDefense() - attackersMonster.getAttack();
					destroyMonster = false;
					attackersHealthAlpha = 1;
					//attackersCardAlpha = 0.5f;
					loser = attacker;
					loserBounds = attackersMonsterBounds;
					newHealth = attacker.getHealth() - damageTaken;
					if(newHealth <=0) {newHealth = 0;}
				}
				//if the attackers attack equals the receivers defense
				else {
					bothLost();
					damageTaken = 0;
					destroyMonster = true;
				}
			}
			
			//if the receivers monster is in attack
			else {
				//if the attackers attack is greater than the receivers attack
				if(attackersMonster.getAttack() > receiversMonster.getAttack()) {
					damageTaken = attackersMonster.getAttack() - receiversMonster.getAttack();
					receiverLost();
					destroyMonster = true;
				}
				//if the attackers attack is less than the receivers attack
				else if(attackersMonster.getAttack() < receiversMonster.getAttack()) {
					damageTaken = receiversMonster.getAttack() - attackersMonster.getAttack();
					attackerLost();
					destroyMonster = true;
				}
				//if the attackers attack is equal to the receivers attack
				else {
					damageTaken =0;
					bothLost();
					destroyMonster = true;
				}
			}
		}
		
		
		
		if(damageTaken < 0) {damageTaken = 0;}
		if(loser != null) {
			newHealth = loser.getHealth() - damageTaken;
		}
		//remove both cards from the board
		if(bothLost) {
			DuelingState.actionHandler.getAction(ActionList.REMOVE_CARD).performAction(receiversMonsterSpot,board,receiversField);
			DuelingState.actionHandler.getAction(ActionList.REMOVE_CARD).performAction(attackersMonsterSpot,board,attackersField);
		}
	}
	/*sets the loser to the receiver and updates the damage taken*/
	private void receiverLost() {
		loser = receiver;
		loserBounds = receiversMonsterBounds;
		newHealth = receiver.getHealth() - damageTaken;
		if(newHealth <=0) {newHealth = 0;}
		DuelingState.actionHandler.getAction(ActionList.REMOVE_CARD).performAction(receiversMonsterSpot,board,receiversField);
	}
	/*sets the loser to the attacker and updates the damage taken*/
	private void attackerLost() {
		loser = attacker;
		loserBounds = attackersMonsterBounds;
		newHealth = attacker.getHealth() - damageTaken;
		if(newHealth <=0) {newHealth = 0;}
		DuelingState.actionHandler.getAction(ActionList.REMOVE_CARD).performAction(attackersMonsterSpot,board,attackersField);
	}
	
	private void bothLost() {
		bothLost = true;
	}
	
	public void render(Graphics2D g) {
		renderBackground(g);
		if(destroyMonster) {
			renderHealth(g);//you should render the health underneath the cards
			renderCards(g);
		}
		else {//if there were no monsters destroyed then show your health over the cards
			renderCards(g);
			renderHealth(g);
		}
		if(startSwordSlashAnimation) {//run the animation and show the particles
			renderSlashingAnimation(g);
			renderParticles(g);
		}
	}
	/*renders all the particles*/
	private void renderParticles(Graphics2D g) {
		for(int i=0; i < NUM_PARTICLES;i++) {
			if(particles[i] != null) {
				particles[i].render(g);
			}
			if(secondParticles[i] != null) {
				secondParticles[i].render(g);
			}
		}
	}
	/*renders the animation for attacking*/
	private void renderSlashingAnimation(Graphics2D g) {
			//if only one player lost
			if(!bothLost) {
				if(loserBounds != null) {
					swordSlashAnimation.drawAnimation(g, (int) (loserBounds.x + loserBounds.width/2 - loserBounds.width/4), loserBounds.y + loserBounds.height/4,3,3);
				}	
			}
			//if both players lost the interaction
			else if(bothLost) {
				swordSlashAnimation.drawAnimation(g, (int) (attackersMonsterBounds.x + attackersMonsterBounds.width/2 - attackersMonsterBounds.width/4), attackersMonsterBounds.y + attackersMonsterBounds.height/4,3,3);
				swordSlashAnimation.drawAnimation(g, (int) (receiversMonsterBounds.x + receiversMonsterBounds.width/2 - receiversMonsterBounds.width/4), receiversMonsterBounds.y + receiversMonsterBounds.height/4,3,3);
			}
		}
	/*renders both cards scaled up*/
	private void renderCards(Graphics2D g) {
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1));
		if(attackersMonster != null) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,attackersCardAlpha));
			g.drawImage(attackersMonster.getImage(), attackersMonsterBounds.x, attackersMonsterBounds.y, attackersMonsterBounds.width, attackersMonsterBounds.height,null);
		}
		if(receiversMonster != null) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,receiversCardAlpha));
			g.drawImage(receiversMonster.getImage(), receiversMonsterBounds.x, receiversMonsterBounds.y, receiversMonsterBounds.width, receiversMonsterBounds.height,null);
		}
	}
	/*render the health*/
	private void renderHealth(Graphics2D g) {
		g.setFont(new Font("Lucida Handwriting",Font.PLAIN,110));
		g.setColor(new Color(0,191,255));
		//the attacker health
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,attackersHealthAlpha));
		if(attacker != null && attackersMonsterBounds != null) {
			renderHealth(g,String.valueOf(attacker.getHealth()),attackersMonsterBounds);
		}
		//the receiver health
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,receiversHealthAlpha));
		if(receiver != null && receiversMonsterBounds != null) {
			renderHealth(g,String.valueOf(receiver.getHealth()), receiversMonsterBounds);
		}
	}
	/*render a specific string*/
	private void renderHealth(Graphics2D g,String str, Rectangle bounds) {
		g.drawString(str, 
				(int) (bounds.x + bounds.width/2 
				- g.getFont().getStringBounds(str, frc).getWidth()/2),
				bounds.y + bounds.height/2)
			;
	}
	/*draws a black background*/
	private void renderBackground(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fill(getBounds());
	}
	
	/*sets the info for this attacking interaction*/
	public void setAttackingInfo(Duelist attacker,Duelist reciever,Spot attackersMonsterSpot,Spot recieversMonsterSpot,Board board,Field attackersField,Field receiversField) {
		this.attacker = attacker;
		this.receiver = reciever;
		this.attackersMonsterSpot = attackersMonsterSpot;
		this.receiversMonsterSpot = recieversMonsterSpot;
		if(attackersMonsterSpot != null) {this.attackersMonster = (Monster)attackersMonsterSpot.getCard();}
		if(receiversMonsterSpot != null) {this.receiversMonster = (Monster)receiversMonsterSpot.getCard();}
		this.board = board;
		this.attackersField = attackersField;
		this.receiversField = receiversField;
	}
	/*schedule a task for starting the state which will start with deciding a winner and creating the particles*/
	public void startState() {
		timer.schedule(new TimerTask() {
			public void run() {
				decideWinner();
				populateParticles();
				doingInteraction = true;
				started = true;
				startSwordSlashAnimation = true;
			}
		}, 300);
	}
	/*creates both arrays for particles*/
	private void populateParticles() {
		if(bothLost) {
			populateParticles(particles,attackersMonsterBounds);
			populateParticles(secondParticles,receiversMonsterBounds);
		}
		if(loserBounds != null && !bothLost) {
			populateParticles(particles,loserBounds);
		}
	}
	
	/*populates an array of particles with a random velX, velY, and a colour withing a range*/
	private void populateParticles(Particle[] particles,Rectangle bounds) {
		for(int i =0; i< particles.length;i++) {
			float randVelX = rand.nextInt((8 - -8) + 1) + -8;
			float randVelY = rand.nextInt((8 - -8) + 1) + -8;
			int randR = rand.nextInt((255 - 153) + 1) + 153;
			int randG = rand.nextInt((178 - 0) + 1) + 0;
			int randB = rand.nextInt((102 - 0) + 1) + 0;
			Color randCol = new Color(randR,randG,randB);
			particles[i] = new Particle(bounds.x + bounds.width/2,bounds.y + bounds.height/2,PARTICLE_SIZE,PARTICLE_SIZE,randVelX,randVelY,PARTICLE_LIFE,randCol);
		}
	}
	/*sets everything back to the default*/
	public void endState() {
		if(attacker instanceof Ai) {
			Ai ai = (Ai)attacker;
			ai.setUsedMonsterIndex(ai.getUsedMonsterIndex()+1);
		}
		started = false;
		bothLost = false;
		destroyMonster = false;
		startSwordSlashAnimation = false;
		swordSlashAnimation.reset();
		this.attacker = null;
		this.attackersField = null;
		this.attackersMonster = null;
		this.attackersMonsterSpot = null;
		this.board = null;
		this.attackersCardAlpha = 1;
		this.attackersHealthAlpha = 0;
		this.receiversCardAlpha = 1;
		this.receiversHealthAlpha = 0;
		this.damageTaken =0;
		this.loser= null;
		this.receiver = null;
		this.receiversField = null;
		this.receiversMonster = null;
		this.receiversMonsterSpot = null;
		this.newHealth = Duelist.STARTING_HEALTH;
		this.transparencyFinished = false;
		this.particles = new Particle[NUM_PARTICLES];
		this.secondParticles = new Particle[NUM_PARTICLES];
		this.doingInteraction = false;
		getGame().setState(StateList.DUELING);
	}
	
	public boolean isStarted() {return started;}
	public boolean isDoingInteraction() {return doingInteraction;}
}

