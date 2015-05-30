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

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import controlor.DB;
import controlor.GameController;
import controlor.gamestate.GameState;
import delaunay.Pnt;

import player.Player;

/**
 * Display the list of players that can be stolen.
 * Callback the callbackState.apply(int selected) methods when chosen.
 * @author david
 */
public class UIChoosePlayerToSteal extends GameState implements ActionListener {
	JFrame chooseMenu;
	JButton ok;
	JComboBox<PlayerNumber> players;
	GameState callbackState;
	
	class PlayerNumber{
		public int numPlayer;
		public String name;
		public PlayerNumber(int numPlayer,String name){
			this.numPlayer = numPlayer;
			this.name = name;
		}
		public String toString(){
			return name;
		}
	}
	
	public UIChoosePlayerToSteal(GameState stateToRestore,GameController gc,Collection<Player> ennemies){
		super(gc);
		this.callbackState = stateToRestore;
		chooseMenu = new JFrame("Choose ennemy to steal");

		chooseMenu.setLayout(new FlowLayout());
		
		players = new JComboBox<PlayerNumber>();
		for(Player p : ennemies)
			players.addItem(new PlayerNumber(p.getNum(),p.getName()));
		chooseMenu.add(players);
		
		ok = new JButton("Ok");
		ok.addActionListener(this);
		chooseMenu.add(ok);

		chooseMenu.setPreferredSize(new Dimension(300, 75));
		
		chooseMenu.setDefaultCloseOperation(chooseMenu.DO_NOTHING_ON_CLOSE);
		chooseMenu.setVisible(true);
		chooseMenu.pack();
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == ok){
			int selected = ((PlayerNumber)players.getSelectedItem()).numPlayer;
			chooseMenu.setVisible(false);
			chooseMenu.dispose();
			callbackState.apply(selected);
		}
	}

	@Override
	public void click(Pnt click) {
		// TODO Auto-generated method stub
	}

	@Override
	public void apply(Object o) {
		// TODO Auto-generated method stub
		
	}

}
