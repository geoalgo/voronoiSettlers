package client.action;

import client.IClient;
import player.Player;
import delaunay.Pnt;;

public class ClientActionClick extends ClientAction{
	Pnt point;
	
	/**
	 * @param player
	 * @param the point should be at absolute catan coordinates in [0,1]^2
	 */
	public ClientActionClick(IClient client,Pnt point){
		super(client);
		this.point = point;
	}
	public Pnt getPoint() {
		return point;
	}
}
