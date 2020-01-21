package gui;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node;
import dataStructure.node_data;
import utils.Point3D;

public class Main
{
	
	private double scale(double data, double r_min, double r_max,double t_min, double t_max)		
	{
		//
		double res = ((data - r_min) / (r_max-r_min)) * (t_max - t_min) + t_min;
		return res;
	}

	public static void main(String[] args) 
	{
		Graph_Algo G = new Graph_Algo();
		graph graph =new DGraph();

		graph.addNode(new node(10,new Point3D(100,100,150),0));
		graph.addNode(new node(11,new Point3D(80,70,0),0));
		graph.addNode(new node(12,new Point3D(120,300,200),0));
		//graph.addNode(new node(13,new Point3D(150,200,100),0));
		//graph.addNode(new node(14,new Point3D(75,250,250),0));
		graph.addNode(new node(15,new Point3D(300,300,300),0));
		graph.addNode(new node(16,new Point3D(500,100,100),0));
		
		

		graph.connect(16, 15, 1);
		graph.connect(16, 10, 5);
		graph.connect(15, 16, 1);
		graph.connect(15, 12, 3);
		graph.connect(12, 15, 3);
		graph.connect(10, 16, 5);
		graph.connect(10, 12, 10);
		graph.connect(12, 10, 10);
//		graph.connect(10, 11, 10);
//		graph.connect(11, 10, 10);
		

		G.init(graph);
//		System.out.println(G.isWay(10, 15));
//		System.out.println(G.isConnected());
//
//		System.out.println(G.shortestPathDist(12, 15));
//		List<node_data> sp =  G.shortestPath(12, 16);
//		System.out.print("ShortestPath: ");
//		for (node_data node : sp) 
//		{ 
//			System.out.print(node.getKey()+",");
//		}
//		System.out.println();
//		List<Integer> tsp =  new ArrayList<Integer>();
//		tsp.add(15);
//		tsp.add(12);
//		tsp.add(16);
//		G.TSP(tsp);
//		System.out.println();
//		System.out.print("TSP: ");
//		for (node_data node : sp) 
//		{ 
//			System.out.print(node.getKey()+",");
//		}
//		System.out.println();
		GUI window = new GUI(graph);
		window.setVisible(true);

	}
}
