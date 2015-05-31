package client.state;

import java.util.Collection;

import player.Player;


/**
 * State when the client is waiting for a selection ex card, player to steal and 
 * buttons are not valid.
 * @author david
 *
 */
public class ClientStateSelection<E> extends ClientState {
	/**
	 * @param msg to be displayed to the client ("ex choose a player to steal")
	 * @param selection elements to select
	 */
	public ClientStateSelection(String msg,Collection<E> selection){
		
	}

}
