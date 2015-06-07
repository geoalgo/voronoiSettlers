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

package client.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import client.IClient;
import client.action.ClientSelection;
import player.Player;
import model.ressources.Ressources;


public class GUILooseRessource implements ActionListener{
	JFrame frame;
	JPanel panel;

	IClient client;
	
	Ressources initialRessource;
	SpinnerNumberModel wood;
	SpinnerNumberModel crop;
	SpinnerNumberModel brick;
	SpinnerNumberModel stone;
	SpinnerNumberModel sheep;

	JButton ok;


	GUILooseRessource(IClient client,Player p){
		this.client = client;
		this.initialRessource = p.getRessource();

		initFrame(p);

		initPanel();
	}

	void initFrame(Player p){
		//      //Create and set up the window.
		frame = new JFrame(p.getName()+", choose "+p.getRessource().num()/2+" number of ressources to loose");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		//Display the window.
		frame.setVisible(true);
	}

	void initPanel(){
		panel = new JPanel();
		frame.add(panel);
		panel.setLayout(new FlowLayout());

		String[] labels = {"Wood","Crop","Brick","Stone","Sheep"};

		wood = new SpinnerNumberModel(0,0,
				initialRessource.numWood(),1);                
		addLabeledSpinner(panel,labels[0],wood);

		crop = new SpinnerNumberModel(0,0,
				initialRessource.numCrop(),1);
		addLabeledSpinner(panel,labels[1],crop);

		brick = new SpinnerNumberModel(0,0,
				initialRessource.numBrick(),1);
		addLabeledSpinner(panel,labels[2],brick);

		stone = new SpinnerNumberModel(0,0,
				initialRessource.numStone(),1);
		addLabeledSpinner(panel,labels[3],stone);

		sheep = new SpinnerNumberModel(0,0,
				initialRessource.numSheep(),1);
		addLabeledSpinner(panel,labels[4],sheep);

		ok = new JButton("Ok");

		panel.add(ok);

		ok.addActionListener(this);
		frame.pack();
	}



	static protected JSpinner addLabeledSpinner(Container c,
			String label,
			SpinnerModel model) {
		JLabel l = new JLabel(label);
		c.add(l);

		JSpinner spinner = new JSpinner(model);
		l.setLabelFor(spinner);
		c.add(spinner);

		return spinner;
	}


	/**
	 * Create the GUI and show it.  For thread safety,
	 * this method should be invoked from the
	 * event dispatch thread.
	 */
	public static void createAndShowGUI(
			IClient client,
			Player p) {
		GUILooseRessource ui = new GUILooseRessource(client,p);
	}

	private Ressources getRessources(){
		Ressources res = new Ressources();
		res.addWood((int)wood.getValue());
		res.addCrop((int)crop.getValue());
		res.addBrick((int)brick.getValue());
		res.addStone((int)stone.getValue());
		res.addSheep((int)sheep.getValue());
		return res;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == ok){
			Ressources res = getRessources();
			if(res.num() == initialRessource.num()/2){
				client.sendAction(new ClientSelection<Ressources>(client,res));
				frame.setVisible(false);
				frame.dispose();
			}
			else{
				System.out.println("Wrong number of released ressources");
				System.out.println("Expected "+initialRessource.num()/2+ " and got "+res.num());
			}
		}
	}


}
