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
	public String msg;
	public Collection<E> selection;
	/**
	 * @param selection elements to select
	 */
	public ClientStateSelection(String msg,Collection<E> selection){
		this.msg = msg;
		this.selection = selection;
	}

}
