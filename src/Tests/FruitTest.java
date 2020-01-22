package Tests;

import static org.junit.Assert.*;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import gameClient.Fruit;
import utils.Point3D;

public class FruitTest {
	
	static game_service game;
	static DGraph g;
	static Fruit f;

	@Before
	public void insert()
	{
		game=Game_Server.getServer(5);
		g=new DGraph();
		g.init(game.getGraph());
		f= new Fruit (1,-1,new Point3D(3,3,0));
	}
	
	@After
	public void reset() 
	{
		f= new Fruit (1,-1,new Point3D(3,3,0));
	}
	
	@Test
	public void testFruit() 
	{
		Fruit f2 = f;
		assertEquals(f2, f);
	}
	
	@Test
	public void testGetTag() 
	{
		int tag = f.getTag();
		assertEquals(tag, 0);
	}
	
	@Test
	public void testSetTag() 
	{
		f.setTag(1);
		int tag = f.getTag();
		assertEquals(tag, 1);
	}
	
	@Test
	public void testGetValue() 
	{
		double val = f.getValue();
		assertEquals(val, 1.0, 0.00001);
	}
	
	@Test
	public void testGetType() 
	{
		int type = f.getType();
		assertEquals(type, -1);
	}
	
	@Test
	public void testGetPos() 
	{
		Point3D pos = f.getPos();
		Point3D pos2 = new Point3D(3,3,0);
		assertEquals(pos, pos2);
	}
	
	@Test
	public void testSetPos() 
	{
		Point3D p = new Point3D(3,3,0);
		f.setPos(p);
		Point3D pos = f.getPos();
		assertEquals(pos, p);
	}

}
