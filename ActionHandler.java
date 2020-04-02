/*
 *Brendan Aucoin
 *03/30/2020
 *Holds a map of all actions it maps an elements of the action list to an action object.
 *maps an enum to an action that can be performed
 * */
package actions;

import java.util.HashMap;

public class ActionHandler {
	private HashMap<ActionList,Action> actions;
	public ActionHandler() {
		actions = new HashMap<ActionList,Action>();
		init();
	}
	/*will put all the action objects in the map*/
	private void init() {
		actions.put(ActionList.PLAY_CARD_FROM_HAND,new PlayCardAction());
		actions.put(ActionList.DEFEND,new DefendAction());
		actions.put(ActionList.TRIBUTE, new TributeAction());
		actions.put(ActionList.ACTIVATE_MONSTER_EFFECT, new MonsterEffectAction());
		actions.put(ActionList.ACTIVATE_SPELL,new ActivateSpellAction());
		actions.put(ActionList.END_TURN, new EndTurnAction());
		actions.put(ActionList.REMOVE_CARD, new RemoveCardAction());
		actions.put(ActionList.START_TURN,new StartTurnAction());
	}
	/*returns an action based on the action list enum you pass in*/
	public Action getAction(ActionList action) {
		return actions.get(action);
	}

	
}
