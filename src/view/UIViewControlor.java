package view;

import controlor.DB;
import controlor.GameControlor;
import controlor.SettlersServer;
import controlor.ui.UITrade;

public class UIViewControlor {
	UITrade uiTrade = null;
	SettlersServer server;
	
	UIViewControlor(SettlersServer server){
		this.server = server;
	}
	
	public void	tradePressed(){
		DB.msg("trade menu");
		uiTrade = new UITrade(server,server.getCurrentPlayer());
	}

	public void closePlayerWindows(){
		if(uiTrade!=null){
			uiTrade.done();
			uiTrade = null;
		}
//		if(uiSelectCard!=null){
//			uiSelectCard.done();
//			uiSelectCard = null;
//		}
	}

	
}
