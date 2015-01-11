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

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import controlor.DB;
import controlor.ui.UIChoosePlayerToSteal.PlayerNumber;

import model.card.MonopoleState;
import model.ressources.Ressource;
import model.ressources.Ressources;
import player.Player;

public class UISelectRessourceMonopole extends JFrame implements ActionListener {
	MonopoleState uicontrolor;
	JButton ok;
	JComboBox<Ressource> ressources;
	MonopoleState callBackState;
	
	public UISelectRessourceMonopole(MonopoleState callBackState){
		super("Choose ressource for monopole");
		this.callBackState = callBackState;
		
		setLayout(new FlowLayout());
		
		ressources = new JComboBox<Ressource>();
		for(Ressource r : Ressource.allRessources())
			ressources.addItem(r);
		add(ressources);
		
		ok = new JButton("Ok");
		ok.addActionListener(this);
		add(ok);

		setVisible(true);
		setPreferredSize(new Dimension(300, 75));
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		pack();
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == ok){
			DB.msg("ok button pressed");
			Ressource selected = ((Ressource)ressources.getSelectedItem());
			callBackState.apply(selected);
			setVisible(false);
			dispose();
		}
	}
	
	public static void main(String args[]){
		UISelectRessourceMonopole s = new UISelectRessourceMonopole(null);
	}

}
