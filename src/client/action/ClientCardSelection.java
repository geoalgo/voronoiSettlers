package client.action;

import model.card.Card;
import client.IClient;

public class ClientCardSelection extends ClientAction {
	Card chosenCard;
	ClientCardSelection(IClient client,Card chosenCard) {
		super(client);
		this.chosenCard = chosenCard;
	}
	public Card getChosenCard(){
		return chosenCard;
	}

}
