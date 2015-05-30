package client;

import server.IServer;
import client.action.ClientAction;

/**
 * Interface to send to the server.
 * @author david
 *
 */
public interface ISendToServer{
	public void receiveAction(ClientAction action);
}
