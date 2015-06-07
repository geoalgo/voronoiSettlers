package client.state;

import player.Player;
import delaunay.Pnt;

public class ClientStatePositionSelection extends ClientState{
	String msg;
	
	public ClientStatePositionSelection(String msg) {
		this.msg = msg;
	}

	@Override
	public void clickBoard(Pnt p){
		//todo communicate to server
	}
}
