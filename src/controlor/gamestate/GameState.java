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

package controlor.gamestate;

import controlor.GameController;
import controlor.ISettlersServer;
import delaunay.Pnt;

/**
 * A game state that corresponds to various UI interaction (choosing road or stealer
 * placement) for instance.
 * Overloads the click method to customize effect.
 * 
 * @author david
 *
 */
public abstract class GameState {

	protected GameController gc;

	public GameState(GameController gc) {
		this.gc = gc;
	}
	
	public void run(){}
	
	public abstract void click(Pnt click);
	
	//called when ui finish its selection
	public abstract void apply(Object o);
	
}
