/*
 *Brendan Aucoin
 *03/30/2020
 *Holds the name of each type of action instead of using strings
 * */
package actions;

public enum ActionList {
	PLAY_CARD_FROM_HAND,
	SUMMON_CARD,//this is for effects that summon cards
	ATTACK,
	DEFEND,
	TRIBUTE,
	ACTIVATE_MONSTER_EFFECT,
	ACTIVATE_SPELL,
	ACTIVATE_TRAP,
	END_TURN,
	REMOVE_CARD,
	START_TURN,
}
