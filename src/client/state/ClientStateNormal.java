package client.state;

import player.Player;

/**
 * Normal state while playing the game. EG waiting for an action such as :
 * build colony,road, city, buy card, play card or next turn.
 * @author david
 */
public class ClientStateNormal extends ClientState {

	@Override
	public void clickEndTurn(){
		//todo next turn
	}

}
