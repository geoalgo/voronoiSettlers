/**
* Voronoi settlers- An implementation of the board game Settlers of 
* Catan.
* This file Copyright (C) 2013-2014 David Salinas <catan.100.sisisoyo@spamgourmet.com>
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 3
* of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*
* The maintainer of this program can be reached at catan.100.sisisoyo@spamgourmet.com
**/

package controlor.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.hexagonalTiling.harbor.Harbor;
import model.hexagonalTiling.harbor.HarbourRessource;
import model.ressources.Crop;
import model.ressources.Ressource;
import player.Player;
import controlor.DB;
import controlor.GameControlor;

public class UITrade extends JFrame implements ActionListener {
	Player p;
	GameControlor gc;
	UIControlor uicontrolor; // for the call back

	JPanel panel0;
	JPanel panel1;
	JPanel panel2;
	
	JLabel ressText;
	JComboBox<PossibleTrade> ressourceToPay;
	JComboBox<Ressource> ressourceToGet;
	JButton exchange;
	JButton done;


	UITrade(Player p,GameControlor gc){
		super("Trade");
		this.p = p;
		this.gc = gc;
		initGui();
	}

	private void initGui(){
		setLayout(new GridLayout(3, 1));

		ressText = new JLabel(p.getRessource().toNiceString());
		
		panel0 = new JPanel();
		add(panel0);
		panel0.add(ressText);
		
		panel1 = new JPanel();
		add(panel1);
		panel1.setLayout(new FlowLayout());
		exchange = new JButton("exchange");
		done = new JButton("done");
		exchange.addActionListener(this);
		done.addActionListener(this);
		
		panel1.add(new JLabel("give "));
		ressourceToPay = new JComboBox<PossibleTrade>();
		setRessourcesToPay();
		panel1.add(ressourceToPay);

		panel1.add(new JLabel(" to get "));		
		ressourceToGet = new JComboBox<Ressource>();
		for(Ressource ress : Ressource.allRessources())
			ressourceToGet.addItem(ress);
		panel1.add(ressourceToGet);

		panel1.add(exchange);

		panel2 = new JPanel();
		panel2.add(done);
		add(panel2);

		this.pack();
		this.setPreferredSize(new Dimension(300,150));
		this.setResizable(false);
		this.setVisible(true);
	}

	private void setRessourcesToPay(){
		ressourceToPay.removeAllItems();
		for(PossibleTrade trade : getPossibleInternalTrades())
			ressourceToPay.addItem(trade);
	}
	
	private void update(){
		panel0.removeAll();
		ressText = new JLabel(p.getRessource().toNiceString());
		panel0.add(ressText);
		
		setRessourcesToPay();
		if(gc!=null)
		gc.getUIControlor().updateView();
	}

	class PossibleTrade{
		int cost;
		Ressource ress;

		PossibleTrade(int cost,Ressource ress){
			this.cost = cost;
			this.ress = ress;
		}

		public String toString(){
			String res = cost+" "+ress;
			return res;
		}

	}

	private Collection<PossibleTrade> getPossibleInternalTrades(){
		Collection<PossibleTrade> res = new Vector<PossibleTrade>();

		res.addAll(getTwoToOneTrades());
		res.addAll(getThreeToOneTrades());
		res.addAll(getFourToOneTrades());
		return res;
	}

	private Collection<PossibleTrade> getTwoToOneTrades(){
		Collection<PossibleTrade> res = new Vector<PossibleTrade>();
		for(Harbor harbor : p.getHarbors()){
			if(harbor.hasRessource()){
				int numRessPlayer = p.getRessource().getNum(harbor.getRessource());
				if(numRessPlayer>=2){
					res.add(new PossibleTrade(2, harbor.getRessource()));
				}
			}
		}
		return res;
	}


	private Collection<PossibleTrade> getThreeToOneTrades(){
		Collection<PossibleTrade> res = new Vector<PossibleTrade>();
		for(Ressource ress : Ressource.allRessources()){
			int numRessPlayer = p.getRessource().getNum(ress); 
			if(numRessPlayer>=3 && p.hasThreeToOneHarbor()){
				res.add(new PossibleTrade(3,ress));
			}	
		}
		return res;
	}

	private Collection<PossibleTrade> getFourToOneTrades(){
		Collection<PossibleTrade> res = new Vector<PossibleTrade>();
		for(Ressource ress : Ressource.allRessources()){
			int numRessPlayer = p.getRessource().getNum(ress); 
			if(numRessPlayer>=4){
				res.add(new PossibleTrade(4, ress));
			}	
		}
		return res;
	}



	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource()== done){
			done();
		}
		if(arg0.getSource() == exchange){
			if(ressourceToPay.getItemCount()==0) return;
			//do exchange
			PossibleTrade paidRess = ((PossibleTrade)ressourceToPay.getSelectedItem());
			Ressource aquiredRess = ((Ressource)ressourceToGet.getSelectedItem());
			
			p.getRessource().add(paidRess.ress, -paidRess.cost);
			p.getRessource().add(aquiredRess,1);

			update();
		}
	}
	
	public void done(){
		setVisible(false);
		dispose();
	}

	public static void main(String[] args){
		Player p = new Player(Color.red, "JO", 0);
		//		p.addHarbor(new HarborThreeToOne());
		p.addHarbor(new HarbourRessource(new Crop()));
		p.getRessource().addBrick(3);
		p.getRessource().addCrop(4);
		p.getRessource().addWood(1);
		UITrade uiTrade = new UITrade(p, null);
	}

}
