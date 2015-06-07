import java.util.Vector;

import server.IServer;
import client.IClient;
import delaunay.Pnt;



//todo save clients order to restore state instead
public class RegisteredGame {

	//set a game state given a sequence of clicks
	public static void game1(IServer server,IClient[] clients){
		Vector<Pnt> points = getPoints();
		int current = 0;
		for (int i = 0; i < points.size(); i++) 
			clients[server.getCurrentPlayer()].mouseClicked(points.get(i));
	}
	
	private static Vector<Pnt> getPoints(){
		Vector<Pnt> res = new Vector<Pnt>();
		res.add(new Pnt(0.31,0.25));
		res.add(new Pnt(0.35564053537284895,0.22765598650927485));
		res.add(new Pnt(0.4856596558317399,0.24957841483979762));
		res.add(new Pnt(0.5468451242829828,0.22765598650927485));
		res.add(new Pnt(0.6730401529636711,0.2613827993254637));
		res.add(new Pnt(.7112810707456979,0.23946037099494097));
		res.add(new Pnt(0.5850860420650096,0.5177065767284992));
		res.add(new Pnt(.6367112810707457,0.5531197301854974));
		return res;
	}

	
}
