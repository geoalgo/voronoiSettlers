package client.action;

import model.card.Card;
import client.IClient;

public class ClientSelection<E> extends ClientAction {
	E selection;
	public ClientSelection(IClient client,E selection) {
		super(client);
		this.selection = selection;
	}
	public E getSelection(){
		return selection;
	}

}
