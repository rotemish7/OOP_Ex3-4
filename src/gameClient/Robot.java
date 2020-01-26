package gameClient;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import utils.Point3D;

/**
 * This class represents a robot 
 * Robot has 6 fields
 * 1) SRC- the node which the robot is located now
 * 2) pos- the coordinates in the window(x,y)
 * 3) id - the id of the robot in the game
 * 4) dest - the destination node in the robot path
 * 5) value -  the amount of score the robot collected
 * 6) speed - the speed of the robot
 * 
 * 
 * @author rotem levy
 *
 */
public class Robot 
{

	private int src;
	private Point3D pos;
	private int id;
	private int dest;
	private int value;
	private int speed;

	public Robot() {}
	
	/**
	 * A constructor of robot
	 * 
	 * @param src
	 * @param pos
	 * @param id
	 * @param dest
	 * @param value
	 * @param speed
	 */
	public Robot(int src, Point3D pos, int id, int dest, int value, int speed)
	{
		this.src=src;
		this.pos=pos;
		this.id=id;
		this.dest=dest;
		this.value=value;
		this.speed=speed;
	}
	
	/**
	 * A constructor from String
	 * 
	 * @param s a json format string
	 */
	public Robot(String s) 
	{
		try 
		{
			JSONObject obj_JSONObject = new JSONObject(s);
			JSONObject JSON_Robot = obj_JSONObject.getJSONObject("Robot");
			int id = JSON_Robot.getInt("id"); // Extract the id of the robot
			this.id = id;
			int src = JSON_Robot.getInt("src"); // Extract the src of the robot
			this.src = src;
			int dest = JSON_Robot.getInt("dest"); // Extract the dest of the robot
			this.dest = dest;
			int speed = JSON_Robot.getInt("speed"); // Extract the speed of the robot
			this.speed = speed;
			int value = JSON_Robot.getInt("value"); // Extract the value of the robot
			this.value = value;
			String pos = JSON_Robot.getString("pos"); // Extract the coordinates to String
			this.pos = new Point3D(pos);

		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return a list of robots
	 */
	public static List<Robot> robotList()
	{
		List<Robot> ans = new ArrayList<Robot>();
		
		
		return ans;
	}

	/**
	 * 
	 * @return the src node of the robot
	 */
	public int getSrc()
	{
		return this.src;
	}

	/**
	 * 
	 * @return a 3d point of the robot location
	 */
	public Point3D getPos()
	{
		return this.pos;
	}

	/**
	 * 
	 * @return the id of the robot
	 */
	public int getId()
	{
		return this.id;
	}

	/**
	 * 
	 * @return the dest of the robot
	 */
	public int getDest()
	{
		return this.dest;
	}

	/**
	 * 
	 * @return the value of the robot
	 */
	public int getValue()
	{
		return this.value;
	}

	/**
	 * 
	 * @return the speed of the robot
	 */
	public int getSpeed()
	{
		return this.speed;
	}
	
	/**
	 * 
	 * @param p set the 3d point of the robot
	 */
	public void setPos(Point3D p)
	{
		this.pos = p;
	}

	/**
	 * 
	 * @param dest set the dest of the robot
	 */
	public void setDest(int dest)
	{
		this.dest = dest;
	}
}
