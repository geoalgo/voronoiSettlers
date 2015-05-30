package server.state;

import player.*;
import delaunay.*;
import controlor.IGameController;
import client.action.ClientAction;
import client.action.ClientActionClick;

public class ServerStatePlayTurn extends ServerState{

	public ServerStatePlayTurn(IGameController gc) {
		super(gc);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void receivesAction(ClientAction action) {
		if(!isCurrentPlayer(action.getPlayer()))
			action.getClient().message("It is not your turn");
		if(action instanceof ClientActionClick) 
			click(((ClientActionClick)action).getPoint());
	}
	
	private boolean isCurrentPlayer(Player player) {
		return true;
	}

	private void click(Pnt point){
		
	}

}
