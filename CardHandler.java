/*
 *Brendan Aucoin
 *04/02/2020
 *holds a map of all the names of the cards to an instance of that card
 * */
package cards;

import java.io.FileNotFoundException;
import java.util.HashMap;

public class CardHandler {
	public static HashMap<CardList,Card> allCards;//the map of all cards in the game
	public CardHandler() {
		allCards = new HashMap<CardList,Card>();
		addCards();
	}
	
	private void addCards() {
		try {
			allCards.put(CardList.DORON,new Doron());
			allCards.put(CardList.WATER_OMOTICS,new WaterOmotics());
			allCards.put(CardList.FLAME_VIPER,new FlameViper());
			allCards.put(CardList.SWORDSMAN_OF_LANDSTAR,new SwordsmanOfLandstar());
			allCards.put(CardList.THE_LEGENDARY_FISHERMAN,new TheLegendaryFisherman());
			allCards.put(CardList.CHANGE_OF_HEART,new ChangeOfHeart());
			allCards.put(CardList.TRAP_HOLE,new TrapHole());
			allCards.put(CardList.MYSTICAL_SPACE_TYPHOON,new MysticalSpaceTyphoon());
			allCards.put(CardList.GAIA_THE_FIERCE_KNIGHT,new GaiaTheFierceKnight());
			allCards.put(CardList.HARPIES_PET_BABY_DRAGON,new HarpiesPetBabyDragon());
			allCards.put(CardList.RIGHT_LEG_OF_THE_FORBIDDEN_ONE,new RightLegOfTheForbiddenOne());
			allCards.put(CardList.OBELISK_THE_TORMENTOR,new ObeliskTheTormentor());
		}catch(FileNotFoundException e) {
				System.out.println("file not found for creating card stats");
		}
	}
	/*returns a card based on the card name you give it*/
	public static Card getCard(CardList cardName) throws CloneNotSupportedException{
		return allCards.get(cardName).clone();
	}
}
