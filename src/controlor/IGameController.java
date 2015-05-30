package controlor;

import player.Player;

public interface IGameController {
	public Player getPlayer(int num);
	
	public Player getCurrentPlayer();

}
