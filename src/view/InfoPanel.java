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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import controlor.DB;

@SuppressWarnings("serial")
public class InfoPanel extends JPanel {

	JTextArea text;
	JScrollPane scroll;

	public InfoPanel() {
		text = new JTextArea(5,40);
		text.setEditable(false);
		this.add(text);
		
	    scroll = new JScrollPane(text);
	    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

	    //Add Textarea in to middle panel
	    this.add(scroll);
		
	}
	
	
	public void setMessage(String msg){
	    String currentTxt = text.getText();
//	    text.append(currentTxt + msg);
	    text.setText(msg);
	}
	

	public void appendMessage(String msg){
	    String currentTxt = text.getText();
	    DB.msg("app msg:"+msg);
	    text.append("\n"+ msg);
	}

	

}
