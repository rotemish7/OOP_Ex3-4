package gameClient;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import java.util.HashMap;
import java.util.List;

import com.mysql.fabric.xmlrpc.base.Array;
/**
 * This class represents a simple example of using MySQL Data-Base.
 * Use this example for writing solution. 
 * @author boaz.benmoshe
 *
 */
public class SimpleDB
{
	public static final String jdbcUrl="jdbc:mysql://db-mysql-ams3-67328-do-user-4468260-0.db.ondigitalocean.com:25060/oop?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
	public static final String jdbcUser="student";
	public static final String jdbcUserPassword="OOP2020student";
	public static final int [] MovesCondition = new int[24];
	public static final int [] GradeCondition = new int[24];
	public static final int[] levels = {0,1,3,5,9,11,13,16,19,20,23};

	/**
	 * Simple main for demonstrating the use of the Data-base
	 * @param args
	 */
	public static void main(String[] args) 
	{

		int idR = 311464671;
		int idS = 318511375;
		insertGrades();
		insertMoves();
		for(int i=0; i<11; i++)
		{
			statistics(idS, levels[i]);
		}
//		printLog(idS);
//		allUsers();	
		//String kml1 = getKML(id1,level);
		System.out.println("***** KML1 file example: ******");
		//System.out.println(kml1);
	}
	
	public static void insertMoves() 
	{
		MovesCondition[0] = 290;
		MovesCondition[1] = 580;
		MovesCondition[3] = 580;
		MovesCondition[5] = 500;
		MovesCondition[9] = 580;
		MovesCondition[11] = 580;
		MovesCondition[13] = 580;
		MovesCondition[16] = 290;
		MovesCondition[19] = 580;
		MovesCondition[20] = 290;
		MovesCondition[23] = 1140;
	}
	
	public static void insertGrades() 
	{
		MovesCondition[0] = 125;
		MovesCondition[1] = 436;
		MovesCondition[3] = 713;
		MovesCondition[5] = 570;
		MovesCondition[9] = 480;
		MovesCondition[11] = 1050;
		MovesCondition[13] = 310;
		MovesCondition[16] = 235;
		MovesCondition[19] = 250;
		MovesCondition[20] = 200;
		MovesCondition[23] = 1000;
	}
	
	/** simply prints all the games as played by the users (in the database).
	 * 
	 */
	public static int printLog(int id)
	{
		int ind = 0;
		try 
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs where userID="+id;

			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			
			while(resultSet.next())
			{
				//System.out.println(ind+") Id: " + resultSet.getInt("UserID")+", level: "+resultSet.getInt("levelID")+", score: "+resultSet.getInt("score")+", moves: "+resultSet.getInt("moves")+", time: "+resultSet.getDate("time"));
				ind++;
			}
			resultSet.close();
			statement.close();		
			connection.close();		
		}

		catch (SQLException sqle) 
		{
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		
		return ind;
	}
	/**
	 * this function returns the KML string as stored in the database (userID, level);
	 * @param id
	 * @param level
	 * @return
	 */
	public static String getKML(int id, int level) 
	{
		String ans = null;
		String allCustomersQuery = "SELECT * FROM Users where userID="+id+";";
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);		
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			if(resultSet!=null && resultSet.next())
			{
				ans = resultSet.getString("kml_"+level);
			}
		}
		catch (SQLException sqle) 
		{
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}

		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return ans;
	}
	
	/**
	 * 
	 * @param id represents a user id
	 * @return the best level the user reach to
	 */
	public static int userBestLevel(int id)
	{
		int ans = 0;
		String allCustomersQuery = "SELECT * FROM Users;";
		try 
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);		
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			while(resultSet.next()) 
			{
				if(id == resultSet.getInt("UserID"))
				{
					ans = resultSet.getInt("levelNum");
					return ans;
				}
			}
			resultSet.close();
			statement.close();		
			connection.close();
		}
		catch (SQLException sqle)
		{
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}

		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		return ans;
	}
	
	/**
	 * 
	 * @return
	 */
	public static int allUsers() 
	{
		int ans = 0;
		String allCustomersQuery = "SELECT * FROM Users;";
		try 
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);		
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			while(resultSet.next()) 
			{
				System.out.println("Id: " + resultSet.getInt("UserID")+", max_level:"+resultSet.getInt("levelNum"));
				ans++;
			}
			resultSet.close();
			statement.close();		
			connection.close();
		}
		catch (SQLException sqle)
		{
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}

		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		return ans;
	}
	
	public static void statistics(int id, int level)
	{
		List<Integer> score = new ArrayList<Integer>();
		int place = 0;
		int i = 0;
		boolean first = true;
		String allCustomersQuery = "SELECT * FROM Logs where levelID="+level;
		try 
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);		
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			int nowId = 0;
			while(resultSet.next()) 
			{
				if(nowId != resultSet.getInt("UserID"))
				{
					first = true;
					nowId = resultSet.getInt("UserID");
				}
				if(first)
				{
					int best = bestScore(nowId, level);
					score.add(best);
					first = false;
				}
			}
			score.sort(Comparator.naturalOrder());
			int myScore = bestScore(id, level);
			while(score.get(i) != myScore)
			{
				place++;
				i++;
			}
			resultSet.close();
			statement.close();		
			connection.close();
		}
		catch (SQLException sqle)
		{
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}

		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		System.out.println("In level " + level + " my statistics is: " + place/score.size());
	}
	
	
	public static int bestScore(int id, int level)
	{
		int maxScore = 0;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs where userID="+id;
		
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			while(resultSet.next() && resultSet.getInt("levelID") == level)
			{
				if(resultSet.getInt("score") <= GradeCondition[level] && resultSet.getInt("moves") <= MovesCondition[level])
				{
					if(resultSet.getInt("score") > maxScore)
					{
						maxScore = resultSet.getInt("score");
					}
				}
			}
			resultSet.close();
			statement.close();		
			connection.close();		
		}
		
		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return maxScore;
	}
}

