package client;

import player.Players;
import model.hexagonalTiling.BoardTiles;


/**
 * Data of the game that a client knows about.
 * @author david
 */
public class GameClientState {
	public final BoardTiles board;
	public final Players players;
	
	public GameClientState(BoardTiles board,Players players){
		this.board = board;
		this.players = players;
	}
}
