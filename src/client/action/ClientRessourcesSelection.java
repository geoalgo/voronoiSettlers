package client.action;

import model.ressources.Ressource;
import model.ressources.Ressources;
import client.IClient;

public class ClientRessourcesSelection extends ClientAction {
	Ressources ressources;
	public Ressources getRessourcesChosen() {
		return ressources;
	}
	public ClientRessourcesSelection(IClient client,Ressources r) {
		super(client);
		this.ressources = r;
	}

}
