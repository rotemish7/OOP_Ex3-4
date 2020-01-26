package gameClient;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import utils.Point3D;


/**
 * class that represents an object of type Fruit
 * Fruit has two type 1 or -1
 * each Fruit composed of 4 fields:
 * 1) VALUE
 * 2) TYPE
 * 3) POSITION(X,Y)
 * 4) TAG(VISIT OR NOT)
 * 
 * 
 * @author rotem levy
 *
 */
public class Fruit 
{

	private double value;
	private int type;
	private Point3D pos;
	private int tag;

	public Fruit() {}
	
	public Fruit(double value, int type, Point3D pos)
	{
		this.value=value;
		this.type=type;
		this.pos=pos;
		this.tag = 0;
	}

	/**
	 * A constructor from String in JSON format
	 * 
	 * @param s represents a json string file
	 */
	public Fruit(String s) 
	{
		try 
		{
			JSONObject obj_JSONObject = new JSONObject(s);
			JSONObject JSON_Fruit = obj_JSONObject.getJSONObject("Fruit");
			String pos = JSON_Fruit.getString("pos");// Extract the coordinates to String
			this.pos = new Point3D(pos);
			double value = JSON_Fruit.getInt("value"); // Extract the value of the fruit
			this.value = value;
			int type = JSON_Fruit.getInt("type"); // Extract the type of the fruit
			this.type = type;

		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 
	 * @return a list of fruits
	 */
	public static List<Fruit> fruitList()
	{
		List<Fruit> ans = new ArrayList<Fruit>();

		return ans;
	}
	
	/*
	 * @return the tag of the fruit
	 */
	public int getTag()
	{
		return this.tag;
	}
	
	/**
	 * 
	 * @param tag a int to mark visit or not
	 */
	public void setTag(int tag)
	{
		this.tag = tag;
	}

	/**
	 * 
	 * @return the value of the fruit
	 */
	public double getValue()
	{
		return this.value;
	}

	/**
	 * 
	 * @return the type of the fruit(1 or -1)
	 */
	public int getType()
	{
		return this.type;
	}

	/**
	 * 
	 * @return a 3D Point represents the fruit coordinates
	 */
	public Point3D getPos()
	{
		return this.pos;
	}

	/**
	 * 
	 * @param p represents a 3d point 
	 */
	public void setPos(Point3D p)
	{
		this.pos = p;
	}
}
