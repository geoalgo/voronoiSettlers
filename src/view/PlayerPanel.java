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
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import controlor.DB;

import player.Player;

@SuppressWarnings("serial")

public class PlayerPanel extends JPanel {
	JTextArea textNamePlayer;
	JTextArea text;
	Player p;

	final static Color colorSelected = new Color(200,200,220);
	
	/**
	 * Create the panel.
	 */
	public PlayerPanel(Player p) {
		this.p = p;
		setLayout(new BorderLayout());
		textNamePlayer = new JTextArea(p.getName());
		
		this.add(textNamePlayer,"North");      // Spacing
		
		text = new JTextArea();
		text.setEditable(false);
		this.add(text,"Center");

		endTurn();
		update();
	}
	
	/**
	 * put in bold to show whose turn it is
	 */
	public void setTurn(){
		DB.msg("set turn");
		Font font = textNamePlayer.getFont();
		textNamePlayer.setFont(new Font(font.getFontName(),1,font.getSize()));
		textNamePlayer.setBackground(colorSelected);
	}
	
	public void endTurn(){
		DB.msg("end turn");
		Font font = textNamePlayer.getFont();
		textNamePlayer.setFont(new Font(font.getFontName(),0,font.getSize()));
		textNamePlayer.setBackground(Color.white);
//		this.repaint();
	}

	public void update(){
		text.setText(playerDescr());
	}
	
	public String playerDescr(){
		String res="";
//		res+=p.getName()+"\n\n";
		res+="\nPoints:"+ p.getScore()+"\n\n";
		res+="Ressources:\n";		
		res += p.getRessource().numWood()+" woods\n";
		res += p.getRessource().numBrick()+" brick\n";
		res += p.getRessource().numCrop()+" crop\n";
		res += p.getRessource().numSheep()+" sheep\n";
		res += p.getRessource().numStone()+" stone\n";
		res += p.getRoads()+" Roads\n";
		res += p.getColony()+" Colonies\n";
		res += p.getCity()+" Cities\n";
		res += "\n"+ p.numCards()+ " cards\n";
		
		return res;
	}
	
}

