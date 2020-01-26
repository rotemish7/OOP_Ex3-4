package gameClient;

import java.awt.FileDialog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
import gameClient.GameGUI;
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
public class SimpleGameClient implements Runnable
{

	//static GameGUI window;
	private static String typegame;
	private static String g_string;
	private String Robots_kml="";
	private String Fruits_kml="";
	private static game_service game;
	private static int scenario_num;
	private static int ID;
	private static DGraph DG = new DGraph();
	private static Graph_Algo GA = new Graph_Algo();
	private static GameServer server = new GameServer();
	private static KML_Logger KML;
	private static JFrame frame;
	private static int id = 0;

	private static int dt = -1;


	/**
	 * 
	 * initialize all the arguments for the necessary functions 
	 * 
	 * set the first location for the robots
	 * 
	 * open a KML Logger
	 * open the right function according to auto or manual play
	 */
	private void gameRunner() 
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
		
	}

	/**
	 * Asking the user to play auto or manual
	 * saving the level chosen by the user
	 * initialize the game graph
	 * initialize the a GameServer server that contains all the game fields
	 */
	public static void GameInit()
	{

		frame = null;

		String user_id = JOptionPane.showInputDialog(frame,"Enter ID");

		try
		{
			id = Integer.parseInt(user_id);
		}catch(Exception e)
		{
			System.out.println("Not a valid ID");
		}

			//choose scenario
			Game_Server.login(id);
			
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
		//		String remark = "This string should be a KML file!!";
		//		game.sendKML(remark); // Should be your KML (will not work on case -1).

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

	/**
	 * Creating a new GUI for the game according to the server
	 * Start the auto play game and move the robots
	 * 
	 */
	public static void autoPlay()
	{
		GameGUI window = new GameGUI(server);
		window.setVisible(true);
		game.startGame();

		dt = 115;
				
		// level 0,1,3: dt =100
		// level 5: dt = 110

		while( game.isRunning()) 
		{
			moveRobots(game, DG);

			
			if(game.timeToEnd()/1000 < 10)
			{
				dt = 67;
			}

			window.repaint();

			try 
			{
				Thread.sleep(dt);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}

		//printing the game stats at the end
		String results = game.toString();
		System.out.println("Game Over: "+results);

		//Saving to kml window
		String kml =  JOptionPane.showInputDialog(frame,"Save to KML format?");
		if(kml.equals("yes"))
		{
			FileDialog chooser = new FileDialog(frame, "Use a .kml extension", FileDialog.SAVE);
			chooser.setVisible(true);
			String filename =chooser.getDirectory()+chooser.getFile();
			KML.save_kml(filename);
			game.sendKML(KML.toString());
		}
	}

	/**
	 * Creating a new GUI for the game according to the server 
	 * 
	 * Starting the manual game
	 */
	public static void manualPlay()
	{
		GameGUI window = new GameGUI(server);
		window.setVisible(true);
		game.startGame();
		JFrame frame = null;
		int node = -1;
		int idr = -1;
		dt = 100;
		
		while( game.isRunning()) 
		{
			String s_robot = JOptionPane.showInputDialog(frame,"Enter a robot id");

			String s_node = JOptionPane.showInputDialog(frame,"Enter a neighboor node destination");
			try
			{
				node = Integer.parseInt(s_node);
				idr = Integer.parseInt(s_robot);
			}
			catch(Exception e) {}

			//moveRobotsM(idr, node, DG);

			window.repaint();
		}
	}

	/**
	 * manual mobe the robots in the game one node at a time
	 * 
	 * 
	 * @param id represents the robot id
	 * @param node represents the node which the robot needs to go to
	 * @param g represents the game graph
	 */
	public static void moveRobotsM(int id, int node,graph g)
	{
		GA.init(g);
		List<String> log = game.move();
		if(log!=null)
		{
			for(int i=0;i<log.size();i++)
			{
				String robot_json = log.get(i);
				try
				{
					JSONObject line = new JSONObject(robot_json);
					JSONObject ttt = line.getJSONObject("Robot");
					int rid = ttt.getInt("id");
					int src = ttt.getInt("src");
					int dest = ttt.getInt("dest");
					String pos = ttt.getString("pos");
					
					
					if(rid == id)
					{
						game.chooseNextEdge(rid, node);
					}
				}
				catch (JSONException e) {e.printStackTrace();}
			}
		}
	}

	/**
	 * Auto allocate the robots on the graph according to the fruits in the game
	 * 
	 * 
	 * @param server represents a GameServer object that contain all the game fields
	 * @param g represents the game graph
	 */
	public static void AutoSetRobot(GameServer server,graph g) 
	{
		List<String> Total_Fruit = server.getFruit();
		edge_data edge2 = null;

		for (int i = 0; i < server.getRobots(); i++) 
		{
			double maxFruit = Double.MIN_VALUE;
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

	/**
	 * Checking on which edge every fruit is located
	 * 
	 * 
	 * @param f represents a fruit
	 * @param g represents the game graph
	 * @return
	 */
	public static edge_data onEdge(Fruit f,graph g)
	{
		double EPSILON = 0.00001;
		for (node_data node : g.getV()) 
		{
			for (edge_data edge : g.getE(node.getKey())) 
			{
				double edgeLen = g.getNode(edge.getSrc()).getLocation().distance2D(g.getNode(edge.getDest()).getLocation());
				double srcTof = g.getNode(edge.getSrc()).getLocation().distance2D(f.getPos());
				double dstTof = g.getNode(edge.getDest()).getLocation().distance2D(f.getPos());

				if((srcTof + dstTof - edgeLen) <= EPSILON)
				{
					if(edge.getSrc()<edge.getDest())
					{
						return edge;
					}
					else
					{
						return g.getEdge(edge.getDest(), edge.getSrc());
					}
				}
			}
		}
		return null;
	}

	/** 
	 * Moves each of the robots along the edge, 
	 * 
	 * @param game represents the game 
	 * @param gg represents the game graph
	 * 
	 */
	private static void moveRobots(game_service game, graph gg)
	{
		GA.init(gg);
		List<String> log = game.move();
		List<String> Sfruit = game.getFruits();
		List<Fruit> fruit = creatFruits(Sfruit);
		double minPath = Double.POSITIVE_INFINITY;
		edge_data ed = null;
		int next=0;
		int bestDest=0;
		edge_data fruitEd = null;
		if(log!=null)
		{
			for(int i=0;i<log.size();i++)
			{
				fruit = creatFruits(Sfruit);
				Fruit f = new Fruit();

				//fruits kml

				String robot_json = log.get(i);
				try {
					JSONObject line = new JSONObject(robot_json);
					JSONObject rob = line.getJSONObject("Robot");
					int rid = rob.getInt("id");
					int src = rob.getInt("src");
					int dest = rob.getInt("dest");
					String pos = rob.getString("pos");
					KML.robot_kml(pos, rid);
					if(dest==-1)
					{	
						for(int j=0; j<fruit.size(); j++)
						{
							if(fruit.get(j).getTag() == 0)
							{
								ed = onEdge(fruit.get(j), gg);
								if (fruit.get(0).getType() == 1) 
								{
									next = ed.getDest();
								}
								else
								{
									next = ed.getSrc();
								}
								double path = GA.shortestPathDist(src, next);
								if(path<minPath)
								{
									minPath = path;
									bestDest = next;
									fruitEd = ed;
									f = fruit.get(j);
								}
							}
						}

						f.setTag(1);
						dest = nextNode(gg, src, bestDest, fruitEd);
						if(ed.getDest() != dest && ed.getSrc() != dest)
						{
							dt = 200;
						}
						game.chooseNextEdge(rid, dest);
						System.out.println("Turn to node: "+dest);
						System.out.println(rob);
					}
				} 
				catch (JSONException e) {e.printStackTrace();}
			}
		}
	}

	/**
	 * 
	 * @param fruits is a list of string represents the fruits in the game
	 * @return a list of Fruit 
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
	 * @param g represents the game graph
	 * @param src the source of the robots
	 * @param dest the destination of the robots path
	 * @param fruitEd the edge on  which the fruit target is located
	 * @return int value represents the next node key
	 */
	private static int nextNode(graph g, int src, int dest, edge_data fruitEd) 
	{

		GA.init(g);
		long t =game.timeToEnd();
		if(t%100 == 0 && t/1000>20)
		{
			List<Fruit> fruit = creatFruits(game.getFruits());
			int next=0, bestDest=0;
			double maxValue=0;
			for(int j=0; j<fruit.size(); j++)
			{
				edge_data ed = onEdge(fruit.get(j), g);
				if (fruit.get(0).getValue() ==1) 
				{
					next = ed.getDest();
				}
				else
				{
					next = ed.getSrc();
				}
				double value = fruit.get(j).getValue();
				if(maxValue<value)
				{
					maxValue = value;
					bestDest = next;
					fruitEd = ed;
				}
			}
			dest = bestDest;
		}
		List<node_data> path = GA.shortestPath(src, dest);
		Queue<node_data> pathQ = new LinkedList<node_data>();
		pathQ.addAll(path);

		if(pathQ.size() == 1)
		{
			edge_data ed = g.getEdge(fruitEd.getDest(), fruitEd.getSrc());
			if(src == fruitEd.getDest())
			{
				return fruitEd.getSrc();
			}
			else if (src == fruitEd.getSrc())
			{
				return fruitEd.getDest();
			}
		}
		boolean first = true;
		int next = 0;
		if (first)
		{	
			pathQ.poll();
			first = false;
		}			
		next = pathQ.poll().getKey();
		return next;
	}

	@Override
	public void run() 
	{
		gameRunner();
	}
}
