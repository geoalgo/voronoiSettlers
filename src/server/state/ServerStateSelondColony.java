package server.state;

import java.util.TreeSet;

import player.*;
import delaunay.*;
import controlor.DB;
import controlor.IGameController;
import client.IClient;
import client.action.*;

public class ServerStateSelondColony extends ServerState{
	public ServerStateSelondColony(IGameController gc,IClient clients[]) {
		super(gc,clients);
	}
	
	
}
