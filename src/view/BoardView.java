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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JPanel;

import client.IClient;
import player.Player;
import model.Model;
import model.hexagonalTiling.BoardTiles;
import model.hexagonalTiling.SettlersEdge;
import model.hexagonalTiling.SettlersTile;
import model.hexagonalTiling.SettlersVertex;
import model.ressources.*;
import controlor.DB;
import controlor.SettlersServer;
import delaunay.Pnt;
import delaunay.Triangle;


//TODO extract simple draw functions such as drawPnt,Edge,...
/**
 * Graphics Panel for board View.
 */
@SuppressWarnings("serial")
public class BoardView extends JPanel{

	public static Color delaunayColor = Color.green;
	public static int pointRadius = 2;

	private IClient client;

	private Map<Object, Color> colorTable;      // Remembers colors for display
	private Graphics g;                         // Stored graphics context
	private Random random = new Random();       // Source of random numbers

	private Pnt selectedPoint;
	private Pnt selectedEdge1;
	private Pnt selectedEdge2;


	/**
	 * Create and initialize the DT.
	 */
	public BoardView(IClient client) {
		this.client = client;
		selectedPoint = null ;
	}

	
	public void setSelectedPoint(Pnt point){
		selectedPoint = convertPntToFrameCoord(point);
	}

	public void setSelectedEdge(Pnt p1,Pnt p2){
		selectedEdge1 = convertPntToFrameCoord(p1);
		selectedEdge2 = convertPntToFrameCoord(p2);
	}

	public Pnt getSelectedPoint(){
		return selectedPoint;
	}

	/**
	 * Re-initialize the DT.
	 */
	public void clear() {
	}

	/**
	 * Get the color for the spcified item; generate a new color if necessary.
	 * @param item we want the color for this item
	 * @return item's color
	 */
	private Color getColor (Object item) {
		if (colorTable.containsKey(item)) return colorTable.get(item);
		Color color = new Color(Color.HSBtoRGB(random.nextFloat(), 1.0f, 1.0f));
		colorTable.put(item, color);
		return color;
	}

	/* Basic Drawing Methods */

	/**
	 * Draw a point.
	 * @param point the Pnt to draw
	 */
	private void draw (Pnt point) {
		int r = pointRadius;
		int x = (int) point.getX();
		int y = (int) point.getY();
		g.fillOval(x-r, y-r, r+r, r+r);
	}

	/**
	 * Draw a circle.
	 * @param center the center of the circle
	 * @param radius the circle's radius
	 * @param fillColor null implies no fill
	 */
	private void draw (Pnt center, double radius, Color fillColor) {
		int x = (int) center.getX();
		int y = (int) center.getY();
		int r = (int) radius;
		if (fillColor != null) {
			Color temp = g.getColor();
			g.setColor(fillColor);
			g.fillOval(x-r, y-r, r+r, r+r);
			g.setColor(temp);
		}
		g.drawOval(x-r, y-r, r+r, r+r);
	}


	/**
	 * Draw an edge.
	 * @param edge1 first point of the edge
	 * @param edge2 second point of the edge
	 * @param radius the circle's radius
	 * @param fillColor null implies no fill
	 */
	private void draw (Pnt edge1,Pnt edge2, double radius, Color fillColor) {
		if (fillColor != null) {
			Color temp = g.getColor();
			g.setColor(fillColor);
			g.setColor(temp);
		}

		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(fillColor);
		g2.setStroke(new BasicStroke((int)radius));
		g2.draw(new Line2D.Float((int)edge1.getX(),(int)edge1.getY(),(int)edge2.getX(),(int)edge2.getY()));
	}


	/**
	 * Draw a polygon.
	 * @param polygon an array of polygon vertices
	 * @param fillColor null implies no fill
	 */
	private void draw (Pnt[] polygon, Color fillColor) {
		int[] x = new int[polygon.length];
		int[] y = new int[polygon.length];
		for (int i = 0; i < polygon.length; i++) {
			x[i] = (int) polygon[i].getX();
			y[i] = (int) polygon[i].getY();
		}
		if (fillColor != null) {
			Color temp = g.getColor();
			g.setColor(fillColor);
			g.fillPolygon(x, y, polygon.length);
			g.setColor(temp);
		}
	}

	private void drawTiles(){
		Iterator<SettlersTile> tilesIt = client.getModel().board().tiles();
		while(tilesIt.hasNext()){
			SettlersTile tile = tilesIt.next();
			drawTileRessource(tile);
			drawTileNumber(tile);
		}
	}


	private Pnt convertPntToFrameCoord(Pnt pt){
		Pnt res = new Pnt(pt);
		res.scale(0,getWidth());
		res.scale(1,getHeight());
		return res;
	}

	private List<Pnt> convertPntToFrameCoord(List<Pnt> pts){
		List<Pnt> res = new LinkedList<Pnt>();
		for(Pnt p : pts)
			res.add(convertPntToFrameCoord(p));
		return res;
	}

	private void drawTileRessource(SettlersTile tile){
		Pnt[] ar = getTilesPolygonPts(tile);
		draw(ar, getRessourceColor(tile.ressource()));
	}

	Pnt[] getTilesPolygonPts(SettlersTile tile){
		List<Pnt> pointsAround = new LinkedList<Pnt>();
		for(SettlersVertex v : client.getModel().board().vertexNeighbors(tile))
			pointsAround.add(v.getPosition());
		List<Pnt> points = 
				convertPntToFrameCoord(
						AngleSortPoints.angleSortPoints(pointsAround));
		Pnt[] ar = (Pnt[])(points.toArray(new Pnt[points.size()]));
		return ar;
	}


	private Color getRessourceColor(Ressource ressource){
		return ressource.getColor();
	}

	private void drawTileNumber(SettlersTile tile){
		if(tile.diceNumber()>=0){
			String res = "";
			res = res +  tile.diceNumber();

			Pnt centerTile = convertPntToFrameCoord(client.getModel().board().getPosition(tile)); 
			draw(centerTile,20,Color.white);
			setFont(tile);
			g.drawString(res,
					(int)centerTile.getX()-5,
					(int)centerTile.getY()+5
					);
			g.setColor(Color.black);
		}
	}

	private void setFont(SettlersTile tile){
		if(tile.diceNumber()<4 || tile.diceNumber()>10){
			g.setFont(new Font(g.getFont().getFontName(),0,g.getFont().getSize()));
		}
		else{
			if(tile.diceNumber()==6 || tile.diceNumber()==8 ){
				g.setColor(Color.red);
				g.setFont(new Font(g.getFont().getFontName(),1,g.getFont().getSize()));
			}
			else
				g.setFont(new Font(g.getFont().getFontName(),1,g.getFont().getSize()));
		}
	}

	private void drawColony(Pnt pnt,double size,Color color){
		double x = pnt.getX();
		double y = pnt.getY();
		double roofElevation=1.85;
		Pnt[] ar = {
				new Pnt(x+size,y+size),
				new Pnt(x-size,y+size),
				new Pnt(x-size,y-size),
				new Pnt(x,y-size*roofElevation),
				new Pnt(x+size,y-size),
		};
		draw(ar,color);
	}

	private void drawThief(Pnt pnt,double size){
		double x = pnt.getX();
		double y = pnt.getY();
		Pnt[] ar = {
				new Pnt(x-size/3,y+size/2),
				new Pnt(x+size/3,y+size/2),
				new Pnt(x,y-size/2)
		};
		draw(ar,Color.black);
	}

	private void drawThief(){
		Pnt thiefPoint =convertPntToFrameCoord(client.getModel().board().getPosition(client.getModel().getThiefPosition())); 
		drawThief(thiefPoint,40.);
	}


	private void drawCity(Pnt pnt,double size,Color color){
		double x = pnt.getX();
		double y = pnt.getY();

		double s = 1.6*size;
		double h= 1.*size;
		double l = 1.2*size;
		
		Pnt[] ar = {
				new Pnt(x+s,y+h),
				new Pnt(x-s,y+h),
				new Pnt(x-s,y-h),
				new Pnt(x+s-l,y-h),
				new Pnt(x+s-l,y-2*h),
				new Pnt(x+s-l/2,y-2.5*h),
				new Pnt(x+s,y-2*h)
		};
		draw(ar,color);
	}

	private void drawRoad(SettlersEdge edge,double size,Color color){
		drawEdge(edge,size,color,0.5);
	}
	
	//ratio is the proportion of the segment that is drawn
	private void drawEdge(SettlersEdge edge,double size,Color color,double ratio){
		Pnt p1 = edge.p1();
		Pnt p2 = edge.p2();
		Pnt m = p1.middle(p2);
		Pnt ext1 = m.add((p2.subtract(m)).scale(ratio));
		Pnt ext2 = m.subtract((p2.subtract(m)).scale(ratio));
		draw(
				convertPntToFrameCoord(ext1),
				convertPntToFrameCoord(ext2),
				size, color);
	}

	private void drawEdges(){
		Iterator<SettlersEdge> edges = client.getModel().board().edges();
		while (edges.hasNext()) {
			SettlersEdge edge = edges.next();
			drawEdge(edge, .8, Color.black,1);
		}

		edges = client.getModel().board().borderEdges();
		while (edges.hasNext()) {
			SettlersEdge edge = edges.next();
			if(edge.hasHarbor())
				drawHarbor(edge);
		}
	}

	private void drawHarbor(SettlersEdge edge){
		if(edge.hasHarbor()){
			draw(convertPntToFrameCoord(edge.p1()),
					convertPntToFrameCoord(edge.p2()),
					10.,edge.harbor().getColor());

			Pnt middle = convertPntToFrameCoord(edge.p1().middle(edge.p2()));
			int offset = 15;

			g.setColor(Color.white);
			g.drawRect((int)(middle.getX()-offset),
					(int)(middle.getY()-offset/2),
					40, 10);
			g.setColor(edge.harbor().getColor());
			g.drawString(edge.harbor().toString(),
					(int)middle.getX()-offset,
					(int)middle.getY()
					);
		}
	}

	private void drawRoads(){
		Iterator<SettlersEdge> edges = client.getModel().board().edges();
		while (edges.hasNext()) {
			SettlersEdge edge = edges.next();
			if(edge.hasBuilding())
				drawRoad(edge, 7, 
						edge.getBuilding().getPlayer().getCouleur());
		}
	}

	private void drawIntersections(){
	}

	private void drawBuildings(){
		Iterator<SettlersVertex> vIt = client.getModel().board().vertices();
		while(vIt.hasNext()){
			SettlersVertex v = vIt.next();
			if(v.hasBuilding()){
				Pnt buildingPt = convertPntToFrameCoord(v.getPosition());
				Color buildingColor = v.getBuilding().getPlayer().getCouleur();
				if(v.getBuilding().isColony())
					drawColony(buildingPt,8.,buildingColor);
				else
					drawCity(buildingPt,8.,buildingColor);
			}

		}
	}


	private void drawSelection(){
		if(selectedPoint!=null){
			draw(selectedPoint, 7, Color.cyan);
		}
		if(selectedEdge1!=null && selectedEdge2 != null){
			draw(selectedEdge1, 7, Color.red);
			draw(selectedEdge2, 7, Color.red);
			draw(selectedEdge1,selectedEdge2,5.,Color.pink);
		}
	}

	/**
	 * Handles painting entire contents of DelaunayPanel.
	 * Called automatically; requested via call to repaint().
	 * @param g the Graphics context
	 */
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		this.g = g;

		// Flood the drawing area with a "background" color
		Color temp = g.getColor();
		g.setColor(new Color(55,113,200));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(temp);

		drawBoard();

		// Draw anybuyCardPressed extra info due to the mouse-entry switches
		temp = g.getColor();
		g.setColor(Color.white);
		drawSelection();

		g.setColor(temp);
	}

	/**
	 * Draw The board.
	 * @param withFill true iff drawing Voronoi cells with fill colors
	 * @param withSites true iff drawing the site for each Voronoi cell
	 */
	private void drawBoard () {
		drawTiles();
		drawEdges();
		drawIntersections();

		drawRoads();
		drawBuildings();
		drawThief();
	}
}

