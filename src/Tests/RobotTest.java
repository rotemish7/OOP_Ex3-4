package Tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import gameClient.Robot;
import utils.Point3D;

public class RobotTest {

	static game_service game;
	static DGraph g;
	static Robot r;

	@Before
	public void insert() 
	{
		game=Game_Server.getServer(5);
		g=new DGraph();
		g.init(game.getGraph());
		r = new Robot(0, new Point3D(2,5,0), 0, -1, 0, 1);
	}

	@After
	public void reset() 
	{
		r = new Robot(0, new Point3D(2,5,0), 0, -1, 0, 1);
	}

	@Test
	public void testRobot() 
	{
		Robot r2 = r;
		assertEquals(r, r2);
	}

	@Test
	public void testGetSrc() 
	{
		int src = r.getSrc();
		assertEquals(src, 0);
	}

	@Test
	public void testGetId() 
	{
		int id = r.getId();
		assertEquals(id, 0);
	}

	@Test
	public void testGetPos() 
	{
		Point3D pos = r.getPos();
		Point3D pos2 = new Point3D(2,5,0);
		assertEquals(pos, pos2);
	}

	@Test
	public void testGetDest() 
	{
		int dest = r.getDest();
		assertEquals(dest, -1);
	}

	@Test
	public void testGetValue() 
	{
		int val = r.getValue();
		assertEquals(val, 0);
	}

	@Test
	public void testGetSpeed() 
	{
		int speed = r.getSpeed();
		assertEquals(speed, 1);
	}

	@Test
	public void testSetPos() 
	{
		Point3D p = new Point3D(5,2,0);
		r.setPos(p);
		Point3D pos = r.getPos();
		assertEquals(pos, p);

	}

	@Test
	public void testSetDest() 
	{
		int dest = 9;
		r.setDest(dest);
		int dst = r.getDest();
		assertEquals(dst, dest);
	}

}
