package client.state;


import java.awt.event.ActionListener;

import player.Player;
import delaunay.Pnt;

/**
 * State of the client. Redirects clicks and action there.
 * @author david
 *
 */
public abstract class ClientState{
	
	/**
	 * What happens when one clicks one the board.
	 * @param p
	 */
	public void clickBoard(Pnt p){
	}
	
	public void clickBuyCard(){
	}
	
	public void clickPlayCard(){
	}
	
	public void clickTrade(){
	}
	
	public void clickEndTurn(){
	}
	
}
