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

package model.card;

import controlor.DB;
import controlor.GameControlor;
import controlor.gamestate.GameState;
import delaunay.Pnt;

public abstract class CardState extends GameState {
	GameState stateToRestore;
	Card card;

	public CardState(GameControlor gc,GameState stateToRestore,Card card) {
		super(gc);
		this.stateToRestore = stateToRestore;
		this.card = card;
	}

	@Override
	public abstract void click(Pnt click);

	@Override
	public abstract void apply(Object o);

	/**
	 * to be called when done to restore previous state
	 */
	public void done(){
		DB.msg("card done");
		gc.setSet(stateToRestore);
	}
	
	public static CardState makeCardState(GameControlor gc, GameState stateToRestore, Card card){
		if(card instanceof Monopole)
			return new MonopoleState(gc, stateToRestore, (Monopole)card);
		if(card instanceof Knight)
			return new KnightState(gc, stateToRestore, (Knight)card);
		if(card instanceof VictoryPoint)
			return new VictoryPointState(gc, stateToRestore, (VictoryPoint)card);
		return null;
	}
	
}
