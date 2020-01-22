package gameClient;
import algorithms.Graph_Algo;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.sql.Time;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultXMLDocumentHandler;

import Server.game_service;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;

import java.text.DecimalFormat;


public class GameGUI extends JFrame implements ActionListener, MouseListener, Runnable, Observer 
{

	/////////////////////////////////////////////////////////////////
	//////////////////////////GUI_fields/////////////////////////////
	////////////////////////////////////////////////////////////////


	//private static final Graphics Graphics = null;
	private static DecimalFormat df2 = new DecimalFormat("#.##");
	static JTextField textfield1, textfield2, textfield3;
	static JButton submit1,submit2;
	//private static JFrame frame;
	boolean isConnected=false;
	double pathweight =-1;
	List<node_data> path;
	int [][] robotsDialog;
	Graphics doubleD; 
	int Level;
	Graphics2D g4;


	/////////////////////////////////////////////////////////////////
	/////////////////////GUI_window_fields//////////////////////////
	////////////////////////////////////////////////////////////////
	private double maxX = Double.NEGATIVE_INFINITY;
	private double maxY = Double.NEGATIVE_INFINITY;
	private double minX = Double.POSITIVE_INFINITY;
	private double minY = Double.POSITIVE_INFINITY;

	private int defaultx = 1800;	private int defaulty = 900;

	//control action for paint//
	int action=0;

	public Collection<node_data> vertex;

	// contains all the edges by ID(src ver) and edge_data. 
	public Collection<edge_data> edges;
	private graph graph;
	private DGraph DG = new DGraph();
	private game_service game;


	/////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	/////////////////////////   Constructor  \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	//////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	public GameGUI() {}
	
	public GameGUI(GameServer server)
	{
		initGUI();
		this.game = server.getGame();
		this.graph = new DGraph();
		this.DG.init(server.getGraph());
		this.graph = this.DG;
		this.Level = server.getLevel();
	}

	public GameGUI(graph dg)
	{
		initGUI();
		this.vertex	= dg.getV();
		this.graph = dg;
		((Observable) graph).addObserver(this);
	}

	public GameGUI(graph dg, game_service game,int gamechooser)
	{
		initGUI();
		this.game = game; 
		this.vertex	= dg.getV();
		this.graph = dg;
		((Observable) graph).addObserver(this);
		this.Level= gamechooser;
	}

	/////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	/////////////////////////   window settings  \\\\\\\\\\\\\\\\\\\\\\\\\\
	//////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

	private void initGUI() 
	{

		this.setSize(defaultx,defaulty);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ImageIcon ImageIcon5 = new ImageIcon("data\\GameIcon.jpg");
		Image GameIcon  =	ImageIcon5.getImage();
		this.setIconImage(GameIcon);

		//creating menu bar//
		MenuBar menuBar	 = new MenuBar();
		Menu menu_file1	 = new Menu("file");


		//adding the file section to the menu bar//
		menuBar.add(menu_file1);

		this.setMenuBar(menuBar);

		//creating a item in bar for short path
		MenuItem Save = new MenuItem("Save"); 
		Save.addActionListener(this);

		//creating a item in bar for short path
		MenuItem Load = new MenuItem("Load"); 
		Load.addActionListener(this);

		//creating a item in bar for clean graph
		MenuItem clean_all = new MenuItem("clean all"); 
		clean_all.addActionListener(this);

		//adding to menus:\\
		//file
		menu_file1.add(Save);
		menu_file1.add(Load);
		menu_file1.add(clean_all);


		//listen to the mouse\\
		this.addMouseListener(this);

	}

	private BufferedImage buff;
	private  Graphics2D g2;
	JLabel background = null;
	
	//////////////////////////////////////////////Paint///////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void paint(Graphics g)
	{	

		/**
		 * double buffering section---
		 */
		if(buff == null || g2 == null || (this.WIDTH != defaultx || this.HEIGHT != defaulty )) 
		{

			if((this.WIDTH != defaultx || this.HEIGHT != defaulty) && background != null )
			{
				remove(background);
			}
			//set background
			setLayout(null);
			ImageIcon img = new ImageIcon("pacmanback.jpg");
			background = new JLabel("", img ,JLabel.CENTER);
			defaultx = this.getWidth();
			defaulty = this.getHeight();
			background.setBounds(0,0, defaultx, defaulty);

			add(background);

			windowScale();
			buff = new BufferedImage(defaultx, defaulty, BufferedImage.TYPE_INT_ARGB);
			g2 = buff.createGraphics();
			super.paint(g2);
			paintgraph(g2);
		}

		g4 = (Graphics2D)g; 	
		g4.drawImage(buff,0,0,null);

		paintFruits();		
		paintRobots();

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////EndPaint//////////////////////////////////////////////////////////////

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void actionPerformed(ActionEvent e){}
	@Override
	public void run() 
	{
		repaint();}
	@Override
	public void update(Observable o, Object arg) 
	{
		repaint();
		run();
	}

	private void paintgraph(Graphics g) 
	{
		super.paintComponents(g);
		Graphics2D g4 = (Graphics2D) g;

		g.setColor(Color.blue);
		
		g4.setStroke(new BasicStroke(5));
		
		//sets the txt size 
		float f=13.0f; // font size.
		g4.setFont(g4.getFont().deriveFont(f));
		
		//go through all the nodes in the graph
		for (node_data node_data : graph.getV()) 
		{
			Point3D nodes_src = node_data.getLocation();
			g.setColor(Color.BLUE);
			double x = scale(nodes_src.x(),this.minX,this.maxX,80,this.getWidth()-80);
			double y = scale(nodes_src.y(),this.minY,this.maxY,80,this.getHeight()-80);
			g.fillOval((int)x-7, (int)y-7,15,15);
			g.setColor(Color.PINK);
			g.drawString(Integer.toString(node_data.getKey()),(int)x-3,(int)y-7);

			//go through all the edges of the specific node
			for (edge_data edge_data : graph.getE(node_data.getKey())) 
			{
				g.setColor(Color.RED);
				Point3D nodes_dest = graph.getNode(edge_data.getDest()).getLocation();
				double nodeX = scale(graph.getNode(edge_data.getDest()).getLocation().x(),minX,maxX,80,this.getWidth()-80);
				double nodeY = scale(graph.getNode(edge_data.getDest()).getLocation().y(),minY,maxY,80,this.getHeight()-80);
				double tempX = scale(node_data.getLocation().x(),minX,maxX,80,this.getWidth()-80);
				double tempY = scale(node_data.getLocation().y(),minY,maxY,80,this.getHeight()-80);
				//Graphics2D g3 = (Graphics2D) g;
				g4.setStroke(new BasicStroke(2));
				g4.drawLine((int)tempX,(int) tempY,(int)nodeX,(int)nodeY);

				//draw a point of the edges direction in Yellow
				g4.setColor(Color.YELLOW);
				double dir_of_edge_x= ((nodes_src.x() + 4*nodes_dest.x())/5);
				double dir_of_edge_y= ((nodes_src.y() + 4*nodes_dest.y())/5);
				double scale_dirX = scale(dir_of_edge_x,minX,maxX,80,this.getWidth()-80);
				double scale_dirY = scale(dir_of_edge_y,minY,maxY,80,this.getHeight()-80);
				g4.fillOval((int)scale_dirX-3 , (int)scale_dirY-5,10,10);

				//draw the edge weight in Black
				double mid_of_edge_x= ((nodes_src.x()+nodes_dest.x())/2);
				double mid_of_edge_y= ((nodes_src.y()+nodes_dest.y())/2);
				g4.setColor(Color.PINK);
				double scale_midX = scale(mid_of_edge_x,minX,maxX,80,this.getWidth()-80);
				double scale_midY = scale(mid_of_edge_y,minY,maxY,80,this.getHeight()-80);
				DecimalFormat df = new DecimalFormat("#.00");
				g4.drawString(df.format(edge_data.getWeight()),(int)scale_midX, (int)scale_midY);
			}
		}

		Graphics2D G = (Graphics2D)g;
		float f1=26.0f; // font size.
		G.setColor(Color.GREEN);
		G.setFont(G.getFont().deriveFont(f1));
		G.drawString("Time-Left    "+game.timeToEnd()/1000,(int)(defaultx*0.861),(int)(defaulty*0.91));
		
		f1 = 35.0f;
		G.setFont(G.getFont().deriveFont(f1));
		G.drawString("Level -- "+this.Level,(int)(defaultx*0.5),(int)(defaulty*0.15));

		f1=35.0f; // font size.
		g4.setFont(g4.getFont().deriveFont(f1));
		g4.drawString("Score:", (int)(defaultx*0.03), (int)(defaulty*0.15));
	}

	///////////////////////////////////////////////////Paint Graph End////////////////////////////////////////////////////////

	///////////////////////////////////////////////////Paint Fruits///////////////////////////////////////////////////////////
	private void paintFruits() 
	{
		List<String> Fruit = game.getFruits();

		for (int j = 0; j < Fruit.size(); j++)
		{
			try 
			{
				JSONObject obj = new JSONObject(Fruit.get(j));
				JSONObject ff = obj.getJSONObject("Fruit");
				double value = ff.getDouble("value");
				double type = ff.getDouble("type");
				String pos = ff.getString("pos");
				Point3D p = new Point3D(pos);
				
				double p_x = scale(p.x(),minX,maxX,80,this.getWidth()-80);
				double p_y = scale(p.y(),minY,maxY,80,this.getHeight()-80);
				ImageIcon apple = new ImageIcon("apple.png");
				Image  apple1  = apple.getImage();
				ImageIcon banana = new ImageIcon("banana.png"); 
				Image  banana1   =	banana.getImage();

				if(type == -1) 
				{
					g4.drawImage(banana1,(int)p_x-30, (int)p_y-30,50,50, this);
				}
				else 
				{
					g4.drawImage(apple1,(int)p_x-30, (int)p_y-30,50,50, this);
				}
			} catch (JSONException e) {e.printStackTrace();}
		}
	}
	///////////////////////////////////////////////////Paint smurfs End//////////////////////////////////////////////////////
	private void paintRobots() 
	{
		List<String> ArnoldSchwarzenegge = game.getRobots();
		int _score =0;
		for (int j = 0; j < ArnoldSchwarzenegge.size(); j++) 
		{
			try 
			{
				JSONObject obj = new JSONObject(ArnoldSchwarzenegge.get(j));
				JSONObject ff = obj.getJSONObject("Robot");

				String pos = ff.getString("pos");
				int rid = ff.getInt("id");
				int score = ff.getInt("value");
				Point3D p = new Point3D(pos);
				double p_x = scale(p.x(),minX,maxX,80,this.getWidth()-80);
				double p_y = scale(p.y(),minY,maxY,80,this.getHeight()-80);
				_score += score;
				
				ImageIcon ghost = new ImageIcon("ghost.png");
				Image  ghost1  = ghost.getImage();
				g4.drawImage(ghost1, (int)p_x-30, (int)p_y-30,80,80, this);
				g4.setColor(Color.RED);
				g4.drawString(""+rid, (int)p_x-20, (int)p_y-20);
			} catch (JSONException e) {e.printStackTrace();}
		}

		g4.setColor(Color.RED);
		float f1=35.0f; // font size.
		g4.setFont(g4.getFont().deriveFont(f1));
		g4.drawString(""+_score,  (int)(defaultx*0.1),  (int)(defaulty*0.15));
	}

	private double scale(double data, double r_min, double r_max,double t_min, double t_max)		
	{
		double res = ((data - r_min) / (r_max-r_min)) * (t_max - t_min) + t_min;
		return res;
	}

	public void windowScale()
	{
		for (node_data nodes : this.graph.getV()) 
		{
			if(nodes.getLocation().x() > maxX)
			{
				maxX = nodes.getLocation().x();
			}
			if(nodes.getLocation().x() < minX)
			{
				minX = nodes.getLocation().x();
			}
			if(nodes.getLocation().y() > maxY)
			{
				maxY = nodes.getLocation().y();
			}
			if(nodes.getLocation().y() < minY)
			{
				minY = nodes.getLocation().y();
			}
		}
	}
}

















