package client.action;

import java.awt.event.KeyEvent;

import com.sun.corba.se.impl.oa.poa.ActiveObjectMap.Key;

import client.IClient;
import player.Player;
import delaunay.Pnt;;

//When a client presses a key
public class ClientActionKey extends ClientAction{
	KeyEvent e;
	
	/**
	 * @param player
	 * @param the point should be at absolute catan coordinates in [0,1]^2
	 */
	public ClientActionKey(IClient client,KeyEvent e){
		super(client);
		this.e = e;
	}
	public KeyEvent getKey() {
		return e;
	}
}
