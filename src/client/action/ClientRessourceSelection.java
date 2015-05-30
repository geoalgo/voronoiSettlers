package client.action;

import model.ressources.Ressource;
import client.IClient;

public class ClientRessourceSelection extends ClientAction {
	Ressource ressource;
	public Ressource getRessource() {
		return ressource;
	}
	ClientRessourceSelection(IClient client,Ressource r) {
		super(client);
		this.ressource = r;
	}

}
