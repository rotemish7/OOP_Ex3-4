package gameClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
/**
 * This class represents a simple example for using the GameServer API:
 * the main file performs the following tasks:
 * 1. Creates a game_service [0,23] (line 36)
 * 2. Constructs the graph from JSON String (lines 37-39)
 * 3. Gets the scenario JSON String (lines 40-41)
 * 4. Prints the fruits data (lines 49-50)
 * 5. Add a set of robots (line 52-53) // note: in general a list of robots should be added
 * 6. Starts game (line 57)
 * 7. Main loop (should be a thread) (lines 59-60)
 * 8. move the robot along the current edge (line 74)
 * 9. direct to the next edge (if on a node) (line 87-88)
 * 10. prints the game results (after "game over"): (line 63)
 *  
 * @author boaz.benmoshe
 *
 */
public class SimpleGameClient 
{

	//static GameGUI window;
	private static String typegame;
	private static String g_string;
	private static game_service game;
	private static int scenario_num;
	private static DGraph DG = new DGraph();
	private static GameServer server = new GameServer();
	private static KML_Logger KML;


	public static void main(String[] a) 
	{
		GameInit();

		g_string = game.getGraph();
		DG.init(g_string);
		KML = new KML_Logger(scenario_num);
		KML.info_kml(DG.graph_to_kml());
		
		AutoSetRobot(server,DG);

		if(typegame.equals("auto"))
		{
			autoPlay();
		}
		else
		{
			manualPlay();
		}

		//first allocation of the robots



		//test1();
	}

	public static void test1() 
	{
		
		String g = game.getGraph();
		//GameServer game2 = new GameServer(game.getFruits(),game.);

		DGraph gg = new DGraph();
		gg.init(g);
		String info = game.toString();
		JSONObject line;
		GameGUI window = null;
		try
		{
			line = new JSONObject(info);
			JSONObject ttt = line.getJSONObject("GameServer");
			int rs = ttt.getInt("robots");
			int moves = ttt.getInt("moves");
			int grade = ttt.getInt("grade");
			System.out.println(info);
			System.out.println(g);
			//GameServer game2 = new GameServer(game.getFruits(),moves,grade,rs,g,game);
			window = new GameGUI(gg,game,scenario_num);

			// the list of fruits should be considered in your solution
			Iterator<String> f_iter = game.getFruits().iterator();
			while(f_iter.hasNext()) {System.out.println(f_iter.next());}	
			int src_node = 4;  // arbitrary node, you should start at one of the fruits
			for(int a = 0;a<rs;a++) 
			{
				game.addRobot(src_node+a);
			}
		}
		catch (JSONException e) {e.printStackTrace();}

		game.startGame();

		window.setVisible(true);

		while(game.isRunning()) 
		{
			moveRobots(game, gg);
			window.repaint();
		}

		String results = game.toString();
		System.out.println("Game Over: "+results);
	}

	public static void GameInit()
	{

		JFrame frame = null;

		//choose scenario
		typegame  = JOptionPane.showInputDialog(frame,"Enter manual or auto");
		scenario_num = 0; 
		
		if(typegame.equals("manual") || typegame.equals("auto"))
		{
			String level = JOptionPane.showInputDialog(frame,"Enter level 0 - 23");

			try
			{
				scenario_num = Integer.parseInt(level);
			}catch(Exception e){}
		}

		game = Game_Server.getServer(scenario_num); // you have [0,23] games
		g_string = game.getGraph();
		DG.init(g_string);
		String info = game.toString();
		JSONObject line;
		
		try
		{
			line = new JSONObject(info);
			JSONObject ttt = line.getJSONObject("GameServer");
			int rs = ttt.getInt("robots");
			int moves = ttt.getInt("moves");
			int grade = ttt.getInt("grade");
			server = new GameServer(game.getFruits(),moves,grade,rs,g_string,game,scenario_num);
		}
		catch (JSONException e) {e.printStackTrace();}
	}

	public static void autoPlay()
	{
		GameGUI window = new GameGUI(server);
		window.setVisible(true);
		game.startGame();

		int dt = 100;
		while( game.isRunning()) 
		{
			moveRobots2(game, DG);
			//moveRobots(game,DG);
			window.repaint();

			try 
			{
				Thread.sleep(dt);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
		String results = game.toString();
		System.out.println("Game Over: "+results);
		
	}

	public static void manualPlay()
	{

	}

	public static void AutoSetRobot(GameServer server,graph g) 
	{
		List<String> Total_Fruit = server.getFruit();
		edge_data edge2 = null;

		for (int i = 0; i < server.getRobots(); i++) 
		{
			int maxFruit = Integer.MIN_VALUE;
			int MaxFruitID = 0;

			for (int j = 0; j < Total_Fruit.size(); j++)
			{
				Fruit f = new Fruit(Total_Fruit.get(j));
				if (f.getValue() > maxFruit) 
				{
					maxFruit = f.getValue();
					MaxFruitID = j;
					edge2 = onEdge(f,g);
				}
			}
			server.getGame().addRobot(edge2.getSrc());
			Total_Fruit.remove(MaxFruitID);
		}
	}

	public static edge_data onEdge(Fruit f,graph g)
	{
		double EPSILON = 0.00005;
		for (node_data node : g.getV()) 
		{
			for (edge_data edge : g.getE(node.getKey())) 
			{
				double edgeLen = g.getNode(edge.getSrc()).getLocation().distance2D(g.getNode(edge.getDest()).getLocation());
				double srcTof = g.getNode(edge.getSrc()).getLocation().distance2D(f.getPos());
				double dstTof = g.getNode(edge.getDest()).getLocation().distance2D(f.getPos());

				if((srcTof + dstTof - edgeLen) <= EPSILON)
				{
					return edge;
				}
			}
		}
		return null;
	}
	
	/** 
	 * Moves each of the robots along the edge, 
	 * in case the robot is on a node the next destination (next edge) is chosen (randomly).
	 * @param game
	 * @param gg
	 * @param log
	 */
	private static void moveRobots2(game_service game, graph gg)
	{
		List<String> robots = game.getRobots();
		List<String> fruits = game.getFruits();
		List<Robot> robot = new ArrayList<Robot>();
		List<Robot> matchRobot = new ArrayList<Robot>();;
		List<Fruit> matchFruit = new ArrayList<Fruit>();;
		List<Robot> matchRobotEnd = new ArrayList<Robot>();;
		List<Fruit> matchFruitEnd = new ArrayList<Fruit>();;
		double sumPath = 0;
		double MinPath = 0;
		double sumValue = 0;
		int endPer=0; // check if we done 5 permutations
		while(endPer<5)
		{
			List<Fruit> f = creatFruits(fruits);
			for (String r : robots) 
			{
				Robot ro = new Robot(r);
				robot.add(ro);
			}
			double maxValue = 0;
			double minPath = Double.POSITIVE_INFINITY;
			Robot moveRobot = null;
			edge_data ed = null;
			while(!f.isEmpty() && !robot.isEmpty())
			{
				int rand = (int)(Math.random()*f.size());
				Fruit randFruit = f.get(rand);
				ed = onEdge(randFruit, gg);
				for(int i=0; i<robot.size(); i++)
				{
					if(robot.get(i).getDest() != -1)
					{
						double path = Graph_Algo.shortestPathDist(ed.getSrc(), robot.get(i).getSrc(),gg);
						if(maxValue < randFruit.getValue() && path < minPath)
						{
							maxValue = randFruit.getValue();
							minPath = path;
							sumPath+=path;
							sumValue+=maxValue;
							moveRobot = robot.get(i);
						}
					}
				}
				matchFruit.add(randFruit);
				matchRobot.add(moveRobot);
				f.remove(rand);
				robot.remove(moveRobot);
			}
			if(sumPath < MinPath)
			{
				MinPath = sumPath;
				matchRobotEnd = matchRobot;
				matchFruitEnd = matchFruit;
				matchRobot.clear();
				matchFruit.clear();
			}
			endPer++;
		}
		for(int i=0; i<matchRobotEnd.size(); i++)
		{
			edge_data dest = onEdge(matchFruitEnd.get(i), gg);
			int next = nextNode2(gg, matchRobotEnd.get(i), dest.getDest());
			game.chooseNextEdge(matchRobotEnd.get(i).getId(), next);
		}
	}

	/**
	 * 
	 * @param fruits a list of string represents the fruits in the game
	 * @return a list of fruits
	 */
	private static List<Fruit> creatFruits(List<String> fruits)
	{
		List<Fruit> temp = new ArrayList<Fruit>(); 
		
		for (String f : fruits) 
		{
			temp.add(new Fruit(f));
		}
		return temp;
	}

	/**
	 * 
	 * @param g
	 * @param r
	 * @param dest
	 * @return
	 */
	private static int nextNode2(graph g, Robot r, int dest) 
	{
		List<node_data> path = Graph_Algo.shortestPath(r.getSrc(), dest,g);
		r.setDest(path.get(1).getKey());
		return path.get(1).getKey();
	}
	
	
	/** 
	 * Moves each of the robots along the edge, 
	 * in case the robot is on a node the next destination (next edge) is chosen (randomly).
	 * @param game
	 * @param gg
	 * @param log
	 */
	private static void moveRobots(game_service game, graph gg)
	{
		List<String> log = game.move();
		if(log!=null) {
			long t = game.timeToEnd();
			for(int i=0;i<log.size();i++)
			{
				String robot_json = log.get(i);
				try {
					JSONObject line = new JSONObject(robot_json);
					JSONObject ttt = line.getJSONObject("Robot");
					int rid = ttt.getInt("id");
					int src = ttt.getInt("src");
					int dest = ttt.getInt("dest");

					if(dest==-1)
					{	
						dest = nextNode(gg, src);
						game.chooseNextEdge(rid, dest);
						System.out.println("Turn to node: "+dest+"  time to end:"+(t/1000));
						System.out.println(ttt);
					}
				} 
				catch (JSONException e) {e.printStackTrace();}
			}
		}
	}
	
	/**
	 * a very simple random walk implementation!
	 * @param g
	 * @param src
	 * @return
	 */
	private static int nextNode(graph g, int src) 
	{
		int ans = -1;
		Collection<edge_data> ee = g.getE(src);
		Iterator<edge_data> itr = ee.iterator();
		int s = ee.size();
		int r = (int)(Math.random()*s);
		int i=0;
		while(i<r) {itr.next();i++;}
		ans = itr.next().getDest();
		return ans;
	}

}
