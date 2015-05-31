package client;

import model.Model;
import server.IServer;
import server.state.ServerState;
import client.action.ClientAction;

//use sockets there
public class SendToServer implements ISendToServer {
	private IServer server;

	public SendToServer(IServer server){
		this.server=server;
	}
	@Override
	public void receiveAction(ClientAction action) {
		server.receiveAction(action);
	}
	@Override
	public Model getModel() {
		return server.getModel();
	}

}
