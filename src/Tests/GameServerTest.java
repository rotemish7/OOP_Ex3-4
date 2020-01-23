package Tests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import gameClient.GameServer;

public class GameServerTest {
	
	static game_service game;
	static DGraph g;
	static GameServer gs;
	static List<String> fruits;

	@Before
	public void insert() {
		game=Game_Server.getServer(5);
		g=new DGraph();
		g.init(game.getGraph());
		gs = new GameServer(fruits, 3, 89, 1, "A5", game, 5);
	}
	
	@After
	public void reset() {
		gs = new GameServer(fruits, 3, 89, 1, "A5", game, 5);
	}
	
	@Test
	public void testGetLevel() {
		int level = gs.getLevel();
		assertEquals(level, 5);
	}
	
	@Test
	public void testGetFruit() {
		assertNull(fruits);
	}
	
	@Test
	public void testGetMoves() {
		int moves = gs.getMoves();
		assertEquals(moves, 3);
	}
	
	@Test
	public void testGetGrade() {
		int grade = gs.getGrade();
		assertEquals(grade, 89);
	}
	
	@Test
	public void testGetRobots() {
		int numRob = gs.getRobots();
		assertEquals(numRob, 1);
	}
	
	@Test
	public void testGetGraph() {
		String str = gs.getGraph();
		assertEquals(str, "A5");
	}
	
	@Test
	public void testGetGame() {
		game_service game1 = gs.getGame();
		assertEquals(game1, game);
	}

}
