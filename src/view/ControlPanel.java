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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controlor.IWindowControlor;
import controlor.WindowControlor;

@SuppressWarnings("serial")
public class ControlPanel extends JPanel{

	private JButton tradeButton;
	private JButton buyCardButton;
	private JButton playCardButton;
	private JButton nextTurnButton;
	
	public ControlPanel(IWindowControlor parentWindow) {
		buyCardButton = new JButton("Buy card");
		playCardButton = new JButton("Play card");
		nextTurnButton = new JButton("End turn");
		tradeButton = new JButton("Trade");
		
		buyCardButton.addActionListener(parentWindow);
		playCardButton.addActionListener(parentWindow);
		tradeButton.addActionListener(parentWindow);
		nextTurnButton.addActionListener(parentWindow);
		
		this.add(buyCardButton);
		this.add(playCardButton);
		this.add(tradeButton);
		this.add(nextTurnButton);
	}

	public boolean isTradeButton(Object o){
		return o == tradeButton;
	}
	
	public boolean isNextTurnButton(Object o){
		return o == nextTurnButton;
	}

	public boolean isBuyCardButton(Object o){
		return o == buyCardButton;
	}
	
	public boolean isPlayCardButton(Object o){
		return o == playCardButton;
	}
	
	public boolean isButton(Object o){
		return isTradeButton(o) ||
				isNextTurnButton(o) || 
				isBuyCardButton(o) || 
				isPlayCardButton(o);
	}

}
