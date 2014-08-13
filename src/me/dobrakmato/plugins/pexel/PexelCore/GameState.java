package me.dobrakmato.plugins.pexel.PexelCore;

/**
 * Specifies minigame's current state.
 * 
 * @author Mato Kormuth
 * 
 */
public enum GameState
{
	/**
	 * Game state when game is operational and is not empty and it's avaiting more players to start game.
	 */
	WAITING_PLAYERS,
	/**
	 * Game state when game is operational but it's empty.
	 */
	WAITING_EMPTY,
	/**
	 * Game state, when game is runnning, but players can join in mid-game.
	 */
	PLAYING_CANJOIN,
	/**
	 * Game sate, when game is running, but no more players can join the running game.
	 */
	PLAYING_CANTJOIN,
	/**
	 * Game state, when game is reseting it's arena, thus is not operational.
	 */
	RESETING,
	/**
	 * Game state, when game is not operational at all.
	 */
	DISABLED
}
