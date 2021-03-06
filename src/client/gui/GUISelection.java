package client.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;

import client.IClient;
import client.action.ClientActionUndo;
import client.action.ClientSelection;
import client.state.ClientStateSelection;
import player.Player;
import controlor.GameController;
import delaunay.Pnt;
public class GUISelection<E> implements ActionListener {
		JFrame chooseMenu;
		JButton cancel;
		JButton ok;
		JComboBox<E> choiceBox;
		IClient client;
		boolean canBeCanceled;
		
		public GUISelection(ClientStateSelection<E>  cs,IClient client){
			this.canBeCanceled = false;
			init(cs,client);
		}
		
		public GUISelection(ClientStateSelection<E>  cs,IClient client,boolean canBeCanceled){
			this.canBeCanceled = canBeCanceled;
			init(cs,client);
		}
		
		private void init(ClientStateSelection<E>  cs,IClient client){
			this.client = client;
			chooseMenu = new JFrame(cs.msg);
			chooseMenu.setLayout(new FlowLayout());
			
			choiceBox = new JComboBox<E>();
			for(E choice : cs.selection)
				choiceBox.addItem(choice);
			chooseMenu.add(choiceBox);
			
			if(canBeCanceled){
				cancel = new JButton("Cancel");
				cancel.addActionListener(this);
				chooseMenu.add(cancel);
			}
			
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
				E selected = ((E)choiceBox.getSelectedItem());
				chooseMenu.setVisible(false);
				chooseMenu.dispose();
				client.sendAction(new ClientSelection<E>(client, selected));
			}
			if(e.getSource() == cancel){
				chooseMenu.setVisible(false);
				chooseMenu.dispose();
				client.sendAction(new ClientActionUndo(client));
			}
		}


	
}
