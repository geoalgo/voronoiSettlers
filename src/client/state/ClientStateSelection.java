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
	public boolean canBeCanceled;
	
	public ClientStateSelection(String msg,Collection<E> selection){
		this.msg = msg;
		this.selection = selection;
		this.canBeCanceled = false;
	}
	
	/**
	 * @param selection elements to select
	 * canBeCanceled indicates if the client can just close the window and return to normal state
	 */
	public ClientStateSelection(String msg,Collection<E> selection,boolean canBeCanceled){
		this.msg = msg;
		this.selection = selection;
		this.canBeCanceled = canBeCanceled;
	}

}
