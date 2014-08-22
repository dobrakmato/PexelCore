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
	WAITING_PLAYERS(false, true),
	/**
	 * Game state when game is operational but it's empty.
	 */
	WAITING_EMPTY(false, true),
	/**
	 * Game state, when game is runnning, but players can join in mid-game.
	 */
	PLAYING_CANJOIN(true, true),
	/**
	 * Game sate, when game is running, but no more players can join the running game.
	 */
	PLAYING_CANTJOIN(true, false),
	/**
	 * Game state, when game is reseting it's arena, thus is not operational.
	 */
	RESETING(false, false),
	/**
	 * Game state, when game is not operational at all.
	 */
	DISABLED(false, false);
	
	private boolean	playing;
	private boolean	canjoin;
	
	private GameState(final boolean playing, final boolean canjoin)
	{
		this.playing = playing;
		this.canjoin = canjoin;
	}
	
	public boolean isPlaying()
	{
		return this.playing;
	}
	
	public boolean canJoin()
	{
		return this.canjoin;
	}
}
